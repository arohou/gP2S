import Vue from 'vue'
import * as sinon from 'sinon'
import AttachmentList from '@/components/App/Attachment/AttachmentList'

const sandbox = sinon.sandbox.create()

describe('AttachmentList', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(AttachmentList)
    vm = new Constructor({})
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('createDownloadURL', () => {
    it('breaks early when fieldId is falsy', () => {
      expect(vm.createDownloadURL('whatever')).to.be.null
    })

    it('should pass fieldId and return getDownloadURL result', () => {
      // given
      const expected = 1337
      vm.attachmentService = {
        getDownloadURL: sandbox.stub().returns(expected)
      }

      // when
      const result = vm.createDownloadURL(expected)

      // then
      vm.attachmentService.getDownloadURL
        .should.have.been.calledWith(expected)
      result.should.be.equal(expected)
    })
  })
})
