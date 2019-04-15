import MapService from '@/services/MapService'

import sinon from 'sinon'

const sandbox = sinon.sandbox.create()

describe('MapService', () => {
  let vm = null

  beforeEach(() => {
    vm = new MapService()
  })

  afterEach(() => {
    sandbox.reset()
    vm = null
  })

  it('should create a file download URL', () => {
    // given
    const expected = 1337

    // when
    vm.getDownloadURL(expected).should.endWith('/attachment/download/' + expected)
  })

  it('should create a file upload URL', () => {
    vm.getUploadURL().should.endWith('/attachment/upload')
  })

})
