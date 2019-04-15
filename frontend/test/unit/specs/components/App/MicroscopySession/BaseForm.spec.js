import Vue from 'vue'
import * as sinon from 'sinon'
import VueRouter from 'vue-router'
import BaseForm from '@/components/App/MicroscopySession/BaseForm'
import InsertionMechanismType from '@/components/App/Admin/Microscope/InsertionMechanismType'

const sandbox = sinon.sandbox.create()

describe('MicroscopySession/BaseForm.vue', () => {
  let vm = null
  let router = null
  let basicInformation = null
  let exposureSettings = null

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
      grid: null
    }
  }

  beforeEach(() => {
    const Constructor = Vue.extend(BaseForm)
    router = new VueRouter()
    vm = new Constructor({
      router,
      propsData: {
        entity: {...DATA.entity},
        projectId: '' + DATA.projectSlugOrId
      }
    })
    vm.$mount()
    basicInformation = vm.$refs['basicInformation']
    exposureSettings = vm.$refs['exposureSettings']

    sandbox.stub(basicInformation, 'loadMicroscopes')
    sandbox.stub(basicInformation, 'loadGrids')
    sandbox.stub(basicInformation, 'fetchElectronDetectorsForMicroscope')
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
    router = null
    basicInformation = null
    exposureSettings = null
  })

  describe('Basic Information', () => {
    describe('detector', () => {

      describe('entity.electronDetector watcher', () => {

        it('should call updateAvailableMagnifications', async () => {
          // given
          const spy = sandbox.spy(exposureSettings, 'updateAvailableMagnifications')

          // when
          vm.entity.electronDetector = 'force update'
          // Calling twice because the watcher calls $nextTick internally
          await vm.$nextTick()
          await vm.$nextTick()

          // then
          expect(spy).to.have.been.called
        })

        it('should call alertExposureSettingsChange', async () => {
          // given
          const spy = sandbox.spy(exposureSettings, 'alertExposureSettingsChange')

          // when
          vm.entity.electronDetector = 'force update'
          // Calling twice because the watcher calls $nextTick internally
          await vm.$nextTick()
          await vm.$nextTick()

          // then
          expect(spy).to.have.been.called
        })

        it('should set changingElectronDetector to false at the end', async () => {
          // given
          exposureSettings.changingElectronDetector = true

          // when
          vm.entity.electronDetector = 'force update'
          // Calling twice because the watcher calls $nextTick internally
          await vm.$nextTick()
          await vm.$nextTick()

          // then
          expect(exposureSettings.changingElectronDetector).to.be.false
        })

        it('should set electronDetectorLoaded to true', async () => {
          // given
          exposureSettings.electronDetectorLoaded = false
          sandbox.stub(vm, 'isNewEntity').returns(false)

          // when
          vm.entity.electronDetector = 'force update'
          // Calling twice because the watcher calls $nextTick internally
          await vm.$nextTick()
          await vm.$nextTick()

          // then
          expect(exposureSettings.electronDetectorLoaded).to.be.true
        })

        it('should set magnification', async () => {
          // given
          exposureSettings.electronDetectorLoaded = false
          sandbox.stub(vm, 'isNewEntity').returns(false)
          const expected = 1337
          sandbox.stub(exposureSettings, 'nominalMagnificationToOption').returns({value: expected})
          // Stop magnification watcher
          sandbox.stub(exposureSettings, 'assignMagnification')

          // when
          vm.entity.electronDetector = 'force update'
          // Calling twice because the watcher calls $nextTick internally
          await vm.$nextTick()
          await vm.$nextTick()

          // then
          expect(exposureSettings.magnification).to.be.equal(expected)
        })

        it('should call clearMagnification if electronDetectorLoaded is true', async () => {
          // given
          exposureSettings.electronDetectorLoaded = true
          const spy = sandbox.spy(exposureSettings, 'clearMagnification')

          // when
          vm.entity.electronDetector = 'force update'
          // Calling twice because the watcher calls $nextTick internally
          await vm.$nextTick()
          await vm.$nextTick()

          // then
          expect(spy).to.have.been.called
        })

        it('should call clearMagnification if isNewEntity is true', async () => {
          // given
          exposureSettings.electronDetectorLoaded = false
          sandbox.stub(exposureSettings, 'isNewEntity').returns(true)
          const spy = sandbox.spy(exposureSettings, 'clearMagnification')

          // when
          vm.entity.electronDetector = 'force update'
          // Calling twice because the watcher calls $nextTick internally
          await vm.$nextTick()
          await vm.$nextTick()

          // then
          expect(spy).to.have.been.called
        })

        it('should call updateExposureSettingsCheckboxes if electronDetectorLoaded is true', async () => {
          // given
          exposureSettings.electronDetectorLoaded = true
          const spy = sandbox.spy(exposureSettings, 'updateExposureSettingsCheckboxes')
          const expected = 'force update'

          // when
          vm.entity.electronDetector = expected
          // Calling twice because the watcher calls $nextTick internally
          await vm.$nextTick()
          await vm.$nextTick()

          // then
          expect(spy).to.have.been.calledWith(expected)
        })

        it('should call updateExposureSettingsCheckboxes if isNewEntity is true', async () => {
          // given
          exposureSettings.electronDetectorLoaded = false
          sandbox.stub(exposureSettings, 'isNewEntity').returns(true)
          const spy = sandbox.spy(exposureSettings, 'updateExposureSettingsCheckboxes')
          const expected = 'force update'

          // when
          vm.entity.electronDetector = expected
          // Calling twice because the watcher calls $nextTick internally
          await vm.$nextTick()
          await vm.$nextTick()

          // then
          expect(spy).to.have.been.calledWith(expected)
        })
      })
    })
  })

  describe('Exposure Settings', () => {
    describe('nominalMagnification', () => {
      it('should assign magnification from drop-down list', async () => {
        // given

        vm.entity.nominalMagnification = null
        vm.entity.calibratedMagnification = null
        vm.entity.pixelSize = null
        // }

        exposureSettings.mainForm = {
          validateField: function () {}
        }

        let availableMagnifications = [{
          value: '1 1',
          nominalMagnification: 1,
          calibratedMagnification: 10000,
          pixelSize: 1,
        }, {
          nominalMagnification: 2,
          calibratedMagnification: 2,
          pixelSize: 2,
        }]
        Object.assign(exposureSettings.availableMagnifications, availableMagnifications)

        // when
        await vm.$nextTick()
        await vm.$nextTick()

        // then
        expect(vm.entity.nominalMagnification).to.be.equal(null)
        expect(vm.entity.calibratedMagnification).to.be.equal(null)
        expect(vm.entity.pixelSize).to.be.equal(null)

        // when
        exposureSettings.assignMagnification(availableMagnifications[0].value)
        await vm.$nextTick()
        await vm.$nextTick()

        //then
        expect(vm.entity.nominalMagnification).to.be.equal(availableMagnifications[0].nominalMagnification)
        expect(vm.entity.calibratedMagnification).to.be.equal(availableMagnifications[0].calibratedMagnification)
        expect(vm.entity.pixelSize).to.be.equal(availableMagnifications[0].pixelSize)
      })

      it('should change exposure rate according to counting mode', async () => {
        // given
        vm.$refs['basicInformation'].loadGrids = () => {}
        vm.$refs['basicInformation'].fetchElectronDetectorsForMicroscope = () => {}

        vm.entity = {
          microscope: {
            sampleInsertionMechanism: InsertionMechanismType.AUTO_LOADER.name
          },
          electronDetector: {},
          countingMode: false
        }

        // let exposureSettings = vm.$refs.exposureSettings
        sandbox.spy(exposureSettings, 'recordExposureSettingsUpdate')

        // when
        let getExposureRateLabel = () => vm.$el.querySelector('#microscopy-session__exposure-rate-item .el-form-item__label').innerHTML
        await vm.$nextTick()

        // then
        expect(exposureSettings.recordExposureSettingsUpdate).to.have.been.called.once
        expect(getExposureRateLabel()).to.be.equal('Exposure rate (counts/pixel/s)')
      })
    })
  })

  describe('updateElectronDetector', () => {

    it('should be called when microscope has changed', async () => {
      sandbox.spy(basicInformation, 'updateElectronDetector')

      // when
      vm.entity = {
        microscope: {
          sampleInsertionMechanism: InsertionMechanismType.AUTO_LOADER.name
        },
        electronDetector: {}
      }
      await vm.$nextTick()

      // then
      expect(basicInformation.updateElectronDetector).to.have.been.called.once
    })

    it('should ignore null parameters', () => {
      vm.entity = {
        electronDetector: 'set'
      }
      sandbox.spy(basicInformation, 'alertWarnOK')

      // when
      basicInformation.updateElectronDetector(null, null)
      basicInformation.updateElectronDetector({id: 1337}, null)

      // then
      expect(vm.entity.electronDetector).to.be.equal('set')
      expect(basicInformation.alertWarnOK).to.not.have.been.called
    })

    it('should ignore undefined parameters', () => {
      vm.entity = {
        electronDetector: 'set'
      }
      sandbox.spy(basicInformation, 'alertWarnOK')

      // when
      basicInformation.updateElectronDetector(undefined, undefined)
      basicInformation.updateElectronDetector({id: 1337}, undefined)

      // then
      expect(vm.entity.electronDetector).to.be.equal('set')
      expect(basicInformation.alertWarnOK).to.not.have.been.called
    })

    it('should ignore setting the same microscope by id', () => {
      vm.entity = {
        electronDetector: 'set'
      }
      sandbox.spy(basicInformation, 'alertWarnOK')

      // when
      basicInformation.updateElectronDetector({id: 1337}, {id: 1337})

      // then
      expect(vm.entity.electronDetector).to.be.equal('set')
      expect(basicInformation.alertWarnOK).to.not.have.been.called
    })

    it('should reset the electron detector and inform the user upon microscope change', async () => {
      vm.entity = {
        electronDetector: {}
      }
      vm.entity.electronDetector = {}

      sandbox.spy(basicInformation, 'alertWarnOK')

      // when
      await vm.$nextTick()
      await vm.$nextTick()
      basicInformation.updateElectronDetector({id: 1337}, {id: 1})

      // then
      expect(vm.entity.electronDetector).to.be.equal(null)
      expect(basicInformation.alertWarnOK).to.have.been.called
    })
  })
})
