import Vue from 'vue'
import * as sinon from 'sinon'
import VueRouter from 'vue-router'
import { fn as momentProto } from 'moment'
import _ from 'lodash'

import BaseFormBasicInformation from '@/components/App/MicroscopySession/BaseFormBasicInformation'
import ValidationError from '@/errors/ValidationError'
import ProtocolType from '@/components/App/Grid/ProtocolType'
import GridService from '@/services/GridService'
import InsertionMechanismType from '@/components/App/Admin/Microscope/InsertionMechanismType'

import * as EntityUtils from '@/utils/EntityUtils'

const sandbox = sinon.sandbox.create()

describe('MicroscopySession/BaseFormBasicInformation.vue', () => {
  let vm = null
  let router = null

  const DATA = {
    projectSlugOrId: '1',
    entity: {
      id: null,
      objectiveAperture: '',
      sessionStartDate: 1,
      sessionStartTime: 1,
      sessionStart: 1,
      sessionFinishDate: 2,
      sessionFinishTime: 2,
      sessionFinish: 2,
      microscope: null,
      electronDetector: null,
      grid: null,
      dataStorageDirectoryName: null
    }
  }

  beforeEach(() => {
    const Constructor = Vue.extend(BaseFormBasicInformation)
    router = new VueRouter()
    vm = new Constructor({
      router,
      propsData: {
        entity: {...DATA.entity},
        projectId: '' + DATA.projectSlugOrId
      }
    })
    sandbox.stub(vm, 'loadMicroscopes')
    sandbox.stub(vm, 'loadGrids')
    sandbox.stub(vm, 'fetchElectronDetectorsForMicroscope')
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
    router = null
  })

  describe('Basic Information - sub-form', () => {

    describe('session dates & times', () => {

      it('should set session finish to now', () => {
        // given
        const expected = 1
        sandbox.stub(momentProto, 'valueOf').returns(expected)

        // when
        vm.setSessionFinishToNow()

        // then
        expect(vm.entity.sessionFinish).to.be.equal(expected)
        expect(vm.entity.sessionFinishDate).to.be.equal(expected)
        expect(vm.entity.sessionFinishTime).to.be.equal(expected)
      })

      describe('setSessionStart', () => {

        it('should be called on start DATE change', async () => {
          // given
          const expected = 1337
          sandbox.spy(vm, 'setSessionStart')

          // when
          vm.entity.sessionStartDate = expected
          await vm.$nextTick()

          // then
          expect(vm.setSessionStart).to.have.been.calledWith(expected, DATA.entity.sessionStartTime)
        })

        it('should be called on start TIME change', async () => {
          // given
          const expected = 1337
          sandbox.spy(vm, 'setSessionStart')

          // when
          vm.entity.sessionStartTime = expected
          await vm.$nextTick()

          // then
          expect(vm.setSessionStart).to.have.been.calledWith(DATA.entity.sessionStartDate, expected)
        })

        it('should nullify sessionStart on any parameter not set', () => {
          // when
          vm.setSessionStart(null, 1)
          vm.setSessionStart(undefined, 1)
          vm.setSessionStart(1, null)
          vm.setSessionStart(1, undefined)

          // then
          expect(vm.entity.sessionStart).to.be.null
        })

        it('set sessionStart based on params', () => {
          // given
          const expected = 1337
          sandbox.stub(momentProto, 'valueOf').returns(expected)

          // when
          vm.setSessionStart(1, 1)

          // then
          expect(vm.entity.sessionStart).to.be.equal(expected)
        })
      })

      describe('setSessionFinish', () => {

        it('should be called on finish DATE change', async () => {
          // given
          const expected = 1337
          sandbox.spy(vm, 'setSessionFinish')

          // when
          vm.entity.sessionFinishDate = expected
          await vm.$nextTick()

          // then
          expect(vm.setSessionFinish).to.have.been.calledWith(expected, DATA.entity.sessionFinishTime)
        })

        it('should be called on finish TIME change', async () => {
          // given
          const expected = 1337
          sandbox.spy(vm, 'setSessionFinish')

          // when
          vm.entity.sessionFinishTime = expected
          await vm.$nextTick()

          // then
          expect(vm.setSessionFinish).to.have.been.calledWith(DATA.entity.sessionFinishDate, expected)
        })

        it('should nullify sessionFinish on any parameter not set', () => {
          // when
          vm.setSessionFinish(null, 1)
          vm.setSessionFinish(undefined, 1)
          vm.setSessionFinish(1, null)
          vm.setSessionFinish(1, undefined)

          // then
          expect(vm.entity.sessionFinish).to.be.null
        })

        it('set sessionFinish based on params', () => {
          // given
          const expected = 1337
          sandbox.stub(momentProto, 'valueOf').returns(expected)

          // when
          vm.setSessionFinish(1, 1)

          // then
          expect(vm.entity.sessionFinish).to.be.equal(expected)
        })

      })

      describe('isSessionStartBeforeSessionFinish', () => {

        it('should fail on session start being after session finish', () => {
          // given
          const spy = sandbox.spy()
          vm.entity.sessionStart = 2
          vm.entity.sessionFinish = 1

          // when
          vm.isSessionStartBeforeSessionFinish(undefined, undefined, spy)

          // then
          expect(spy).to.have.been.calledWith(sinon.match.instanceOf(ValidationError))
        })

        it('should fail on session start being same as session finish', () => {
          // given
          const spy = sandbox.spy()
          vm.entity.sessionStart = 1
          vm.entity.sessionFinish = 1

          // when
          vm.isSessionStartBeforeSessionFinish(undefined, undefined, spy)

          // then
          expect(spy).to.have.been.calledWith(sinon.match.instanceOf(ValidationError))
        })

        it('should succeed when session start before session finish', () => {
          // given
          const spy = sandbox.spy()
          vm.entity.sessionStart = 1
          vm.entity.sessionFinish = 2

          // when
          vm.isSessionStartBeforeSessionFinish(undefined, undefined, spy)

          // then
          expect(spy).to.have.been.calledWithExactly()
        })

        it('should ignore when both params aren\' defined', () => {
          // given
          const spy = sandbox.spy()
          vm.entity.sessionStart = null
          vm.entity.sessionFinish = undefined

          // when
          vm.isSessionStartBeforeSessionFinish(undefined, undefined, spy)

          // then
          expect(spy).to.have.been.calledWithExactly()
        })

      })

      describe('isSessionFinishAfterSessionStart', () => {

        it('should fail on session finish being before session start', () => {
          // given
          const spy = sandbox.spy()
          vm.entity.sessionStart = 2
          vm.entity.sessionFinish = 1

          // when
          vm.isSessionFinishAfterSessionStart(undefined, undefined, spy)

          // then
          expect(spy).to.have.been.calledWith(sinon.match.instanceOf(ValidationError))
        })

        it('should fail on session finish being same as session start', () => {
          // given
          const spy = sandbox.spy()
          vm.entity.sessionStart = 1
          vm.entity.sessionFinish = 1

          // when
          vm.isSessionFinishAfterSessionStart(undefined, undefined, spy)

          // then
          expect(spy).to.have.been.calledWith(sinon.match.instanceOf(ValidationError))
        })

        it('should succeed when session finish is after session start', () => {
          // given
          const spy = sandbox.spy()
          vm.entity.sessionStart = 1
          vm.entity.sessionFinish = 2

          // when
          vm.isSessionFinishAfterSessionStart(undefined, undefined, spy)

          // then
          expect(spy).to.have.been.calledWithExactly()
        })

        it('should ignore when both params aren\' defined', () => {
          // given
          const spy = sandbox.spy()
          vm.entity.sessionStart = null
          vm.entity.sessionFinish = undefined

          // when
          vm.isSessionFinishAfterSessionStart(undefined, undefined, spy)

          // then
          expect(spy).to.have.been.calledWithExactly()
        })

      })

    })

    describe('grid', () => {

      it('should load grids on projectId change', async () => {
        // when
        vm.projectId = 1337
        await vm.$nextTick()

        // then
        expect(vm.loadGrids).to.have.been.called
      })

      it('should load grids on entity change', async () => {
        // when
        vm.entity = {id: 1337}
        await vm.$nextTick()

        // then
        expect(vm.loadGrids).to.have.been.called
      })

      it('should call updateSampleHolder on grid changed', async () => {
        // given
        sandbox.spy(vm, 'updateSampleHolder')

        // when
        vm.entity.grid = 1337
        await vm.$nextTick()

        // then
        expect(vm.updateSampleHolder).to.have.been.called
      })

      describe('setDefaultGridReturnedToStorage', () => {

        beforeEach(() => {
          vm.shouldSetDefaultGridReturned = true
        })

        it('should ignore null or undefined values', () => {
          // given
          vm.shouldSetDefaultGridReturned = true

          // when
          vm.setDefaultGridReturnedToStorage(null)
          vm.setDefaultGridReturnedToStorage(undefined)

          // then
          expect(vm.entity.gridReturnedToStorage).to.be.undefined
        })

        it('should ignore if shouldSetDefaultGridReturned is null', () => {
          // given
          vm.shouldSetDefaultGridReturned = false

          // when
          vm.setDefaultGridReturnedToStorage(1337)

          // then
          expect(vm.entity.gridReturnedToStorage).to.be.undefined
        })

        it('should ignore unknown protocol type', () => {
          // when
          vm.setDefaultGridReturnedToStorage({
            protocolType: 'Unknown'
          })

          // then
          expect(vm.entity.gridReturnedToStorage).to.be.undefined
        })

        it('should set gridReturnedToStorage to false when protocol type is Cryo', () => {
          // when
          vm.setDefaultGridReturnedToStorage({
            protocolType: ProtocolType.Cryo.name
          })

          // then
          expect(vm.entity.gridReturnedToStorage).to.be.false
        })

        it('should set gridReturnedToStorage to true when protocol type is Stain', () => {
          // when
          vm.setDefaultGridReturnedToStorage({
            protocolType: ProtocolType.Stain.name
          })

          // then
          expect(vm.entity.gridReturnedToStorage).to.be.true
        })

      })

      describe('loadGrids', () => {

        const GRIDS = [
          {id: 1},
          {id: 2},
          {id: 3}
        ]

        beforeEach(() => {
          vm._gridService = sinon.createStubInstance(GridService)
          vm._gridService.findAvailableGrids.resolves({data: [...GRIDS]})
          vm._gridService.findAvailableGridsOrGridWithId.resolves({data: [...GRIDS]})

          vm.$refs['field-grid'] = {
            resetField: sandbox.spy()
          }
        })

        it('should call findAvailableGrids when there\'s no grid', async () => {
          // given
          const expected = 1337
          vm.projectId = expected
          vm.entity.grid = null
          await vm.$nextTick()
          vm.loadGrids.restore()

          // when
          await vm.loadGrids()

          // then
          expect(vm._gridService.findAvailableGrids).to.have.been.called
        })

        it('should call findAvailableGridsOrGridWithId when there\'s an entity id', async () => {
          // given
          const expected = 1337
          vm.projectId = expected
          vm.entity.id = 1
          vm.entity.grid = {
            id: expected
          }
          await vm.$nextTick()
          vm.loadGrids.restore()

          // when
          await vm.loadGrids()

          // then
          expect(vm._gridService.findAvailableGridsOrGridWithId)
            .to.have.been.calledWith(expected, expected)
        })

        it('should build a list of grids without a historical value', done => {
          // given
          vm.loadGrids.restore()

          // when
          vm.loadGrids()

          // then
          vm.$nextTick()
            .then(() => {
              expect(vm.grids).to.be.deep.equal(GRIDS)
            }).then(done, done)
        })

        it('should build a list of grids with a non existing historical value', done => {
          // given
          const historicalGrid = {id: 1337}
          vm.originalEntity = {
            grid: historicalGrid
          }
          vm.loadGrids.restore()

          // when
          vm.loadGrids()

          // then
          vm.$nextTick()
            .then(() => {
              expect(vm.grids).to.be.deep.equal(
                [historicalGrid, ...GRIDS])
            }).then(done, done)
        })

        it('should build a list of grids with an existing historical value', done => {
          // given
          const historicalGrid = {id: 2}
          vm.originalEntity = {
            grid: historicalGrid
          }
          vm.loadGrids.restore()

          // when
          vm.loadGrids()

          // then
          vm.$nextTick()
            .then(() => {
              expect(vm.grids).to.be.deep.equal(_.uniqBy(
                [historicalGrid, ...GRIDS], 'id'))
            }).then(done, done)
        })

        it('should nullify entity grid if there\'s no grids available', done => {
          // given
          vm._gridService.findAvailableGrids.resolves({data: []})
          vm.loadGrids.restore()
          chai.assert(vm.entity)
          chai.assert(!vm.entity.grid)

          // when
          vm.loadGrids()

          // then
          vm.$nextTick()
            .then(() => {
              expect(vm.grids).to.be.empty
              expect(vm.entity.grid).to.be.null
              expect(vm.$refs['field-grid'].resetField)
                .to.have.been.called
            }).then(done, done)
        })

        it('should nullify entity grid if there\'s no grid', done => {
          // given
          vm.entity.grid = 1337
          chai.assert(vm.entity)
          vm.loadGrids.restore()

          // when
          vm.loadGrids()

          // then
          vm.$nextTick()
            .then(() => {
              expect(vm.grids).to.be.not.empty
              expect(vm.entity.grid).to.be.null
              expect(vm.$refs['field-grid'].resetField)
                .to.have.been.called
            }).then(done, done)
        })

        it('should nullify entity grid if it\'s not on the grids list', done => {
          // given
          vm.entity.grid = {
            id: 1337
          }
          chai.assert(vm.entity)
          vm.loadGrids.restore()

          // when
          vm.loadGrids()

          // then
          vm.$nextTick()
            .then(() => {
              expect(vm.grids).to.be.deep.equal(GRIDS)
              expect(vm.entity.grid).to.be.null
              expect(vm.$refs['field-grid'].resetField)
                .to.have.been.called
            }).then(done, done)
        })

      })

    })

    describe('microscope', () => {

      describe('sample holder update based on watcher', () => {

        beforeEach(() => {
          sandbox.stub(vm, 'updateCondenser2ApertureDiameter')
          sandbox.stub(vm, 'updateMicroscopeSettings')
          sandbox.stub(vm, 'updateElectronDetector')

          vm.$set(vm.entity, 'microscope', null)
        })

        it('should nullify entity\'s sampleHolder when new microscope is falsy', async () => {
          // given
          vm.$refs['field-sample_holder'] = {
            resetField: sandbox.spy()
          }

          // when
          vm.entity.microscope = ''
          await vm.$nextTick()

          // then
          expect(vm.$refs['field-sample_holder'].resetField).to.have.been.called
          expect(vm.entity.sampleHolder).to.be.null
        })

        it('should nullify entity\'s sampleHolder when new microscope has autoloader as insertion mechanism',
          async () => {
            // given
            vm.$refs['field-sample_holder'] = {
              resetField: sandbox.spy()
            }

            // when
            vm.entity.microscope = {
              sampleInsertionMechanism: InsertionMechanismType.AUTO_LOADER.name
            }

            await vm.$nextTick()

            // then
            expect(vm.$refs['field-sample_holder'].resetField).to.have.been.called
            expect(vm.entity.sampleHolder).to.be.null
          })

        it('should update sample holders when new microscope has side entry holder mechanism', async () => {
          // given
          const expected = 1337
          const findByMicroscopeStub = sandbox.stub(vm._sampleHolderService, 'findByMicroscope')
            .resolves({data: expected})
          const updateSampleHolderStub = sandbox.stub(vm, 'updateSampleHolder')

          // when
          vm.entity.microscope = {
            id: expected,
            sampleInsertionMechanism: InsertionMechanismType.SIDE_ENTRY_HOLDER.name
          }
          await vm.$nextTick()

          // then
          expect(findByMicroscopeStub).to.have.been.calledWith(expected)
          expect(vm.sampleHolders).to.be.equal(expected)
          expect(updateSampleHolderStub).to.have.been.called
        })

      })

      it('loadMicroscopes creates a list of microscopes ', done => {
        // given
        const expected = [1, 2, 3]
        const listEntitiesStub = sandbox.stub(vm._microscopeService, 'listEntities')
          .resolves({data: expected})
        const optionsSpy = sandbox.stub(vm, 'options').returns(expected)
        vm.loadMicroscopes.restore()

        // when
        vm.loadMicroscopes()

        // then
        vm.$nextTick()
          .then(() => {
            expect(listEntitiesStub).to.have.been.called
            expect(optionsSpy).to.have.been.calledWith(expected)
            expect(vm.microscopes).to.deep.equal(expected)
          }).then(done, done)
      })

      it('loadMicroscopes should call initDefaultMicroscope', async () => {
        // given
        sandbox.spy(vm, 'initDefaultMicroscope')

        vm._microscopeService = {
          listEntities: () => vm.$nextTick()
        }
        vm.loadMicroscopes.restore()

        // when
        vm.loadMicroscopes()
        await vm.$nextTick()

        // then
        expect(vm.initDefaultMicroscope).to.have.been.called
      })

      it('should set default microscope', async () => {
        vm.isNewEntity = () => true
        vm.microscopes = [
          {id: 1},
          {id: 2}
        ]
        vm.defaultMicroscope = vm.microscopes[1].id

        // when
        await vm.initDefaultMicroscope()

        // then
        expect(vm.entity.microscope).to.be.deep.equal(vm.microscopes[1])
      })
    })

    describe('detector', () => {
      describe('fetchElectronDetectorsForMicroscope', () => {

        const expected = 1337
        let fieldDetectorResetStub

        beforeEach(() => {
          const Constructor = Vue.extend(BaseFormBasicInformation)
          vm = new Constructor({
            router: new VueRouter(),
            propsData: {
              entity: {
                microscope: {
                  id: expected
                }
              }
            }
          })

          sandbox.stub(vm._microscopeService, 'getElectronDetectorsBySlugOrId')
            .resolves({data: [expected]})
          sandbox.stub(vm, 'options').returns([expected])

          fieldDetectorResetStub = sandbox.stub()
          vm.$refs['field-detector'] = {
            resetField: fieldDetectorResetStub
          }
        })

        it('should set empty electronDetectors if there\'s no microscope', () => {
          // given
          vm.entity.microscope = null

          // when
          vm.fetchElectronDetectorsForMicroscope()

          // then
          expect(vm.electronDetectors).to.be.to.be.deep.equal([])
        })

        it('should fetch electron detectors', done => {
          // when
          vm.fetchElectronDetectorsForMicroscope()

          // then
          vm.$nextTick()
            .then(() => {
              expect(vm._microscopeService.getElectronDetectorsBySlugOrId).to.have.been.calledWith(1337)
              expect(vm.electronDetectors).to.be.deep.equal([expected])
            }).then(done, done)
        })

        it('should set entity\'s electron detector to null', done => {
          // when
          vm.fetchElectronDetectorsForMicroscope()

          // then
          vm.$nextTick()
            .then(() => {expect(vm.entity.electronDetector).to.have.null})
            .then(done, done)
        })

        it('should call resetField on field-detector', done => {
          // when
          vm.fetchElectronDetectorsForMicroscope()

          // then
          vm.$nextTick()
            .then(() => {expect(fieldDetectorResetStub).to.have.been.called})
            .then(done, done)

        })

        it('should break early if entity\'s electron detector is on options list', async () => {
          // given
          vm.entity.electronDetector = {}

          // when
          await vm.fetchElectronDetectorsForMicroscope()

          // then
          expect(vm.entity.electronDetector).to.be.not.null
          expect(fieldDetectorResetStub).to.not.have.been.called
        })

      })

    })

    describe('sample holder', () => {

      describe('updateSampleHolder', () => {

        beforeEach(() => {
          vm.entity.grid = {}
          vm.sampleHolders = [1337]
        })

        it('should early break if entity has an empty grid', () => {
          // given
          vm.entity.grid = undefined

          // when
          vm.updateSampleHolder()

          // then
          expect(vm.filteredSampleHolders).to.be.empty
        })

        it('should early break if sample holders is empty', () => {
          // given
          vm.entity.grid = 1337
          vm.sampleHolders = []

          // when
          vm.updateSampleHolder()

          // then
          expect(vm.filteredSampleHolders).to.be.empty
        })

        it('should call options on filteredSampleHolders', () => {
          // given
          const expected = [1337]
          const stub = sandbox.stub(vm, 'options').returns(expected)

          // when
          vm.updateSampleHolder()

          // then
          expect(stub).to.have.been.called
          expect(vm.filteredSampleHolders).to.be.deep.equal(expected)
        })

        it('should filter non cryo-capable sample holders', () => {
          // given
          vm.entity.grid['protocolType'] = ProtocolType.Cryo.name
          vm.sampleHolders = [
            {cryoCapable: true},
            {cryoCapable: false}
          ]
          sandbox.stub(vm, 'options').returnsArg(0)

          // when
          vm.updateSampleHolder()

          // then
          expect(vm.filteredSampleHolders).to.be.deep.equal([{cryoCapable: true}])
        })

        it('should sort sample holders cryo capable first', () => {
          // given
          vm.entity.grid['protocolType'] = ProtocolType.Stain.name
          vm.sampleHolders = [
            {cryoCapable: true},
            {cryoCapable: false}
          ]
          sandbox.stub(vm, 'options').returnsArg(0)

          // when
          vm.updateSampleHolder()

          // then
          expect(vm.filteredSampleHolders).to.be.deep.equal([
            {cryoCapable: false},
            {cryoCapable: true}
          ])
        })

        it('should not filter sample holders', () => {
          // given
          vm.entity.grid['protocolType'] = 'Unknown'
          const expected = [
            {cryoCapable: true},
            {cryoCapable: false}
          ]
          vm.sampleHolders = expected
          sandbox.stub(vm, 'options').returnsArg(0)

          // when
          vm.updateSampleHolder()

          // then
          expect(vm.filteredSampleHolders).to.be.deep.equal(expected)
        })

      })

    })

    describe('isEntityCreated', () => {
      it('should call isEntityCreated with this.entity', () => {
        // given
        const spy = sandbox.spy(EntityUtils, 'isEntityCreated')

        // when
        vm.isEntityCreated

        // then
        expect(spy).to.have.been.calledWith(vm.entity)
      })
    })
  })
})
