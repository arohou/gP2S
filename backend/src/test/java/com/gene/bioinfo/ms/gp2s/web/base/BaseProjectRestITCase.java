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

package com.gene.bioinfo.ms.gp2s.web.base;

import com.gene.bioinfo.ms.gp2s.GP2SApplication;
import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import lombok.NonNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Base integration test case class.
 * <p>
 * Sets up the whole spring context for running and provides some utils.
 *
 * @author Cezary Krzyżanowski on 25.07.2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GP2SApplication.class)
@WebAppConfiguration
@ActiveProfiles("integration-test")
public abstract class BaseProjectRestITCase<T extends BaseSlugAndLabelEntity> extends BaseRestITCase<T> {

    protected static final Long PROJECT_ID_1 = 1L;
    protected static final Long PROJECT_ID_2 = 2L;
    protected static final String PROJECT_SLUG_1 = "project-slug-1";
    protected static final String PROJECT_SLUG_2 = "project-slug-2";
    protected static final String PROJECT_LABEL_1 = "project 1";
    protected static final String PROJECT_LABEL_2 = "project 2";
    protected static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_1 = "project-management-system-slug-slug-1";
    protected static final String PROJECT_MANAGEMENT_SYSTEM_SLUG_2 = "project-management-system-slug-slug-2";

    protected static final String PROJECTS_URI = "/api/v1/project/";
    protected final List<Project> projects = new ArrayList<>();
    protected ProjectRepository projectRepository;
    @Autowired
    protected SampleRepository sampleRepository;

    @Override
    public void cleanupRepository() {
        this.sampleRepository.deleteAll();
        super.cleanupRepository();
        this.projectRepository.deleteAll();
    }

    protected void initProjectBaseRestTests(
            @NonNull final BaseSlugRepository<T> entitiesRepository,
            @NonNull final ProjectRepository projectRepository)
    {
        this.projectRepository = projectRepository;

        this.projects.clear();
        this.projects.add(projectRepository.save(
                Project.builder()
                        .id(PROJECT_ID_1)
                        .slug(PROJECT_SLUG_1)
                        .label(PROJECT_LABEL_1)
                        .projectManagementSystemSlug(PROJECT_MANAGEMENT_SYSTEM_SLUG_1)
                        .build()));
        this.projects.add(projectRepository.save(
                Project.builder()
                        .id(PROJECT_ID_2)
                        .slug(PROJECT_SLUG_2)
                        .label(PROJECT_LABEL_2)
                        .projectManagementSystemSlug(PROJECT_MANAGEMENT_SYSTEM_SLUG_2)
                        .build()));

        this.initBaseRestTests(entitiesRepository);
    }

    @Test
    public void readEntityProjectsById() throws Exception {
        // Put new entity into two projects
        final T entity = entityRepository.findOneBySlug(ENTITY_SLUG_1);

        //check if new protein is associated to two projects
        getMockMvc()
                .perform(get(URI + entity.getId() + "/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(projects.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(projects.get(0).getSlug())))
                .andExpect(jsonPath("$[0].label", is(projects.get(0).getLabel())))
                .andExpect(jsonPath("$[1].id", is(projects.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].slug", is(projects.get(1).getSlug())))
                .andExpect(jsonPath("$[1].label", is(projects.get(1).getLabel())));
    }

    @Test
    public void readEntityProjectsBySlug() throws Exception {
        // Put new entity into the second project
        final T entity = entityRepository.findOneBySlug(ENTITY_SLUG_1);

        //check if new protein is associated to two projects
        getMockMvc()
                .perform(get(URI + entity.getSlug() + "/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(projects.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].slug", is(projects.get(0).getSlug())))
                .andExpect(jsonPath("$[0].label", is(projects.get(0).getLabel())))
                .andExpect(jsonPath("$[1].id", is(projects.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].slug", is(projects.get(1).getSlug())))
                .andExpect(jsonPath("$[1].label", is(projects.get(1).getLabel())));
    }

    @Test
    public void readEntityProjectionFromProjectById() throws Exception {
        final Long projectId = projects.get(0).getId();

        ResultActions resultActions = getMockMvc().perform(get(PROJECTS_URI + projectId + "/" +
                getProjectionFromProjectPath()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(entities.size())));

        for (int i = 0; i < entities.size() - 1; i++) {
            final int reverseIndex = entities.size() - 1 - i;
            final T entity = entities.get(reverseIndex);
            resultActions = resultActions
                    .andExpect(jsonPath("$[" + i + "].id", is(entity.getId().intValue())))
                    .andExpect(jsonPath("$[" + i + "].slug", is(entity.getSlug())))
                    .andExpect(jsonPath("$[" + i + "].label", is(entity.getLabel())));
        }
    }

    @Test
    public void readEntityProjectionFromProjectBySlug() throws Exception {
        final String projectSlug = projects.get(0).getSlug();
        ResultActions resultActions = getMockMvc().perform(get(PROJECTS_URI + projectSlug + "/" +
                getProjectionFromProjectPath()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(entities.size())));

        for (int i = 0; i < entities.size() - 1; i++) {
            final int reverseIndex = entities.size() - 1 - i;
            final T entity = entities.get(reverseIndex);
            resultActions = resultActions
                    .andExpect(jsonPath("$[" + i + "].id", is(entity.getId().intValue())))
                    .andExpect(jsonPath("$[" + i + "].slug", is(entity.getSlug())))
                    .andExpect(jsonPath("$[" + i + "].label", is(entity.getLabel())));
        }
    }

    @Override
    public String getCreateUri() {
        return URI + "/" + PROJECT_SLUG_1;
    }

    @NonNull
    public abstract String getProjectionFromProjectPath();
}
