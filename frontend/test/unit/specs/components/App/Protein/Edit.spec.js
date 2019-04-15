import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'

import ProteinEdit from '@/components/App/Protein/Edit'
import VueRouter from 'vue-router'
import { HTTP } from '@/utils/http-common'

const sandbox = sinon.sandbox.create()

const router = new VueRouter()
describe('Protein/Edit.vue', () => {
  let vm = null

  const DATA = {
    projectSlugOrId: 'project-1',
    protein: {
      id: 1,
      slug: 'protein-label',
      label: 'Protein label',
      purificationId: '333',
      tubeLabel: 'T333',
      concentration: '0.2'
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
    const Constructor = Vue.extend(ProteinEdit)
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
    moxios.stubRequest(process.env.API_URL + 'protein/', {
      status: 200,
      response: DATA.protein
    })

    vm.id = DATA.protein.slug
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
      .to.deep.equal(['Cancel', 'Save'])
  })

  it('should send an update protein event on save button ', done => {
    vm.protein = DATA.protein

    Vue.nextTick().then(() => {
      expect(vm.protein).to.be.deep.equal(DATA.protein)
      vm.$el.querySelector('.actions-header__action-buttons__submit').click()

      return new Promise((resolve, reject) => moxios.wait(resolve))
    }).then(() => {
      expect(vm.protein).to.be.equal(DATA.protein)
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
