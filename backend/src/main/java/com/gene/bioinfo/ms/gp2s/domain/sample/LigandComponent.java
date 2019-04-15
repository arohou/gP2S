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

package com.gene.bioinfo.ms.gp2s.domain.sample;

import com.gene.bioinfo.ms.gp2s.domain.Ligand;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of= {}, callSuper = true)
@Table(name="ligand_component")
@Entity
public class LigandComponent extends BaseSampleComponent<Ligand> {
	
	@ManyToOne()
	@JoinColumn(name = "ligand_id", foreignKey = @ForeignKey(name = "ligand_component_to_ligand_fk"))
	private Ligand aliquot;
	
	@Builder
	public LigandComponent(Long id, Integer version, Date createdDate, Date modifiedDate,
                           String createdBy, String modifiedBy, Float finalConcentration, Ligand aliquot) {
		super(id, version, createdDate, modifiedDate, createdBy, modifiedBy, finalConcentration);
		this.aliquot = aliquot;
	}

	public LigandComponent(final LigandComponent other) {
		super(other);
		this.aliquot = other.aliquot;
	}
}
