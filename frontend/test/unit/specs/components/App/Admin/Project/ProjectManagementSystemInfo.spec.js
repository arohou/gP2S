import Vue from 'vue'
import * as sinon from 'sinon'

import ProjectManagementSystemInfo from '@/components/App/Admin/Project/management_system_ext/ProjectManagementSystemInfo'
import ProjectService from '@/services/ProjectService'

const sandbox = sinon.sandbox.create()

describe('Admin/Project/management_system_ext/ProjectManagementSystemInfo.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(ProjectManagementSystemInfo)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('handleProjectManagementSystemError', () => {
    it('should set the state to error', () => {
      // when
      vm.handleProjectManagementSystemError('whatever')

      // then
      vm.state.should.equal('error')
    })
  })

  describe('handleProjectInfoDownloaded', () => {
    it('should set projectInfo to the argument given', () => {
      // given
      const expected = 1337

      // when
      vm.handleProjectInfoDownloaded(expected)

      // then
      vm.projectInfo.should.be.equal(expected)
    })

    it('should set state to correct', () => {
      // when
      vm.handleProjectInfoDownloaded('whatever')

      // then
      vm.state.should.be.equal('correct')
    })
  })

  describe('loadProjectManagementSystemData', () => {
    it('should be called from projectManagementSystemSlug watcher', async () => {
      // given
      const expected = 1337
      const spy = sandbox.stub(vm, 'loadProjectManagementSystemData')

      // when
      vm.projectManagementSystemSlug = expected
      await vm.$nextTick()

      // then
      spy.should.have.been.calledWith(expected)
    })

    it('should early break if given projectManagementSystemSlug is falsy', async () => {
      // given
      const expected = 1337
      vm.state = expected
      const spy = sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves({data: 'whatever'})
      sandbox.stub(vm, 'handleProjectInfoDownloaded')

      // when
      await vm.loadProjectManagementSystemData('')
      await vm.loadProjectManagementSystemData(false)
      await vm.loadProjectManagementSystemData(null)
      await vm.loadProjectManagementSystemData(undefined)

      // then
      vm.state.should.be.equal(expected)
      spy.should.not.have.been.called
    })

    it('should set state to processing', async () => {
      // given
      vm.state = 'unknown'
      sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves({data: 'anything'})
      sandbox.stub(vm, 'handleProjectInfoDownloaded')

      // when
      await vm.loadProjectManagementSystemData('whatever')

      // then
      vm.state.should.be.equal('processing')
    })

    it('should call handleProjectInfoDownloaded with the result of _projectService.findProjectManagementSystemEntity', async () => {
      // given
      const expected = 1337
      sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves({data: expected})
      const spy = sandbox.stub(vm, 'handleProjectInfoDownloaded')

      // when
      await vm.loadProjectManagementSystemData('whatever')

      // then
      spy.should.have.been.calledWith(expected)
    })

    it('should call handleProjectManagementSystemError if _projectService.findProjectManagementSystemEntity throws one', async () => {
      // given
      const expected = 1337
      sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').rejects(expected)
      const spy = sandbox.stub(vm, 'handleProjectManagementSystemError')

      // when
      await vm.loadProjectManagementSystemData('whatever')

      // then
      spy.should.have.been.calledWith(expected)
    })
  })

  describe('created', () => {
    it('should instantiate ProjectService', () => {
      vm._projectService.should.be.instanceOf(ProjectService)
    })
  })

  describe('data', () => {
    it('should set state to processing by default', () => {
      vm.state.should.be.equal('processing')
    })
  })
})
