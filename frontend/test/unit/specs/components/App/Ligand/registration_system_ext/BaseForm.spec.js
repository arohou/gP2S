import Vue from 'vue'
import * as sinon from 'sinon'

import BaseForm from '@/components/App/Ligand/registration_system_ext/BaseForm'
import LigandEnabledFields from '@/components/App/Ligand/LigandEnabledFields'
import DialogsService from '@/services/DialogsService'

const sandbox = sinon.sandbox.create()

describe('Ligand/BaseForm.vue', () => {
  let vm = null

  const DATA = {
    projectSlugOrId: 'project-1',
    ligand: {
      id: 1,
      slug: 'ligand-label',
      label: 'Lignad Label',
      tubeLabel: 'Tube label',
      concentration: '0.3',
      conceptId: 'CONCEPT1',
      solvent: 'DMSO',
      ligandEnabledFields: LigandEnabledFields.CONCEPT,
      batchId: 'G00001234.1-1'
    }
  }

  beforeEach(() => {
    const Constructor = Vue.extend(BaseForm)
    vm = new Constructor({})
    vm.ligand = DATA.ligand
    vm = vm.$mount()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should update state for CONCEPT validation call', done => {
    Vue.nextTick().then(() => {
      let conceptId = '123'
      vm.validateConceptId(conceptId, () => {
      })

      vm.validateConceptId(null)
      expect(vm.conceptIdValidationState).to.be.null

      conceptId = DATA.ligand.conceptId
      vm.validConceptIds = {}
      vm.validConceptIds[DATA.ligand.conceptId] = DATA.ligand.batchId
      vm.validBatchIds = {}
      vm.validBatchIds[DATA.ligand.batchId] = DATA.ligand.conceptId
      vm.validateConceptId(conceptId)

      expect(vm.conceptIdValidationState).to.be.equal('correct')

      vm.validConceptIds = {}
      vm.conceptIdValidationState = 'unknown'
      vm.validateConceptId(conceptId)
      expect(vm.conceptIdValidationState).to.be.equal('processing')
    }).then(() => {
      vm.callLigandRegistration = sandbox.spy()

      vm.validateConceptId('123', () => {
      })
      return Vue.nextTick()
    }).then(() => {
      expect(vm.callLigandRegistration).to.have.been.calledOnce
    }).then(done, done)
  })

  describe('setConceptIdError', () => {
    it('sets errors.conceptId to the given error', () => {
      // given
      vm.errors = {}
      const expected = 1337

      // when
      vm.setConceptIdError(expected)

      // then
      expect(vm.errors.conceptId).to.be.equal(expected)
    })
  })

  describe('setBatchIdError', () => {
    it('sets errors.batchId to the given error', () => {
      // given
      vm.errors = {}
      const expected = 1337

      // when
      vm.setBatchIdError(expected)

      // then
      expect(vm.errors.batchId).to.be.equal(expected)
    })
  })

  describe('setLigandRegistrationRemoteValidationErrors', () => {
    it('sets batch id error and clears ligand registration error when BATCH field is enabled', () => {
      // given
      vm.errors = {}
      vm.ligand.ligandEnabledFields = LigandEnabledFields.CONCEPT
      const expected = 1337

      // when
      vm.setConceptIdError(expected)

      // then
      expect(vm.errors.batchId).to.be.null
      expect(vm.errors.conceptId).to.be.equal(expected)
    })

    it('clears batch id error and sets ligand registration error when CONCEPT field is enabled', () => {
      // given
      vm.errors = {}
      vm.ligand.ligandEnabledFields = LigandEnabledFields.BATCH
      const expected = 1337

      // when
      vm.setBatchIdError(expected)

      // then
      expect(vm.errors.conceptId).to.be.null
      expect(vm.errors.batchId).to.be.equal(expected)
    })
  })

  describe('displayNoMolecularWeightInCONCEPTDialog', () => {
    it('should call showMolecularWeightError after dialog closes', async () => {
      // given
      sandbox.stub(DialogsService, 'showConfirmDialog').resolves()
      const spy = sandbox.stub(vm, 'showMolecularWeightError')

      // when
      await vm.displayNoMolecularWeightInCONCEPTDialog()

      // then
      spy.should.have.been.called
    })
  })

  describe('showMolecularWeight', () => {
    it('should set conceptIdValidationState to invalid if ligand.conceptId is present', () => {
      // given
      vm.ligand = {conceptId: 'non null'}
      sandbox.stub(vm, 'setConceptIdError')

      // when
      vm.showMolecularWeightError()

      // then
      vm.conceptIdValidationState.should.equal('invalid')
    })

    it('should call setConceptIdError if ligand.conceptId is present', () => {
      // given
      vm.ligand = {conceptId: 'non null'}
      const spy = sandbox.stub(vm, 'setConceptIdError')

      // when
      vm.showMolecularWeightError()

      // then
      spy.should.have.been.called
    })

    it('should set batchIdValidationState to invalid if ligand.conceptId is not present', () => {
      // given
      vm.ligand = {batchId: 'non null'}
      sandbox.stub(vm, 'setBatchIdError')

      // when
      vm.showMolecularWeightError()

      // then
      vm.batchIdValidationState.should.equal('invalid')
    })

    it('should call setBatchIdError if ligand.conceptId is not present', () => {
      // given
      vm.ligand = {batchId: 'non null'}
      const spy = sandbox.stub(vm, 'setBatchIdError')

      // when
      vm.showMolecularWeightError()

      // then
      spy.should.have.been.called
    })
  })
})
