import Vue from 'vue'
import * as sinon from 'sinon'

import LigandComponent from '@/components/App/Sample/registration_system_ext/LigandComponent'

const sandbox = sinon.sandbox.create()

describe('Sample/LigandComponent.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(LigandComponent)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

})
