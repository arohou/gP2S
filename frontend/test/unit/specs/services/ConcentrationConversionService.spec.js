import ConcentrationConversionService from '@/services/ConcentrationConversionService'

import sinon from 'sinon'

const sandbox = sinon.sandbox.create()

describe('ConcentrationConversionService', () => {
  let vm = null

  beforeEach(() => {
    vm = new ConcentrationConversionService()
  })

  afterEach(() => {
    sandbox.reset()
    vm = null
  })

  describe('uMToMgPerMl', () => {
    it('should throw TypeError when concentration is <= 0', () => {
      // given
      const wrongConcentrations =
        [null, undefined, 0, -1, '']

      // then
      for (const concentration of wrongConcentrations) {
        expect(() => vm.uMToMgPerMl(concentration, 10)).to.throw()
      }
    })

    it('should convert uM to mg/ml', () => {
      // when
      const result = vm.uMToMgPerMl(123456.78, 10)

      // then
      expect(result).to.be.equal('1.235')
    })
  })

  describe('mgPerMlToUm', () => {
        it('should throw TypeError when concentration is <= 0', () => {
      // given
      const wrongConcentrations =
        [null, undefined, 0, -1, '']

      // then
      for (const concentration of wrongConcentrations) {
        expect(() => vm.uMToMgPerMl(concentration, 10)).to.throw()
      }
    })

    it('should convert mg/ml to uM', () => {
      // when
      const result = vm.mgPerMlToUm(123456.78, 10)

      // then
      expect(result).to.be.equal('12345678000.000')
    })
  })
})
