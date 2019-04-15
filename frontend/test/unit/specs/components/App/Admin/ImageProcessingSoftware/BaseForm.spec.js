import Vue from 'vue'
import * as sinon from 'sinon'

import BaseForm from '@/components/App/Admin/ImageProcessingSoftware/BaseForm'

const sandbox = sinon.sandbox.create()

describe('Admin/ImageProcessingSoftware/BaseForm.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(BaseForm)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('removeVersion', () => {
    it('should remove an item from entity.softwareVersion of a given index', () => {
      // given
      vm.entity = {
        softwareVersions: [1, 2, 3, 4, 5]
      }

      // when
      vm.removeVersion(1)

      //then
      vm.entity.softwareVersions.should.be.deep.equal([1, 3, 4, 5])
    })
  })

  describe('addVersion', () => {
    it('should add an empty string to entity.softwareVersion', () => {
      // given
      vm.entity = {
        softwareVersions: [1, 2, 3, 4, 5]
      }

      // when
      vm.addVersion()

      //then
      vm.entity.softwareVersions.should.be.deep.equal([1, 2, 3, 4, 5, ''])
    })
  })
})
