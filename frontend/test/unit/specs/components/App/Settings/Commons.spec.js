import Vue from 'vue'
import Commons from '@/components/App/Admin/Settings/Commons'

import * as sinon from 'sinon'

const sandbox = sinon.sandbox.create()

describe('Settings/Commons.js', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(Commons)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('addTitleToEntityIfNeeded', () => {
    it('should be called from watcher', done => {
      // given
      const spy = sandbox.spy(vm, 'addTitleToEntityIfNeeded')
      const expected = {}

      // when
      vm.entity = expected

      vm.$nextTick()
        .then(() => {
          // then
          expect(spy).to.have.been.calledWith(expected)
        }).then(done, done)
    })

    it('doesn\'t substitute if a title exists', () => {
      // given
      const expected = 'Expected title'
      const entity = {
        label: expected
      }

      // when
      vm.addTitleToEntityIfNeeded(entity)

      // then
      expect(entity.label).to.be.equal(expected)
    })

    it('should add the \'Settings\' label to entity', () => {
      // given
      const expected = 'Settings'
      const entity = {}

      // when
      vm.addTitleToEntityIfNeeded(entity)

      // then
      expect(entity.label).to.be.equal(expected)
    })
  })

})
