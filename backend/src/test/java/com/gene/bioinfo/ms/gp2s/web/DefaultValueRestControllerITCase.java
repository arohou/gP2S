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

package com.gene.bioinfo.ms.gp2s.web;

import com.gene.bioinfo.ms.gp2s.GP2SApplication;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GP2SApplication.class)
@WebAppConfiguration
@ActiveProfiles("integration-test")
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@TestExecutionListeners({SpringBootDependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup(value = "DefaultValueRestControllerITCaseData.xml")
@DatabaseTearDown(value = "ClearDB.xml")
public class DefaultValueRestControllerITCase {

    public static final MediaType JSON_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private HttpMessageConverter<Object> mappingJackson2HttpMessageConverter;

    private MockMvc mockMvc;

    @Autowired
    private void setConverters(HttpMessageConverter<Object>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                                                         .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                                                         .findAny()
                                                         .orElse(null);

        assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public final void initMockMvc() throws Exception {
        setMockMvc(webAppContextSetup(webApplicationContext).alwaysDo(MockMvcResultHandlers.print()).build());
    }

    @Test
    public void shouldListDefaultValuesByTypeAndProjectSlug() throws Exception {
        getMockMvc().perform(get("/api/v1/default-value/grid/project-slug-1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(JSON_CONTENT_TYPE))
                    .andExpect(jsonPath("$.key-1", is("value 1")))
                    .andExpect(jsonPath("$.key-2", is("value 2")))
                    .andExpect(jsonPath("$.key-3", is("value 3")));
    }

    @Test
    public void shouldListDefaultValuesByTypeAndProjectId() throws Exception {
        getMockMvc().perform(get("/api/v1/default-value/grid/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(JSON_CONTENT_TYPE))
                    .andExpect(jsonPath("$.key-1", is("value 1")))
                    .andExpect(jsonPath("$.key-2", is("value 2")))
                    .andExpect(jsonPath("$.key-3", is("value 3")));
    }

    @Test
    public void shouldListDefaultValuesByTypeCapitalLettersAndProjectId() throws Exception {
        getMockMvc().perform(get("/api/v1/default-value/GRID/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(JSON_CONTENT_TYPE))
                    .andExpect(jsonPath("$.key-1", is("value 1")))
                    .andExpect(jsonPath("$.key-2", is("value 2")))
                    .andExpect(jsonPath("$.key-3", is("value 3")));
    }

    @Test
    public void shouldThrowExceptionWhenUsingNotExistingType() throws Exception {
        getMockMvc().perform(get("/api/v1/default-value/test/1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(JSON_CONTENT_TYPE));
    }

    @Test
    public void shouldThrowExceptionWhenUsingNotExistingProject() throws Exception {
        getMockMvc().perform(get("/api/v1/default-value/grid/not-existing-project"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(JSON_CONTENT_TYPE));
    }

}
