import Vue from 'vue'
import * as sinon from 'sinon'
import VueRouter from 'vue-router'

import BaseFormMicroscopeSettings from '@/components/App/MicroscopySession/BaseFormMicroscopeSettings'

const sandbox = sinon.sandbox.create()

describe('MicroscopySession/BaseFormMicroscopeSettings.vue', () => {
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
      grid: null
    }
  }

  beforeEach(() => {
    const Constructor = Vue.extend(BaseFormMicroscopeSettings)
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

  describe('Microscope Settings', () => {

    it('should update microscope settings', () => {
      vm.microscopeLoaded = true
      vm.entity = {
        accelerationVoltageKV: 5,
        energyFilter: 5,
        microscope: {
          defaultObjectiveApertureIndex: 2,
          objectiveAperture1: {diameter: 11},
          objectiveAperture2: {diameter: 12},
          objectiveAperture3: {diameter: 13},
          objectiveAperture4: {diameter: 14},

          defaultVoltageKV: 1,
          extractionVoltageKV: 2,
          defaultGunLensSetting: 3,
          defaultEnergyFilterSlitWidth: 4,
          energyFilter: true,
          availableVoltagesKV: [1, 2, 3]
        }
      }

      vm.updateMicroscopeSettings(vm.entity.microscope)

      expect(vm.entity.accelerationVoltageKV).to.be.equal(vm.entity.microscope.defaultVoltageKV)
      expect(vm.entity.extractionVoltageKV).to.be.equal(vm.entity.microscope.defaultExtractionVoltageKV)
      expect(vm.entity.gunLensSetting).to.be.equal(vm.entity.microscope.defaultGunLensSetting)
      expect(vm.entity.energyFilterSlitWidth).to.be.equal(vm.entity.microscope.defaultEnergyFilterSlitWidth)
      expect(vm.entity.objectiveAperture.diameter).to.be.equal(vm.entity.microscope.objectiveAperture2.diameter)
    })

    describe('objectiveAperture', () => {

      it('should not update objective aperture default index is not set', () => {
        let microscope = {
          defaultObjectiveApertureIndex: null,
          objectiveAperture1: {diameter: 11},
          objectiveAperture2: {diameter: 12},
          objectiveAperture3: {diameter: 13},
          objectiveAperture4: {diameter: 14}
        }

        vm.entity.objectiveAperture = 'test'

        vm.updateObjectiveApertureFromMicroscopeDefault(microscope)
        expect(vm.entity.objectiveAperture).to.be.equal('test')

      })

      it('should update objective aperture', () => {
        let microscope = {
          defaultObjectiveApertureIndex: 1,
          objectiveAperture1: {diameter: 11},
          objectiveAperture2: {diameter: 12},
          objectiveAperture3: {diameter: 13},
          objectiveAperture4: {diameter: 14}
        }

        vm.updateObjectiveApertureFromMicroscopeDefault(microscope)
        expect(vm.entity.objectiveAperture.diameter).to.be.equal(microscope.objectiveAperture1.diameter)

        microscope.defaultObjectiveApertureIndex = 2
        vm.updateObjectiveApertureFromMicroscopeDefault(microscope)
        expect(vm.entity.objectiveAperture.diameter).to.be.equal(microscope.objectiveAperture2.diameter)

        microscope.defaultObjectiveApertureIndex = 3
        vm.updateObjectiveApertureFromMicroscopeDefault(microscope)
        expect(vm.entity.objectiveAperture.diameter).to.be.equal(microscope.objectiveAperture3.diameter)

        microscope.defaultObjectiveApertureIndex = 4
        vm.updateObjectiveApertureFromMicroscopeDefault(microscope)
        expect(vm.entity.objectiveAperture.diameter).to.be.equal(microscope.objectiveAperture4.diameter)
      })

      describe('Objective Apertures List', () => {

        it('should collapse the list with different historical value', () => {
          // Order is important.
          vm.entity.microscope = {
            id: 1,
            objectiveAperture1: {
              phasePlate: false,
              diameter: null
            },
            objectiveAperture2: {
              phasePlate: true,
              diameter: null
            },
            objectiveAperture3: {
              phasePlate: false,
              diameter: null
            },
            objectiveAperture4: {
              phasePlate: true,
              diameter: null,
            }
          }

          vm.originalEntity = {
            microscope: {
              id: 1,
            },
            objectiveAperture: {
              phasePlate: false,
              diameter: 1337,
            },
          }

          expect(vm.objectiveAperturesList)
            .to.be.deep.equal([
            {
              id: 1337,
              phasePlate: false,
              diameter: 1337
            },
            {
              id: '—',
              phasePlate: false,
              diameter: null
            },
            {
              id: 'phasePlate',
              phasePlate: true,
              diameter: null,
            }
          ])
        })

        it('should collapse the list with identical historical value', () => {
          // Order is important. It's set up, that the default value is different from the first null value
          vm.entity.microscope = {
            id: 1,
            objectiveAperture1: {
              phasePlate: false,
              diameter: null
            },
            objectiveAperture2: {
              phasePlate: true,
              diameter: null
            },
            objectiveAperture3: {
              phasePlate: false,
              diameter: null
            },
            objectiveAperture4: {
              phasePlate: true,
              diameter: null,
            }
          }
          vm.originalEntity = {
            microscope: {
              id: 1,
            },
            objectiveAperture: {
              phasePlate: true,
              diameter: null,
            },
          }

          expect(vm.objectiveAperturesList)
            .to.be.deep.equal([
            {
              id: 'phasePlate',
              phasePlate: true,
              diameter: null
            },
            {
              id: '—',
              phasePlate: false,
              diameter: null,
            }
          ])
        })

        it('should collapse the list when there\'s no historical value', () => {
          // Order is important.
          vm.entity.microscope = {
            objectiveAperture1: {
              phasePlate: false,
              diameter: null
            },
            objectiveAperture2: {
              phasePlate: true,
              diameter: null
            },
            objectiveAperture3: {
              phasePlate: false,
              diameter: null
            },
            objectiveAperture4: {
              phasePlate: true,
              diameter: null,
            }
          }

          expect(vm.objectiveAperturesList)
            .to.be.deep.equal([
            {
              id: '—',
              phasePlate: false,
              diameter: null,
            },
            {
              id: 'phasePlate',
              phasePlate: true,
              diameter: null
            }
          ])
        })

        it('should produce an empty list when there\'s no entity', () => {
          vm.entity = {}

          expect(vm.objectiveAperturesList).to.be.deep.equal([])
        })

        it('should produce an empty list when there\'s microscope', () => {
          vm.entity.microscope = null

          expect(vm.objectiveAperturesList).to.be.deep.equal([])
        })

      })

    })

    describe('condenser2ApertureDiameter', () => {

      it('should not update condenser when no microscope selected', () => {
        vm.entity.condenser2ApertureDiameter = 'test'
        vm.updateCondenser2ApertureDiameter(null)
        expect(vm.entity.condenser2ApertureDiameter).to.be.equal('test')
      })

      it('should update condenser when microscope loaded', () => {
        let microscope = {
          condenser2ApertureDiameter: 12,
          defaultCondenserApertureIndex: 2
        }
        vm.microscopeLoaded = true
        vm.entity.condenser2ApertureDiameter = microscope.condenser1ApertureDiameter

        vm.updateCondenser2ApertureDiameter(microscope)
        expect(vm.entity.condenser2ApertureDiameter).to.be.equal(microscope.condenser2ApertureDiameter)
      })

      it('should update condenser when microscope not loaded and new entity is edited', () => {
        let microscope = {
          condenser2ApertureDiameter: 12,
          defaultCondenserApertureIndex: 2
        }
        vm.microscopeLoaded = false
        vm.entity.condenser2ApertureDiameter = 10

        let stub = sandbox.stub(vm, 'isNewEntity')
        stub.returns(true)

        vm.updateCondenser2ApertureDiameter(microscope)
        expect(vm.entity.condenser2ApertureDiameter).to.be.equal(microscope.condenser2ApertureDiameter)
      })

      it('should set condenser when is not present', () => {
        let microscope = {
          condenser2ApertureDiameter: 12,
          defaultCondenserApertureIndex: 2
        }
        vm.microscopeLoaded = true
        vm.entity.condenser2ApertureDiameter = null

        vm.updateCondenser2ApertureDiameter(microscope)
        expect(vm.entity.condenser2ApertureDiameter).to.be.equal(microscope.condenser2ApertureDiameter)
      })

      describe('Condenser 2 Apertures List', () => {
        it('should collapse the list with different historical value', () => {
          // Order is important.
          vm.entity.microscope = {
            id: 1,
            condenser1ApertureDiameter: null,
            condenser2ApertureDiameter: 1,
            condenser3ApertureDiameter: null,
            condenser4ApertureDiameter: 1
          }

          vm.originalEntity = {
            microscope: {
              id: 1
            },
            condenser2ApertureDiameter: 2
          }

          expect(vm.condenser2ApertureDiametersList)
            .to.be.deep.equal([2, null, 1])
        })

        it('should collapse the list with identical historical value', () => {
          // Order is important.
          vm.entity.microscope = {
            id: 1,
            condenser1ApertureDiameter: null,
            condenser2ApertureDiameter: 1,
            condenser3ApertureDiameter: null,
            condenser4ApertureDiameter: 1
          }

          vm.originalEntity = {
            microscope: {
              id: 1
            },
            condenser2ApertureDiameter: 1
          }

          expect(vm.condenser2ApertureDiametersList)
            .to.be.deep.equal([1, null])
        })

        it('should collapse the list when there\'s no historical value', () => {
          // Order is important.
          vm.entity.microscope = {
            condenser1ApertureDiameter: null,
            condenser2ApertureDiameter: 1,
            condenser3ApertureDiameter: null,
            condenser4ApertureDiameter: 1
          }

          expect(vm.condenser2ApertureDiametersList)
            .to.be.deep.equal([null, 1])
        })

        it('should produce an empty list when there\'s no entity', () => {
          vm.entity = null

          expect(vm.condenser2ApertureDiametersList).to.be.deep.equal([])
        })

        it('should produce an empty list when there\'s microscope', () => {
          vm.entity.microscope = null

          expect(vm.condenser2ApertureDiametersList).to.be.deep.equal([])
        })

        it('should update condenser 2 aperture diameter', () => {
          vm.entity.condenser2ApertureDiameter = null

          let microscope = {
            defaultCondenserApertureIndex: 3,
            condenser2ApertureDiameter: 5,
            condenser3ApertureDiameter: 7
          }

          vm.updateCondenser2ApertureDiameter(null)
          expect(vm.entity.condenser2ApertureDiameter).to.be.null

          vm.updateCondenser2ApertureDiameter(null)
          expect(vm.entity.condenser2ApertureDiameter).to.be.null

          vm.microscopeLoaded = true
          vm.updateCondenser2ApertureDiameter(microscope)
          expect(vm.entity.condenser2ApertureDiameter).to.be.equal(microscope.condenser3ApertureDiameter)
        })

      })

    })

    describe('acceleration voltages list', () => {
      it('should create empty list', async () => {
        // given
        vm.entity.microscope = {
          availableVoltagesKV: []
        }

        // when
        await vm.$nextTick()
        await vm.$nextTick()

        // then
        expect(vm.accelerationVoltagesKVList).to.be.deep.equal([])
      })

      it('should create not populated list', async () => {
        // given
        vm.entity.microscope = {
          availableVoltagesKV: [
            1, 2, 3, 4
          ]
        }
        // when
        await vm.$nextTick()
        await vm.$nextTick()
        // then
        expect(vm.accelerationVoltagesKVList).to.be.deep.equal(vm.entity.microscope.availableVoltagesKV)
      })

      it('should create accelerationVoltagesKVList with historical value', async () => {
        // given
        vm.originalEntity = {
          microscope: {
            id: 1,
          },
          accelerationVoltageKV: 15
        }
        vm.entity.microscope = {
          id: 1,
          availableVoltagesKV: [1, 2]
        }

        // when
        await vm.$nextTick()
        await vm.$nextTick()

        // then
        expect(vm.accelerationVoltagesKVList).to.be.deep.equal([vm.originalEntity.accelerationVoltageKV]
          .concat(vm.entity.microscope.availableVoltagesKV))
      })
    })

    describe('condenser 2 aperture diameters list', () => {
      it('should create empty list', async () => {
        // given
        vm.entity.microscope = null

        // when
        await vm.$nextTick()
        await vm.$nextTick()

        // then
        expect(vm.condenser2ApertureDiametersList).to.be.deep.equal([])
      })

      it('should create populated list', async () => {
        // given
        vm.entity.microscope = {
          condenser1ApertureDiameter: 1,
          condenser2ApertureDiameter: 2,
          condenser3ApertureDiameter: 3,
          condenser4ApertureDiameter: 4,
          id: 1
        }
        vm.originalEntity = {
          microscope: {
            id: 1
          },
          condenser2ApertureDiameter: 50
        }

        // when
        await vm.$nextTick()
        await vm.$nextTick()

        // then
        expect(vm.condenser2ApertureDiametersList).to.be.deep.equal([50, 1, 2, 3, 4])
      })
    })

    it('should update energy filter on microscope change', () => {
      // given
      vm.microscopeLoaded = true
      vm.entity = {
        accelerationVoltageKV: 5,
        energyFilter: 5,
        microscope: {
          energyFilter: true
        }
      }

      let newMicroscope = Object.assign({}, vm.entity.microscope)
      newMicroscope.energyFilter = false

      // when
      vm.updateMicroscopeSettings(newMicroscope)

      // then
      expect(vm.entity.energyFilter).to.be.equal(newMicroscope.energyFilter)
    })
  })
})
