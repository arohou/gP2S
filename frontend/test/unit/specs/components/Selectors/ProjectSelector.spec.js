import Vue from 'vue'
import VueRouter from 'vue-router'
import moxios from 'moxios'
import * as sinon from 'sinon'

import { HTTP } from '@/utils/http-common'
import ProjectSelector from '@/components/Selectors/ProjectSelector'

const sandbox = sinon.sandbox.create()

describe('ProjectSelector.vue', () => {
  const PROJECTS = [
    {id: 1, slug: 'project-1', label: 'Project 1'},
    {id: 2, slug: 'project-2', label: 'Project 2'},
    {id: 3, slug: 'project-3', label: 'Project 3'},
  ]

  const router = new VueRouter({
    routes: [{
      name: 'project',
      path: '/:projectId'
    }]
  })

  let vm = null

  beforeEach(() => {
    moxios.install(HTTP)
    moxios.stubRequest(process.env.API_URL + 'project/', {
      status: 200,
      response: PROJECTS
    })

    const Constructor = Vue.extend(ProjectSelector)
    vm = new Constructor({router})
  })

  afterEach(() => {
    moxios.uninstall()
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should download and populate the list of projects', done => {
    // Initial list of project is empty
    expect(vm.projects).to.be.eql([])

    // Create the view thus downloading projects
    vm = vm.$mount()
    Vue.nextTick()
      .then(() => new Promise((resolve, reject) => moxios.wait(resolve)))
      .then(() => {
        expect(vm.$el.textContent).to.be.equal(PROJECTS.map(project => project.label).join(''))
      }).then(done, done)
  })

  it('checks path on mounting', done => {
    sandbox.spy(vm, 'projectIdChanged')
    vm = vm.$mount()
    Vue.nextTick()
      .then(() => new Promise((resolve, reject) => moxios.wait(resolve)))
      .then(() => {
        expect(vm.projectIdChanged).to.have.been.called
      }).then(done, done)
  })

  it('reacts on route change', async () => {
    // given
    sandbox.spy(vm, 'projectIdChanged')

    // when
    router.push('/' + PROJECTS[0].slug)
    await vm.$nextTick()
    router.push('/' + PROJECTS[1].slug)
    await vm.$nextTick()

    // then
    expect(vm.projectIdChanged).to.have.been.calledTwice
  })

  it('selects project by id', done => {
    sandbox.spy(vm, '$emit')
    vm.projects = PROJECTS
    router.push('/' + PROJECTS[1].id)

    Vue.nextTick().then(() => {
      expect(vm.value).to.be.equal(PROJECTS[1].slug)
      expect(vm.$emit).to.have.been.calledWith('projectSelected', PROJECTS[1].slug)
    }).then(done, done)
  })

  it('selects project by slug', done => {
    sandbox.spy(vm, '$emit')
    vm.projects = PROJECTS
    router.push('/' + PROJECTS[1].slug)

    Vue.nextTick().then(() => {
      expect(vm.value).to.be.equal(PROJECTS[1].slug)
      expect(vm.$emit).to.have.been.calledWith('projectSelected', PROJECTS[1].slug)
    }).then(done, done)
  })

  it('redirects to project URL when a project is selected', () => {
    sandbox.spy(router, 'push')
    vm.value = PROJECTS[0].slug
    vm.selectProject()

    expect(router.push).to.have.been.calledWith({
      name: 'project',
      path: '/' + PROJECTS[0].slug,
      params: {projectId: PROJECTS[0].slug}
    })
  })
})
