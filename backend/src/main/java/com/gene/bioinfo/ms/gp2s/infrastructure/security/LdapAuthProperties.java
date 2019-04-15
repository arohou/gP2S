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

package com.gene.bioinfo.ms.gp2s.infrastructure.security;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "gp2s.auth.ldap")
@Slf4j
@Data
public class LdapAuthProperties {
    private String url;
    private String userSearchQuery;
    private String managerDn;
    private String managerPassword;

    @Getter(lazy = true)
    private final boolean isLdapAuthEnabled = areValidLdapPropertiesPresent();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "("
                    + "url=" + this.url
                    + ", managerDn=" + this.managerDn + ", managerPassword="
                        + (StringUtils.isNotEmpty(this.managerPassword) ? "<hidden>" : this.managerPassword)
                    + ", userSearchQuery=" + this.userSearchQuery
                + ")";
    }

    private boolean areValidLdapPropertiesPresent() {
        List<String> errors = new ArrayList<>();

        if (StringUtils.isEmpty(url)) {
            errors.add("LDAP url is required");
        }
        if (StringUtils.isEmpty(userSearchQuery)) {
            errors.add("User search query is required");
        }
        if (this.isOneOfTheCredentialsMissing()) {
            errors.add("Both the manager and managerPassword are required together");
        }

        errors.forEach(log::error);
        return errors.isEmpty();
    }

    private boolean isOneOfTheCredentialsMissing() {
        return ((StringUtils.isNotEmpty(managerDn) && StringUtils.isEmpty(managerPassword))
        || (StringUtils.isEmpty(managerDn) && StringUtils.isNotEmpty(managerPassword)));
    }
}

