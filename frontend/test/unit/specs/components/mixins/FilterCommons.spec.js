import Vue from 'vue'
import * as sinon from 'sinon'

import FilterCommons from '@/components/mixins/FilterCommons'

const sandbox = sinon.sandbox.create()

describe('FilterCommons.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(FilterCommons)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm = null
  })

  describe('formatArray', () => {
    it('should call formatValue if the given array is empty', () => {
      // given
      const results = []

      // when
      results.push(vm.$options.filters.formatArray(undefined))
      results.push(vm.$options.filters.formatArray(null))
      results.push(vm.$options.filters.formatArray([]))

      // then
      results.should.be.deep.equal(['—', '—', '—'])
    })

    it('should apply all given filters to all given values', () => {
      // given
      const spy1 = sandbox.stub().returnsArg(0)
      const spy2 = sandbox.stub().returnsArg(0)
      const spy3 = sandbox.stub().returnsArg(0)
      const values = [1, 2, 3]

      // when
      vm.$options.filters.formatArray(values, spy1, spy2, spy3)

      // then
      for (let aValue of values) {
        spy1.should.have.been.calledWith(aValue)
        spy2.should.have.been.calledWith(aValue)
        spy3.should.have.been.calledWith(aValue)
      }
    })

    it('should return — if a value becomes empty after filters', () => {
      // given
      const stub1 = sandbox.stub().returnsArg(0)
      const stub2 = sandbox.stub().returnsArg(0)
      const stub3 = sandbox.stub().returns('')
      const values = [1, 2, 3]

      // when
      const result = vm.$options.filters.formatArray(values, stub1, stub2, stub3)

      // then
      result.should.be.equal('—, —, —')
    })

    it('should connect all values into a comma separated list', () => {
      // given
      const filter1 = sandbox.stub().returnsArg(0)
      const filter2 = sandbox.stub().returnsArg(0)
      const filter3 = sandbox.stub().returnsArg(0)
      const values = [1, 2, 3]

      // when
      const result = vm.$options.filters.formatArray(values, filter1, filter2, filter3)

      // then
      result.should.be.equal('1, 2, 3')
    })
  })
})
