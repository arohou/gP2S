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

import com.gene.bioinfo.ms.gp2s.domain.Model;
import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import com.gene.bioinfo.ms.gp2s.repository.ModelRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelAttachmentService extends SingleAttachmentService implements ISingleAttachmentService {
    private ModelRepository modelRepository;

    public ModelAttachmentService(final FileRepository fileRepository,
                                  @NonNull final ModelRepository modelRepository) {
        super(fileRepository);
        this.modelRepository = modelRepository;
    }

    public void deleteFile(@NonNull final String fileId) {
        removeAttachmentInfoFromMaps(modelRepository, fileId);
        super.deleteFile(fileId);
    }

    protected void removeAttachmentInfoFromMaps(@NonNull final ModelRepository modelRepository,
                                                @NonNull final String mongoId) {
        final List<Model> models = modelRepository.findAllByAttachmentMongoId(mongoId);
        models.stream().forEach(model -> {
            model.setAttachmentFileName(null);
            model.setAttachmentMongoId(null);
        });
        modelRepository.save(models);
    }
}
