import Vue from 'vue'
import VueRouter from 'vue-router'
import ActionsHeader from '@/components/App/ActionsHeader'
import * as sinon from 'sinon'

const sandbox = sinon.sandbox.create()

describe('ActionsHeader.vue', () => {
  let vm = null
  let router = null

  beforeEach(() => {
    const Constructor = Vue.extend(ActionsHeader)
    router = new VueRouter()
    vm = new Constructor({router}).$mount()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
    router = null
  })

  it('should display default title and buttons', () => {
    vm.$el.querySelector('.actions-header__title').textContent.should.be.equal('Unknown title')
    expect([...vm.$el.querySelectorAll('.actions-header__buttons > button')]
      .map(button => button.textContent.trim()))
      .to.be.deep.equal(['Cancel', 'Save'])
  })

  it('should display titles supplied by props', done => {
    const title = 'Test title'
    const confirmTitle = 'Confirm title'
    const cancelTitle = 'Cancel title'
    vm.title = title
    vm.confirmTitle = confirmTitle
    vm.cancelTitle = cancelTitle

    Vue.nextTick().then(() => {
      vm.$el.querySelector('.actions-header__title').textContent.should.be.equal(title)
      vm.$el.querySelector('.actions-header__buttons').textContent
        .should.match(new RegExp('^' + cancelTitle + '[^]*' + confirmTitle + '[^]*$'))
    }).then(done, done)
  })

  it('should emit actionConfirm on save click', () => {
    sandbox.spy(vm, '$emit')
    vm.$el.querySelector('.actions-header__action-buttons__submit').click()
    vm.$emit.should.have.been.calledWith('actionConfirm')
  })

  it('should go back in history on cancel click', () => {
    sandbox.stub(router, 'go')
    vm.$el.querySelector('.actions-header__action-buttons__cancel').click()
    vm.$router.go.should.have.been.calledWith(-1)
  })

})
