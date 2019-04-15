import Vue from 'vue'
import * as sinon from 'sinon'

import ActionButtons from '@/components/App/ActionButtons'

describe('ActionButtons.vue', () => {
  let vm = null
  const sandbox = sinon.sandbox.create()

  beforeEach(() => {
    const Constructor = Vue.extend(ActionButtons)
    vm = new Constructor().$mount()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should display three default buttons', () => {
    vm.$el.querySelectorAll('.el-button--default')
      .should.be.lengthOf(4)

    const buttonImages = [...vm.$el.querySelectorAll('[alt]')]
    buttonImages.map(el => el.getAttribute('alt'))
      .should.deep.equal(['Print', 'Edit', 'Copy', 'Delete'])
    buttonImages.map(el => el.getAttribute('src'))
      .should.be.lengthOf(4)
  })

  it('emits actionEdit on edit button click', () => {
    const spy = sandbox.spy(vm.$events, 'emit')
    vm.$el.querySelector('#action-buttons__edit').click()
    spy.should.have.been.calledOnce
    spy.should.have.been.calledWith('actionEdit')
  })

  it('emits actionCopy on copy button click', () => {
    const spy = sandbox.spy(vm.$events, 'emit')
    vm.$el.querySelector('#action-buttons__copy').click()
    spy.should.have.been.calledOnce
    spy.should.have.been.calledWith('actionCopy')
  })

  it('emits actionDelete on delete button click', () => {
    const spy = sandbox.spy(vm.$events, 'emit')
    // FIXME: Remove once the delete button gets enabled
    vm.$el.querySelector('#action-buttons__delete').disabled = false
    vm.$el.querySelector('#action-buttons__delete').click()
    spy.should.have.been.calledOnce
    spy.should.have.been.calledWith('actionDelete')
  })

})
