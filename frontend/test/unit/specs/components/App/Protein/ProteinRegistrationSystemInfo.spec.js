import Vue from 'vue'
import * as sinon from 'sinon'

import ProteinRegistrationSystemInfo from '@/components/App/Protein/registration_system_ext/ProteinRegistrationSystemInfo'

const sandbox = sinon.sandbox.create()

describe('Protein/ProteinRegistrationSystemInfo.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(ProteinRegistrationSystemInfo)
    vm = new Constructor()
    vm = vm.$mount()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
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

  describe('proteinRegistrationSystemPurLoaded', () => {
    beforeEach(() => {
      sandbox.stub(vm, 'loadInfo')
    })

    it('sets state to correct and clears error', () => {
      // given
      vm.purId = 'test-id'
      vm.error = 'should be nulled'

      // when
      vm.$events.emit('proteinRegistrationSystemPurLoaded', 'test-id', '', '', '', '', '')

      // then
      expect(vm.state).to.be.equal('correct')
      expect(vm.error).to.be.null
    })

    it('calls loadInfo passing all args', () => {
      // given
      const expected = 1337
      vm.purId = expected

      // when
      vm.$events.emit('proteinRegistrationSystemPurLoaded',
        expected, expected, expected, expected, expected, expected)

      // then
      expect(vm.loadInfo).has.been.calledWith(
        expected, expected, expected, expected, expected, expected)
    })
  })

  describe('proteinRegistrationSystemServerError', () => {
    it('sets state to unknown and sets error', () => {
      // given
      vm.purId = 'test-id'

      // when
      const expected = 1337
      vm.$events.emit('proteinRegistrationSystemServerError', 'test-id', expected)

      // then
      expect(vm.state).to.be.equal('unknown')
      expect(vm.error).to.be.equal(expected)
    })
  })

  describe('proteinRegistrationSystemPurInvalid', () => {
    it('sets state to invalid and error to "Invalid PUR ID"', () => {
      // given
      const expected = 'Invalid PUR ID'
      vm.purId = 'test-id'

      // when
      vm.$events.emit('proteinRegistrationSystemPurInvalid', 'test-id')

      // then
      expect(vm.state).to.be.equal('invalid')
      expect(vm.error).to.be.equal(expected)
    })
  })

  describe('proteinRegistrationSystemProcessing', () => {
    it('sets state to processing and clears error', () => {
      // given
      vm.state = 'should be changed'
      vm.error = 'should be cleared'
      vm.purId = 'test-id'

      // when
      vm.$events.emit('proteinRegistrationSystemProcessing', 'test-id')

      // then
      expect(vm.state).to.be.equal('processing')
      expect(vm.error).to.be.null
    })
  })
})
