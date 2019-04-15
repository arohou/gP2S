import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'

import View from '@/components/App/Ligand/registration_system_ext/View'
import VueRouter from 'vue-router'
import { HTTP } from '@/utils/http-common'

const sandbox = sinon.sandbox.create()

const router = new VueRouter()
describe('Ligand/View.vue', () => {
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
      solvent: 'DMSO'
    }
  }

  const PROJECTS = [
    {id: 1, slug: 'project-1', label: 'Project 1', ligands: [DATA.ligand]},
    {id: 2, slug: 'project-2', label: 'Project 2'},
    {id: 3, slug: 'project-3', label: 'Project 3'}
  ]

  beforeEach(() => {
    sandbox.stub(router, 'push')
    const Constructor = Vue.extend(View)
    vm = new Constructor({router})

    moxios.install(HTTP)
    moxios.stubRequest(process.env.API_URL + 'ligand/' + DATA.ligand.slug, {
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
    vm.id = DATA.ligand.slug
    vm.projectId = DATA.projectSlugOrId
    vm.$mount()
  })

  afterEach(() => {
    moxios.uninstall()
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should display the edit button', () => {
    expect(vm.$el.querySelector('#action-buttons__edit').title).to.be.eql('Edit')
  })

  it('should edit button switch to edit form', done => {
    Vue.nextTick().then(() => {
      vm.$el.querySelector('#action-buttons__edit').click()

      const expectedPush = {
        name: 'ligand-edit',
        params: {
          id: DATA.ligand.slug,
          projectId: DATA.projectSlugOrId
        }
      }

      expect(router.push).to.have.been.calledOnce
      expect(router.push).to.have.been.calledWith(expectedPush)
    }).then(done, done)
  })

  it('should change CONCEPT link upon edit', done => {
    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      expect(vm.$el.querySelector('a#conceptId').href).to.have.string('/#/dashboard/details/' + DATA.ligand.conceptId)

      vm.ligand.conceptId = 'CHANGED'
      return Vue.nextTick()
    }).then(() => {
      expect(vm.$el.querySelector('a#conceptId').href).to.have.string('/#/dashboard/details/' + 'CHANGED')
    }).then(done, done)
  })

  describe('validateFullBatchId', () => {
    it('errors should be indicated for empty fullBatchId', () => {
      // given
      const fullBatchId = null
      // when
      vm.validateFullBatchId(fullBatchId)
      // then
      expect(vm.fullBatchIdState).to.be.eql(vm.ProcessingState.ERROR)
    })

    it('errors should be indicated for CONCEPT error', () => {
      // given
      const fullBatchId = 123
      const errorMessage = 'Hello world'
      // when
      vm.validateFullBatchId(fullBatchId, errorMessage)
      // then
      expect(vm.fullBatchIdState).to.be.eql(vm.ProcessingState.ERROR)
    })

    it('should indicate Batch Id change', () => {
      // given
      vm.ligand.batchId = 456
      const fullBatchId = 123
      // when
      vm.validateFullBatchId(fullBatchId)
      // then
      expect(vm.fullBatchIdState).to.be.eql(vm.ProcessingState.OUTDATED)
    })

    it('should recognize unchanged Batch Id', () => {
      // given
      vm.ligand.batchId = 123
      const fullBatchId = 123
      // when
      vm.validateFullBatchId(fullBatchId)
      // then
      expect(vm.fullBatchIdState).to.be.eql(vm.ProcessingState.VALID)
    })
  })
})
