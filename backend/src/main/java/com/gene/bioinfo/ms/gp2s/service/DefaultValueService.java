/*
 * Copyright 2018 Genentech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gene.bioinfo.ms.gp2s.service;

import com.gene.bioinfo.ms.gp2s.domain.DefaultValue;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.base.Persistable;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefault;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultComposite;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultDefinition;
import com.gene.bioinfo.ms.gp2s.infrastructure.annotation.GP2SDefaultType;
import com.gene.bioinfo.ms.gp2s.repository.DefaultValueRepository;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.netflix.config.validation.ValidationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DefaultValueService {

    private DefaultValueRepository repository;

    private ProjectRepository projectRepository;

    @Autowired
    DefaultValueService(@NonNull final DefaultValueRepository repository,
                        @NonNull final ProjectRepository projectRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
    }

    public Map<String, Object> getValues(DefaultValue.DefaultValueType type, Long projectId) {
        if (!projectRepository.exists(projectId)) {
            throw new ValidationException(String.format("Project with following id: '%s' doesn't exist", projectId));
        }
        List<DefaultValue> defaultValues = new ArrayList<>();
        defaultValues.addAll(Optional.ofNullable(repository.findByEntityTypeAndProject_Id(type, projectId)).orElse(Collections.emptyList()));
        defaultValues.addAll(Optional.ofNullable(repository.findByEntityTypeAndProjectIsNull(type)).orElse(Collections.emptyList()));

        return groupByProperty(defaultValues.stream().collect(Collectors.toMap(DefaultValue::getFieldName,
                DefaultValue::getValue)));
    }

    public Map<String, Object> getValues(DefaultValue.DefaultValueType type, String projectSlug) {
        if (!projectRepository.existsBySlug(projectSlug)) {
            throw new ValidationException(String.format("Project with following slug: '%s' doesn't exist", projectSlug));
        }
        List<DefaultValue> defaultValues = new ArrayList<>();
        defaultValues.addAll(Optional.ofNullable(repository.findByEntityTypeAndProject_Slug(type, projectSlug)).orElse(Collections.emptyList()));
        defaultValues.addAll(Optional.ofNullable(repository.findByEntityTypeAndProjectIsNull(type)).orElse(Collections.emptyList()));

        return groupByProperty(defaultValues.stream().collect(Collectors.toMap(DefaultValue::getFieldName,
                DefaultValue::getValue)));
    }

    protected Map<String, Object> groupByProperty(Map<String, Object> data) {
        Map<String, Object> map = new HashMap<>();
        data.entrySet().forEach(entry -> addToMap(map, entry.getKey(), entry.getValue()));
        return map;
    }

    private void addToMap(Map<String, Object> map, String key, Object value) {
        int idx = key.indexOf('.');
        if (idx < 0) {
            map.put(key, value);
        } else {
            String subKey = key.substring(0, idx);
            Map<String, Object> subMap = (Map<String, Object>) map.get(subKey);
            if (subMap == null) {
                subMap = new HashMap<>();
                map.put(subKey, subMap);
            }
            addToMap(subMap, key.substring(idx + 1, key.length()), value);
        }
    }

    public <U extends Persistable<Long>> void saveDefaultValues(@NonNull final Project project, @NonNull final U entity) {
        final GP2SDefaultType annotation = entity.getClass()
                .getAnnotation(GP2SDefaultType.class);
        Objects.requireNonNull(annotation, "Missing GP2SDefaultType annotation");

        Arrays.stream(entity.getClass()
                .getDeclaredFields())
                .filter(this::isGp2sDefaultAnnotationPresent)
                .map(field -> DefaultValue.builder()
                        .entityType(annotation.value())
                        .project(project)
                        .fieldName(field.getName())
                        .value(getFieldValue(entity, field))
                        .build())
                .filter(defaultValue -> defaultValue.getValue() != null)
                .map(this::mapToExistingIfPresent)
                .forEach(defaultValue -> repository.save(defaultValue));

        Arrays.stream(entity.getClass()
                .getDeclaredFields())
                .filter(this::isGp2sDefaultCompositeAnnotationPresent)
                .forEach(field -> createAndSaveCompositeDefaultValues(project, annotation.value(), entity, field));
    }

    protected <U extends Persistable<Long>> void createAndSaveCompositeDefaultValues(
            @NonNull final Project project,
            @NonNull final DefaultValue.DefaultValueType type,
            @NonNull final U entity,
            @NonNull final Field field) {

        List<DefaultValue> defaultValues = new ArrayList<>();
        Arrays.stream(field.getAnnotation(GP2SDefaultComposite.class).definition())
                .forEach(definition -> {
                    try {
                        defaultValues.add(createDefaultValue(project, type, entity, field, definition));
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        log.error("Unexpected error when accessing filed: " + field.getName() + " on class " + field.getType()
                                .getCanonicalName(), e);
                    }
                });

        defaultValues.stream()
                .filter(defaultValue -> StringUtils.isNotBlank(defaultValue.getValue()))
                .map(this::mapToExistingIfPresent)
                .forEach(repository::save);
    }

    protected <U extends Persistable<Long>> DefaultValue createDefaultValue(@NonNull final Project project,
                                                                            @NonNull final DefaultValue.DefaultValueType type,
                                                                            @NonNull final U entity,
                                                                            @NonNull final Field field,
                                                                            @NonNull final GP2SDefaultDefinition definition) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return DefaultValue.builder()
                .project(definition.project() ? project : null)
                .entityType(type)
                .fieldName(process(entity, definition.key()))
                .value(process(entity, definition.value()))
                .build();
    }

    protected String process(Object obj, String definition) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        StringBuffer value = new StringBuffer(definition);
        Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String path = matcher.group();
            path = path.substring(2, path.length() - 1);
            String replacement = getPathValue(obj, path);
            value.replace(matcher.start(), matcher.end(), "" + replacement);
            matcher.reset(value);
        }
        return value.toString();
    }

    private String getPathValue(Object obj, String path) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        try {
            PropertyUtilsBean pub = new PropertyUtilsBean();
            Object property = pub.getProperty(obj, path);
            return toString(property);
        } catch (IndexOutOfBoundsException | NullPointerException npe) {
            return "";
        }
    }

    private boolean isGp2sDefaultAnnotationPresent(@NonNull final Field field) {
        return field.isAnnotationPresent(GP2SDefault.class);
    }

    private boolean isGp2sDefaultCompositeAnnotationPresent(@NonNull final Field field) {
        return field.isAnnotationPresent(GP2SDefaultComposite.class);
    }

    private <U extends Persistable<Long>> String getFieldValue(@NonNull final U entity, @NonNull final Field field) {
        @NonNull final boolean acc = field.isAccessible();
        try {
            field.setAccessible(true);
            return toString(field.get(entity));
        } catch (IllegalAccessException e) {
            log.error("Unexpected error when accessing filed: " + field.getName() + " on class " + field.getType()
                    .getCanonicalName());
            return null;
        } finally {
            field.setAccessible(acc);
        }
    }

    private String toString(final Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof Persistable) {
            return ((Persistable) obj).getId()
                    .toString();
        } else {
            return obj.toString();
        }
    }

    @NonNull
    private DefaultValue mapToExistingIfPresent(@NonNull final DefaultValue defaultValue) {
        final DefaultValue fetchedDefaultValue = repository.findByEntityTypeAndProject_IdAndFieldName(
                defaultValue.getEntityType(),
                defaultValue.getProject() != null ? defaultValue.getProject().getId() : null,
                defaultValue.getFieldName());

        if (fetchedDefaultValue == null) {
            return defaultValue;
        }
        fetchedDefaultValue.setValue(defaultValue.getValue());
        return fetchedDefaultValue;
    }

}
