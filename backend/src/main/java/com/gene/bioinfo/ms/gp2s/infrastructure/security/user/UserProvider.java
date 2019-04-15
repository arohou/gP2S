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

package com.gene.bioinfo.ms.gp2s.infrastructure.security.user;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @author Przemysław Stankowski on 24.11.2017
 */
public interface UserProvider extends AuditorAware<String> {

    String getCurrentUser();

    List<GrantedAuthority> getCurrentUserRoles();

    Boolean getSecurityEnabled();
}
