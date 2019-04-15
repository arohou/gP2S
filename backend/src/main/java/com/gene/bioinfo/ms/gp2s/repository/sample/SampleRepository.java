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

package com.gene.bioinfo.ms.gp2s.repository.sample;

import com.gene.bioinfo.ms.gp2s.domain.sample.Sample;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SampleRepository extends BaseSlugRepository<Sample> {

    @Query("select distinct sample from Sample sample \n" +
            "where (sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where proj.slug = ?1)\n" +
            "or sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where proj.slug = ?1))\n" +
            "order by sample.id desc")
    List<Sample> findByProject_SlugOrderByIdDesc(final String slug);

    @Query("select distinct sample from Sample sample \n" +
            "where (sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj where proj.id = ?1)\n" +
            "or sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj where proj.id = ?1))\n" +
            "order by sample.id desc")
    List<Sample> findByProject_IdOrderByIdDesc(final Long id);

    @Query("select count(distinct sample) from Sample sample \n" +
            "where (sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where proj.slug = ?1)\n" +
            "or sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where proj.slug = ?1))")
    Integer countForProject(final String slug);

    @Query("select count(distinct sample) from Sample sample \n" +
            "where (sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where proj.id = ?1)\n" +
            "or sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where proj.id = ?1))")
    Integer countForProject(final Long id);

    List<Sample> findByProteinComponent_Aliquot_IdOrderByIdDesc(final Long id);

    List<Sample> findByLigandComponent_Aliquot_IdOrderByIdDesc(final Long id);

    @Query("select distinct sample from Sample sample \n" +
            "where (sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.ligandComponent lc join lc.aliquot lcAliquot  join lcAliquot.projects proj where proj.id = ?1)\n" +
            "or sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj where proj.id = ?1))\n" +
            "and sample.availableForGridMaking = true\n" +
            "order by sample.id desc")
    List<Sample> findSamplesAvailableForGridMaking(Long projectId);

    @Query("select distinct sample from Sample sample \n" +
            "where (sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where proj.slug = ?1)\n" +
            "or sample in (select distinct smpl from Sample smpl\n" +
            "    join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where proj.slug = ?1))\n" +
            "and sample.availableForGridMaking = true\n" +
            "order by sample.id desc")
    List<Sample> findSamplesAvailableForGridMaking(String slug);

    /**
     * Find comma separated name of project labels.
     *
     * @param sampleIds Comma-separated sample IDs.
     * @return List of lists that contain sample id and project label.
     */
    @Query("select distinct new list(sample.id, project.label) from Sample sample, Project project\n" +
            "where (sample.id, project.label) in\n" +
            "    (select distinct smpl.id, proj.label from Sample smpl\n" +
            "    join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects\n" +
            "    proj where smpl.id in (?1))\n" +
            "or (sample.id, project.label) in\n" +
            "    (select distinct smpl.id, proj.label from Sample smpl\n" +
            "    join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects\n" +
            "    proj where smpl.id in (?1)) order by sample.id, project.label desc")
    List<List<Object>> findProjectLabels(final List<Long> sampleIds);
}
