import Vue from 'vue'
import * as sinon from 'sinon'

import View from '@/components/App/Map/View'
import MapService from '@/services/MapService'

const sandbox = sinon.sandbox.create()

describe('Map/View.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(View)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('created', () => {
    it('should init _service', () => {
      expect(vm._service).to.be.instanceOf(MapService)
    })
  })

  describe('downloadUrl', () => {
    it('should get the url from MapService', () => {
      // given
      const expected = 1337
      vm.entity.attachmentMongoId = expected
      const spy = sandbox.stub(vm._service, 'getDownloadURL').returns(expected)

      // then
      expect(vm.downloadUrl).to.be.equal(expected)
      spy.should.have.been.calledWith(expected)
    })
  })

})
