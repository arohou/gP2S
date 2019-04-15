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

import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.repository.ProjectRepository;
import com.gene.bioinfo.ms.gp2s.repository.sample.SampleRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.IOException;

public class ProjectRestControllerITCase extends BaseRestITCase<Project> {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @Override
    public String getUri() {
        return "/api/v1/project/";
    }

    @Override
    public Project createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
        return Project.builder().id(id).label(label).slug(slug).projectManagementSystemSlug("project-management-system-" + slug).build();
    }

    @Override
    public void cleanupRepository() {
        sampleRepository.deleteAll();
        super.cleanupRepository();
    }

    @Before
    public void setUp() throws Exception {
        initBaseRestTests(projectRepository);
    }

    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(Project.builder().label(label)
                .projectManagementSystemSlug("a-project-management-system-slug")
                .build());
    }
}
