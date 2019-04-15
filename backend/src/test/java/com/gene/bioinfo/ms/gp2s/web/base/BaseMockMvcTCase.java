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

package com.gene.bioinfo.ms.gp2s.web.base;

import com.gene.bioinfo.ms.gp2s.domain.base.BaseAuditableEntity;
import com.gene.bioinfo.ms.gp2s.infrastructure.security.user.DefaultUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Nullable;
import java.nio.charset.Charset;

/**
 * Base class for using {@link MockMvc}
 *
 * @author Cezary Krzyżanowski on 25.07.2017.
 */
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public abstract class BaseMockMvcTCase<T extends BaseAuditableEntity> {

    public static final MediaType JSON_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static final String DEFAULT_USER = DefaultUser.DEFAULT_USER_NAME;
    public final String URI = getUri();
    private MockMvc mockMvc;

    @NonNull
    public abstract String getUri();

    @NonNull
    public String getCreateUri() {
        return getUri();
    }

    public abstract T createEntity(@Nullable final Long id,
                                   @Nullable final String label,
                                   @Nullable final String slug);
}
