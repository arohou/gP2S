import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'

import Edit from '@/components/App/Ligand/registration_system_ext/Edit'
import VueRouter from 'vue-router'
import { HTTP } from '@/utils/http-common'
import LigandEnabledFields from '@/components/App/Ligand/LigandEnabledFields'

const sandbox = sinon.sandbox.create()

const router = new VueRouter()
describe('Ligand/Edit.vue', () => {
  let vm = null

  const DATA = {
    projectSlugOrId: 'project-1',
    ligand: {
      id: 1,
      slug: 'ligand-label',
      label: 'Ligand Label',
      tubeLabel: 'Tube label',
      concentration: '0.3',
      conceptId: 'CONCEPT1',
      solvent: 'DMSO',
      ligandEnabledFields: LigandEnabledFields.CONCEPT,
      batchId: 'G00001234.1-1'
    }
  }

  const PROJECTS = [
    {
      id: 1,
      slug: DATA.projectSlugOrId,
      label: 'Project 1',
      ligands: [DATA.ligand]
    },
    {id: 2, label: 'Project 2', slug: 'project-2'},
    {id: 3, label: 'Project 3', slug: 'project-3'}
  ]

  beforeEach(() => {
    moxios.install(HTTP)
    moxios.stubRequest(process.env.API_URL + '/ligand/' + DATA.ligand.slug, {
      status: 200,
      response: DATA.ligand
    })
    moxios.stubRequest(process.env.API_URL + 'ligand/' + DATA.ligand.slug + '/projects', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'project/', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'ligand/', {
      status: 200,
      response: DATA.ligand
    })

    sandbox.stub(router, 'push')
    sandbox.stub(router, 'go')
    const Constructor = Vue.extend(Edit)
    vm = new Constructor({router})
    vm.id = DATA.ligand.slug
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

  it('should display the save and cancel button', () => {
    expect([...vm.$el.querySelectorAll('.actions-header__buttons > button')]
      .map(x => x.textContent.trim()))
      .to.be.deep.equal(['Cancel', 'Save'])
  })

  describe('createLigand callback', () => {
    it('should open view form after ligand create', async () => {
      // given
      vm.projectId = DATA.projectSlugOrId
      vm.ligand = Object.assign({}, DATA.ligand)
      sandbox.stub(vm, 'loadFormData')
      const spy = sandbox.stub(vm, 'openViewForm')
      vm._service = {
        saveLigand: sandbox.stub().resolves({data: DATA.ligand})
      }

      // when
      vm.createLigand()
      await vm.$nextTick()

      // then
      spy.should.have.been.calledOnce
      spy.should.have.been.calledWith('ligand', DATA.ligand)
    })

    it('should create ligand event callback execute ligand create', async () => {
      // given
      vm.projectId = DATA.projectSlugOrId
      vm.ligand = Object.assign({}, DATA.ligand)
      sandbox.stub(vm, 'loadFormData')
      sandbox.stub(vm, 'openViewForm')
      vm._service = {
        saveLigand: sandbox.stub().resolves({data: DATA.ligand})
      }

      // when
      vm.createLigand()

      // then
      vm._service.saveLigand.should.have.been.calledOnce
    })
  })

  describe('saveForm', () => {
    it('should send a create ligand event on save form ', async () => {
      // given
      vm.projectId = DATA.projectSlugOrId
      vm.ligand = Object.assign({}, DATA.ligand)
      sandbox.stub(vm, 'loadFormData')
      const spy = sandbox.stub(vm.$events, '$emit')
      const callback = sandbox.stub(vm, 'createLigand')

      // when
      vm.saveForm()

      // then
      spy.should.have.been.calledWith('validateAndSaveLigand', callback)
    })

    it('should not send update signal due to form validation', done => {
      Vue.nextTick().then(() => {
        vm.saveForm = sandbox.spy()
        vm.submitBaseFormBy('ligand')

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
})
