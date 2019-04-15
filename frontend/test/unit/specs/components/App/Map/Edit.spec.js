import Vue from 'vue'
import * as sinon from 'sinon'

import Edit from '@/components/App/Map/Edit'

const sandbox = sinon.sandbox.create()

describe('Map/Edit.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(Edit)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('confirmDeletingMap', () => {
    it('should break early when confirmDeletionCalled is true', async () => {
      // given
      vm.confirmDeletionCalled = true
      const spy = sandbox.stub(vm, '$confirm').resolves()

      // when
      const result = await vm.confirmDeletingMap()

      // then
      spy.should.not.have.been.called
      result.should.be.true
    })

    it('should return false if confirm chooses Keep', async () => {
      // given
      sandbox.stub(vm, '$confirm').resolves()

      // when
      const result = await vm.confirmDeletingMap()

      // then
      result.should.be.false
    })

    it('should set confirmDeletionCalled to true when confirm chooses Delete', async () => {
      // given
      sandbox.stub(vm, '$confirm').rejects()

      // when
      await vm.confirmDeletingMap()

      // then
      vm.confirmDeletionCalled.should.be.true
    })

    it('should return true if confirm chooses Delete', async () => {
      // given
      sandbox.stub(vm, '$confirm').rejects()

      // when
      const result = await vm.confirmDeletingMap()

      // then
      result.should.be.true
    })
  })

  describe('confirmDeletionCalled prop', () => {
    it('should be false by default', () => {
      vm.confirmDeletionCalled.should.be.false
    })
  })
})
