import Vue from 'vue'
import * as sinon from 'sinon'
import moment from 'moment'

import ViewHeader from '@/components/App/ViewHeader'

describe('ViewHeader.vue', () => {
  let vm = null
  const sandbox = sinon.sandbox.create()

  beforeEach(() => {
    const Constructor = Vue.extend(ViewHeader)
    vm = new Constructor().$mount()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should display the action buttons', () => {
    vm.$el.querySelector('.action-buttons')
      .should.not.be.null
  })

  it('displays a default title', () => {
    vm.$el.querySelector('.view-header__content__title')
      .textContent.should.be.equal('Unknown Title')
  })

  it('displays title from prop', done => {
    const testTitle = 'Test title'
    vm.title = testTitle

    Vue.nextTick().then(() => {
      vm.$el.querySelector('.view-header__content__title')
        .textContent.should.be.equal(testTitle)
    }).then(done, done)
  })

  it('displays empty metada by default', () => {
    vm.$el.querySelector('.view-header__content__metadata')
      .textContent.trim().should.be.equal('created on  , by')
  })

  it('displays metadata from props', done => {
    const id = 12345
    const createdDate = '1984.11.05, 04:11:05'
    vm.id = id
    vm.createdDate = moment(createdDate, 'YYYY.MM.DD, HH:mm:ss')

    Vue.nextTick().then(() => {
      vm.$el.querySelector('#view-header__content__metadata__id')
        .textContent.should.be.equal('#' + id.toString() + ', ')
      vm.$el.querySelector('#view-header__content__metadata__created-date')
        .textContent.trim().should.be.equal('created on ' + createdDate)
    }).then(done, done)
  })

  it('displays icon from passed props', done => {
    const icon = 'Test icon'
    const iconAlt = 'Icon alt'
    vm.icon = icon
    vm.iconAlt = iconAlt

    Vue.nextTick().then(() => {
      const img = vm.$el.querySelector('.view-header__title__icon img')
      img.getAttribute('src')
        .should.equal(icon)
      img.getAttribute('alt')
        .should.equal(iconAlt)
    }).then(done, done)
  })
})
