import Vue from 'vue'
import * as sinon from 'sinon'

import CommonValidations from '@/components/mixins/CommonValidations'

const sandbox = sinon.sandbox.create()

describe('CommonValidations.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(CommonValidations)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm = null
  })

  it('should check concentration pass when no value pass', () => {
    let callback = sandbox.spy()
    vm.checkConcentration(null, null, callback)
    expect(callback).to.have.been.calledOnce
    expect(callback.getCall(0).args.length).to.be.eql(0)
  })

  it('should check concentration return error when value is not numeric', () => {
    let callback = sandbox.spy()
    let stub = sandbox.stub(vm, 'isNumeric')
    stub.returns(false)
    vm.checkConcentration(null, 'value', callback)

    expect(callback).to.have.been.calledOnce
    expect(callback.getCall(0).args[0]).to.be.instanceOf(Error)
  })

  it('should check concentration return error when value less or equal 0', () => {
    let callback = sandbox.spy()
    let stub = sandbox.stub(vm, 'isNumeric')
    stub.returns(true)
    vm.checkConcentration(null, 0, callback)
    vm.checkConcentration(null, -1, callback)
    vm.checkConcentration(null, 0.1, callback)

    expect(callback.getCall(0).args[0]).to.be.instanceOf(Error)
    expect(callback.getCall(1).args[0]).to.be.instanceOf(Error)
    expect(callback.getCall(2).args).to.be.of.length(0)
  })

  it('should check concentration callback successful', () => {
    let callback = sandbox.spy()
    vm.checkConcentration(null, 3, callback)
    expect(callback).to.have.been.calledOnce
    expect(callback.getCall(0).args).to.be.of.length(0)
  })


  it('should validate double number values', () => {
    expect(vm.isNumeric('1')).to.be.true
    expect(vm.isNumeric('0')).to.be.true
    expect(vm.isNumeric('1.0')).to.be.true
    expect(vm.isNumeric('1.1')).to.be.true
    expect(vm.isNumeric('.42')).to.be.true
    expect(vm.isNumeric('0.0')).to.be.true
    expect(vm.isNumeric('-1')).to.be.true
    expect(vm.isNumeric('1.2222')).to.be.true
    expect(vm.isNumeric('')).to.be.false
    expect(vm.isNumeric(' ')).to.be.false
    expect(vm.isNumeric('\t\t')).to.be.false
    expect(vm.isNumeric('\n')).to.be.false
    expect(vm.isNumeric('\n\r')).to.be.false
    expect(vm.isNumeric('1,2')).to.be.false
    expect(vm.isNumeric('0.1.2.3')).to.be.false
    expect(vm.isNumeric('abc')).to.be.false
    expect(vm.isNumeric('!')).to.be.false
    expect(vm.isNumeric('#')).to.be.false
    expect(vm.isNumeric('8g')).to.be.false
    expect(vm.isNumeric('8e5')).to.be.false
    expect(vm.isNumeric('0x87f')).to.be.false
  })

  it('should validate integer number values', () => {
    expect(vm.isInteger('1')).to.be.true
    expect(vm.isInteger('0')).to.be.true
    expect(vm.isInteger('1.0')).to.be.false
    expect(vm.isInteger('1.1')).to.be.false
    expect(vm.isInteger('.42')).to.be.false
    expect(vm.isInteger('0.0')).to.be.false
    expect(vm.isInteger('-1')).to.be.true
    expect(vm.isInteger('1.2222')).to.be.false
    expect(vm.isInteger('')).to.be.false
    expect(vm.isInteger(' ')).to.be.false
    expect(vm.isInteger('\t\t')).to.be.false
    expect(vm.isInteger('\n')).to.be.false
    expect(vm.isInteger('\n\r')).to.be.false
    expect(vm.isInteger('1,2')).to.be.false
    expect(vm.isInteger('0.1.2.3')).to.be.false
    expect(vm.isInteger('abc')).to.be.false
    expect(vm.isInteger('!')).to.be.false
    expect(vm.isInteger('#')).to.be.false
    expect(vm.isInteger('8g')).to.be.false
    expect(vm.isInteger('8e5')).to.be.false
    expect(vm.isInteger('0x87f')).to.be.false
  })

  describe('isGreaterThan', () => {
    const validatorName = 'Test validator name'

    it('ignores all forms of empty value when not required', () => {
      const rule = {required: true}
      const callback = sandbox.spy()
      const validator = vm.isGreaterThan(validatorName, 0)
      validator(rule, null, callback)
      callback.should.have.been.calledOnce
      validator(rule, undefined, callback)
      callback.should.have.callCount(2)
      validator(rule, '', callback)
      callback.should.have.callCount(3)
    })
  })

  describe('isGreaterThanOrEqualTo', () => {
    const validatorName = 'Test validator name'

    it('ignores all forms of empty value when not required', () => {
      const rule = {required: true}
      const callback = sandbox.spy()
      const validator = vm.isGreaterThanOrEqualTo(validatorName, 0)
      validator(rule, null, callback)
      callback.should.have.been.calledOnce
      validator(rule, undefined, callback)
      callback.should.have.callCount(2)
      validator(rule, '', callback)
      callback.should.have.callCount(3)
    })
  })

  describe('checkBatchIdFormat', () => {

    it('common g salt batch number', () => {
      const callback = sandbox.spy()
      vm.checkBatchIdFormat(null, 'G12345678.1-1', callback)
      callback.should.have.been.calledOnce
      expect(callback.getCall(0).args).to.be.of.length(0)
    })

    it('extended g salt batch number', () => {
      const callback = sandbox.spy()
      vm.checkBatchIdFormat(null, 'G12345678.1234567890-1234567890', callback)
      callback.should.have.been.calledOnce
      expect(callback.getCall(0).args).to.be.of.length(0)
    })

    it('too long value', () => {
      const callback = sandbox.spy()
      vm.checkBatchIdFormat(null, 'G12345678.12345678901-1234567890', callback)
      callback.should.have.been.calledOnce
      expect(callback.getCall(0).args).to.be.of.length(1)
    })

    it('too long value 2', () => {
      const callback = sandbox.spy()
      vm.checkBatchIdFormat(null, 'G12345678.1234567890-12345678901', callback)
      callback.should.have.been.calledOnce
      expect(callback.getCall(0).args).to.be.of.length(1)
    })

    it('empty value', () => {
      const callback = sandbox.spy()
      vm.checkBatchIdFormat(null, '', callback)
      callback.should.have.been.calledOnce
      expect(callback.getCall(0).args).to.be.of.length(1)
    })

    it('trim value', () => {
      const callback = sandbox.spy()
      vm.checkBatchIdFormat(null, 'G123456', callback)
      callback.should.have.been.calledOnce
      expect(callback.getCall(0).args).to.be.of.length(1)
    })

    it('to short value', () => {
      const callback = sandbox.spy()
      vm.checkBatchIdFormat(null, 'G123456.1-1', callback)
      callback.should.have.been.calledOnce
      expect(callback.getCall(0).args).to.be.of.length(1)
    })

    it('wrong prefix', () => {
      const callback = sandbox.spy()
      vm.checkBatchIdFormat(null, 'T123456.1-1', callback)
      callback.should.have.been.calledOnce
      expect(callback.getCall(0).args).to.be.of.length(1)
    })

    it('replaced dot', () => {
      const callback = sandbox.spy()
      vm.checkBatchIdFormat(null, 'G12345678-1-1', callback)
      callback.should.have.been.calledOnce
      expect(callback.getCall(0).args).to.be.of.length(1)
    })
  })
})
