import Vue from 'vue'
import * as sinon from 'sinon'

import List from '@/components/App/Admin/ImageProcessingSoftware/List'

import ImageProcessingSoftwareService from '@/services/ImageProcessingSoftwareService'

const sandbox = sinon.sandbox.create()

describe('ImageProcessingSoftware/List.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(List)
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
