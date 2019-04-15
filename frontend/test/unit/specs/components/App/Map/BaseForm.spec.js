import Vue from 'vue'
import * as sinon from 'sinon'
import VueRouter from 'vue-router'

import BaseForm from '@/components/App/Map/BaseForm'
import ValidationError from '@/errors/ValidationError'
import ProcessingSessionService from '@/services/ProcessingSessionService'

const sandbox = sinon.sandbox.create()

describe('Map/BaseForm.vue', () => {
  let vm = null
  let router = null

  const DATA = {
    projectSlugOrId: '1',
    entity: {
      id: null,
      estimatedResolutionInBestParts: 5
    }
  }

  beforeEach(() => {
    const Constructor = Vue.extend(BaseForm)
    router = new VueRouter()
    vm = new Constructor({
      router,
      propsData: {
        entity: DATA.entity,
        projectId: DATA.projectSlugOrId
      }
    })

    vm = vm.$mount()
  })
  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
    router = null
  })

  describe('created', () => {
    it('should initialize _processingSessionService', () => {
      vm._processingSessionsService.should.be.an.instanceOf(ProcessingSessionService)
    })
  })

  it('should validate estimated resolution in worst parts', done => {
    Vue.nextTick().then(() => {
      let callback = function (error) {
        return error
      }

      let validator = vm.validateEstimatedResolutionInWorstParts()

      expect(validator(null, vm.entity.estimatedResolutionInBestParts + 1, callback)).to.be.undefined
      expect(validator(null, vm.entity.estimatedResolutionInBestParts, callback)).to.be.undefined
      expect(validator(null, vm.entity.estimatedResolutionInBestParts - 1, callback)).to.be.instanceOf(ValidationError)
      vm.entity.estimatedResolutionInBestParts = null
      expect(validator(null, 1, callback)).to.be.undefined
    }).then(done, done)
  })

  it('should validate symmetry applied', done => {
    Vue.nextTick().then(() => {
      let callback = function (error) {
        return error
      }

      let validator = vm.validateSymmetryApplied()

      expect(validator(null, 'C1', callback)).to.be.undefined
      expect(validator(null, 'D12', callback)).to.be.undefined
      expect(validator(null, 'O', callback)).to.be.undefined
      expect(validator(null, 'T', callback)).to.be.undefined
      expect(validator(null, 'I', callback)).to.be.undefined
      expect(validator(null, 'C', callback)).to.be.instanceOf(ValidationError)
      expect(validator(null, 'D', callback)).to.be.instanceOf(ValidationError)
      expect(validator(null, 'Da', callback)).to.be.instanceOf(ValidationError)
      expect(validator(null, 'I5', callback)).to.be.instanceOf(ValidationError)
    }).then(done, done)
  })
})
