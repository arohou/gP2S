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

import com.gene.bioinfo.ms.gp2s.infrastructure.security.LdapAuthProperties;
import com.netflix.discovery.converters.Auto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Przemysław Stankowski on 24.11.2017
 */
@Component("userProvider")
public class SpringSecurityUserProvider implements UserProvider, AuditorAware<String> {


    private final LdapAuthProperties ldapAuthProperties;

    @Autowired
    public SpringSecurityUserProvider(LdapAuthProperties ldapAuthProperties) {
        this.ldapAuthProperties = ldapAuthProperties;
    }

    @Override
    public String getCurrentUser() {
        return Optional
                .ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User is not authenticated"))
                .getName();
    }

    @Override
    public List<GrantedAuthority> getCurrentUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ArrayList<>(auth.getAuthorities());
    }

    @Override
    public Boolean getSecurityEnabled() {
        return this.ldapAuthProperties.isLdapAuthEnabled();
    }

    @Override
    public String getCurrentAuditor() {
        if (getSecurityEnabled()) {
            return getCurrentUser();
        }
        return DefaultUser.DEFAULT_USER_NAME;
    }
}
