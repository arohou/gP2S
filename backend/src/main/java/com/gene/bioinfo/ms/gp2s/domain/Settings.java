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

package com.gene.bioinfo.ms.gp2s.domain;

import com.gene.bioinfo.ms.gp2s.domain.base.BaseAuditableEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "settings")
@Entity
public class Settings extends BaseAuditableEntity {

    @Column(name = "pattern_for_data_storage_directory_name", nullable = false)
    private String patternForDataStorageDirectoryName =
            "${MicroscopyStartDate}_${ProjectLabel}_${MicroscopeLabel}_${GridID}_${MicroscopySessionID}";

    @Builder
    public Settings(
            Long id, Integer version,
            Date createdDate, Date modifiedDate, String createdBy,
            String modifiedBy, String patternForDataStorageDirectoryName)
    {
        super(id, version, createdDate, modifiedDate, createdBy, modifiedBy);
        this.patternForDataStorageDirectoryName = patternForDataStorageDirectoryName;
    }
}
