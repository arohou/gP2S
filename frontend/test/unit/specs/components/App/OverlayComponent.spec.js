import Vue from 'vue'
import * as sinon from 'sinon'

import OverlayComponent from '@/components/App/OverlayComponent'

describe('OverlayComponent.vue', () => {
  let vm = null
  const sandbox = sinon.sandbox.create()

  beforeEach(() => {
    const Constructor = Vue.extend(OverlayComponent)
    vm = new Constructor().$mount()
  })

  afterEach(() => {
    sandbox.restore()
    vm = vm.$destroy()
    vm = null
  })

  it('is hidden by default', () => {
    vm.$el.style.display
      .should.be.equal('none')
  })

  it('reacts to visible prop change', done => {
    vm.visible = true
    Vue.nextTick().then(() => {
      vm.$el.style.display
        .should.be.equal('', 'Shows when visible is true')
      vm.visible = false
      return Vue.nextTick()
    }).then(done, done)
  })

  it('displays default content', done => {
    // Body is rendered only on display
    vm.visible = true
    Vue.nextTick().then(() => {
      vm.$el.querySelector('.el-dialog__body')
        .textContent.trim().should.equal('Empty content')
    }).then(done, done)
  })

  it('reacts to default slot change', done => {
    vm = new Vue({
      template: '<overlay-component :visible=true>Test</overlay-component>',
      components: {OverlayComponent}
    }).$mount()
    Vue.nextTick().then(() => {
      vm.$el.querySelector('.el-dialog__body')
        .textContent.trim().should.equal('Test')
    }).then(done, done)
  })

  it('emits actionClose event on outside of dialog click', done => {
    sandbox.spy(vm, '$emit')
    vm.visible = true
    Vue.nextTick().then(() => {
      vm.$emit.should.have.been.calledWith('update:visible')
      vm.$el.click()
      return Vue.nextTick()
    }).then(() => {
      vm.$emit.should.have.been.calledWith('update:visible')
    }).then(done, done)
  })

})
