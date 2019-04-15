import Vue from 'vue'
import * as sinon from 'sinon'

import BaseForm from '@/components/App/Model/BaseForm'

const sandbox = sinon.sandbox.create()

describe('Model/BaseForm.vue', () => {
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

  describe('entity.attachmentFileName watcher', () => {
    it('should call validation', async () => {
      // given
      const spy = sandbox.stub()
      vm.$refs = {
        attachmentFileName: {
          validate: spy
        }
      }

      // when
      vm.entity = {
        attachmentFileName: 'changed'
      }
      await vm.$nextTick()

      // then
      spy.should.have.been.called
    })
  })
})
