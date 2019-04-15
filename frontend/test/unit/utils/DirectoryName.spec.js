import DirectoryName from '@/utils/DirectoryName'
import Token from '@/utils/MicroscopySessionToken'

import sinon from 'sinon'

const sandbox = sinon.sandbox.create()

describe('DirectoryName', () => {
  let vm = null

  beforeEach(() => {
    vm = new DirectoryName()
  })

  afterEach(() => {
    sandbox.reset()
    vm = null
  })

  describe('* _regExpSubstitutionPairs', () => {
    it('returns transposed map', () => {
      // given
      const input = [
        [Token.MICROSCOPY_SESSION_ID, 1337],
        [Token.PROJECT_LABEL, 'A Project']
      ]
      const expected = [
        [Token.MICROSCOPY_SESSION_ID.regExp, 1337],
        [Token.PROJECT_LABEL.regExp, 'A Project']
      ]

      // when
      const result = [...vm._regExpSubstitutionPairs(input)]

      // then
      expect(result).to.be.deep.equal(expected)
    })

    it('ignores bogus entries', () => {
      // given
      const input = [
        [Token.MICROSCOPY_SESSION_ID, 1337],
        [undefined, 'Undefined'],
        [Token.PROJECT_LABEL, 'A Project'],
        [{}, 'Empty object'],
        [[], 'Empty array'],
        ['', 'Empty string']
      ]
      const expected = [
        [Token.MICROSCOPY_SESSION_ID.regExp, 1337],
        [Token.PROJECT_LABEL.regExp, 'A Project']
      ]

      // when
      const result = [...vm._regExpSubstitutionPairs(input)]

      // then
      expect(result).to.be.deep.equal(expected)
    })
  })

  describe('_underscoreInsteadOfWhitespace', () => {
    it('should be called from setter', () => {
      // given
      const expected = 'Anything'
      const spy = sandbox.spy(vm, '_underscoreInsteadOfWhitespace')

      // when
      vm.value = expected

      // then
      expect(spy).to.have.been.calledWith(expected)
    })

    it('should replace all whitespace to underscore', () => {
      // given
      const allWhitespace = ' \f\n\r\t\v\u00A0\u2028\u2029'
      const expected = 'Anything'

      // when
      vm.value = allWhitespace + expected + allWhitespace

      // then
      expect(vm.value).to.have.been.equal(
        '_'.repeat(allWhitespace.length) + expected + '_'.repeat(allWhitespace.length))
    })
  })

  describe('substituteTokens', () => {
    it('should substitute ${ProjectLabel}', () => {
      // given
      const expected = 'Substituted project label'
      const substitutions = [
        [Token.PROJECT_LABEL, expected]
      ]
      const toSubstitute = '${ProjectLabel}${ProjectLabel}'

      // when
      const result = vm.substituteTokens(toSubstitute, substitutions)

      // then
      expect(result).to.be.equal(expected.repeat(2))
    })

    it('should substitute ${GridID}', () => {
      // given
      const expected = 'Substituted grid ID'
      const substitutions = [
        [Token.GRID_ID, expected]
      ]
      const toSubstitute = '${GridID}${GridID}'

      // when
      const result = vm.substituteTokens(toSubstitute, substitutions)

      // then
      expect(result).to.be.equal(expected.repeat(2))
    })

    it('should substitute ${GridLabel}', () => {
      // given
      const expected = 'Substituted grid label'
      const substitutions = [
        [Token.GRID_LABEL, expected]
      ]
      const toSubstitute = '${GridLabel}${GridLabel}'

      // when
      const result = vm.substituteTokens(toSubstitute, substitutions)

      // then
      expect(result).to.be.equal(expected.repeat(2))
    })

    it('should substitute ${MicroscopyLabel}', () => {
      // given
      const expected = 'Substituted microscopy label'
      const substitutions = [
        [Token.MICROSCOPY_LABEL, expected]
      ]
      const toSubstitute = '${MicroscopyLabel}${MicroscopyLabel}'

      // when
      const result = vm.substituteTokens(toSubstitute, substitutions)

      // then
      expect(result).to.be.equal(expected.repeat(2))
    })

    it('should substitute ${MicroscopySessionID}', () => {
      // given
      const expected = 'Substituted microscopy session ID '
      const substitutions = [
        [Token.MICROSCOPY_SESSION_ID, expected]
      ]
      const toSubstitute = '${MicroscopySessionID}${MicroscopySessionID}'

      // when
      const result = vm.substituteTokens(toSubstitute, substitutions)

      // then
      expect(result).to.be.equal(expected.repeat(2))
    })

    it('should substitute ${MicroscopyStartDate}', () => {
      // given
      const expected = 'Substituted microscopy session start date'
      const substitutions = [
        [Token.MICROSCOPY_START_DATE, expected]
      ]
      const toSubstitute = '${MicroscopyStartDate}${MicroscopyStartDate}'

      // when
      const result = vm.substituteTokens(toSubstitute, substitutions)

      // then
      expect(result).to.be.equal(expected.repeat(2))
    })

    it('should substitute ${MicroscopyStartTime}', () => {
      // given
      const expected = 'Substituted microscopy session start time'
      const substitutions = [
        [Token.MICROSCOPY_START_TIME, expected]
      ]
      const toSubstitute = '${MicroscopyStartTime}${MicroscopyStartTime}'

      // when
      const result = vm.substituteTokens(toSubstitute, substitutions)

      // then
      expect(result).to.be.equal(expected.repeat(2))
    })

    it('should substitute ${MicroscopeLabel}', () => {
      // given
      const expected = 'Substituted microscope label'
      const substitutions = [
        [Token.MICROSCOPE_LABEL, expected]
      ]
      const toSubstitute = '${MicroscopeLabel}${MicroscopeLabel}'

      // when
      const result = vm.substituteTokens(toSubstitute, substitutions)

      // then
      expect(result).to.be.equal(expected.repeat(2))
    })

    it('should substitute all tokens at once', () => {
      // given
      const substitutions = [
        [Token.PROJECT_LABEL, 'MyProject'],
        [Token.GRID_ID, '000143'],
        [Token.GRID_LABEL, 'Mybestgrid'],
        [Token.MICROSCOPY_LABEL, 'MyMicroscopySession'],
        [Token.MICROSCOPY_SESSION_ID, '000098'],
        [Token.MICROSCOPY_START_DATE, '171004'],
        [Token.MICROSCOPY_START_TIME, '031415'],
        [Token.MICROSCOPE_LABEL, 'Krios2']
      ]
      const toSubstitute = '${ProjectLabel}/${GridID}/${GridLabel}/'
        + '${MicroscopyLabel}/${MicroscopySessionID}/'
        + '${MicroscopyStartDate}/${MicroscopyStartTime}/'
        + '${MicroscopeLabel}'
      const expected = 'MyProject/000143/Mybestgrid/'
        + 'MyMicroscopySession/000098/'
        + '171004/031415/'
        + 'Krios2'

      // when
      const result = vm.substituteTokens(toSubstitute, substitutions)

      // then
      expect(result).to.be.equal(expected)
    })
  })

  describe('constructor', () => {
    it('should call sanitize when setting the value', () => {
      // given
      const spy = sandbox.spy()
      sandbox.stub(DirectoryName.prototype, 'value').set(spy)
      const expected = 'Some value'
      const stub = sandbox.stub(DirectoryName.prototype, 'substituteTokens').returns(expected)

      // when
      new DirectoryName(expected, expected)

      // then
      expect(stub).to.have.been.calledWith(expected, expected)
      expect(spy).to.have.been.calledWith(expected)
    })
  })
})
