import Vue from 'vue'
import * as sinon from 'sinon'

import LigandRegistrationInfo from '@/components/App/Ligand/registration_system_ext/LigandRegistrationInfo'
import LigandRegistrationService from '@/components/App/Ligand/registration_system_ext/LigandRegistrationService'
import { ligandRegistrationConfig } from '@/utils/ExternalSystemUtils'

const sandbox = sinon.sandbox.create()

describe('Protein/LigandRegistrationInfo.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(LigandRegistrationInfo)
    vm = new Constructor()
    vm = vm.$mount()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('created', () => {
    it('should assign the _ligandRegistrationService', () => {
      vm._ligandRegistrationService.should.be.instanceOf(LigandRegistrationService)
    })
  })

  describe('isErrorState', () => {
    it('should return true for the unknown state', () => {
      // given
      vm.state = 'unknown'

      // then
      expect(vm.isErrorState).to.be.true
    })

    it('should return true for the invalid state', () => {
      // given
      vm.state = 'invalid'

      // then
      expect(vm.isErrorState).to.be.true
    })

    it('should return false for the processing state', () => {
      // given
      vm.state = 'processing'

      // then
      expect(vm.isErrorState).to.be.false
    })

    it('should return false for the correct state', () => {
      // given
      vm.state = 'correct'

      // then
      expect(vm.isErrorState).to.be.false
    })
  })

  describe('ligandRegistrationLoaded', () => {
    it('should call setError with null', () => {
      // given
      const spy = sandbox.stub(vm, 'setError')
      sandbox.stub(vm, 'setMolecularWeight').resolves()
      sandbox.stub(vm, 'fetchStructureDrawing').resolves()
      vm.batchId = 1337

      // when
      vm.$events.$emit('ligandRegistrationLoaded', {molecularWeight: 1337, batchId: 1337})

      // then
      spy.should.have.been.calledWith(null)
    })

    it('should set ligandRegistrationData with the received argument', () => {
      // given
      sandbox.stub(vm, 'setError')
      sandbox.stub(vm, 'setMolecularWeight').resolves()
      sandbox.stub(vm, 'fetchStructureDrawing').resolves()
      const expected = {molecularWeight: 1337, batchId: 1337}
      vm.batchId = 1337

      // when
      vm.$events.$emit('ligandRegistrationLoaded', expected)

      // then
      vm.ligandRegistrationData.should.be.deep.equal(expected)
    })

    it('should call setMolecularWeight with the molecularWeight', async () => {
      // given
      const expected = 1337
      sandbox.stub(vm, 'setError')
      const spy = sandbox.stub(vm, 'setMolecularWeight').resolves()
      sandbox.stub(vm, 'fetchStructureDrawing').resolves()
      vm.batchId = expected

      // when
      vm.$events.$emit('ligandRegistrationLoaded', {molecularWeight: 1337, batchId: 1337})

      // then
      spy.should.have.been.calledWith(expected)
    })

    it('should call fetchStructureDrawing with id', () => {
      // given
      const expected = 1337
      sandbox.stub(vm, 'setError')
      sandbox.stub(vm, 'setMolecularWeight').resolves()
      const spy = sandbox.stub(vm, 'fetchStructureDrawing').resolves()
      vm.batchId = expected

      // when
      vm.$events.$emit('ligandRegistrationLoaded', {molecularWeight: 1337, batchId: 1337})

      // then
      spy.should.have.been.calledWith(expected)
    })

    it('should set state to correct', async () => {
      // given
      sandbox.stub(vm, 'setError')
      sandbox.stub(vm, 'setMolecularWeight').resolves()
      sandbox.stub(vm, 'fetchStructureDrawing').resolves()
      vm.batchId = 1337

      // when
      vm.$events.$emit('ligandRegistrationLoaded', {molecularWeight: 1337, batchId: 1337})
      await vm.$nextTick()
      await vm.$nextTick()

      // then
      vm.state.should.be.equal('correct')
    })
  })

  describe('ligandRegistrationServerError', () => {
    it('sets state to unknown and sets error', () => {
      // given
      const expected = 1337
      vm.conceptId = expected

      // when
      vm.$events.emit('ligandRegistrationServerError', expected, null, expected)

      // then
      expect(vm.state).to.be.equal('unknown')
      expect(vm.error).to.be.equal(expected)
    })
  })

  describe('conceptIdentifierInvalid', () => {
    it('sets state to invalid and error to "Invalid CONCEPT identifier"', () => {
      // when
      const expected = 'Invalid ' + ligandRegistrationConfig.systemName + ' identifier'
      vm.conceptId = expected
      vm.$events.emit('conceptIdentifierInvalid', expected, null, expected)

      // then
      expect(vm.state).to.be.equal('invalid')
      expect(vm.error).to.be.equal(expected)
    })
  })

  describe('ligandRegistrationProcessing', () => {
    it('sets state to processing and clears error', () => {
      // given
      vm.batchId = 'test-g'
      vm.state = 'should be changed'
      vm.error = 'should be cleared'

      // when
      vm.$events.emit('ligandRegistrationProcessing', null, 'test-g')

      // then
      expect(vm.state).to.be.equal('processing')
      expect(vm.error).to.be.null
    })
  })

  describe('fetchStructureDrawing', () => {
    it('should call _ligandRegistrationService.findBase64LigandPicture with the given id and batch id', async () => {
      // given
      const expected = 1337
      vm.batchId = expected
      sandbox.stub(vm._ligandRegistrationService, 'findBase64LigandPicture').resolves({data: expected})

      // when
      await vm.fetchStructureDrawing(expected)

      // then
      vm.structureDrawing.should.be.equal(expected)
    })
  })

  describe('setMolecularWeight', () => {
    it('should early break rejecting if the downloaded LigandRegistrationData has no molecularWeight', () => {
      // then
      expect(vm.setMolecularWeight(1337)).to.be.rejected
    })

    it('should set molecularWeight with the result of the _ligandRegistrationService.findLigand call', async () => {
      // given
      const expected = 1337

      // when
      await vm.setMolecularWeight(expected)

      // then
      vm.molecularWeight.should.be.equal(expected)
    })
  })

  describe('setError', () => {
    it('should set error to null when given a null', () => {
      // when
      vm.setError(null)

      // then
      expect(vm.error).to.be.null
    })

    it('should append error in a new line when some error already exists', () => {
      // given
      const expected = 'Some error description'
      vm.error = expected

      // when
      vm.setError(new Error(expected))

      // then
      vm.error.should.be.equal([expected, 'Error: ' + expected].join('\n'))
    })

    it('should set the given error when no previous error was set', () => {
      // given
      const expected = 'Some error description'

      // when
      vm.setError(new Error(expected))

      // then
      vm.error.should.be.equal('Error: ' + expected)
    })
  })
})
