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

import com.gene.bioinfo.ms.gp2s.domain.Project;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
public interface ProjectRepository extends BaseSlugRepository<Project> {

    Boolean existsBySlug(final String slug);

    @Query("select distinct project from Project project \n" +
            "where (project in (select distinct proj from Sample smpl\n" +
            "    join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where smpl.slug = ?1)\n" +
            "or project in (select distinct proj from Sample smpl\n" +
            "    join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where smpl.slug = ?1))\n" +
            "order by project.id")
    Set<Project> findBySamples_Slug(final String slug);

    @Query("select distinct project from Project project \n" +
            "where (project in (select distinct proj from Sample smpl\n" +
            "    join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where smpl.id = ?1)\n" +
            "or project in (select distinct proj from Sample smpl\n" +
            "    join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where smpl.id = ?1))\n" +
            "order by project.id")
    Set<Project> findBySamples_Id(final Long id);

    @Query("select distinct project from Project project \n" +
            "where (project in (select distinct proj from Grid grid\n" +
            "    join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where grid.slug = ?1)\n" +
            "or project in (select distinct proj from Grid grid\n" +
            "    join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where grid.slug = ?1))\n" +
            "order by project.id")
    Set<Project> findByGrid_Slug(final String slug);

    @Query("select distinct project from Project project \n" +
            "where (project in (select distinct proj from Grid grid\n" +
            "    join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where grid.id = ?1)\n" +
            "or project in (select distinct proj from Grid grid\n" +
            "    join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where grid.id = ?1))\n" +
            "order by project.id")
    Set<Project> findByGrid_Id(final Long id);

    @Query("select distinct project from Project project \n" +
            "where (project in (select distinct proj from MicroscopySession session\n" +
            "    join session.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where session.slug = ?1)\n" +
            "or project in (select distinct proj from MicroscopySession session\n" +
            "    join session.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where session.slug = ?1))\n" +
            "order by project.id")
    Set<Project> findByMicroscopySession_Slug(final String slug);

    @Query("select distinct project from Project project \n" +
            "where (project in (select distinct proj from MicroscopySession session\n" +
            "    join session.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where session.id = ?1)\n" +
            "or project in (select distinct proj from MicroscopySession session\n" +
            "    join session.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where session.id = ?1))\n" +
            "order by project.id")
    Set<Project> findByMicroscopySession_Id(final Long id);

    @Query("select distinct project from Project project\n" +
            "where (project in (select distinct proj from ProcessingSession session\n" +
            "    join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where session.slug = ?1)\n" +
            "or project in (select distinct proj from ProcessingSession session\n" +
            "    join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where session.slug = ?1))\n" +
            "order by project.slug")
    Set<Project> findByProcessingSession_Slug(@Param("slug") final String slug);

    @Query("select distinct project from Project project\n" +
            "where (project in (select distinct proj from ProcessingSession session\n" +
            "    join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where session.id = ?1)\n" +
            "or project in (select distinct proj from ProcessingSession session\n" +
            "    join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where session.id = ?1))\n" +
            "order by project.id")
    Set<Project> findByProcessingSession_Id(@Param("id") final Long id);

    @Query("select distinct project from Project project\n" +
            "where (project in (select distinct proj from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where map.slug = ?1)\n" +
            "or project in (select distinct proj from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where map.slug = ?1))\n" +
            "order by project.slug")
    Set<Project> findByMap_Slug(@Param("slug") final String slug);

    @Query("select distinct project from Project project\n" +
            "where (project in (select distinct proj from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where map.id = ?1)\n" +
            "or project in (select distinct proj from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where map.id = ?1))\n" +
            "order by project.id")
    Set<Project> findByMap_Id(@Param("id") final Long id);

    @Query("select distinct project from Project project\n" +
            "where (project in (select distinct proj from Model model\n" +
            "    join model.maps map join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where model.slug = ?1)\n" +
            "or project in (select distinct proj from Model model\n" +
            "    join model.maps map join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where model.slug = ?1))\n" +
            "order by project.slug")
    Set<Project> findByModel_Slug(@Param("slug") final String slug);

    @Query("select distinct project from Project project\n" +
            "where (project in (select distinct proj from Model model\n" +
            "    join model.maps map join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where model.id = ?1)\n" +
            "or project in (select distinct proj from Model model\n" +
            "    join model.maps map join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where model.id = ?1))\n" +
            "order by project.id")
    Set<Project> findByModel_Id(@Param("id") final Long id);
}
