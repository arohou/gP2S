import Vue from 'vue'
import * as sinon from 'sinon'
import DialogsService from '@/services/DialogsService'
import CommentService from '@/services/CommentService'
import EntityNotFoundError from '@/errors/EntityNotFoundError'

import CommentActions from '@/components/App/Comment/CommentActions'

const sandbox = sinon.sandbox.create()

describe('CommentActions', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(CommentActions)
    vm = new Constructor({
      propsData: {
        comment: {not: 'used'}
      }
    })
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('creates CommentService', () => {
    expect(vm._commentService).to.be.instanceOf(CommentService)
  })

  describe('showDeleteDialog', () => {
    it('calls DialogsService.showConfirmDialog', () => {
      // given
      const spy = sandbox.stub(DialogsService, 'showConfirmDialog').resolves()
      sandbox.stub(vm.deleteComment())

      // when
      vm.showDeleteDialog('whatever')

      // then
      expect(spy).to.have.been.calledWith('Are you sure you want to delete ?')
    })

    it('calls deleteComment when dialog returns', async () => {
      // given
      sandbox.stub(DialogsService, 'showConfirmDialog').resolves()
      const spy = sandbox.stub(vm, 'deleteComment')
      const expected = 1337

      // when
      vm.showDeleteDialog(expected)
      await vm.$nextTick()

      // then
      expect(spy).to.have.been.calledWith(expected)
    })

    it('doesn\'t call deleteComment when dialog is canceled', async () => {
      // given
      sandbox.stub(DialogsService, 'showConfirmDialog').rejects()
      const spy = sandbox.stub(vm, 'deleteComment')
      const expected = 1337

      // when
      await vm.showDeleteDialog(expected)

      // then
      expect(spy).to.not.have.been.calledWith(expected)
    })
  })

  describe('deleteComment', () => {
    it('calls _commentService.deleteComment with the given id', () => {
      // given
      const spy = sandbox.stub(vm._commentService, 'deleteComment')
        .returns(Promise.resolve())
      sandbox.stub(vm.$events, '$emit')
      const expected = 1337

      // when
      vm.deleteComment(expected)

      // then
      expect(spy).to.have.been.calledWith(expected)
    })

    it('emits commentDeleted with the given id', async () => {
      // given
      sandbox.stub(vm._commentService, 'deleteComment').resolves()
      const spy = sandbox.stub(vm.$events, '$emit')
      const expected = 1337

      // when
      await vm.deleteComment(expected)

      // then
      expect(spy).to.have.been.calledWith('commentDeleted', expected)
    })

    it('calls handleDeletingError on error', async () => {
      // given
      const spy = sandbox.stub(vm, 'handleDeletingError')
      const expected = 1337
      sandbox.stub(vm._commentService, 'deleteComment').rejects(expected)

      // when
      await vm.deleteComment('whatever')

      // then
      expect(spy).to.have.been.calledWith(expected)
    })
  })

  describe('handleDeletingError', () => {
    it('emits reloadComments if error is EntityNotFoundError', () => {
      // given
      const spy = sandbox.stub(vm.$events, '$emit')

      // when
      vm.handleDeletingError(new EntityNotFoundError())

      // then
      expect(spy).to.have.been.calledWith('reloadComments')
    })

    it('emits reloadComments if any other error', () => {
      // given
      const spy = sandbox.stub(DialogsService, 'showError')
      sandbox.stub(vm.$log, 'error')

      // when
      vm.handleDeletingError({
        response: {
          status: 'definitely not EntityNotFoundError'
        }
      })

      // then
      expect(spy).to.have.been.called
    })
  })

})
