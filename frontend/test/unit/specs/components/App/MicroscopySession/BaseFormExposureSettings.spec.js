import Vue from 'vue'
import * as sinon from 'sinon'
import VueRouter from 'vue-router'
import BaseFormExposureSettings from '@/components/App/MicroscopySession/BaseFormExposureSettings'

const sandbox = sinon.sandbox.create()

describe('MicroscopySession/BaseFormExposureSettings.vue', () => {
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
      superResolution: null,
      supersamplingFactor: null,
      pixelBinning: null
    }
  }

  beforeEach(() => {
    const Constructor = Vue.extend(BaseFormExposureSettings)
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

  describe('Exposure Settings', () => {

    describe('nominalMagnification', () => {

      it('should update available magnifications list', () => {
        vm.entity.microscope = {}
        vm.entity.electronDetector = {
          pixelLinearDimensionUm: 1
        }

        vm.entity.electronDetector.availableMagnifications = [{
          nominalMagnification: 1,
          calibratedMagnification: 10000,
          pixelSize: 1,
        }, {
          nominalMagnification: 2,
          calibratedMagnification: 2,
          pixelSize: 2,
        }]

        vm.microscopeLoaded = true
        vm.electronDetectorLoaded = false

        vm.entity.nominalMagnification = 1
        vm.entity.pixelSize = 1

        vm.updateAvailableMagnifications()

        expect(vm.availableMagnifications.length).to.be.equal(2)

        vm.originalEntity = {
          nominalMagnification: 3,
          electronDetector: {}
        }
        vm.entity.pixelSize = 3

        vm.updateAvailableMagnifications()

        expect(vm.availableMagnifications.length).to.be.equal(3)
      })

      it('should create magnification label', () => {
        vm.entity.microscope = {}
        vm.entity.electronDetector = {
          pixelLinearDimensionUm: 1,
          availableMagnifications: []
        }

        let expectedLabel = '1 × (1 Å/pixel)'

        let label = vm.createMagnificationLabel(vm.entity.electronDetector, {
          nominalMagnification: 1,
          calibratedMagnification: 10000
        })
        expect(label).to.be.equal(expectedLabel)
        label = vm.createMagnificationLabel(vm.entity.electronDetector, {
          nominalMagnification: 1,
          calibratedMagnification: null
        }, 1)
        expect(label).to.be.equal(expectedLabel)
      })

      it('should create exposure rate label', () => {
        expect(vm.createExposureRateLabel(true)).to.be.equal('Exposure rate (electrons/pixel/s)')

        expect(vm.createExposureRateLabel(false)).to.be.equal('Exposure rate (counts/pixel/s)')
      })
    })

    describe('supersamplingFactor', () => {

      describe('setSupersamplingFactorToDefaultIfNeeded', () => {
        it('should be called when entity.superResolution changes', done => {
          // given
          const expected = 1337
          const spy = sandbox.spy(vm, 'setSupersamplingFactorToDefaultIfNeeded')

          // when
          vm.entity.superResolution = expected

          // then
          Vue.nextTick()
            .then(() => {
              expect(spy).to.have.been.calledWith(expected)
            })
            .then(done, done)
        })

        it('nullifies the value if superResolution changes to false', () => {
          // given
          const expected = null
          vm.entity.supersamplingFactor = 1337

          // when
          vm.setSupersamplingFactorToDefaultIfNeeded(false)

          // then
          expect(vm.entity.supersamplingFactor).to.be.equal(expected)
        })

        it('keeps the value if superResolution changes to true', () => {
          // given
          const expected = 1337
          vm.entity.supersamplingFactor = expected

          // when
          vm.setSupersamplingFactorToDefaultIfNeeded(true)

          // then
          expect(vm.entity.supersamplingFactor).to.be.equal(expected)
        })

        it('sets default value of 2 if superResolution changes to true and previous was undefined', () => {
          // given
          const expected = 2
          vm.entity.supersamplingFactor = undefined

          // when
          vm.setSupersamplingFactorToDefaultIfNeeded(true)

          // then
          expect(vm.entity.supersamplingFactor).to.be.equal(expected)
        })

        it('sets default value of 2 if superResolution changes to true and previous was null', () => {
          // given
          const expected = 2
          vm.entity.supersamplingFactor = null

          // when
          vm.setSupersamplingFactorToDefaultIfNeeded(true)

          // then
          expect(vm.entity.supersamplingFactor).to.be.equal(expected)
        })

        it('sets the original value if superResolution is true', () => {
          // given
          const expected = 1337
          vm.originalEntity = {
            supersamplingFactor: expected
          }

          // when
          vm.setSupersamplingFactorToDefaultIfNeeded(true)

          // then
          expect(vm.entity.supersamplingFactor).to.be.equal(expected)
        })

        it('sets the default value of 2 if superResolution is true and there\'s no original value', () => {
          // given
          const expected = 2
          vm.originalEntity = {
            supersamplingFactor: null
          }
          vm.entity.supersamplingFactor = null

          // when
          vm.setSupersamplingFactorToDefaultIfNeeded(true)

          // then
          expect(vm.entity.supersamplingFactor).to.be.equal(expected)
        })

      })

    })

    describe('pixelBinning', () => {

      describe('setPixelBinningToDefaultIfNeeded', () => {
        it('should be called when entity.superResolution changes', done => {
          // given
          const expected = 1337
          const spy = sandbox.spy(vm, 'setPixelBinningToDefaultIfNeeded')

          // when
          vm.entity.superResolution = expected

          // then
          Vue.nextTick()
            .then(() => {
              expect(spy).to.have.been.calledWith(expected)
            })
            .then(done, done)
        })

        it('nullifies the value if superResolution changes to true', () => {
          // given
          const expected = null
          vm.entity.pixelBinning = 1337

          // when
          vm.setPixelBinningToDefaultIfNeeded(true)

          // then
          expect(vm.entity.pixelBinning).to.be.equal(expected)
        })

        it('keeps the value if superResolution changes to false', () => {
          // given
          const expected = 1337
          vm.entity.pixelBinning = expected

          // when
          vm.setPixelBinningToDefaultIfNeeded(false)

          // then
          expect(vm.entity.pixelBinning).to.be.equal(expected)
        })

        it('sets default value of 1 if superResolution changes to true and previous was undefined', () => {
          // given
          const expected = 1
          vm.entity.pixelBinning = undefined

          // when
          vm.setPixelBinningToDefaultIfNeeded(false)

          // then
          expect(vm.entity.pixelBinning).to.be.equal(expected)
        })

        it('sets default value of 1 if superResolution changes to true and previous was null', () => {
          // given
          const expected = 1
          vm.entity.pixelBinning = null

          // when
          vm.setPixelBinningToDefaultIfNeeded(false)

          // then
          expect(vm.entity.pixelBinning).to.be.equal(expected)
        })

        it('sets the original value if superResolution is true', () => {
          // given
          const expected = 1337
          vm.originalEntity = {
            pixelBinning: expected
          }

          // when
          vm.setPixelBinningToDefaultIfNeeded(false)

          // then
          expect(vm.entity.pixelBinning).to.be.equal(expected)
        })

        it('sets the default value of 1 if superResolution is true and there\'s no original value', () => {
          // given
          const expected = 1
          vm.originalEntity = {
            pixelBinning: null
          }
          vm.entity.pixelBinning = null

          // when
          vm.setPixelBinningToDefaultIfNeeded(false)

          // then
          expect(vm.entity.pixelBinning).to.be.equal(expected)
        })

      })

    })
  })
})
