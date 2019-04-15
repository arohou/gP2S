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

import com.gene.bioinfo.ms.gp2s.domain.Grid;
import com.gene.bioinfo.ms.gp2s.repository.base.BaseSlugRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface GridRepository extends BaseSlugRepository<Grid> {
    String findGridByProjectIdStatement =
            "select distinct grid from Grid grid \n" +
                    "where (grid in (select distinct grid from Grid grid\n" +
                    "    join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
                    "    where proj.id = ?1)\n" +
                    "or grid in (select distinct grid from Grid grid\n" +
                    "    join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
                    "    where proj.id = ?1))\n";

    String findGridByProjectSlugStatement =
            "select distinct grid from Grid grid \n" +
                    "where (grid in (select distinct grid from Grid grid\n" +
                    "    join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
                    "    where proj.slug = ?1)\n" +
                    "or grid in (select distinct grid from Grid grid\n" +
                    "    join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
                    "    where proj.slug = ?1))\n";

    String orderByGridIdStatement = "order by grid.id desc";

    String gridIsAvailableCondition =
            "and grid not in(select distinct session.grid from MicroscopySession session where session.gridReturnedToStorage = false)\n";

    String gridIsAvailableOrHasIdCondition =
            "and (grid not in(select distinct session.grid from MicroscopySession session where session.gridReturnedToStorage = false)\n" +
                    "or grid.id = ?2)";

    @Query(findGridByProjectIdStatement + orderByGridIdStatement)
    List<Grid> findByProject_IdOrderByIdDesc(final Long id);

    @Query(findGridByProjectSlugStatement + orderByGridIdStatement)
    List<Grid> findByProject_SlugOrderByIdDesc(final String slug);


    @Query(findGridByProjectIdStatement + gridIsAvailableCondition + orderByGridIdStatement)
    List<Grid> findAvailableGrids(final Long id);

    @Query(findGridByProjectSlugStatement + gridIsAvailableCondition + orderByGridIdStatement)
    List<Grid> findAvailableGrids(final String slug);

    @Query(findGridByProjectIdStatement + gridIsAvailableOrHasIdCondition + orderByGridIdStatement)
    List<Grid> findAvailableGrids(final Long projectId, final Long gridId);

    @Query(findGridByProjectSlugStatement + gridIsAvailableOrHasIdCondition + orderByGridIdStatement)
    List<Grid> findAvailableGrids(final String projectSlug, final Long gridId);

    @Query("select count(distinct grid) from Grid grid \n" +
            "where (grid in (select distinct grid from Grid grid\n" +
            "    join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where proj.slug = ?1)\n" +
            "or grid in (select distinct grid from Grid grid\n" +
            "    join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where proj.slug = ?1))\n")
    Integer countForProject(final String slug);

    @Query("select count(distinct grid) from Grid grid \n" +
            "where (grid in (select distinct grid from Grid grid\n" +
            "    join grid.sample smpl join smpl.ligandComponent lc join lc.aliquot lcAliquot join lcAliquot.projects proj\n" +
            "    where proj.id = ?1)\n" +
            "or grid in (select distinct grid from Grid grid\n" +
            "    join grid.sample smpl join smpl.proteinComponent pc join pc.aliquot pcAliquot join pcAliquot.projects proj\n" +
            "    where proj.id = ?1))\n")
    Integer countForProject(final Long id);

    @Query("SELECT CASE WHEN COUNT(ms) > 0 THEN false ELSE true END FROM MicroscopySession ms " +
            "WHERE ms.grid.id = :id AND ms.gridReturnedToStorage = false")
    Boolean hasBeenReturnedToStorage(@Param("id") final Long id);
}
