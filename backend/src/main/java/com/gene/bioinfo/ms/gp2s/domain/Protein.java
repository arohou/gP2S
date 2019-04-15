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

import com.gene.bioinfo.ms.gp2s.domain.sample.BaseAliquot;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "protein")
@Entity
public class Protein extends BaseAliquot {

    @Column(name = "purification_id")
    private String purificationId;

    private Concentration concentration;

    @OrderBy("id")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "project_protein",
            joinColumns = {@JoinColumn(name = "protein_id")},
            foreignKey = @ForeignKey(name = "project_protein_to_protein_fk"),
            inverseJoinColumns = {@JoinColumn(name = "project_id")},
            inverseForeignKey = @ForeignKey(name = "project_protein_to_project_fk"),
            uniqueConstraints = {@UniqueConstraint(name = "project_protein_unique_constraint",
                    columnNames = {"project_id", "protein_id"})})
    private List<Project> projects;

    @Builder
    public Protein(Long id, String label, String slug, Integer version,
                   Date createdDate, Date modifiedDate, String createdBy, String modifiedBy, String tubeLabel,
                   Boolean availableForSampleMaking,
                   String purificationId, Concentration concentration,
                   List<Project> projects) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy, tubeLabel, availableForSampleMaking);
        this.projects = projects;
        this.purificationId = purificationId;
        this.concentration = concentration;
    }

    public Protein(final Protein other) {
        super(other);
        this.purificationId = other.purificationId;
        this.projects = other.projects;
        this.concentration = other.concentration;
    }
}
