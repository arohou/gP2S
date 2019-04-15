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

import com.gene.bioinfo.ms.gp2s.domain.base.BaseSlugAndLabelEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of= {}, callSuper = true)
@Table(name="surface_treatment_protocol")
@Entity
public class SurfaceTreatmentProtocol extends BaseSlugAndLabelEntity {

	private Float duration;
	
	private Float pressure;
	
	private Float current;
	
	@Column(name = "chemicals_in_atmosphere")
	private String chemicalsInAtmosphere;

	@Column(name = "polarity")
	private Polarity polarity;

	@ManyToOne
	@JoinColumn(name = "surface_treatment_machine_id", foreignKey = @ForeignKey(name = "surface_treatment_protocol_to_surface_treatment_machine_fk"))
	private SurfaceTreatmentMachine machine;

	public enum Polarity {
		Negative, Positive
	}

	@Builder
	public SurfaceTreatmentProtocol(Long id, String label, String slug, Integer version,
            Date createdDate, Date modifiedDate, String createdBy,
            String modifiedBy, Float duration, Float pressure, Float current, String chemicalsInAtmosphere,
            SurfaceTreatmentMachine machine, Polarity polarity) {
		super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);
		this.duration = duration;
		this.pressure = pressure;
		this.current = current;
		this.chemicalsInAtmosphere = chemicalsInAtmosphere;
		this.machine = machine;
		this.polarity = polarity;
	}
}
