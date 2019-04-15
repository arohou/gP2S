import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'

import ProteinCopy from '@/components/App/Protein/registration_system_ext/Copy'
import VueRouter from 'vue-router'
import { HTTP } from '@/utils/http-common'

const sandbox = sinon.sandbox.create()

const router = new VueRouter()
describe('Protein/registration_system_ext/Copy.vue', () => {
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
      id: '',
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
    const Constructor = Vue.extend(ProteinCopy)
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
    sandbox.stub(vm, 'formLoadingStarted') //prevent disable save button on form mount
    vm = vm.$mount()
  })

  afterEach(() => {
    moxios.uninstall(HTTP)
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should create copy protein form have save and cancel buttons enabled', done => {
    new Promise((resolve, reject) => moxios.wait(resolve))
      .then(() => {
        expect([...vm.$el.querySelectorAll('.actions-header__buttons > button')]
          .map(x => x.textContent.trim()))
          .to.be.eql(['Cancel', 'Save'])
        expect(vm.$el.querySelector('.actions-header__action-buttons__submit').disabled).to.be.eql(false)
        expect(vm.$el.querySelector('.actions-header__action-buttons__cancel').disabled).to.be.eql(false)
      }).then(done, done)
  })

  it('should create copy protein form have defaults', done => {
    new Promise((resolve, reject) => moxios.wait(resolve))
      .then(() => {
        expect([...vm.$el.querySelectorAll('input')].map(x => x.value)).to.be.eql(['Copy of ' + DATA.protein.label, '',
          DATA.protein.tubeLabel, (!DATA.protein.concentration.isDilutedOrConcentrated).toString(),
          DATA.protein.concentration.isDilutedOrConcentrated.toString()
        ])
      }).then(done, done)
  })

  describe('createProtein callback', () => {
    it('should open view form after protein create', async () => {
      // given
      vm.projectId = DATA_2.projectSlugOrId
      vm.protein = Object.assign({}, DATA_2.protein)
      vm.protein.concentration = Object.assign({}, DATA_2.protein.concentration)
      sandbox.stub(vm, 'loadFormData')
      const spy = sandbox.stub(vm, 'openViewForm')
      vm._proteinService = {
        createProtein: sandbox.stub().resolves({data: DATA.protein})
      }

      // when
      vm.createProtein()
      await vm.$nextTick()

      // then
      spy.should.have.been.calledOnce
      spy.should.have.been.calledWith('protein', DATA.protein)
    })

    it('should create protein event callback execute protein create', async () => {
      // given
      vm.projectId = DATA_2.projectSlugOrId
      vm.protein = Object.assign({}, DATA_2.protein)
      vm.protein.concentration = Object.assign({}, DATA_2.protein.concentration)
      sandbox.stub(vm, 'loadFormData')
      sandbox.stub(vm, 'openViewForm')
      vm._proteinService = {
        createProtein: sandbox.stub().resolves({data: DATA.protein})
      }

      // when
      vm.createProtein()

      // then
      vm._proteinService.createProtein.should.have.been.calledOnce
    })
  })
  describe('saveForm', () => {
    it('should send a create protein event on save form ', async () => {
      // given
      vm.projectId = DATA_2.projectSlugOrId
      vm.protein = Object.assign({}, DATA_2.protein)
      vm.protein.concentration = Object.assign({}, DATA_2.protein.concentration)
      sandbox.stub(vm, 'loadFormData')
      const spy = sandbox.stub(vm.$events, '$emit')
      const callback = sandbox.stub(vm, 'createProtein')

      // when
      vm.saveForm()

      // then
      spy.should.have.been.calledWith('validateAndSaveProtein', DATA_2.protein.purificationId, callback)
    })
  })

  it('should update url query upon new protein creation', done => {
    new Promise((resolve, reject) => moxios.wait(resolve))
      .then(() => {
        vm.protein.purificationId = DATA.protein.purificationId
        vm.$events.$emit('initialProteinRegistrationSystemCall', vm.protein.purificationId, vm.protein.purificationId)
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

  describe('loadFromData', () => {
    it('should prepare the copied protein', (done) => {
      // given
      sandbox.stub(vm._proteinService, 'getProteinBy').resolves({
        data: {
          testKey: 'testValue',
          label: 'test label'
        }
      })

      // when
      vm.loadFormData('anything').then(() => {
        // then
        vm.protein.should.be.deep.equal({
          testKey: 'testValue',
          id: null,
          slug: null,
          label: 'Copy of test label',
          purificationId: '',
          availableForSampleMaking: true
        })
      }).then(done, done)
    })
  })
})
