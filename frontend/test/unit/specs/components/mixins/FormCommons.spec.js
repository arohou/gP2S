import Vue from 'vue'
import * as sinon from 'sinon'

import FormCommons from '@/components/mixins/FormCommons'

const sandbox = sinon.sandbox.create()

describe('FormCommons.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(FormCommons)
    vm = new Constructor({
      data () {
        return {
          entity: ''
        }
      }
    })
  })

  afterEach(() => {
    sandbox.restore()
    vm = null
  })

  describe('default values', () => {

    it('should return valid default new name', () => {
      //given
      const label = 'Label'
      const timestamp = '18-01-26 14:58'
      vm.timestampForLabel = function () {
        return timestamp
      }
      //when
      expect(vm.defaultNewLabel(label))
        .to
        .be
        //then
        .equal(label + ' ' + timestamp)
    })
  })

  describe('options (simple types)', () => {
    it('should pass array when current element was set to null', () => {
      expect(vm.options(['item1', 'item2'], null)).to.be.deep.equal(['item1', 'item2'])
    })

    it('should pass array when no current element was set and one element instead of options array', () => {
      expect(vm.options('item1')).to.be.deep.equal(['item1'])
    })

    it('should pass array when no current element was set', () => {
      expect(vm.options(['item1', 'item2'])).to.be.deep.equal(['item1', 'item2'])
    })

    it('should add missing selected value as first option', () => {
      expect(vm.options(['item1', 'item2'], 'item3')).to.be.deep.equal(['item3', 'item1', 'item2'])
    })

    it('should move selected value as first option', () => {
      expect(vm.options(['item1', 'item2', 'item3'], 'item3')).to.be.deep.equal(['item3', 'item1', 'item2'])
      // check is value is not duplicated
    })

    it('should create array even when no initial array was pass', () => {
      expect(vm.options(null, 'item')).to.be.deep.equal(['item', null])
    })

    it('should create array when the list of options is empty', () => {
      expect(vm.options([], 'item')).to.be.deep.equal(['item'])
    })

    it('should create array with the null current value', () => {
      expect(vm.options(null, null)).to.be.deep.equal([null])
    })

    it('should create array even when element was pass instead of array', () => {
      expect(vm.options('item1', 'item2')).to.be.deep.equal(['item2', 'item1'])
    })

    it('should do distinct on values', () => {
      expect(vm.options(['test', 'test', 'test'], 'item2')).to.be.deep.equal(['item2', 'test'])
    })

  })

  describe('options (objects)', () => {
    it('should add missing selected value as first option - objects', () => {
      expect(vm.options(
        [{prop1: 'val1', prop2: 'val2'}, {prop1: 'val3', prop2: 'val4'}],
        {prop1: 'val5', prop2: 'val6'}
      )).to.be.deep.equal(
        [{prop1: 'val5', prop2: 'val6'},
          {prop1: 'val1', prop2: 'val2'},
          {prop1: 'val3', prop2: 'val4'}])
    })

    it('should move selected value as first option - objects', () => {
      expect(vm.options(
        [{prop1: 'val1', prop2: 'val2'}, {prop1: 'val3', prop2: 'val4'}, {prop1: 'val5', prop2: 'val6'}],
        {prop1: 'val5', prop2: 'val6'},
        (value) => {return value.prop1}
      )).to.be.deep.equal(
        [{prop1: 'val5', prop2: 'val6'},
          {prop1: 'val1', prop2: 'val2'},
          {prop1: 'val3', prop2: 'val4'}])
    })

    it('should move selected value as first option using property name for comparing objects - objects', () => {
      expect(vm.options(
        [{prop1: 'val1', prop2: 'val2'}, {prop1: 'val3', prop2: 'val4'}, {prop1: 'val5', prop2: 'val6'}],
        {prop1: 'val5', prop2: 'val6'},
        'prop1'
      )).to.be.deep.equal(
        [{prop1: 'val5', prop2: 'val6'},
          {prop1: 'val1', prop2: 'val2'},
          {prop1: 'val3', prop2: 'val4'}])
    })
  })

  describe('initOriginalEntity', () => {
    beforeEach(() => {
      vm.initOriginalEntity('entity')
    })

    it('shouldn\'t set null or undefined values', () => {
      // Set to non-null since null is default.
      vm.originalEntity = 1337

      vm.entity = null
      vm.entity = undefined

      expect(vm.originalEntity).to.be.equal(1337)
    })

    it('should set the originalEntity', done => {
      vm.entity = 1337

      Vue.nextTick().then(() => {
        expect(vm.originalEntity).to.be.equal(1337)
      }).then(done, done)
    })

    it('shouldn\'t modify the originalEntity after setting', done => {
      vm.entity = 1337
      vm.etntiy = 'changed'

      Vue.nextTick().then(() => {
        expect(vm.originalEntity).to.be.equal(1337)
      }).then(done, done)
    })
  })
})
