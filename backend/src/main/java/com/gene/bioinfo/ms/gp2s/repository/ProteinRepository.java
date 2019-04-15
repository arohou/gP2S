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

package com.gene.bioinfo.ms.gp2s.repository;

import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProteinRepository extends BaseSlugRepository<Protein> {
    @Query("select count(distinct protein) from Protein protein join protein.projects project where project.slug = ?1")
    Integer countForProject(final String slug);

    @Query("select count(distinct protein) from Protein protein join protein.projects project where project.slug = ?1")
    Integer countForProject(final Long id);

    @Query(value = "select protein from Protein protein where protein.purificationId = ?1 order by protein.id desc")
    List<Protein> findAvailableProteins(final String purificationId);

    List<Protein> findAllByProjects_SlugOrderByIdDesc(String projectSlug);

    List<Protein> findAllByProjects_IdOrderByIdDesc(Long id);
}