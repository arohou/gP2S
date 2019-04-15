import Vue from 'vue'
import * as sinon from 'sinon'

import Commons from '@/components/App/Admin/ImageProcessingSoftware/Commons'

import ImageProcessingSoftwareService from '@/services/ImageProcessingSoftwareService'

const sandbox = sinon.sandbox.create()

describe('ImageProcessingSoftware/Commons.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(Commons)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm = null
  })

  describe('created', () => {
    it('should instantiate _imageProcessingSoftwareService', () => {
      expect(vm._imageProcessingSoftwareService).to.be.of.instanceOf(ImageProcessingSoftwareService)
    })
  })
})
