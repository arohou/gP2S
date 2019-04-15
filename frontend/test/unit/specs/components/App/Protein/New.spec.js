import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'

import ProteinNew from '@/components/App/Protein/registration_system_ext/New'
import VueRouter from 'vue-router'
import { HTTP } from '@/utils/http-common'

const sandbox = sinon.sandbox.create()

const router = new VueRouter()
describe('Protein/registration_system_ext/New.vue', () => {
  let vm = null

  const DATA = {
    projectSlugOrId: 'project-1',
    protein: {
      id: 1,
      slug: 'protein-label',
      label: 'Protein label',
      purificationId: '333',
      tubeLabel: 'T333',
      concentration: {
        isDilutedOrConcentrated: false,
        concentrationType: 'Concentration',
        dilutionFactor: null,
        concentrationFactor: null
      }
    }
  }

  const DATA_2 = {
    projectSlugOrId: 'project-1',
    protein: {
      label: 'Protein label',
      purificationId: '333',
      tubeLabel: 'T333',
      concentration: {
        isDilutedOrConcentrated: false,
        concentrationType: 'Concentration',
        dilutionFactor: null,
        concentrationFactor: null
      }
    }
  }

  const PROJECTS = [
    {id: 1, label: 'Project 1', proteins: [DATA.protein]},
    {id: 2, label: 'Project 2'},
    {id: 3, label: 'Project 3'}
  ]

  beforeEach(() => {
    sandbox.stub(router, 'push')
    sandbox.stub(router, 'go')
    const Constructor = Vue.extend(ProteinNew)
    vm = new Constructor({router})

    moxios.install(HTTP)
    moxios.stubRequest(process.env.API_URL + 'protein/' + DATA.protein.slug, {
      status: 200,
      response: DATA.protein
    })
    moxios.stubRequest(process.env.API_URL + 'protein/' + DATA.protein.slug + '/projects', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'project/', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'protein/' + DATA.projectSlugOrId, {
      status: 200,
      response: DATA.protein
    })

    vm.id = DATA.protein.slug
    vm.projectId = DATA.projectSlugOrId
    vm = vm.$mount()
  })

  afterEach(() => {
    moxios.uninstall(HTTP)
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should create new protein form have save and cancel buttons enabled', done => {
    Vue.nextTick().then(() => {
      expect([...vm.$el.querySelectorAll('.actions-header__buttons > button')]
        .map(x => x.textContent.trim()).join())
        .to.be.equal('Cancel,Save')
      expect(vm.$el.querySelector('.actions-header__action-buttons__submit').disabled).to.be.eql(false)
      expect(vm.$el.querySelector('.actions-header__action-buttons__cancel').disabled).to.be.eql(false)
    }).then(done, done)
  })

  it('should send a create protein event on save button ', done => {
    vm.projectId = DATA_2.projectSlugOrId
    vm.protein = Object.assign({}, DATA_2.protein)
    vm.protein.concentration = Object.assign({}, DATA_2.protein.concentration)
    vm.$events.$emit('initialProteinRegistrationSystemCall', vm.protein.purificationId, vm.protein.purificationId)

    // After protein object assign there need to be a tick to pickup protein
    // and put it on form otherwise form validation will fail
    Vue.nextTick().then(() => {
      moxios.requests.reset()
      vm.$el.querySelector('.actions-header__action-buttons__submit').click()
      vm._watcher.run()
      return new Promise((resolve, reject) => moxios.wait(resolve))
    }).then(() => {
      const request = moxios.requests.mostRecent()
      expect(request.config.method).to.equal('post')
      expect(JSON.parse(request.config.data)).to.be.deep.equal(DATA_2.protein)
    }).then(done, done)
  })

  it('should update url query upon new protein creation', done => {
    Vue.nextTick().then(() => {
      vm.protein = DATA.protein
      vm.$events.$emit('initialProteinRegistrationSystemCall', vm.protein.purificationId, vm.protein.purificationId)
      // After DATA object assign there need to be a tick to pickup DATA
      // and put it on form otherwise form validation will fail
      return Vue.nextTick()
    }).then(() => {
      vm.$el.querySelector('.actions-header__action-buttons__submit').click()
      return new Promise((resolve, reject) => moxios.wait(resolve))
    }).then(() => {
      const expectedPush = {
        name: 'protein-view',
        params: {
          id: DATA.protein.slug,
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
      vm.saveForm = sandbox.spy()
      vm.submitBaseFormBy('protein')
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
})
