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

package com.gene.bioinfo.ms.gp2s.service;

import com.gene.bioinfo.ms.gp2s.domain.MapLink;
import com.gene.bioinfo.ms.gp2s.exception.ResourceNotFoundException;
import com.gene.bioinfo.ms.gp2s.repository.MapLinkRepository;
import com.gene.bioinfo.ms.gp2s.repository.MapRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MapLinkService {

    private final MapLinkRepository mapLinkRepository;
    private final MapRepository mapRepository;

    @Autowired
    MapLinkService(@NonNull final MapLinkRepository mapLinkRepository,
                   @NonNull final MapRepository mapRepository) {
        this.mapLinkRepository = mapLinkRepository;
        this.mapRepository = mapRepository;
    }

    public List<MapLink> getMapLinks(@NonNull final Long mapId) {
        if (!this.mapRepository.exists(mapId)) {
            throw new ResourceNotFoundException("Map with given id doesn't exist: " + mapId);
        }

        return this.mapLinkRepository.getAllRelationsForMap(mapId);
    }

    public void deleteMapLink(@NonNull final Long mapLinkId) {
        Optional.ofNullable(this.mapLinkRepository.findOne(mapLinkId))
                .orElseThrow(() -> new ResourceNotFoundException("MapLink not found for id: " + mapLinkId));
        this.mapLinkRepository.delete(mapLinkId);
    }

    public MapLink createMapLink(@NonNull final MapLink input) {
        final MapLink link = MapLink.builder()
                .parentMap(this.mapRepository.findOne(input.getParentMap().getId()))
                .childMap(this.mapRepository.findOne(input.getChildMap().getId()))
                .comment(input.getComment())
                .relationshipType(input.getRelationshipType())
                .build();
        return this.mapLinkRepository.save(link);

    }

    public void deleteMapLinksByParentMap(Long mapId) {
        this.mapLinkRepository.deleteByParentMap_Id(mapId);
    }

    public void deleteMapLinksByChildMap(Long mapId) {
        this.mapLinkRepository.deleteByChildMap_Id(mapId);
    }
}
