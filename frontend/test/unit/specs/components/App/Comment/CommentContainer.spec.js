import Vue from 'vue'
import * as sinon from 'sinon'

import CommentContainer from '@/components/App/Comment/CommentContainer'
import CommentsUtil from '@/components/App/Comment/Comments'
import CommentService from '@/services/CommentService'

const sandbox = sinon.sandbox.create()

describe('CommentContainer', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(CommentContainer)
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

  describe('data creation', () => {
    it('sets comments to an empty array', () => {
      expect(vm.comments).to.be.deep.equal([])
    })

    it('sets unixId to null', () => {
      expect(vm.unixId).to.be.null
    })
  })

  describe('entityId watcher', () => {
    it('calls loadComments with both old and new values of entityId', async () => {
      // given
      const expected = 1337
      const spy = sandbox.stub(vm, 'loadComments')

      // when
      vm.entityId = expected
      await vm.$nextTick()

      // then
      expect(spy).to.have.been.calledWith(null, expected)
    })
  })

  describe('actionsVisible', () => {
    beforeEach(function () {
      if (!process.env.SECURITY_ENABLED) {
        this.skip()
      }
    })

    it('returns true if comment creator is the same as unixId', () => {
      // given
      const expected = 1337
      vm.unixId = expected

      // when
      const result = vm.actionsVisible({createdBy: expected})

      // then
      result.should.be.true
    })

    it('returns false if comment creator different than unixId', () => {
      // given
      vm.unixId = 1337

      // when
      const result = vm.actionsVisible({createdBy: 'different'})

      // then
      result.should.be.false
    })
  })

  describe('created', () => {
    it('creates _commentService', () => {
      vm._commentService.should.be.instanceOf(CommentService)
    })
  })

  describe('loadComments', () => {
    it('breaks early if entityType is falsy', () => {
      // given
      const spy = sandbox.stub(vm._commentService, 'listComments')

      // when
      vm.loadComments(null, 'ignored')
      vm.loadComments(undefined, 'ignored')
      vm.loadComments('')

      // then
      spy.should.not.have.been.called
    })

    it('breaks early if entityId is falsy', () => {
      // given
      const spy = sandbox.stub(vm._commentService, 'listComments')

      // when
      vm.loadComments('non falsy', null)
      vm.loadComments('non falsy', undefined)
      vm.loadComments('non falsy', '')

      // then
      spy.should.not.have.been.called
    })

    it('calls _commentService.listComments with given arguments', () => {
      // given
      const spy = sandbox.stub(vm._commentService, 'listComments').resolves()
      const expected = 1337

      // when
      vm.loadComments(expected, expected)

      // then
      spy.should.have.been.calledWith(expected, expected)
    })

    it('sets comments with the result of _commentService.listComments', async () => {
      // given
      const expected = 1337
      sandbox.stub(vm._commentService, 'listComments')
        .resolves({data: expected})

      // when
      vm.loadComments(expected, expected)
      await vm.$nextTick()

      // then
      vm.comments.should.equal(expected)
    })

    it('logs error from _commentService.listComments', async () => {
      // given
      const expected = 1337
      sandbox.stub(vm._commentService, 'listComments')
        .rejects(expected)
      const spy = sandbox.stub(vm.$log, 'error')

      // when
      vm.loadComments(expected, expected)
      await vm.$nextTick()

      // then
      spy.should.have.been.calledWith(expected)
    })
  })

  describe('commentAdded', () => {
    it('appends given comment to comments', () => {
      // given
      vm.comments = []
      const expected = 1337

      // when
      vm.commentAdded(expected)

      // then
      vm.comments.should.deep.equal([expected])
    })
  })

  describe('commentUpdated', () => {
    it('appends given comment to comments', () => {
      // given
      const expected = 1337
      vm.comments = expected
      const spy = sandbox.stub(CommentsUtil, 'replaceUpdatedComment')
        .returns(expected)

      // when
      vm.commentUpdated(expected)

      // then
      spy.should.have.been.calledWith(expected, expected)
      vm.comments.should.deep.equal(expected)
    })
  })

  describe('commentDeleted', () => {
    it('removes comment with given id', () => {
      // given
      const expected = 1337
      vm.comments = [
        {id: expected},
        {id: 'not expected'}
      ]

      // when
      vm.commentDeleted(expected)

      // then
      vm.comments.should.deep.equal([{id: 'not expected'}])
    })

    it('does nothing if given id is not in comments', () => {
      // given
      const expected = [
        {id: 'not expected'},
        {id: 'another not expected'},
      ]
      vm.comments = expected

      // when
      vm.commentDeleted(1337)

      // then
      vm.comments.should.deep.equal(expected)
    })
  })

  describe('mountEvents', () => {
    it('listens to commentAdded', () => {
      // given
      const spy = sandbox.stub(vm.$events, 'on')
      sandbox.stub(vm, 'loadComments')

      // when
      vm.mountEvents()

      // then
      spy.should.have.been.calledWith('commentAdded', vm.commentAdded)
    })

    it('listens to commentUpdated', () => {
      // given
      const spy = sandbox.stub(vm.$events, 'on')
      sandbox.stub(vm, 'loadComments')

      // when
      vm.mountEvents()

      // then
      spy.should.have.been.calledWith('commentUpdated', vm.commentUpdated)
    })

    it('listens to commentDeleted', () => {
      // given
      const spy = sandbox.stub(vm.$events, 'on')
      sandbox.stub(vm, 'loadComments')

      // when
      vm.mountEvents()

      // then
      spy.should.have.been.calledWith('commentDeleted', vm.commentDeleted)
    })

    it('listens to reloadComments', () => {
      // given
      sandbox.stub(vm, 'loadComments')
      const spy = sandbox.stub(vm.$events, 'on').callsFake(vm.loadComments)
      const expected = 1337
      vm.entityType = expected
      vm.entityId = expected

      // when
      vm.mountEvents()

      // then
      spy.should.have.been.calledWith('reloadComments', sinon.match.func)
    })

    it('calls loadComments on the reloadComments event', async () => {
      // given
      const spy = sandbox.stub(vm, 'loadComments')
      const expected = 1337
      vm.entityType = expected
      vm.entityId = expected

      // when
      vm.$events.$emit('reloadComments')
      await vm.$nextTick()

      // then
      spy.should.have.been.calledWith(expected, expected)
    })
  })

  describe('removeEvents', () => {
    it('stops listening to commentAdded', () => {
      // given
      const spy = sandbox.stub(vm.$events, 'off')

      // when
      vm.removeEvents()

      // then
      spy.should.have.been.calledWith('commentAdded', vm.commentAdded)
    })

    it('stops listening to commentUpdated', () => {
      // given
      const spy = sandbox.stub(vm.$events, 'off')

      // when
      vm.removeEvents()

      // then
      spy.should.have.been.calledWith('commentUpdated')
    })

    it('stops listening to commentDeleted', () => {
      // given
      const spy = sandbox.stub(vm.$events, 'off')

      // when
      vm.removeEvents()

      // then
      spy.should.have.been.calledWith('commentDeleted', vm.commentDeleted)
    })

    it('stops listening to reloadComments', () => {
      // given
      const spy = sandbox.stub(vm.$events, 'off')

      // when
      vm.removeEvents()

      // then
      spy.should.have.been.calledWith('reloadComments')
    })
  })

  describe('mounted', () => {
    it('calls loadComments', () => {
      // given
      const expected = 1337
      vm.entityType = expected
      vm.entityId = expected
      const spy = sandbox.stub(vm, 'loadComments')
      sandbox.stub(vm, 'mountEvents')

      // when
      vm = vm.$mount()

      // then
      spy.should.have.been.calledWith(expected, expected)
    })

    it('calls mountEvents', () => {
      // given
      sandbox.stub(vm, 'loadComments')
      const spy = sandbox.stub(vm, 'mountEvents')

      // when
      vm = vm.$mount()

      // then
      spy.should.have.been.called
    })
  })

  describe('beforeDestroy', () => {
    it('calls removeEvents', () => {
      // given
      const spy = sandbox.stub(vm, 'removeEvents')

      // when
      vm.$destroy()

      // then
      spy.should.have.been.called
    })
  })
})
