import Vue from 'vue'
import * as sinon from 'sinon'

import Edit from '@/components/App/Admin/Project/management_system_ext/Edit'

const sandbox = sinon.sandbox.create()

describe('Admin/Project/management_system_ext/Edit.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(Edit)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('loadFromData', () => {
    it('should emit initialProjectManagementSystemCall after fetchEntityBySlugOrId', async () => {
      // given
      const spy = sandbox.stub(vm.$events, '$emit')
      const expected = 1337
      vm.entity = {
        projectManagementSystemSlug: expected
      }
      sandbox.stub(vm, 'fetchEntityBySlugOrId').resolves()

      // when
      await vm.loadFormData(expected)

      // then
      spy.should.have.been.calledWith('initialProjectManagementSystemCall', expected)
    })
  })
})
