import Vue from 'vue'
import * as sinon from 'sinon'
import AttachmentForm from '@/components/App/Attachment/AttachmentForm'
import AttachmentService from '@/services/AttachmentService'
import * as FineUploaderTraditional from 'fine-uploader-wrappers'

const sandbox = sinon.sandbox.create()

describe('AttachmentForm', () => {
  let vm = null

  const DATA = {
    projectSlugOrId: 'project-1',
    entity: {
      attachments: [
        {}
      ]
    }
  }

  beforeEach(() => {
    const Constructor = Vue.extend(AttachmentForm)
    vm = new Constructor({
      propsData: {
        attachmentService: sinon.createStubInstance(AttachmentService)
      }
    })
    vm.entity = DATA.entity
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('createUploader', () => {
    it('should create FineUploaderTraditional', () => {
      // given
      const spy = sandbox.stub(FineUploaderTraditional, 'default')
      sandbox.stub(vm, 'connectUploaderEvents')

      // when
      vm.createUploader('')

      // then
      expect(spy).to.have.been.called
    })

    it('should call connectUploaderEvents', () => {
      // given
      sandbox.stub(FineUploaderTraditional, 'default')
      const spy = sandbox.stub(vm, 'connectUploaderEvents')

      // when
      vm.createUploader('')

      // then
      expect(spy).to.have.been.called
    })
  })

  describe('connectUploaderEvents', () => {
    it('connects error event to handleUploadedErrors', () => {
      // given
      const spy = sandbox.stub(vm.uploader, 'on')

      // when
      vm.connectUploaderEvents(vm.uploader)

      // then
      expect(spy).to.have.been.calledWith('error', vm.handleUploadErrors)
    })

    it('connects complete event to fileUploadSuccess', () => {
      // given
      const spy = sandbox.stub(vm.uploader, 'on')

      // when
      vm.connectUploaderEvents(vm.uploader)

      // then
      expect(spy).to.have.been.calledWith('complete', vm.fileUploadSuccess)
    })

    it('connects allComplete event to executeSubmitCallback', () => {
      // given
      const spy = sandbox.stub(vm.uploader, 'on')

      // when
      vm.connectUploaderEvents(vm.uploader)

      // then
      expect(spy).to.have.been.calledWith('allComplete', vm.executeSubmitCallback)
    })
  })

  describe('submit', () => {
    it('should run callback if list of files to upload is empty', async () => {
      // given
      vm.attachmentService.deleteAttachments = sandbox.spy()
      vm.uploadCallback = sandbox.spy()
      const spy = sandbox.stub(vm, 'upload')
      const entity = {id: 1}

      vm.uploader = {
        methods: {
          setParams: function () {},
          getUploads: function () {
            return []
          }
        }
      }

      // when
      vm.submit(entity)

      // then
      vm.attachmentService.deleteAttachments.should.not.have.been.called
      expect(spy).to.have.been.called
    })

    it('should call file upload if list of files is not empty', async () => {
      // given
      vm.entity = {
        id: 1,
        attachments: [{id: 1}, {id: 2}]
      }
      vm.attachmentsForRemoval = [1]

      vm.attachmentService.deleteAttachments = sandbox.spy()
      // vm.uploadCallback = sandbox.spy()

      vm.uploader = {
        methods: {
          setParams: function () {},
          getUploads: function () {
            return [{}]
          },
          uploadStoredFiles: sandbox.spy()
        }
      }

      // when
      vm.submit(vm.entity)

      // then
      vm.attachmentService.deleteAttachments.should.have.been.called
      vm.uploader.methods.uploadStoredFiles.should.have.been.called
    })
  })

  describe('handleUploadErrors', () => {
    it('calls isNoFilesError with errorMessage parameter', () => {
      // given
      const spy = sandbox.stub(vm, 'isNoFilesError').returns(false)
      const expected = '123'
      // when
      vm.handleUploadErrors(null, null, expected)
      // then
      expect(spy).to.have.been.calledWith(expected)
    })

    it('calls executeSubmitCallback if no files error', () => {
      // given
      sandbox.stub(vm, 'isNoFilesError').returns(true)
      const spy = sandbox.stub(vm, 'executeSubmitCallback')
      // when
      vm.handleUploadErrors()
      // then
      expect(spy).to.have.been.called
    })

    it('calls showErrorDialog if error', () => {
      // given
      sandbox.stub(vm, 'isNoFilesError').returns(false)
      const spy = sandbox.stub(vm, 'showErrorDialog')
      const expected = '123'
      // when
      vm.handleUploadErrors(null, expected, expected)
      // then
      expect(spy).to.have.been.calledWith(expected, expected)
    })
  })

  describe('deleteAttachments', () => {
    it('should not delete attachments for empty list', () => {
      // given
      vm.attachmentsForRemoval = []
      vm.attachmentService = {deleteAttachments: sandbox.spy()}
      // when
      vm.deleteAttachments()
      // then
      expect(vm.attachmentService.deleteAttachments).to.not.have.been.called
    })

    it('should delete attachments for populated list', () => {
      // given
      const expected = [1, 2]
      vm.attachmentsForRemoval = expected
      vm.attachmentService = {deleteAttachments: sandbox.spy()}
      // when
      vm.deleteAttachments()
      // then
      expect(vm.attachmentService.deleteAttachments).to.have.been.calledWith(expected)
    })
  })

  describe('visible watcher', () => {
    it('calls resetComponent when visible is true', async () => {
      // given
      const spy = sandbox.stub(vm, 'resetComponent')

      // when
      vm.visible = true
      await vm.$nextTick()

      // then
      expect(spy).to.have.been.called
    })
  })

  describe('attachmentService watcher', () => {
    it('should call createUploader', async () => {
      // given
      const spy = sandbox.stub(vm, 'createUploader')
      const expected = 1337
      const attachmentServiceMock = {
        getUploadURL: sandbox.stub().returns(expected)
      }

      // when
      vm.attachmentService = attachmentServiceMock
      await vm.$nextTick()

      // then
      attachmentServiceMock.getUploadURL.should.have.been.called
      spy.should.have.been.calledWith(expected)
    })
  })

  describe('resetComponent', () => {

    it('should call resetUploader', () => {
      // given
      vm.resetUploader = sandbox.stub()
      vm.setAllAttachmentsFromEntity = sandbox.stub()

      // when
      vm.resetComponent()

      // then
      vm.resetUploader.should.have.been.called
    })

    it('should call setAllAttachmentsFromEntity', () => {
      // given
      vm.resetUploader = sandbox.stub()
      vm.setAllAttachmentsFromEntity = sandbox.stub()

      // when
      vm.resetComponent()

      // then
      vm.setAllAttachmentsFromEntity.should.have.been.called
    })

    it('clears attachmentsForRemoval', () => {
      // given
      sandbox.stub(vm.uploader.methods, 'cancelAll')
      sandbox.stub(vm.uploader.methods, 'clearStoredFiles')
      vm.attachmentsForRemoval = [1, 2, 3]

      // when
      vm.resetComponent()

      // then
      expect(vm.attachmentsForRemoval).to.be.empty
    })
  })

  describe('resetUploader', () => {
    it('breaks early if uploader is falsy', () => {
      // given
      vm.uploader = null

      // then
      expect(vm.resetUploader()).not.to.throw
    })

    it('calls uploader\'s cancelAll', () => {
      // given
      const spy = sandbox.stub(vm.uploader.methods, 'cancelAll')
      sandbox.stub(vm.uploader.methods, 'clearStoredFiles')

      // when
      vm.resetUploader()

      // then
      expect(spy).to.have.been.called
    })

    it('calls uploader\'s clearStoredFiles', () => {
      // given
      sandbox.stub(vm.uploader.methods, 'cancelAll')
      const spy = sandbox.stub(vm.uploader.methods, 'clearStoredFiles')

      // when
      vm.resetUploader()

      // then
      expect(spy).to.have.been.called
    })
  })

  describe('setAllAttachmentsFromEntity', () => {
    it('should break early if entity is falsy', () => {
      // given
      vm.entity = null
      const expected = [1337]
      vm.allAttachments = expected

      // when
      vm.setAllAttachmentsFromEntity()

      // then
      vm.allAttachments.should.be.deep.equal(expected)
    })

    it('sets allAttachments from entity.attachments', () => {
      // given
      sandbox.stub(vm.uploader.methods, 'cancelAll')
      sandbox.stub(vm.uploader.methods, 'clearStoredFiles')
      const expected = [1, 2, 3]
      vm.entity.attachments = expected

      // when
      vm.resetComponent()

      // then
      expect(vm.allAttachments).to.be.deep.equal(expected)
    })
  })

  describe('fileUploadSuccess', () => {
    it('breaks early if response.success is false', () => {
      // given
      const expected = 1337
      const response = {
        success: false,
        id: expected,
        mongoId: expected
      }

      // when
      vm.fileUploadSuccess(expected, expected, response)

      // then
      expect(vm.allAttachments).to.be.empty
    })

    it('modifies allAttachments if response.success', () => {
      // given
      const expected = 1337
      const response = {
        success: true,
        id: expected,
        mongoId: expected
      }

      // when
      vm.fileUploadSuccess(expected, expected, response)

      // then
      expect(vm.allAttachments).to.be.deep.equal([{
        mongoId: expected,
        fileName: expected,
        id: expected
      }])
    })
  })

  describe('executeSubmitCallback', () => {
    it('should execute submit callback', () => {
      // given
      let expected = [1, 2]
      vm.allAttachments = expected
      // vm.attachmentsForRemoval = expected
      vm.submitCallback = sandbox.spy()
      // when
      vm.executeSubmitCallback()
      // then
      expect(vm.submitCallback).to.have.been.calledWith({attachments: expected})
    })
  })

  describe('upload', () => {
    it('calls uploadStoredFiles', () => {
      // given
      const spy = sandbox.stub(vm.uploader.methods, 'uploadStoredFiles')

      // when
      vm.upload()

      // then
      expect(spy).to.have.been.called
    })
  })

  describe('checkAttachmentsModified', () => {
    it('indicate attachments modified when attachments for removal', () => {
      // given
      vm.attachmentsForRemoval = [{}]
      // when
      const result = vm.checkAttachmentsModified()
      // then
      expect(result).to.be.true
    })

    it('indicate attachments modified when uploads exist', () => {
      // given
      vm.uploader = {methods: {getUploads: () => [{}]}}
      vm.attachmentsForRemoval = []
      // when
      const result = vm.checkAttachmentsModified()
      // then
      expect(result).to.be.true
    })

    it('indicate attachments not modified when nothing changes', () => {
      // given
      vm.uploader = {methods: {getUploads: () => []}}
      vm.attachmentsForRemoval = []
      // when
      const result = vm.checkAttachmentsModified()
      // then
      expect(result).to.be.false
    })
  })

  describe('removeAttachment', () => {
    it('should return empty attachments list', () => {
      // given
      const attachmentForRemoval = {id: 1}
      vm.allAttachments = [attachmentForRemoval]
      // when
      vm.removeAttachment(attachmentForRemoval)
      // then
      vm.allAttachments.should.have.lengthOf(0)
    })
  })

  describe('isNoFilesError', () => {
    it('returns true if errorMessage is a NoFilesError', () => {
      // given
      const errorMessage = vm.uploader.methods._options.messages.noFilesError
      expect(vm.isNoFilesError(errorMessage)).to.be.true
    })

    it('returns false if errorMessage is NOT NoFilesError', () => {
      // given
      const errorMessage = 'Unknown error'
      expect(vm.isNoFilesError(errorMessage)).to.be.false
    })
  })
})
