import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'
import { fn as momentProto } from 'moment'

import New from '@/components/App/Sample/New'
import VueRouter from 'vue-router'
import { HTTP } from '@/utils/http-common'

const sandbox = sinon.sandbox.create()

const router = new VueRouter()
describe('Sample/New.vue', () => {
  let vm = null
  const LIGAND_COMPONENTS = [
    {
      'id': 1,
      'finalConcentration': 4,
      'finalDrop': null,
      'type': 'G',
      'aliquot': {
        'id': 1,
        'slug': 'lignad-no-1',
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
    'type': 'PUR',
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

  const DATA_2 = {
    projectSlugOrId: 'project-1',
    sample: {
      id: null,
      slug: null,
      version: null,
      label: 'Sample 17-08-01 08:13',
      incubationTime: 13,
      incubationTemperature: 137,
      otherBufferComponents: 'other components',
      availableForGridMaking: true,
      ligandComponent: LIGAND_COMPONENTS.map(c => {
        c.id = null
        c.slug = null
        c.version = null
        return c
      }),
      proteinComponent: PROTEIN_COMPONENTS.map(c => {
        c.id = null
        c.slug = null
        c.version = null
        return c
      })
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
    sandbox.stub(router, 'push')
    const Constructor = Vue.extend(New)
    vm = new Constructor({router})

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

    sandbox.stub(momentProto, 'format')
    momentProto.format.withArgs('YY-MM-DD HH:mm').returns('17-08-01 08:13')

    vm.id = DATA.sample.slug
    vm.projectId = DATA.projectSlugOrId
    vm = vm.$mount()
  })

  afterEach(() => {
    moxios.uninstall(HTTP)
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should create new sample form have save and cancel buttons enabled', done => {
    Vue.nextTick().then(() => {
      expect([...vm.$el.querySelectorAll('.actions-header__buttons > button')]
        .map(x => x.textContent.trim()))
        .to.be.deep.equal(['Cancel', 'Save'])
      expect(vm.$el.querySelector('.actions-header__action-buttons__submit').disabled).to.be.eql(false)
      expect(vm.$el.querySelector('.actions-header__action-buttons__cancel').disabled).to.be.eql(false)
    }).then(done, done)
  })

  it('should send a create sample event on save button', done => {
    // Need to copy DATA. Would be modified otherwise.
    vm.sample = Object.assign({}, DATA.sample)
    vm.sample.components = [
      ...LIGAND_COMPONENTS,
      ...PROTEIN_COMPONENTS
    ]

    Vue.nextTick()
      .then(() => {
        vm.$el.querySelector('.actions-header__action-buttons__submit').click()
        vm._watcher.run()

        return new Promise((resolve, reject) => moxios.wait(resolve))
      }).then(() => {
      const request = moxios.requests.mostRecent()

      let result = Object.assign({}, DATA_2)
      result.sampleToSave = result.sample

      expect(JSON.parse(request.config.data)).to.be.deep.equal(result.sample)
    }).then(done, done)
  })

  it('should update url query upon new sample creation', done => {
    // Need to copy DATA. Would be modified otherwise.
    vm.sample = Object.assign({}, DATA.sample)
    vm.sample.components = [
      ...LIGAND_COMPONENTS,
      ...PROTEIN_COMPONENTS
    ]
    Vue.nextTick().then(() => {
      vm.$el.querySelector('.actions-header__action-buttons__submit').click()

      return new Promise((resolve, reject) => moxios.wait(resolve))
    }).then(() => {
      const expectedPush = {
        name: 'sample-view',
        params: {
          id: DATA.sample.slug,
          projectId: DATA.projectSlugOrId
        }
      }
      expect(router.push).to.have.been.calledOnce
      expect(router.push).to.have.been.calledWith(expectedPush)
    }).then(done, done)
  })

  it('should not send update signal due to form validation', done => {
    vm.projectId = '3'

    Vue.nextTick().then(() => {
      vm.sample.label = ''
      vm.saveForm = sandbox.spy()
      vm.submitBaseFormBy('sample')

      return Vue.nextTick()
    }).then(() => {
      expect(vm.saveForm).to.not.have.been.called
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
      incubationTime: 'error',
      incubationTemperature: 'error'
    }
    vm.$events.$emit('validationError', errors)
    Vue.nextTick().then(() => {
      const errorMessages = vm.$el.querySelectorAll('.el-form-item__error')

      errorMessages.should.have.lengthOf(2)
    }).then(done, done)
  })
})
