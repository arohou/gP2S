import Vue from 'vue'
import * as sinon from 'sinon'

import SingleFileSelector from '@/components/App/Attachment/SingleFileSelector'
import FineUploaderTraditional from 'fine-uploader-wrappers'

const sandbox = sinon.sandbox.create()

describe('SingleFileSelector.vue', () => {

  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(SingleFileSelector)
    vm = new Constructor({
      propsData: {
        entity: {},
        attachmentService: {
          getDownloadURL: sandbox.stub().returnsArg(0),
          getUploadURL: sandbox.stub().returns('/attachments/upload')
        }
      }
    })
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('data', () => {
    it('should have an uploader', () => {
      vm.uploader.should.be.instanceOf(FineUploaderTraditional)
    })
  })

  describe('parseUploadResult', () => {
    it('should not crash if responseJSON is bogus', () => {
      // given
      const wrapper = () => vm.parseUploadResult(null, null, null)

      // then
      expect(wrapper).not.to.throw
    })

    it('should break early if no entity', () => {
      // given
      vm.entity = null
      const expected = 1337
      vm.attachmentFileName = expected
      vm.attachmentMongoId = expected

      // when
      vm.parseUploadResult(null, '', {mongoId: ''})

      // then
      vm.attachmentFileName.should.be.equal(expected)
      vm.attachmentMongoId.should.be.equal(expected)
    })

    it('should assign attachment parameters', () => {
      // given
      vm.entity = {
        attachmentFileName: '',
        attachmentMongoId: ''
      }
      const expected = 1337

      // when
      vm.parseUploadResult(null, expected, {mongoId: expected})

      // then
      vm.entity.attachmentFileName.should.be.equal(expected)
      vm.entity.attachmentMongoId.should.be.equal(expected)
    })
  })

  describe('createUploader', () => {
    it('should return an instance of FineUploaderTraditional', () => {
      // when
      const result = vm.createUploader()

      // then
      result.should.be.instanceOf(FineUploaderTraditional)
    })
  })

  describe('removeAttachment', () => {
    it('should break early if deleteConfirmationCallback returns false', async () => {
      // given
      vm.attachmentService = {
        deleteAttachment: sandbox.stub().resolves()
      }
      sandbox.stub(vm.$events, '$emit')
      const expected = 1337
      vm.entity = {
        attachmentFileName: expected,
        attachmentMongoId: expected
      }
      vm.deleteConfirmationCallback = sandbox.stub().resolves(false)

      // when
      await vm.removeAttachment(expected)

      // then
      vm.deleteConfirmationCallback.should.have.been.called
      vm.attachmentService.deleteAttachment.should.not.have.been.called
      vm.entity.attachmentFileName.should.equal(expected)
      vm.entity.attachmentMongoId.should.equal(expected)
      vm.$events.$emit.should.not.have.been.called
    })

    it('should call attachmentService.deleteAttachment with the given fileId', async () => {
      // given
      const expected = 1337
      vm.attachmentService = {
        deleteAttachment: sandbox.stub().resolves()
      }
      sandbox.stub(vm.$events, '$emit')

      // when
      await vm.removeAttachment(expected)

      // then
      vm.attachmentService.deleteAttachment.should.have.been.calledWith(expected)
    })

    it('should clear attachment in entity', async () => {
      // given
      vm.attachmentService = {
        deleteAttachment: sandbox.stub().resolves()
      }
      sandbox.stub(vm.$events, '$emit')
      const expected = 1337
      vm.entity = {
        attachmentFileName: expected,
        attachmentMongoId: expected
      }

      // when
      await vm.removeAttachment('whatever')

      // then
      expect(vm.entity.attachmentFileName).to.be.null
      expect(vm.entity.attachmentMongoId).to.be.null
    })

    it('should emit the attachmentRemoved event', async () => {
      // given
      vm.attachmentService = {
        deleteAttachment: sandbox.stub().resolves()
      }
      const spy = sandbox.stub(vm.$events, '$emit')

      // when
      await vm.removeAttachment('whatever')

      // then
      spy.should.have.been.called
    })
  })
})
