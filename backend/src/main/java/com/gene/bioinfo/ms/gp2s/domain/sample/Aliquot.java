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
import com.gene.bioinfo.ms.gp2s.domain.Protein;
import com.gene.bioinfo.ms.gp2s.domain.base.EntityType;
import lombok.*;

import java.util.Optional;

@Getter
@ToString
@NoArgsConstructor
public class Aliquot {

    private Long id;
    private String label;
    private String slug;
    private Boolean availableForSampleMaking;
    private String reference;
    private EntityType type;

    @Builder
    public Aliquot(Long id, String label, String slug, Boolean availableForSampleMaking, String reference, EntityType
            type) {
        this.id = id;
        this.label = label;
        this.slug = slug;
        this.availableForSampleMaking = availableForSampleMaking;
        this.reference = reference;
        this.type = type;
    }

    public static Aliquot fromProtein(@NonNull Protein protein) {
        return builder().id(protein.getId())
                        .label(protein.getLabel())
                        .slug(protein.getSlug())
                        .availableForSampleMaking(protein.getAvailableForSampleMaking())
                        .reference(protein.getPurificationId())
                        .type(EntityType.PROTEIN)
                        .build();
    }

    public static Aliquot fromLigand(@NonNull Ligand ligand) {
        return builder().id(ligand.getId())
                        .label(ligand.getLabel())
                        .slug(ligand.getSlug())
                        .availableForSampleMaking(ligand.getAvailableForSampleMaking())
                        .reference(ligand.getBatchId())
                        .type(EntityType.LIGAND)
                        .build();
    }

    public boolean matchesQuery(@NonNull String queryString) {
        String queryLowerCase = queryString.toLowerCase();
        if (this.label.toLowerCase()
                      .contains(queryLowerCase)) {
            return true;
        }
        return Optional.ofNullable(this.reference)
                       .map(String::toLowerCase)
                       .map(ref -> ref.contains(queryLowerCase))
                       .orElse(false);
    }
}
