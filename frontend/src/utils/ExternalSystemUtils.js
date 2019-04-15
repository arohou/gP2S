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

export function projectManagementSystemExists () {
  return !!process.env.PROJECT_MANAGEMENT_SYSTEM_EXISTS
}

export function projectManagementSystemName () {
  return process.env.PROJECT_MANAGEMENT_SYSTEM_EXISTS
    ? process.env.PROJECT_MANAGEMENT_SYSTEM_NAME : 'Project Management System'
}

export function proteinRegistrationSystemName () {
  return process.env.PROTEIN_REGISTRATION_SYSTEM_EXISTS
    ? process.env.PROTEIN_REGISTRATION_SYSTEM_NAME : 'Protein Registration System'
}

export function proteinRegistrationSystemExists () {
  return !!process.env.PROTEIN_REGISTRATION_SYSTEM_EXISTS
}

export function ligandRegistrationSystemExists () {
  return !!process.env.LIGAND_REGISTRATION_SYSTEM_EXISTS
}

export function proteinRegistrationConfig () {
  let env = process.env
  return {
    systemName: proteinRegistrationSystemName(),
    purIdLabelShort: process.env.PROTEIN_REGISTRATION_SYSTEM_EXISTS && env.PROTEIN_REGISTRATION_PUR_LABEL_SHORT
      ? env.PROTEIN_REGISTRATION_PUR_LABEL_SHORT : 'pur-id'
  }
}

export function ligandRegistrationConfig () {
  let env = process.env
  return {
    systemName: env.LIGAND_REGISTRATION_SYSTEM_EXISTS && env.LIGAND_REGISTRATION_SYSTEM_NAME ? env.LIGAND_REGISTRATION_SYSTEM_NAME : 'Ligand Registration System',
    conceptIdLabel: env.LIGAND_REGISTRATION_SYSTEM_EXISTS && env.LIGAND_REGISTRATION_CONCEPT_ID_LABEL ? env.LIGAND_REGISTRATION_CONCEPT_ID_LABEL : 'Concept ID',
    batchIdLabel: env.LIGAND_REGISTRATION_SYSTEM_EXISTS && env.LIGAND_REGISTRATION_BATCH_ID_LABEL ? env.LIGAND_REGISTRATION_BATCH_ID_LABEL : 'Batch ID',
    batchIdLabelShort: env.LIGAND_REGISTRATION_SYSTEM_EXISTS && env.LIGAND_REGISTRATION_BATCH_ID_LABEL_SHORT ? env.LIGAND_REGISTRATION_BATCH_ID_LABEL_SHORT : 'batch-id',
    batchIdLabelListItem: env.LIGAND_REGISTRATION_SYSTEM_EXISTS && env.LIGAND_REGISTRATION_BATCH_ID_LABEL_LIST_ITEM ? env.LIGAND_REGISTRATION_BATCH_ID_LABEL_LIST_ITEM : 'Batch ID'
  }
}

export function registrationConfig () {
  return {
    protein: proteinRegistrationConfig(),
    ligand: ligandRegistrationConfig()
  }
}
