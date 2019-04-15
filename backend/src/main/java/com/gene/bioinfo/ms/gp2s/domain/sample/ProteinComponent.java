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

import com.gene.bioinfo.ms.gp2s.domain.Protein;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of= {}, callSuper = true)
@Table(name="protein_component")
@Entity
public class ProteinComponent extends BaseSampleComponent<Protein> {
	
	@ManyToOne()
	@JoinColumn(name = "protein_id", foreignKey = @ForeignKey(name = "protein_component_to_protein_fk"))
	private Protein aliquot;
	
	@Builder
	public ProteinComponent(Long id, Integer version, Date createdDate, Date modifiedDate,
							String createdBy, String modifiedBy, Float finalConcentration, Protein aliquot) {
		super(id, version, createdDate, modifiedDate, createdBy, modifiedBy, finalConcentration);
		this.aliquot = aliquot;
	}

	public ProteinComponent(final ProteinComponent other) {
		super(other);
		this.aliquot = other.aliquot;
	}
	
}
