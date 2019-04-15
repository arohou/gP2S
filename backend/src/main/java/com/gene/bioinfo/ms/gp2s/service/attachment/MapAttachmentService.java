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

package com.gene.bioinfo.ms.gp2s.service.attachment;

import com.gene.bioinfo.ms.gp2s.domain.Map;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import com.gene.bioinfo.ms.gp2s.repository.MapRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapAttachmentService extends SingleAttachmentService implements ISingleAttachmentService {
    private MapRepository mapRepository;

    public MapAttachmentService(@NonNull final FileRepository fileRepository,
                                @NonNull final MapRepository mapRepository) {
        super(fileRepository);
        this.mapRepository = mapRepository;
    }

    public void deleteFile(@NonNull final String fileId) {
        removeAttachmentInfoFromMaps(mapRepository, fileId);
        super.deleteFile(fileId);
    }

    protected void removeAttachmentInfoFromMaps(MapRepository mapRepository, String mongoId) {
        final List<Map> maps = mapRepository.findAllByAttachmentMongoId(mongoId);
        maps.stream().forEach(map -> {
            map.setAttachmentFileName(null);
            map.setAttachmentMongoId(null);
        });
        mapRepository.save(maps);
    }
}
