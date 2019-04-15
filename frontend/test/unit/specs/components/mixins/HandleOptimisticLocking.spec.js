import Vue from 'vue'
import Router from 'vue-router'
import * as sinon from 'sinon'

import HandleOptimisticLocking from '@/components/mixins/HandleOptimisticLocking'
import OptimisticLockingError from '@/errors/OptimisticLockingError'

Vue.use(Router)

describe('HandleOptimisticLocking', () => {
  const sandbox = sinon.sandbox.create()
  let router = null
  let vm = null

  beforeEach(() => {
    router = new Router({abstract: true})
    const Constructor = Vue.extend(HandleOptimisticLocking)
    vm = new Constructor({router})
  })

  afterEach(() => {
    router = null
    vm = null
    sandbox.restore()
  })

  it('reloads upon receiving the OptimisticLockingError', () => {
    router.go = sandbox.spy()

    vm.handleOptimisticLockingError(new OptimisticLockingError('Test error'))
    expect(router.go).to.have.been.calledWith(router.path)
  })

  it('re-throws non OptimisticLockingErrors', () => {
    router.go = sandbox.spy()

    const genericError = new Error('Generic error')
    expect(() => vm.handleOptimisticLockingError(genericError)).to.throw(genericError)
    expect(router.go).to.not.have.been.called
  })
})
