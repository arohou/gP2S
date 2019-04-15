import Vue from 'vue'
import * as sinon from 'sinon'

import DashboardListItem from '@/components/App/DashboardListItem'

describe('DashboardListItem.vue', () => {
  let vm = null
  const sandbox = sinon.sandbox.create()

  beforeEach(() => {
    const Constructor = Vue.extend(DashboardListItem)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('commentsCount', () => {
    it('should return 0 if entityType is not set', async () => {
      // given
      vm.entityType = null
      vm.entity = {}
      vm._commentService = {
        countComments: sandbox.stub().rejects()
      }

      // when
      await vm.$nextTick()

      // then
      vm.commentsCount.should.be.equal(0)
    })

    it('should return 0 if entity is not set', async () => {
      // given
      vm.entityType = 'doesn\'t matter'
      vm.entity = null
      vm._commentService = {
        countComments: sandbox.stub().rejects()
      }

      // when
      await vm.$nextTick()

      // then
      vm.commentsCount.should.be.equal(0)
    })

    it('should return result of call to commentService.countComments', async () => {
      // given
      const expected = 1337
      vm.entityType = expected.toString()
      vm.entity = {id: expected}
      vm._commentService = {
        countComments: sandbox.stub().resolves({data: expected})
      }

      // when
      await vm.$nextTick()
      await vm.$nextTick()

      // then
      vm.commentsCount.should.be.equal(expected)
      vm._commentService.countComments
        .should.have.been.calledWith(expected.toString(), expected)
    })

    it('should log error of commentService.countComments', async () => {
      // given
      const expected = 1337
      vm.entityType = 'anything'
      vm.entity = {id: expected}
      vm._commentService = {
        countComments: sandbox.stub().rejects(expected)
      }
      const spy = sandbox.stub(vm.$log, 'error')

      // when
      await vm.$nextTick()
      await vm.$nextTick()

      // then
      spy.should.have.been.calledWith(expected)
    })

    it('should return 0 if error in commentService.countComments', async () => {
      // given
      vm.entityType = 'anything'
      vm.entity = {id: 1337}
      vm._commentService = {
        countComments: sandbox.stub().rejects()
      }
      sandbox.stub(vm.$log, 'error')

      // when
      await vm.$nextTick()

      // then
      vm.commentsCount.should.be.equal(0)
    })
  })

})
