import Vue from 'vue'
import * as sinon from 'sinon'

import Commons from '@/components/App/MicroscopySession/Commons'
import MicroscopySessionToken from '@/utils/MicroscopySessionToken'

import MicroscopySessionService from '@/services/MicroscopySessionService'
import SettingsService from '@/services/SettingsService'
import ProjectsService from '@/services/ProjectService'
import DirectoryName from '@/utils/DirectoryName'

const sandbox = sinon.sandbox.create()

describe('MicroscopySession/Commons.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(Commons)
    vm = new Constructor()
  })

  afterEach(() => {
    sandbox.restore()
    vm = null
  })

  describe('initServices', () => {
    it('should instantiate _microscopySessionService', () => {
      // given
      vm._microscopySessionService = null

      // when
      vm.initServices()

      // then
      expect(vm._microscopySessionService).to.be.of.instanceOf(MicroscopySessionService)
    })

    it('should instantiate _settingsService', () => {
      // given
      vm._settingsService = null

      // when
      vm.initServices()

      // then
      expect(vm._settingsService).to.be.of.instanceOf(SettingsService)
    })

    it('should instantiate _projectsService', () => {
      // given
      vm._projectsService = null

      // when
      vm.initServices()

      // then
      expect(vm._projectsService).to.be.of.instanceOf(ProjectsService)
    })

  })

  describe('doesDirectoryNameContainId', () => {
    it('should return false for null string', () => {
      expect(vm.doesDirectoryNameContainId(null)).to.be.false
    })

    it('should return false for undefined', () => {
      expect(vm.doesDirectoryNameContainId(undefined)).to.be.false
    })

    it('should return false for an empty string', () => {
      expect(vm.doesDirectoryNameContainId('')).to.be.false
    })

    it('should return true for just MicroscopySessionToken.MICROSCOPY_SESSION_ID)', () => {
      expect(vm.doesDirectoryNameContainId(MicroscopySessionToken.MICROSCOPY_SESSION_ID.toString()))
        .to.be.true
    })

    it('should return true for MicroscopySessionToken.MICROSCOPY_SESSION_ID with in the middle)', () => {
      // given
      const directoryName = 'Something here' + MicroscopySessionToken.MICROSCOPY_SESSION_ID + '1337'

      // then
      expect(vm.doesDirectoryNameContainId(directoryName)).to.be.true
    })
  })

  describe('fetchDirectoryPatternIfNeeded', () => {
    it('should call settingsService', () => {
      // given
      const spy = sandbox.spy(vm._settingsService, 'get')

      // when
      vm.fetchDirectoryPatternIfNeeded()

      // then
      expect(spy).to.have.been.calledOnce
    })

    it('should return result.data.patternForDataStorageDirectoryName', () => {
      // given
      const expected = 1337
      sandbox.stub(vm._settingsService, 'get').returns(
        Promise.resolve({data: {patternForDataStorageDirectoryName: expected}}))

      // when
      const result = vm.fetchDirectoryPatternIfNeeded()

      // then
      expect(result).to.be.eventually.equal(expected)
    })
  })

  describe('addDataStorageDirectoryName', () => {
    const expected = 1337
    let exampleEntity = null

    beforeEach(() => {
      exampleEntity = {
        patternForDataStorageDirectoryName: expected,
        grid: {
          id: expected,
          label: expected
        },
        label: expected,
        id: expected,
        sessionStart: expected,
        microscope: {label: expected}
      }

      vm.projectId = expected
      vm.project = {label: expected}
      vm.patternForDataStorageDirectoryName = expected
      sandbox.stub(vm, 'fetchProjectIfNeeded')
    })

    it('should call fetchProjectIfNeeded', async () => {
      // given
      await vm.addDataStorageDirectoryName(exampleEntity, vm.patternForDataStorageDirectoryName)

      // then
      expect(vm.fetchProjectIfNeeded).has.been.calledOnce
    })

    it('should add the dataStorageDirectoryName to entity', async () => {
      // when
      const result = await vm.addDataStorageDirectoryName(exampleEntity, vm.patternForDataStorageDirectoryName)

      // then
      expect(result.dataStorageDirectoryName).to.be.not.empty
    })

    it('sets entity.dataStorageDirectoryName with DirectoryName.fromTokenString', async () => {
      // given
      const stub = sandbox.stub(DirectoryName, 'fromTokenString').returns(expected)

      // when
      const result = await vm.addDataStorageDirectoryName(exampleEntity, vm.patternForDataStorageDirectoryName)

      // then
      expect(stub).to.have.been.calledWith(expected)
      expect(result.dataStorageDirectoryName).to.be.equal(expected.toString())
    })

    it('calls DirectoryName.fromTokenString with mappings for all MicroscopySessionToken enum values', async () => {
      // given
      const stub = sandbox.stub(DirectoryName, 'fromTokenString').returns('ignored')

      // when
      await vm.addDataStorageDirectoryName(exampleEntity, vm.patternForDataStorageDirectoryName)

      // then
      const tokenToSubstitutionPairs = stub.getCall(0).args[1]
      const tokens = tokenToSubstitutionPairs.map(pair => pair[0])
      expect(tokens).to.deep.equal(MicroscopySessionToken.enumValues)
    })

    it('extracts values for all the MicroscopySessionTokens', async () => {
      // given
      const stub = sandbox.stub(DirectoryName, 'fromTokenString').returns('ignored')
      sandbox.stub(vm, 'tokenDate').returns(expected)
      sandbox.stub(vm, 'tokenTime').returns(expected)

      // when
      await vm.addDataStorageDirectoryName(exampleEntity, vm.patternForDataStorageDirectoryName)

      // then
      const tokenToSubstitutionPairs = stub.getCall(0).args[1]
      const substitutions = tokenToSubstitutionPairs.map(pair => pair[1])
      expect(substitutions).to.deep.equal(
        [expected, '00' + expected, expected, expected, '00' + expected, expected, expected, expected])
    })
  })

  describe('reSaveIfNeeded', () => {
    beforeEach(() => {
      sandbox.stub(vm, 'addDataStorageDirectoryName').returnsArg(0)
      sandbox.stub(vm._microscopySessionService, 'saveEntity')
        .callsFake((project, entity) => Promise.resolve({data: entity}))
    })

    it('should return entity unchanged if directory name doesn\'t contain id', async () => {
      // given
      const expected = 1337
      sandbox.stub(vm, 'doesDirectoryNameContainId').returns(false)

      // when
      const result = await vm.reSaveIfNeeded({dataStorageDirectoryName: expected})

      // then
      expect(result.dataStorageDirectoryName).to.be.equal(expected)
    })

    it('should return entity when directory name contains id', async () => {
      // given
      const expected = {}
      sandbox.stub(vm, 'doesDirectoryNameContainId').returns(true)

      // when
      const result = await vm.reSaveIfNeeded(expected)

      // then
      expect(result).to.be.equal(expected)
    })

    it('should call addDataStorageDirectoryName when directory name contains id', async () => {
      // given
      const expected = {}
      sandbox.stub(vm, 'doesDirectoryNameContainId').returns(true)

      // when
      const result = await vm.reSaveIfNeeded(expected)

      // then
      expect(vm.addDataStorageDirectoryName).to.have.been.calledWith(expected)
      expect(result).to.be.equal(expected)
    })

    it('should call _microscopySessionService.saveEntity when directory name contains id', async () => {
      // given
      const expected = {}
      sandbox.stub(vm, 'doesDirectoryNameContainId').returns(true)

      // when
      const result = await vm.reSaveIfNeeded(expected)

      // then
      expect(vm._microscopySessionService.saveEntity).to.have.been.calledWith(vm.projectId, expected)
      expect(result).to.be.deep.equal(expected)
    })
  })

  describe('padStartIfNotNil', () => {
    it('should return null for a null parameter', () => {
      expect(vm.padStartIfNotNil(null)).to.be.null
    })

    it('should return null for an undefined parameter', () => {
      expect(vm.padStartIfNotNil(undefined)).to.be.null
    })

    it('should return parameter padded with zeros to six places', () => {
      expect(vm.padStartIfNotNil('')).to.be.equal('000000')
    })
  })

})
