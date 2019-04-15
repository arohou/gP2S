import Vue from 'vue'
import * as sinon from 'sinon'

import CommentForm from '@/components/App/Comment/CommentForm'

const sandbox = sinon.sandbox.create()

describe('CommentForm', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(CommentForm)
    vm = new Constructor({
      propsData: {
        savedEventName: 'not used',
        showEventName: 'not used'
      }
    })
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('data', () => {
    it('sets data to default values', () => {
      vm.editedContent.should.be.equal('')
      vm.visible.should.be.false
      vm.saving.should.be.false
      vm.comment.should.be.deep.equal({
        createdBy: null,
        attachments: []
      })
      vm.saveInProgress.should.be.false
    })
  })

  describe('save', () => {
    it('should throw unimplemented error', () => {
      expect(() => vm.save('', '')).to.throw('Unimplemented method error!')
    })
  })

  describe('onCancel', () => {
    it('sets visible to false', () => {
      // given
      vm.visible = true

      // when
      vm.onCancel()

      // then
      vm.visible.should.be.false
    })
  })

  describe('onSubmit', () => {
    it('breaks early if editedContent is empty', () => {
      // given
      const spy = sandbox.stub(vm, 'startFormSaving')
      sandbox.stub(vm, 'save')
      vm.comment = {
        content: '      '
      }

      // when
      vm.onSubmit()

      // then
      spy.should.not.have.been.called
    })

    it('calls startFormSaving', () => {
      // given
      const spy = sandbox.stub(vm, 'startFormSaving')
      sandbox.stub(vm, 'save').resolves()
      vm.editedContent = 'edited'
      vm.$refs = {
        attachment: {
          checkAttachmentsModified: sandbox.stub(),
          submit: sandbox.stub()
        }
      }
      sandbox.stub(vm, 'stopFormSaving')

      // when
      vm.onSubmit()

      // then
      spy.should.have.been.called
    })

    it('sets comment.content with the given editedContent', () => {
      // given
      sandbox.stub(vm, 'startFormSaving')
      sandbox.stub(vm, 'save').resolves()
      const expected = 1337
      vm.editedContent = expected
      vm.comment = {}
      vm.$refs = {
        attachment: {
          checkAttachmentsModified: sandbox.stub(),
          submit: sandbox.stub()
        }
      }
      sandbox.stub(vm, 'stopFormSaving')

      // when
      vm.onSubmit()

      // then
      vm.comment.content.should.be.equal(expected)
    })

    it('calls save', () => {
      // given
      sandbox.stub(vm, 'startFormSaving')
      const spy = sandbox.stub(vm, 'save').resolves()
      const expected = 1337
      vm.editedContent = expected
      vm.comment = {}
      vm.$refs = {
        attachment: {
          checkAttachmentsModified: sandbox.stub().returns(expected),
          submit: sandbox.stub()
        }
      }
      sandbox.stub(vm, 'stopFormSaving')

      // when
      vm.onSubmit()

      // then
      vm.$refs.attachment.checkAttachmentsModified.should.have.been.called
      spy.should.have.been.calledWith({content: expected}, expected)
    })

    it('sets comment with response ', async () => {
      // given
      sandbox.stub(vm, 'startFormSaving')
      const expected = 1337
      sandbox.stub(vm, 'save').resolves({
        data: {
          id: expected,
          modifiedDate: expected,
          createdDate: expected
        }
      })
      vm.editedContent = expected
      vm.comment = {}
      vm.$refs = {
        attachment: {
          checkAttachmentsModified: sandbox.stub(),
          submit: sandbox.stub()
        }
      }
      sandbox.stub(vm, 'stopFormSaving')

      // when
      vm.onSubmit()
      await vm.$nextTick()

      // then
      vm.comment.should.deep.equal({
        id: expected,
        modifiedDate: expected,
        createdDate: expected,
        content: expected
      })
    })

    it('sets saveInProgress to true', async () => {
      // given
      sandbox.stub(vm, 'startFormSaving')
      const expected = 1337
      sandbox.stub(vm, 'save').resolves({
        data: {
          id: expected,
          modifiedDate: expected,
          createdDate: expected
        }
      })
      vm.editedContent = expected
      vm.comment = {}
      vm.$refs = {
        attachment: {
          checkAttachmentsModified: sandbox.stub(),
          submit: sandbox.stub()
        }
      }
      sandbox.stub(vm, 'stopFormSaving')
      vm.saveInProgress = false

      // when
      vm.onSubmit()
      await vm.$nextTick()

      // then
      vm.saveInProgress.should.be.true
    })

    it('calls refs[attachment].submit', async () => {
      // given
      sandbox.stub(vm, 'startFormSaving')
      const expected = 1337
      sandbox.stub(vm, 'save').resolves({
        data: {
          id: expected,
          modifiedDate: expected,
          createdDate: expected
        }
      })
      vm.editedContent = expected
      vm.comment = {}
      vm.$refs = {
        attachment: {
          checkAttachmentsModified: sandbox.stub(),
          submit: sandbox.stub()
        }
      }
      sandbox.stub(vm, 'stopFormSaving')

      // when
      vm.onSubmit()
      await vm.$nextTick()

      // then
      vm.$refs.attachment.submit.should.have.been.called
    })

    it('calls handleFormSubmitError on error', async () => {
      // given
      sandbox.stub(vm, 'startFormSaving')
      const expected = 1337
      sandbox.stub(vm, 'save').rejects(expected)
      vm.editedContent = expected
      vm.$refs = {
        attachment: {
          checkAttachmentsModified: sandbox.stub()
        }
      }
      sandbox.stub(vm, 'stopFormSaving')
      const spy = sandbox.stub(vm, 'handleFormSubmitError')

      // when
      vm.onSubmit()
      await vm.$nextTick()

      // then
      spy.should.have.been.calledWith(expected)
    })

    it('calls stopFormSaving at a successful end', async () => {
      // given
      sandbox.stub(vm, 'startFormSaving')
      const expected = 1337
      sandbox.stub(vm, 'save').resolves({
        data: {
          id: expected,
          modifiedDate: expected,
          createdDate: expected
        }
      })
      vm.editedContent = expected
      vm.comment = {}
      vm.$refs = {
        attachment: {
          checkAttachmentsModified: sandbox.stub(),
          submit: sandbox.stub()
        }
      }
      const spy = sandbox.stub(vm, 'stopFormSaving')

      // when
      await vm.onSubmit()
      await vm.$nextTick()

      // then
      spy.should.have.been.called
    })

    it('calls stopFormSaving when error was thrown', async () => {
      // given
      sandbox.stub(vm, 'startFormSaving')
      const expected = 1337
      sandbox.stub(vm, 'save').rejects(expected)
      vm.editedContent = expected
      vm.$refs = {
        attachment: {
          checkAttachmentsModified: sandbox.stub()
        }
      }
      const spy = sandbox.stub(vm, 'stopFormSaving')
      sandbox.stub(vm, 'handleFormSubmitError')

      // when
      await vm.onSubmit()
      await vm.$nextTick()

      // then
      spy.should.have.been.called
    })
  })

  describe('afterSuccessfulSubmission', () => {
    it('emits savedEventName when updatedComment is set', () => {
      // given
      const expected = 1337
      vm.savedEventName = expected
      const spy = sandbox.stub(vm.$events, 'emit')

      // when
      vm.afterSuccessfulSubmission(expected)

      // then
      spy.should.have.been.calledWith(expected, expected)
    })

    it('sets visible to false', () => {
      // given
      vm.visible = true

      // when
      vm.afterSuccessfulSubmission(false)

      // then
      vm.visible.should.be.false
    })

    it('sets saveInProgress to false', () => {
      // given
      vm.saveInProgress = true

      // when
      vm.afterSuccessfulSubmission(false)

      // then
      vm.saveInProgress.should.be.false
    })
  })

  describe('handleFormSubmitError', () => {
    it('logs the given error', () => {
      // given
      const expected = 1337
      const spy = sandbox.stub(vm.$log, 'error')

      // when
      vm.handleFormSubmitError(expected)

      // then
      spy.should.have.been.calledWith(expected)
    })
  })

  describe('showCommentEdit', () => {
    it('should set properties', () => {

      // given
      const expected = 1337
      vm.visible = false
      vm.comment = 'different from expected'
      vm.editedContent = 'different from expected'

      // when
      vm.showCommentEdit({content: expected})

      // then
      vm.visible.should.be.true
      vm.comment.should.be.deep.equal({content: expected})
      vm.editedContent.should.be.equal(expected)
    })
  })

  describe('startFormSaving', () => {
    it('should set saving to true', () => {
      // given
      vm.saving = false

      // when
      vm.startFormSaving()

      // then
      vm.saving.should.be.true
    })
  })

  describe('stopFormSaving', () => {
    it('should set saving to false', () => {
      // given
      vm.saving = true

      // when
      vm.stopFormSaving()

      // then
      vm.saving.should.be.false
    })
  })

  describe('mounted', () => {
    it('sets editedContent', () => {
      // given
      vm.editedContent = 'to be changed'
      const expected = 1337
      vm.comment = {
        content: expected
      }

      // when
      vm = vm.$mount()

      // then
      vm.editedContent.should.be.equal(expected)
    })

    it('should set showEventName', () => {
      // given
      const expected = 1337
      vm.showEventName = expected
      const spy = sandbox.spy(vm.$events, 'on')

      // when
      vm = vm.$mount()

      // then
      spy.should.have.been.calledWith(expected, vm.showCommentEdit)
    })
  })
})
