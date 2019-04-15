import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'

import Edit from '@/components/App/Sample/Edit'
import VueRouter from 'vue-router'
import { HTTP } from '@/utils/http-common'

const sandbox = sinon.sandbox.create()

const router = new VueRouter()
describe('Sample/Edit.vue', () => {
  let vm = null

  const LIGAND_COMPONENTS = [
    {
      'id': 1,
      'finalConcentration': 4,
      'finalConcentrationInMgPerMl': 1,
      'finalDrop': null,
      'aliquot': {
        'id': 1,
        'slug': 'ligand-no-1',
        'label': 'Ligand no.1',
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

  const DATA = {
    projectSlugOrId: 'project-1',
    sample: {
      id: 1,
      slug: 'sample-label',
      label: 'Sample Label',
      incubationTime: 13,
      incubationTemperature: 137,
      otherBufferComponents: 'other components',
      ligandComponent: LIGAND_COMPONENTS,
      proteinComponent: PROTEIN_COMPONENTS
    }
  }

  const PROJECTS = [
    {
      id: 1,
      slug: 'project-1',
      label: 'Project 1',
      samples: [DATA.sample]
    },
    {id: 2, label: 'Project 2', slug: 'project-2'},
    {id: 3, label: 'Project 3', slug: 'project-3'}
  ]

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
    moxios.stubRequest(process.env.API_URL + 'sample/', {
      status: 200,
      response: DATA.sample
    })

    sandbox.stub(router, 'push')
    sandbox.stub(router, 'go')
    const Constructor = Vue.extend(Edit)
    vm = new Constructor({
      router,
      propsData: {
        projectId: DATA.projectSlugOrId,
        id: DATA.sample.slug
      }
    })
    vm.id = DATA.sample.slug
    vm.projectId = DATA.projectSlugOrId
    vm = vm.$mount()
  })

  afterEach(() => {
    moxios.uninstall()
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should display the save and cancel button', () => {
    expect([...vm.$el.querySelectorAll('.actions-header__buttons > button')]
      .map(x => x.textContent.trim()))
      .to.be.deep.equal(['Cancel', 'Save'])
  })

  it('should send an update sample event on save button ', done => {
    Vue.nextTick().then(() => {
      vm.sample = DATA.sample
      expect(vm.sample).to.be.deep.equal(DATA.sample)
      vm.$el.querySelector('.actions-header__action-buttons__submit').click()

      return new Promise((resolve, reject) => moxios.wait(resolve))
    }).then(() => {
      expect(vm.sample).to.be.deep.equal(DATA.sample)
    }).then(done, done)
  })

  it('should not send update signal due to form validation', done => {
    Vue.nextTick().then(() => {
      vm.sample.label = ''
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

      expect(router.go).to.be.calledOnce
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
      vm.sample.incubationTemperature = -273.15
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
      incubationTime: 'error'
    }
    // vm.$events.$emit('validationError', errors);
    vm.$events.$emit('validationError', errors)
    Vue.nextTick().then(() => {
      const errorMessages = vm.$el.querySelectorAll('.el-form-item__error')

      errorMessages.should.have.lengthOf(2)
    }).then(done, done)
  })

})
