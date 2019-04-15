import Vue from 'vue'
import * as sinon from 'sinon'

import BaseForm from '@/components/App/Admin/Microscope/BaseForm'
import InsertionMechanismType from '@/components/App/Admin/Microscope/InsertionMechanismType';
import ValidationError from '@/errors/ValidationError';

const sandbox = sinon.sandbox.create()

describe('Admin/Microscope/BaseForm.vue', () => {
  let vm = null

  const DATA = {
    label: 'label',
    manufacturer: 'manufacturer',
    model: 'model',
    location: 'location',
    availableVoltagesKV: [80, 120, 200, 300],
    defaultVoltageKV: 80,
    defaultExtractionVoltageKV: 1.4,
    defaultGunLensSetting: 10,
    condenser1ApertureDiameter: 1,
    condenser2ApertureDiameter: 2,
    condenser3ApertureDiameter: 3,
    condenser4ApertureDiameter: 4,
    defaultCondenserApertureIndex: 1,
    sampleInsertionMechanism: InsertionMechanismType.SIDE_ENTRY_HOLDER,
    objectiveAperture1: {phasePlate: false, diameter: 1},
    objectiveAperture2: {phasePlate: false, diameter: 2},
    objectiveAperture3: {phasePlate: false, diameter: 3},
    objectiveAperture4: {phasePlate: false, diameter: 4},
    defaultObjectiveApertureIndex: 1,
    energyFilter: false,
    defaultEnergyFilterSlitWidth: null,
    defaultSpotSize: 15,
    availableMagnifications: []
  }

  beforeEach(() => {
    const Constructor = Vue.extend(BaseForm)
    vm = new Constructor({
      propsData: {
        entity: DATA,
        template: {}
      }
    })
    vm = vm.$mount()
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should validate objective aperture properly', done => {
    let callback = (error) => {
      return error;
    };
    let validationFunction = vm.validateObjectiveAperture();

    Vue.nextTick().then(() => {
      expect(validationFunction({required: true}, vm.entity.objectiveAperture1, callback)).to.be.undefined;
    }).then(() => {
      vm.entity.objectiveAperture1.diameter = null;
      expect(validationFunction({required: true}, vm.entity.objectiveAperture1, callback)).to.be.instanceOf(ValidationError);
    }).then(() => {
      vm.entity.objectiveAperture1.phasePlate = true;
      expect(validationFunction({required: true}, vm.entity.objectiveAperture1, callback)).to.be.undefined;
    }).then(() => {
      let callback = sandbox.spy();
      validationFunction({required: true}, vm.entity.objectiveAperture2, callback);
      expect(callback).to.have.been.calledOnce;
    }).then(done, done);
  })
})
