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

import com.gene.bioinfo.ms.gp2s.domain.Map;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRepository extends BaseSlugRepository<Map> {

    @Query("select distinct map from Map map \n" +
            "where map in (select distinct map from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where proj.id = ?1)\n" +
            "or map in (select distinct map from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where proj.id = ?1)\n" +
            "order by map.id desc")
    List<Map> findByProject_IdOrderByIdDesc(@Param("id") final Long id);

    @Query("select distinct map from Map map \n" +
            "where map in (select distinct map from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where proj.slug = ?1)\n" +
            "or map in (select distinct map from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where proj.slug = ?1)\n" +
            "order by map.id desc")
    List<Map> findByProject_SlugOrderByIdDesc(@Param("slug") final String slug);

    @Query("select count(distinct map) from Map map \n" +
            "where map in (select distinct map from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where proj.id = ?1)\n" +
            "or map in (select distinct map from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where proj.id = ?1)")
    Integer countForProject(final Long id);

    @Query("select count(distinct map) from Map map \n" +
            "where map in (select distinct map from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where proj.slug = ?1)\n" +
            "or map in (select distinct map from Map map\n" +
            "    join map.processingSession session join session.microscopySessions ms join ms.grid grid join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where proj.slug = ?1)")
    Integer countForProject(final String slug);

    List<Map> findAllByAttachmentMongoId(final String attachmentId);
}
