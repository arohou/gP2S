import Vue from 'vue'
import * as sinon from 'sinon'

import ListItem from '@/components/App/Protein/registration_system_ext/ListItem'
import ConcentrationConversionService from '@/services/ConcentrationConversionService'

const sandbox = sinon.sandbox.create()

describe('Protein/registration_system_ext/ListItem.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(ListItem)
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
    it('should set entity.concentration with the result of _concentrationConversionService.uMToMgPerMl', () => {
      // given
      const expected = 1337
      sandbox.stub(vm._concentrationConversionService, 'uMToMgPerMl').returns(expected)
      vm.entity = {concentration: 'anything'}

      // when
      vm.performConversionToMgPerMl()

      // then
      vm.concentrationMgMl.should.be.equal(expected)
    })
  })
})
