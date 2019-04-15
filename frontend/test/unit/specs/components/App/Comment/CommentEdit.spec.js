import Vue from 'vue'
import * as sinon from 'sinon'

import CommentEdit from '@/components/App/Comment/CommentEdit'
import CommentService from '@/services/CommentService'

const sandbox = sinon.sandbox.create()

describe('CommentEdit', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(CommentEdit)
    vm = new Constructor({
      propsData: {
        entityId: null,
        entityType: null
      }
    })
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('created', () => {
    it('creates _commentService', () => {
      vm._commentService.should.be.instanceOf(CommentService)
    })
  })

  describe('save', () => {
    it('calls the _commentService.updateComment with the given arguments', () => {
      // given
      const expected = 1337
      const spy = sandbox.stub(vm._commentService, 'updateComment')

      // when
      vm.save(expected, expected)

      // then
      spy.should.have.been.calledWith(expected, expected)
    })
  })
})
