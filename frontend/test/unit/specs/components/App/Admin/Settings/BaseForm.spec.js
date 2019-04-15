import Vue from 'vue'
import * as sinon from 'sinon'
import { validateVueProp } from '../../../../../utils/ValidatorUtils'

import BaseForm from '@/components/App/Admin/Settings/BaseForm'

const sandbox = sinon.sandbox.create()

describe('Admin/Settings/BaseForm.vue', () => {
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

  describe('patternForDataStorageDirectoryName validator', () => {

    const FIELD_NAME = 'patternForDataStorageDirectoryName'

    it('is required', async () => {
      // when
      const errors = await validateVueProp(vm, FIELD_NAME, null)

      // then
      expect(errors).to.be.not.empty
    })

    it('cannot be empty', async () => {
      // when
      const errors = await validateVueProp(vm, FIELD_NAME, '')

      // then
      expect(errors).to.be.not.empty
    })

    it('passes ok when at least "${MicroscopySessionID}" is present', async () => {
      // when
      const errors = await validateVueProp(vm, FIELD_NAME, '${MicroscopySessionID}')

      // then
      expect(errors).to.be.empty
    })

    it('passes ok for a multi token string', async () => {
      // when
      const errors = await validateVueProp(vm, FIELD_NAME,
        '${MicroscopyStartDate}_${ProjectLabel}_${MicroscopeLabel}_${GridID}_${MicroscopySessionID}')

      // then
      expect(errors).to.be.empty
    })

  })

})
