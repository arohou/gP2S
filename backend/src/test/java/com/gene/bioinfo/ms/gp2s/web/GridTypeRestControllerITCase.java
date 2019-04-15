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

import java.io.IOException;

import javax.annotation.Nullable;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.gene.bioinfo.ms.gp2s.domain.GridType;
import com.gene.bioinfo.ms.gp2s.repository.GridTypeRepository;
import com.gene.bioinfo.ms.gp2s.web.base.BaseRestITCase;

public class GridTypeRestControllerITCase extends BaseRestITCase<GridType> {

	@Autowired
	private GridTypeRepository repository;
	
	@Before
	public void setUp() throws Exception {
		initBaseRestTests(repository);
	}
	
    @Override
    public String getUri() {
        return "/api/v1/grid-type/";
    }

    @Override
    public GridType createEntity(@Nullable Long id, @Nullable String label, @Nullable String slug) {
    	
        return GridType.builder().id(id).label(label).slug(slug).manufacturer("Simens")
        		.description("Lorem Ipsum").build();
    }


    @Override
    protected String createRequestBody(String label) throws IOException {
        return json(GridType.builder().label(label).manufacturer("Simens")
        		.description("Lorem Ipsum").build());
    }
}
