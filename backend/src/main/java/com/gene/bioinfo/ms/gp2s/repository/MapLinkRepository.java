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

import com.gene.bioinfo.ms.gp2s.domain.MapLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface MapLinkRepository extends JpaRepository<MapLink, Long> {

    @Query(value = "select mapLink from MapLink mapLink where mapLink.parentMap.id = ?1 or mapLink.childMap.id = ?1 "
            + "order by mapLink.id asc")
    List<MapLink> getAllRelationsForMap(final Long mapId);

    void deleteByParentMap_Id(Long mapId);

    void deleteByChildMap_Id(Long mapId);
}