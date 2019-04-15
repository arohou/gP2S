import Vue from 'vue'
import * as sinon from 'sinon'

import ProteinComponent from '@/components/App/Sample/registration_system_ext/ProteinComponent'
import ConcentrationConversionService from '@/services/ConcentrationConversionService'

const sandbox = sinon.sandbox.create()

describe('Sample/protein_registration_system_ext/ProteinComponent.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(ProteinComponent)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('created', () => {
    it('should initialize _concentrationConversionService', () => {
      expect(vm._concentrationConversionService).to.be.instanceOf(ConcentrationConversionService)
    })
  })

  describe('performConversionToMgPerMl', () => {
    it('should set component.finalConcentrationInMgPerMl with the result of _concentrationConversionService.uMToMgPerMl', () => {
      // given
      const expected = 1337
      sandbox.stub(vm, 'conversionCannotBePerformed').returns(false)
      sandbox.stub(vm._concentrationConversionService, 'uMToMgPerMl').returns(expected)
      vm.component = {}

      // when
      vm.performConversionToMgPerMl()

      // then
      vm.component.finalConcentrationInMgPerMl.should.be.equal(expected)
    })
  })

  describe('performConversionToUm', () => {
    it('should set component.finalConcentration with the result of _concentrationConversionService.mgPerMlToUm', () => {
      // given
      const expected = 1337
      sandbox.stub(vm, 'conversionCannotBePerformed').returns(false)
      sandbox.stub(vm._concentrationConversionService, 'mgPerMlToUm').returns(expected)
      vm.component = {}

      // when
      vm.performConversionToUm()

      // then
      vm.component.finalConcentration.should.be.equal(expected)
    })
  })
})
