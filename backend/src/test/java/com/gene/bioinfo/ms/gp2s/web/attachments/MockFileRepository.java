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

package com.gene.bioinfo.ms.gp2s.web.attachments;

import com.gene.bioinfo.ms.gp2s.repository.FileRepository;
import com.gene.bioinfo.ms.gp2s.service.attachment.File;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Profile("integration-test")
public class MockFileRepository implements FileRepository {

    private final Map<String, File> files = new ConcurrentHashMap<>();
    private final Map<String, byte[]> content = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(0);

    @Override
    @NonNull
    public String store(@NonNull final File file) {
        try {
            final String next = String.valueOf(sequence.incrementAndGet());
            files.put(next, file);
            content.put(next, IOUtils.toByteArray(file.getContentStream()));
            file.setLength(new Integer(content.get(next).length).longValue());
            return next;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File findOne(@NonNull final String fileId) {
        final File file = files.get(fileId);
        if (file == null) {
            return null;
        }
        file.setContentStream(new ByteArrayInputStream(content.get(fileId)));
        return file;
    }

    @Override
    public void delete(@NonNull final String mongoId) {
        files.remove(mongoId);
        content.remove(mongoId);
    }
}
