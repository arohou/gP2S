import Vue from 'vue'
import * as sinon from 'sinon'

import CommonAlerts from '@/components/mixins/CommonAlerts'

describe('CommonAlerts', () => {
  const sandbox = sinon.sandbox.create()
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(CommonAlerts)
    vm = new Constructor()
  })

  afterEach(() => {
    vm = null
    sandbox.restore()
  })

  it('calls vue alert no project', () => {
    vm.$alert = sandbox.spy()

    vm.alertNoProject()
    expect(vm.$alert).to.have.been.calledOnce
  })
  
  it('calls vue alert general form', () => {
    vm.$alert = sandbox.spy()

    vm.alertGeneralFormIssue()
    expect(vm.$alert).to.have.been.calledOnce
  })

  it('calls vue alert general error', () => {
    vm.$alert = sandbox.spy()

    vm.alertErrorOK()
    expect(vm.$alert).to.have.been.calledOnce
  })

  it('calls vue alert general warn', () => {
    vm.$alert = sandbox.spy()

    vm.alertWarnOK()
    expect(vm.$alert).to.have.been.calledOnce
  })

})
