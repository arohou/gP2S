import AttachmentService from '@/services/AttachmentService'
import { HTTP } from '@/utils/http-common'

import sinon from 'sinon'

const sandbox = sinon.sandbox.create()

describe('AttachmentService', () => {
  let vm = null

  beforeEach(() => {
    vm = new AttachmentService()
  })

  afterEach(() => {
    sandbox.reset()
    vm = null
  })

  it('saves parentURI in constructor', () => {
    // given
    const expected = 1337

    // when
    vm = new AttachmentService(expected)

    // then
    vm.parentURI.should.equal(expected)
  })

  it('should construct download URI based on given parameter', () => {
    // given
    const expected = 1337

    // when
    const result = vm.getDownloadURL(expected)

    // then
    expect(result).to.endWith('download/' + expected)
  })

  it('should construct upload URL', () => {
    // given
    const expected = '1337/'
    vm.parentURI = expected

    // then
    expect(vm.getUploadURL()).to.endWith(
      expected + 'attachment/upload')
  })

  it('should DELETE /attachment on deleteAttachments', () => {
    // given
    const spy = sandbox.spy(HTTP, 'delete')
    const expected = 1337
    vm.parentURI = expected.toString()

    // when
    vm.deleteAttachments(expected)

    // then
    expect(spy).to.have.been.calledWith(
      expected.toString() + 'attachment', {data: expected})
  })

})
