import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'

import SampleCopy from '@/components/App/Sample/Copy'
import VueRouter from 'vue-router'
import { HTTP } from '@/utils/http-common'

const sandbox = sinon.sandbox.create()

const router = new VueRouter()
describe('Sample/Copy.vue', () => {
  let vm = null

  const LIGAND_COMPONENTS = [
    {
      'id': 1,
      'finalConcentration': 4,
      'finalDrop': null,
      'aliquot': {
        'id': 1,
        'slug': 'ligand-no-1',
        'label': 'Lignad no.1',
        'tubeLabel': 'tube no.1',
        'concentration': null,
        'conceptId': 'CONCEPT5',
        'solvent': 'solvent no.1'
      }
    }]

  const PROTEIN_COMPONENTS = [{
    'id': 1,
    'finalConcentration': 1.3,
    'finalDrop': null,
    'aliquot': {
      'id': 1,
      'slug': 'protein-no-1',
      'label': 'PROTEIN no.1',
      'tubeLabel': 'tube no.1',
      'concentration': null,
      'purificationId': 'PUR1AH1'
    }
  }]

  const SAMPLE = {
    id: '1',
    slug: 'sample-label',
    label: 'Sample Label',
    incubationTime: 13,
    incubationTemperature: 137,
    otherBufferComponents: 'other components',
    ligandComponent: LIGAND_COMPONENTS,
    proteinComponent: PROTEIN_COMPONENTS
  }

  const SAMPLE_2 = {
    label: 'Sample Label 2',
    id: null,
    slug: null,
    version: null,
    aliquot: {
      label: 'Test aliquot label 2'
    }
  }

  const PROJECTS = [
    {id: 1, slug: 'project-1', label: 'Project 1'},
    {id: 2, slug: 'project-2', label: 'Project 2'},
    {id: 3, slug: 'project-3', label: 'Project 3'}
  ]

  const DATA = {
    projectSlugOrId: PROJECTS[0].slug,
    sample: SAMPLE
  }

  const DATA_2 = {
    projectSlugOrId: PROJECTS[0].slug,
    sample: SAMPLE_2
  }

  beforeEach(() => {
    moxios.install(HTTP)
    moxios.stubRequest(process.env.API_URL + 'sample/' + DATA.sample.slug, {
      status: 200,
      response: DATA.sample
    })
    moxios.stubRequest(process.env.API_URL + 'sample/' + DATA.sample.slug + '/projects', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'project/', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'sample/' + DATA.projectSlugOrId, {
      status: 200,
      response: DATA.sample
    })

    sandbox.stub(router, 'push')
    sandbox.stub(router, 'go')
    const Constructor = Vue.extend(SampleCopy)
    vm = new Constructor({router})
    vm.id = DATA.sample.slug
    vm.projectId = DATA.projectSlugOrId
    sandbox.stub(vm, 'formLoadingStarted') //prevent disable save button on form mount
    vm = vm.$mount()
  })

  afterEach(() => {
    moxios.uninstall()
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should create copy sample form have save and cancel buttons enabled', done => {
    new Promise((resolve, reject) => moxios.wait(resolve))
      .then(() => {
        expect([...vm.$el.querySelectorAll('.actions-header__buttons > button')]
          .map(x => x.textContent.trim())).to.be.eql(['Cancel', 'Save'])
        expect(vm.$el.querySelector('.actions-header__action-buttons__submit').disabled).to.be.eql(false)
        expect(vm.$el.querySelector('.actions-header__action-buttons__cancel').disabled).to.be.eql(false)
      }).then(done, done)
  })

  it('should send a create sample event on save button', done => {
    vm.projectId = DATA_2.projectSlugOrId
    vm.sample = Object.assign({}, DATA_2.sample)
    vm.sample.components = []
    vm.sample.components.push({aliquot: {label: 'my label'}, finalConcentration: 3, type: 'PUR'})

    Vue.nextTick().then(() => {
      moxios.requests.reset()
      vm.$el.querySelector('.actions-header__action-buttons__submit').click()
      vm._watcher.run()
    }).then(() => {
      const request = moxios.requests.first()
      expect(JSON.parse(request.config.data).label).to.be.equal(DATA_2.sample.label)
    }).then(done, done)
  })

  it('should not send update signal due to form validation', done => {
    vm.projectId = '3'

    Vue.nextTick().then(() => {
      vm.saveForm = sandbox.spy()
      vm.submitBaseFormBy('sample')
      return Vue.nextTick()
    }).then(() => {
      expect(vm.saveForm).to.not.have.been.called
    }).then(done, done)
  })

  it('should cancel button execute history back', done => {
    Vue.nextTick().then(() => {
      vm.$el.querySelector('.actions-header__action-buttons__cancel').click()
      vm._watcher.run()

      expect(router.go).to.have.been.calledOnce
    }).then(done, done)
  })

  it('should remove component from a sample', done => {
    new Promise((resolve, reject) => moxios.wait(resolve))
      .then(() => {
        const components = [...vm.$el.querySelectorAll('.sample-base-form__components__component-row')]
        components.should.have.lengthOf(2)

        vm.$el.querySelector('.sample-base-form__components__component-row .el-button.el-button--danger').click()
        return Vue.nextTick()
      }).then(() => {
      const components = [...vm.$el.querySelectorAll('.sample-base-form__components__component-row')]
      components.should.have.lengthOf(1)
    }).then(done, done)
  })

  it('should not send update signal due to incubation time validation', done => {
    vm.sample = DATA.sample

    vm.saveForm = sandbox.spy()
    Vue.nextTick().then(() => {
      vm.sample.incubationTime = 'non number'
      vm.submitBaseFormBy('sample')
      vm.sample.incubationTime = 0
      vm.submitBaseFormBy('sample')
      vm.sample.incubationTime = -15
      vm.submitBaseFormBy('sample')
    }).then(() => {
      expect(vm.saveForm).to.not.have.been.called
    }).then(done, done)
  })

  it('should not send update signal due to incubation temperature validation', done => {
    vm.sample = DATA.sample

    vm.saveForm = sandbox.spy()
    Vue.nextTick().then(() => {
      vm.sample.incubationTemperature = 'non number'
      vm.submitBaseFormBy('sample')
      vm.sample.incubationTemperature = -273.151
      vm.submitBaseFormBy('sample')
      vm.sample.incubationTemperature = -460
      vm.submitBaseFormBy('sample')
    }).then(() => {
      expect(vm.saveForm).to.not.have.been.called
    }).then(done, done)
  })

  it('should not update sample with no components', done => {
    vm.saveForm = sandbox.spy()
    Vue.nextTick().then(() => {
      vm.sample.label = 'My sample name'
    }).then(() => {
      vm.submitBaseFormBy('sample')
    }).then(() => {
      expect(vm.saveForm).to.not.have.been.called

      vm.sample.components.push({aliquot: {label: 'my label', finalConcentration: '5'}})
      vm.submitBaseFormBy('sample')
    }).then(() => {
      expect(vm.saveForm).to.have.been.calledOnce
    }).then(done, done)
  })

  it('should show error messages on server error', done => {
    let errors = {
      label: 'error',
      incubationTime: 'error',
      incubationTemperature: 'error',
      otherBufferComponents: 'error'
    }
    vm.$events.$emit('validationError', errors)
    Vue.nextTick().then(() => {
      const errorMessages = vm.$el.querySelectorAll('.el-form-item__error')

      errorMessages.should.have.lengthOf(4)
    }).then(done, done)
  })

})
