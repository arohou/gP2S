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

package com.gene.bioinfo.ms.gp2s.web.base.integration.preparers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gene.bioinfo.ms.gp2s.domain.*;
import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugEntity;
import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MockMvcExecutor {
    // region ADMIN REST ENDPOINTS
    public static final String API_V1_CRYO_STORAGE_DEVICE = "/api/v1/cryo-storage-device/";
    public static final String API_V1_VITRIFICATION_MACHINE = "/api/v1/vitrification-machine/";
    public static final String API_V1_VITRIFICATION_PROTOCOL = "/api/v1/vitrification-protocol/";
    public static final String API_V1_BLOTTING_PAPER = "/api/v1/blotting-paper/";
    public static final String API_V1_NEGATIVE_STAIN_PROTOCOL = "/api/v1/negative-stain-protocol/";
    public static final String API_V1_MICROSCOPE = "/api/v1/microscope/";
    public static final String API_V1_SURFACE_TREATMENT_PROTOCOL = "/api/v1/surface-treatment-protocol/";
    public static final String API_V1_SURFACE_TREATMENT_MACHINE = "/api/v1/surface-treatment-machine/";
    public static final String API_V1_ELECTRON_DETECTOR = "/api/v1/electron-detector/";
    public static final String API_V1_GRID_TYPE = "/api/v1/grid-type/";
    public static final String API_V1_SAMPLE_HOLDER = "/api/v1/sample-holder/";
    public static final String API_V1_PROJECT = "/api/v1/project/";
    public static final String API_V1_IMAGE_PROCESSING_SOFTWARE = "/api/v1/image-processing-software/";
    // endregion

    public static final String API_V1_COMMENT = "/api/v1/comment/%s/%s";
    public static final String API_V1_DEFAULT_VALUE = "/api/v1/default-value/";

    // region CRYO REST ENDPOINTS
    public static final String API_V1_PROTEIN = "/api/v1/protein/";
    public static final String API_V1_LIGAND = "/api/v1/ligand/";
    public static final String API_V1_SAMPLE = "/api/v1/sample/";
    public static final String API_V1_GRID = "/api/v1/grid/";
    public static final String API_V1_MICROSCOPY_SESSION = "/api/v1/microscopy-session/";
    public static final String API_V1_PROCESSING_SESSION = "/api/v1/processing-session/";
    public static final String API_V1_MAP = "/api/v1/map/";
    public static final String API_V1_MODEL = "/api/v1/model/";
    public static final String API_V1_MAP_LINK = "/api/v1/map-link/";
    public static final String API_V1_MODEL_LINK = "/api/v1/model-link/";
    // endregion
    public static final MediaType JSON_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    private static final Logger LOGGER = LoggerFactory.getLogger(MockMvcExecutor.class);
    private static final Map<Class, String> URLS = new HashMap<Class, String>() {{

        put(CryoStorageDevice.class, API_V1_CRYO_STORAGE_DEVICE);
        put(VitrificationMachine.class, API_V1_VITRIFICATION_MACHINE);
        put(VitrificationProtocol.class, API_V1_VITRIFICATION_PROTOCOL);
        put(BlottingPaper.class, API_V1_BLOTTING_PAPER);
        put(NegativeStainProtocol.class, API_V1_NEGATIVE_STAIN_PROTOCOL);
        put(Microscope.class, API_V1_MICROSCOPE);
        put(SurfaceTreatmentProtocol.class, API_V1_SURFACE_TREATMENT_PROTOCOL);
        put(SurfaceTreatmentMachine.class, API_V1_SURFACE_TREATMENT_MACHINE);
        put(ElectronDetector.class, API_V1_ELECTRON_DETECTOR);
        put(GridType.class, API_V1_GRID_TYPE);
        put(SampleHolder.class, API_V1_SAMPLE_HOLDER);
        put(ImageProcessingSoftware.class, API_V1_IMAGE_PROCESSING_SOFTWARE);

        put(Project.class, API_V1_PROJECT);
        put(Comment.class, API_V1_COMMENT);

        put(Protein.class, API_V1_PROTEIN);
        put(Ligand.class, API_V1_LIGAND);
        put(Sample.class, API_V1_SAMPLE);
        put(Grid.class, API_V1_GRID);
        put(MicroscopySession.class, API_V1_MICROSCOPY_SESSION);
        put(ProcessingSession.class, API_V1_PROCESSING_SESSION);
        put(com.gene.bioinfo.ms.gp2s.domain.Map.class, API_V1_MAP);
        put(Model.class, API_V1_MODEL);
        put(MapLink.class, API_V1_MAP_LINK);
        put(ModelLink.class, API_V1_MODEL_LINK);
    }};
    private final MockMvc mockMvc;

    public MockMvcExecutor(@NonNull final MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @NonNull
    public static <T> String url(@NonNull final Class<T> clazz) {
        return URLS.get(clazz);
    }

    @NonNull
    public <T> T post(@NonNull final T entity) {
        return post(url(entity.getClass()), entity, null);
    }

    @NonNull
    public <T> T post(@NonNull final String url, @NonNull final T entity) {
        return post(url, entity, null);
    }

    @NonNull
    public <T> T post(@NonNull final String url, @NonNull final T entity, final String projectSlug) {
        try {
            final String json = new ObjectMapper().writeValueAsString(entity);

            final MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post(url
                    + Optional.ofNullable(projectSlug).orElse(""))
                    .contentType(JSON_CONTENT_TYPE)
                    .content(json))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();
            return (T) new ObjectMapper().readValue(result.getResponse()
                    .getContentAsByteArray(), entity.getClass());
        } catch (final Exception ex) {
            LOGGER.error(format("Error when creating data in integration test over REST API: %s",
                    entity.getClass()
                            .toString()), ex);
            throw new RuntimeException(ex);
        }
    }

    @NonNull
    public <T> T put(@NonNull final T entity) {
        return put(url(entity.getClass()), entity, null);
    }

    @NonNull
    public <T> T put(@NonNull final String url, @NonNull final T entity) {
        return put(url, entity, null);
    }

    @NonNull
    public <T> T put(@NonNull final String url, @NonNull final T entity, final String projectSlug) {
        try {
            final String json = new ObjectMapper().writeValueAsString(entity);

            final MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put(url
                    + Optional.ofNullable(projectSlug).orElse(""))
                    .contentType(JSON_CONTENT_TYPE)
                    .content(json))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
            return (T) new ObjectMapper().readValue(result.getResponse()
                    .getContentAsByteArray(), entity.getClass());
        } catch (final Exception ex) {
            LOGGER.error(format("Error when updating data in integration test over REST API: %s",
                    entity.getClass()
                            .toString()), ex);
            throw new RuntimeException(ex);
        }
    }

    public <T extends BaseSlugEntity> void delete(@NonNull final T entity) {
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete(url(entity.getClass()) + entity.getSlug())
                    .contentType(JSON_CONTENT_TYPE))
                    .andDo(print())
                    .andExpect(status().isOk());
        } catch (final Exception ex) {
            LOGGER.error(format("Error when deleting data in integration test over REST API: %s, %s",
                    entity.getClass()
                            .toString(), entity.getSlug()), ex);
            throw new RuntimeException(ex);
        }
    }

    @NonNull
    public <T> T get(@NonNull final String url, @NonNull final Class<T> clazz) {

        try {
            final MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                    .contentType(JSON_CONTENT_TYPE))
                    .andExpect(status().isOk())
                    .andReturn();
            return new ObjectMapper().readValue(result.getResponse()
                    .getContentAsByteArray(), clazz);
        } catch (final Exception ex) {
            LOGGER.error(format("Error when fetching data in integration test over REST API: %s, %s",
                    clazz.toString(), url), ex);
            throw new RuntimeException(ex);
        }
    }

    @NonNull
    public <T> List<T> getList(@NonNull final String url, @NonNull final Class<T> clazz) {
        try {
            final MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                    .contentType(JSON_CONTENT_TYPE))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(result.getResponse()
                    .getContentAsByteArray(), mapper.getTypeFactory()
                    .constructCollectionType(List.class, clazz));
        } catch (final Exception ex) {
            LOGGER.error(format("Error when fetching data in integration test over REST API: %s, %s",
                    clazz.toString(), url), ex);
            throw new RuntimeException(ex);
        }
    }
}
