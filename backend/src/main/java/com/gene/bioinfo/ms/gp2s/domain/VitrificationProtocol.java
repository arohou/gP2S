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
@Table(name="vitrification_protocol")
@Entity
public class VitrificationProtocol extends BaseSlugAndLabelEntity {

	@Column(name = "relative_humidity")
	private Integer relativeHumidity;

	private Float temperature;

	@Column(name = "blot_time")
	private Float blotTime;

	@Column(name = "wait_time")
	private Float waitTime;

	@Column(name = "drain_time")
	private Float drainTime;

	@Column(name = "blot_force")
	private Integer blotForce;

	private String description;

	@Column(name = "number_of_blots")
	private Integer numberOfBlots;

	@Column(name = "number_of_sample_applications")
	private Integer numberOfSampleApplications;

	@ManyToOne
	@JoinColumn(name = "vitrification_machine_id", foreignKey = @ForeignKey(name = "vitrification_protocol_to_vitrification_machine_fk"))
	private VitrificationMachine vitrificationMachine;

	@ManyToOne
	@JoinColumn(name = "blotting_paper_id", foreignKey = @ForeignKey(name = "vitrification_protocol_to_blotting_paper_fk"))
	private BlottingPaper blottingPaper;

	@Builder
	public VitrificationProtocol(Long id, String label, String slug, Integer version,
                                 Date createdDate, Date modifiedDate, String createdBy,
                                 String modifiedBy, Integer relativeHumidity, Float temperature,
								 Float blotTime, Float waitTime, Float drainTime, Integer blotForce,
								 String description, Integer numberOfBlots, Integer numberOfSampleApplications,
								 VitrificationMachine vitrificationMachine, BlottingPaper blottingPaper) {
		super(id, label, slug, version, createdDate, modifiedDate, createdBy, modifiedBy);

		this.relativeHumidity = relativeHumidity;
		this.temperature = temperature;
		this.blotTime = blotTime;
		this.waitTime = waitTime;
		this.drainTime = drainTime;
		this.blotForce = blotForce;
		this.description = description;
		this.numberOfBlots = numberOfBlots;
		this.numberOfSampleApplications = numberOfSampleApplications;
		this.vitrificationMachine = vitrificationMachine;
		this.blottingPaper = blottingPaper;

	}
}
