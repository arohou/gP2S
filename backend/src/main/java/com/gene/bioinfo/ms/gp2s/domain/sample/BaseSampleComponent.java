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

import com.gene.bioinfo.ms.gp2s.domain.base.BaseAuditableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of= {}, callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseSampleComponent<T extends BaseAliquot> extends BaseAuditableEntity {

	@Column(name = "final_concentration")
	private Float finalConcentration;

	public abstract T getAliquot();
	
	public BaseSampleComponent(Long id, Integer version, Date createdDate, Date modifiedDate,
                               String createdBy, String modifiedBy, Float finalConcentration) {
		super(id, version, createdDate, modifiedDate, createdBy, modifiedBy);
		this.finalConcentration = finalConcentration;
	}

	public BaseSampleComponent(final BaseSampleComponent other) {
		super(other);
		this.finalConcentration = other.finalConcentration;
	}
}
