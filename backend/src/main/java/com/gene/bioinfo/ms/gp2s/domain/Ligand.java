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
import com.gene.bioinfo.ms.gp2s.infrastructure.constants.DomainConstants;
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
@Table(name = "ligand")
@Entity
public class Ligand extends BaseAliquot {

    @Column(name = "concept_id")
    private String conceptId;

    @Column(name = "batch_id")
    private String batchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ligand_enabled_fields")
    private LigandEnabledFields ligandEnabledFields;

    public enum LigandEnabledFields {
        ALL,
        CONCEPT,
        BATCH
    }

    @Column(name = "solvent", length = DomainConstants.SHORT_STRING_LENGTH)
    private String solvent;

    @Column(name = "concentration")
    private Float concentration;

    @OrderBy("id")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "project_ligand",
            joinColumns = {@JoinColumn(name = "ligand_id")},
            foreignKey = @ForeignKey(name = "project_ligand_to_ligand_fk"),
            inverseJoinColumns = {@JoinColumn(name = "project_id")},
            inverseForeignKey = @ForeignKey(name = "project_ligand_to_project_fk"),
            uniqueConstraints = {@UniqueConstraint(name = "project_ligand_unique_constraint",
                    columnNames = {"project_id", "ligand_id"})})
    private List<Project> projects;

    @Builder
    public Ligand(Long id, String label, String slug, Integer version,
                  Date createdDate, Date modifiedDate, String createdBy, String modifiedBy,
                  String tubeLabel, Boolean availableForSampleMaking, Float concentration,
                  String conceptId, String solvent, String batchId, LigandEnabledFields ligandEnabledFields,
                  List<Project> projects) {
        super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy, tubeLabel, availableForSampleMaking);
        this.projects = projects;
        this.conceptId = conceptId;
        this.batchId = batchId;
        this.ligandEnabledFields = ligandEnabledFields;
        this.solvent = solvent;
        this.concentration = concentration;
    }

    public Ligand(final Ligand other) {
        super(other);
        this.conceptId = other.conceptId;
        this.batchId = other.batchId;
        this.ligandEnabledFields = other.ligandEnabledFields;
        this.solvent = other.solvent;
        this.projects = other.projects;
        this.concentration = other.concentration;
    }
}
