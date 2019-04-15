import Vue from 'vue'
import * as sinon from 'sinon'

import BaseForm from '@/components/App/Protein/registration_system_ext/BaseForm'
import ConcentrationType from '@/components/App/ConcentrationType'
import DialogsService from '@/services/DialogsService'

const sandbox = sinon.sandbox.create()

describe('Protein/BaseForm.vue', () => {
  let vm = null

  const DATA = {
    projectSlugOrId: 'project-1',
    protein: {
      id: 1,
      slug: 'protein-label',
      label: 'Protein label',
      purificationId: '333',
      tubeLabel: 'T333',
      concentration: {
        concentrationType: ConcentrationType.Concentration,
        concentrationFactor: '0.2',
        isDilutedOrConcentrated: true
      }
    }
  }

  beforeEach(() => {
    const Constructor = Vue.extend(BaseForm)
    vm = new Constructor({
      propsData: {
        protein: DATA.protein,
        projectId: DATA.projectSlugOrId,
        template: {}
      }
    })
    vm = vm.$mount()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('proteinRegistrationSystemPurError', () => {
    it('calls setProteinRegistrationSystemError with the one received', () => {
      // given
      const expected = 'error message'
      const spy = sandbox.spy(vm, 'setProteinRegistrationSystemError')

      // when
      vm.handleProteinRegistrationSystemError({message: expected})

      // then
      expect(spy).to.have.been.calledWith(expected)
    })
  })

  describe('proteinRegistrationSystemPurInvalid', () => {
    it('calls setProteinRegistrationSystemError with the one received', () => {
      // given
      const expected = 'Invalid PUR ID'
      const spy = sandbox.spy(vm, 'setProteinRegistrationSystemError')

      // when
      vm.handleProteinRegistrationSystemError({purId: 11111})

      // then
      expect(spy).to.have.been.calledWith(expected)
    })
  })

  describe('proteinRegistrationSystemPurLoaded', () => {
    it('calls setProteinRegistrationSystemError with the one received', () => {
      // given
      const spy = sandbox.spy(vm, 'setProteinRegistrationSystemError')

      // when
      vm.proteinRegistrationSystemPurLoaded(123)

      // then
      expect(spy).to.have.been.calledWith(null)
    })
  })

  describe('setProteinRegistrationSystemError', () => {
    it('sets errors.purificationId to the given error', () => {
      // given
      const expected = 1337

      // when
      vm.setProteinRegistrationSystemError(expected)

      // then
      expect(vm.errors.purificationId).to.be.equal(expected)
    })
  })

  describe('displayNoMolecularWeightInRegistrationSystemDialog', () => {
    it('should not be called from purHasNoMolecularWeight when purId doesn\'t match', () => {
      // given
      vm.protein = {purificationId: 'some-value'}
      const spy = sandbox.stub(vm, 'displayNoMolecularWeightInRegistrationSystemDialog')

      // when
      vm.$events.$emit('purHasNoMolecularWeight', 'different-value')

      // then
      spy.should.not.have.been.called
    })

    it('should be called from purHasNoMolecularWeight when purId matches', () => {
      // given
      const purId = 1337
      vm.protein = {purificationId: 1337}
      const spy = sandbox.stub(vm, 'displayNoMolecularWeightInRegistrationSystemDialog')

      // when
      vm.$events.$emit('purHasNoMolecularWeight', purId)

      // then
      spy.should.have.been.called
    })

    it('should call DialogService.showConfirmationDialog', () => {
      // given
      const spy = sandbox.stub(DialogsService, 'showConfirmDialog').resolves()
      sandbox.stub(vm, 'showMolecularWeightError')

      // when
      vm.displayNoMolecularWeightInRegistrationSystemDialog()

      // then
      spy.should.have.been.called
    })

    it('should call showMolecularWeightError after dialog finishes', async () => {
      // given
      sandbox.stub(DialogsService, 'showConfirmDialog').resolves()
      const spy = sandbox.stub(vm, 'showMolecularWeightError')

      // when
      await vm.displayNoMolecularWeightInRegistrationSystemDialog()

      // then
      spy.should.have.been.called
    })
  })

  describe('shouldProceedWithSaving', () => {
    it('should break early if the purId argument is falsy', () => {
      // given
      const spy = sandbox.stub(vm, '_proteinService').resolves()

      // when
      vm.shouldProceedWithSaving()
      vm.shouldProceedWithSaving(null)
      vm.shouldProceedWithSaving('')

      // then
      spy.should.not.have.been.called
    })
  })
})
