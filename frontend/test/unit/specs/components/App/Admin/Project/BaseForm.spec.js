import Vue from 'vue'
import * as sinon from 'sinon'

import BaseForm from '@/components/App/Admin/Project/management_system_ext/BaseForm'
import ProjectService from '@/services/ProjectService'

const sandbox = sinon.sandbox.create()

describe('Admin/Project/management_system_ext/BaseForm.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(BaseForm)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    if (vm) {
      vm.$destroy()
      vm = null
    }
  })

  describe('created', () => {
    it('should create the ProjectService', () => {
      expect(vm._projectService).to.be.instanceOf(ProjectService)
    })
  })

  describe('mounted', () => {
    it('should connect to initialProjectManagementSystemCall', () => {
      // given
      const spy = sandbox.spy(vm.$events, 'on')

      // when
      vm = vm.$mount()

      // then
      spy.should.have.been.calledWith('initialProjectManagementSystemCall', vm.initialProjectManagementSystemCall)
    })

    it('should connect to validateAndSaveProject', () => {
      // given
      const spy = sandbox.spy(vm.$events, 'on')

      // when
      vm = vm.$mount()

      // then
      spy.should.have.been.calledWith('validateAndSaveProject', vm.validateAndSaveProject)
    })
  })

  describe('beforeDestroy', () => {
    it('should disconnect from initialProjectManagementSystemCall', () => {
      // given
      const spy = sandbox.spy(vm.$events, 'off')
      const callback = vm.validateAndSaveProject

      // when
      vm = vm.$destroy()

      // then
      spy.should.have.been.calledWith('validateAndSaveProject', callback)
    })
  })

  describe('setProjectManagementSystemError', () => {
    it('should set the given error', () => {
      // given
      const expected = 1337

      // when
      vm.setProjectManagementSystemError(expected)

      // then
      vm.errors.projectManagementSystemSlug.should.be.equal(expected)
    })
  })

  describe('validateProjectManagementSystemSlug', () => {
    it('should reset projectManagementSystemSlug error', async () => {
      // given
      sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves({displayId: 'whatever'})
      sandbox.stub(vm, 'handleProjectManagementSystemProjectValidated')
      vm.entity = {
        projectManagementSystemSlug: 'doesn\'t matter'
      }
      vm.errors.projectManagementSystemSlug = 'Some error'

      // when
      await vm.validateProjectManagementSystemSlug()

      // then
      vm.errors.projectManagementSystemSlug.should.be.equal('')
    })

    it('should set projectManagementSystemSlugValidationSate to correct and early break if slug was previously validated', async () => {
      // given
      sandbox.stub(vm, 'setProjectManagementSystemError')
      const spy = sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves({displayId: 'whatever'})
      sandbox.stub(vm, 'handleProjectManagementSystemProjectValidated')
      vm.entity = {
        projectManagementSystemSlug: 'doesn\'t matter'
      }
      vm.validProjectManagementSystemSlugs.push(vm.entity.projectManagementSystemSlug)

      // when
      await vm.validateProjectManagementSystemSlug()

      // then
      vm.projectManagementSystemSlugValidationState.should.equal('correct')
      spy.should.not.have.been.called
    })

    it('should set projectManagementSystemSlugValidationSate to processing if slug wasn\'t previously validated', async () => {
      // given
      sandbox.stub(vm, 'setProjectManagementSystemError')
      sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves({displayId: 'whatever'})
      sandbox.stub(vm, 'handleProjectManagementSystemProjectValidated')
      vm.entity = {
        projectManagementSystemSlug: 'doesn\'t matter'
      }

      // when
      await vm.validateProjectManagementSystemSlug()

      // then
      vm.projectManagementSystemSlugValidationState.should.equal('processing')
    })

    it('should call _projectService.findProjectManagementSystemEntity', async () => {
      // given
      const expected = 1337
      vm.entity = {
        projectManagementSystemSlug: expected
      }
      const spy = sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves({displayId: 'whatever'})
      sandbox.stub(vm, 'handleProjectManagementSystemProjectValidated')

      // when
      await vm.validateProjectManagementSystemSlug()

      // then
      spy.should.have.been.calledWith(expected)
    })

    it('should call handleProjectManagementSystemProjectValidated with the downloaded projectManagementSystemSlug', async () => {
      // given
      const expected = 1337
      vm.entity = {
        projectManagementSystemSlug: expected
      }
      sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves({displayId: expected})
      const spy = sandbox.stub(vm, 'handleProjectManagementSystemProjectValidated')

      // when
      await vm.validateProjectManagementSystemSlug()

      // then
      spy.should.have.been.calledWith(expected)
    })

    it('should call handleProjectManagementSystemErrors when _projectService.findProjectManagementSystemEntity throws', async () => {
      // given
      const expected = new Error('1337')
      sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').rejects(expected)
      const spy = sandbox.stub(vm, 'handleProjectManagementSystemError')
      vm.entity = {
        projectManagementSystemSlug: 'doesn\'t matter'
      }

      // when
      try {
        await vm.validateProjectManagementSystemSlug()
      } catch (ignore) {
        window.console.warn('Ignore exception')
      }

      // then
      spy.should.have.been.calledWith(expected)
    })
  })

  describe('handleEmptyProjectManagementSystemSlug', () => {
    it('should set projectManagementSystemSlugValidationState to invalid', () => {
      // given
      sandbox.stub(vm, 'setProjectManagementSystemError')

      // when
      vm.handleEmptyProjectManagementSystemSlug()

      // then
      vm.projectManagementSystemSlugValidationState.should.equal('invalid')
    })

    it('should call setProjectManagementSystemError', () => {
      // given
      const spy = sandbox.stub(vm, 'setProjectManagementSystemError')

      // when
      vm.handleEmptyProjectManagementSystemSlug()

      // then
      spy.should.have.been.called
    })
  })

  describe('handleProjectManagementSystemError', () => {
    it('should early break if error is falsy', () => {
      // given
      const spy = sandbox.stub(vm, 'setProjectManagementSystemError')

      // when
      vm.handleProjectManagementSystemError('')
      vm.handleProjectManagementSystemError(null)
      vm.handleProjectManagementSystemError()

      // then
      expect(vm.projectManagementSystemSlugValidationState).to.not.equal('invalid')
      spy.should.not.have.been.called
    })

    it('should set projectManagementSystemSlugValidationState to invalid', () => {
      // given
      sandbox.stub(vm, 'setProjectManagementSystemError')

      // when
      vm.handleProjectManagementSystemError('whatever')

      // then
      vm.projectManagementSystemSlugValidationState.should.equal('invalid')
    })

    it('should call setProjectManagementSystemError', () => {
      // given
      const spy = sandbox.stub(vm, 'setProjectManagementSystemError')

      // when
      vm.handleProjectManagementSystemError('whatever')

      // then
      spy.should.have.been.called
    })
  })

  describe('handleProjectManagementSystemProjectValidated', () => {
    it('should push the given slug to validProjectManagementSystemSlugs', () => {
      // given
      sandbox.stub(vm, 'setProjectManagementSystemError')
      vm.validProjectManagementSystemSlugs = []
      const expected = 1337

      // when
      vm.handleProjectManagementSystemProjectValidated(expected)

      // then
      vm.validProjectManagementSystemSlugs.should.be.deep.equal([expected])
    })

    it('should reset setProjectManagementSystemError', () => {
      // given
      const spy = sandbox.stub(vm, 'setProjectManagementSystemError')

      // when
      vm.handleProjectManagementSystemProjectValidated('whatever')

      // then
      spy.should.have.been.calledWith('')
    })

    it('should set projectManagementSystemSlugValidationState to correct', () => {
      // given
      sandbox.stub(vm, 'setProjectManagementSystemError')

      // when
      vm.handleProjectManagementSystemProjectValidated('whatever')

      // then
      vm.projectManagementSystemSlugValidationState.should.equal('correct')
    })
  })

  describe('initialProjectManagementSystemCall', () => {
    it('should set originalProjectManagementSystemSlug with the given parameter', () => {
      // given
      const expected = 1337
      sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves()

      // when
      vm.initialProjectManagementSystemCall(expected)

      // then
      vm.originalProjectManagementSystemSlug.should.equal(expected)
    })

    it('should set projectManagementSystemSlugValidationState to correct', () => {
      // given
      const expected = 1337
      sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves()

      // when
      vm.initialProjectManagementSystemCall(expected)

      // then
      vm.projectManagementSystemSlugValidationState.should.equal('correct')
    })

    it('should push the given originalProjectManagementSystemSlug to validProjectManagementSystemSlugs', () => {
      // given
      const expected = 1337
      sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves()

      // when
      vm.initialProjectManagementSystemCall(expected)

      // then
      vm.validProjectManagementSystemSlugs.should.deep.equal([expected])
    })

    it('should call _projectService.findProjectManagementSystemEntity', () => {
      // given
      const expected = 1337
      const spy = sandbox.stub(vm._projectService, 'findProjectManagementSystemEntity').resolves()

      // when
      vm.initialProjectManagementSystemCall(expected)

      // then
      spy.should.have.been.calledWith(expected)
    })
  })

  describe('validateAndSaveProject', () => {
    it('should emit formSavingStarted', async () => {
      // given
      const spy = sandbox.stub(vm.$events, '$emit')
      sandbox.stub(vm, 'validateProjectManagementSystemSlug').resolves()
      const saveCallback = sandbox.stub()
      vm.entity = {
        projectManagementSystemSlug: 'whatever'
      }

      // when
      await vm.validateAndSaveProject(saveCallback)

      // then
      spy.should.have.been.calledWith('formSavingStarted')
    })

    it('should call validateProjectManagementSystemSlug with the projectManagementSystemSlug', async () => {
      // given
      sandbox.stub(vm.$events, '$emit')
      const spy = sandbox.stub(vm, 'validateProjectManagementSystemSlug').resolves()
      const expected = 1337
      const saveCallback = sandbox.stub()
      vm.entity = {
        projectManagementSystemSlug: expected
      }

      // when
      await vm.validateAndSaveProject(saveCallback)

      // then
      spy.should.have.been.calledWith(expected)
    })

    it('should call the given saveCallback', async () => {
      // given
      sandbox.stub(vm.$events, '$emit')
      sandbox.stub(vm, 'validateProjectManagementSystemSlug').resolves()
      const expected = 1337
      const saveCallback = sandbox.stub()
      vm.entity = {
        projectManagementSystemSlug: expected
      }

      // when
      await vm.validateAndSaveProject(saveCallback)

      // then
      saveCallback.should.have.been.called
    })

    it('should emit the formSavingFinished when validateProjectManagementSystemSLug throws', async () => {
      // given
      const spy = sandbox.stub(vm.$events, '$emit')
      sandbox.stub(vm, 'validateProjectManagementSystemSlug').rejects()
      const expected = 1337
      const saveCallback = sandbox.stub()
      vm.entity = {
        projectManagementSystemSlug: expected
      }

      // when
      await vm.validateAndSaveProject(saveCallback)
      await vm.$nextTick()

      // then
      spy.should.have.been.calledWith('formSavingFinished')
    })
  })
})
