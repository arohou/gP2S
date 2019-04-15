import Vue from 'vue'
import * as sinon from 'sinon'

import Utils from '@/components/App/MicroscopySession/Utils'

const sandbox = sinon.sandbox.create()

describe('MicroscopySession/Utils.vue', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(Utils)
    vm = new Constructor()

    vm.microscopySession1 = {
      electronDetector: {
        pixelLinearDimensionUm: 1,
        countsPerElectronsFactor: 10
      },
      exposureRate: 1,
      calibratedMagnification: 1,
      superResolution: true,
      supersamplingFactor: 1,
      countingMode: true,
      exposureDuration: 1,
      numberOfFrames: 5
    }
    vm.microscopySession2 = {
      electronDetector: {
        pixelLinearDimensionUm: 1,
        countsPerElectronsFactor: 5
      },
      exposureRate: 1,
      calibratedMagnification: 1,
      superResolution: false,
      pixelBinning: 0.01,
      countingMode: false
    }
  })

  afterEach(() => {
    sandbox.restore()
    vm = null
  })

  it('should calculate image pixel size', () => {
    const expectedPixelSize1 = 10000
    const expectedPixelSize2 = 100

    let imagePixelSize = vm.calculateImagePixelSize(vm.microscopySession1)
    expect(imagePixelSize).to.be.equal(expectedPixelSize1)

    imagePixelSize = vm.calculateImagePixelSize(vm.microscopySession2)
    expect(imagePixelSize).to.be.equal(expectedPixelSize2)
  })

  it('should calculate exposure rate', () => {
    const expectedExposureRate1 = 1
    const expectedExposureRate2 = 0.2
    vm.calculateImagePixelSize = () => {return 1}

    let exposureRate = vm.calculateExposureRate(vm.microscopySession1)
    expect(exposureRate).to.be.equal(expectedExposureRate1)

    exposureRate = vm.calculateExposureRate(vm.microscopySession2)
    expect(exposureRate).to.be.equal(expectedExposureRate2)
  })

  it('should calculate total exposure', () => {
    const expectedTotalExposure1 = 1
    vm.calculateExposureRate = () => {return 1}

    let totalExposure = vm.calculateTotalExposure(vm.microscopySession1)
    expect(totalExposure).to.be.equal(expectedTotalExposure1)

    totalExposure = vm.calculateTotalExposure(vm.microscopySession2)
    expect(totalExposure).to.to.be.null
  })

  it('should calculate frame duration', () => {
    const expectedFrameDuration1 = 0.2
    let frameDuration = vm.calculateFrameDuration(vm.microscopySession1)

    expect(frameDuration).to.be.equal(expectedFrameDuration1)

    vm.microscopySession2.numberOfFrames = 2
    frameDuration = vm.calculateFrameDuration(vm.microscopySession2)
    expect(frameDuration).to.be.null
  })

  it('should calculate exposure per frame', () => {
    const expectedExposurePerFrame1 = 0.2
    vm.calculateTotalExposure = () => {return 1}

    let exposurePerFrame = vm.calculateExposurePerFrame(vm.microscopySession1)
    expect(exposurePerFrame).to.be.equal(expectedExposurePerFrame1)

    exposurePerFrame = vm.calculateExposurePerFrame(vm.microscopySession2)
    expect(exposurePerFrame).to.be.null
  })

  describe('isApplicableObjectiveAperture', () => {
    it('should not pass when value objectiveAperture is not defined', () => {
      let microscope = {
        objectiveAperture1: {diameter: '1'},
        objectiveAperture2: {diameter: '2'},
        objectiveAperture3: {diameter: '3'},
        objectiveAperture4: {diameter: '4'}
      }
      expect(vm.isApplicableObjectiveAperture(microscope, null)).to.be.false
    })

    it('should not pass when value objectiveAperture is not on list', () => {
      let microscope = {
        objectiveAperture1: {diameter: '1'},
        objectiveAperture2: {diameter: '2'},
        objectiveAperture3: {diameter: '3'},
        objectiveAperture4: {diameter: '4'}
      }
      expect(vm.isApplicableObjectiveAperture(microscope, {diameter: '5'})).to.be.false
    })

    it('should not pass when value objectiveAperture is not on list and diameter is null', () => {
      let microscope = {
        objectiveAperture1: {diameter: '5', phasePlate: false},
        objectiveAperture2: {diameter: null, phasePlate: true},
        objectiveAperture3: {diameter: null, phasePlate: true},
        objectiveAperture4: {diameter: '4', phasePlate: false}
      }
      expect(vm.isApplicableObjectiveAperture(microscope, {diameter: null})).to.be.false
    })

    it('should not pass when value objectiveApertures are not defined', () => {
      let microscope = {
        objectiveAperture1: {diameter: null},
        objectiveAperture2: {diameter: null},
        objectiveAperture3: {diameter: null},
        objectiveAperture4: {diameter: null}
      }
      expect(vm.isApplicableObjectiveAperture(microscope, {diameter: '5'})).to.be.false
    })

    it('should pass when value objectiveAperture is not defined but one of objectiveAperture has no diameter', () => {
      let microscope = {
        objectiveAperture1: {diameter: '1'},
        objectiveAperture2: {diameter: '2'},
        objectiveAperture3: {diameter: null},
        objectiveAperture4: {diameter: '4'}
      }
      expect(vm.isApplicableObjectiveAperture(microscope, {diameter: null})).to.be.true
    })

    it('should pass when value objectiveAperture is on list', () => {
      let microscope = {
        objectiveAperture1: {diameter: '1'},
        objectiveAperture2: {diameter: '2'},
        objectiveAperture3: {diameter: '3'},
        objectiveAperture4: {diameter: '4'}
      }
      expect(vm.isApplicableObjectiveAperture(microscope, {diameter: '3'})).to.be.true
    })

    it('should pass when value phasePlate is true and it exists on the list', () => {
      let microscope = {
        objectiveAperture1: {diameter: '5', phasePlate: false},
        objectiveAperture2: {diameter: null, phasePlate: true},
        objectiveAperture3: {diameter: '3', phasePlate: false},
        objectiveAperture4: {diameter: '4', phasePlate: false}
      }
      expect(vm.isApplicableObjectiveAperture(microscope, {phasePlate: true})).to.be.true
    })
  })

  describe('isApplicableAccelerationVoltageKV', () => {
    it('should not pass when value accelerationVoltageKV is not defined', () => {
      let microscope = {availableVoltagesKV: [10, 20, 30]}
      expect(vm.isApplicableAccelerationVoltageKV(microscope, null)).to.be.false
    })

    it('should not pass when value accelerationVoltageKV is not on list', () => {
      let microscope = {availableVoltagesKV: [10, 20, 30]}
      expect(vm.isApplicableAccelerationVoltageKV(microscope, 40)).to.be.false
    })

    it('should not pass when availableVoltagesKV list is empty', () => {
      let microscope = {availableVoltagesKV: []}
      expect(vm.isApplicableAccelerationVoltageKV(microscope, 10)).to.be.false
    })

    it('should pass when value accelerationVoltageKV is on list', () => {
      let microscope = {availableVoltagesKV: [10, 20, 30]}
      expect(vm.isApplicableAccelerationVoltageKV(microscope, 10)).to.be.true
    })
  })

  describe('isApplicableCondenser2ApertureDiameter', () => {
    it('should not pass when value condenser2ApertureDiameter is not defined', () => {
      let microscope = {
        condenser1ApertureDiameter: 10,
        condenser2ApertureDiameter: 20,
        condenser3ApertureDiameter: 30,
        condenser4ApertureDiameter: 40
      }
      expect(vm.isApplicableCondenser2ApertureDiameter(microscope, null)).to.be.false
    })

    it('should not pass when value condenser2ApertureDiameter is not on list', () => {
      let microscope = {
        condenser1ApertureDiameter: 10,
        condenser2ApertureDiameter: 20,
        condenser3ApertureDiameter: 30,
        condenser4ApertureDiameter: 40
      }
      expect(vm.isApplicableCondenser2ApertureDiameter(microscope, 50)).to.be.false
    })

    it('should not pass when condenser aperture diameter list is empty', () => {
      let microscope = {
        condenser1ApertureDiameter: null,
        condenser2ApertureDiameter: null,
        condenser3ApertureDiameter: null,
        condenser4ApertureDiameter: null
      }
      expect(vm.isApplicableCondenser2ApertureDiameter(microscope, 10)).to.be.false
    })

    it('should pass when value condenser2ApertureDiameter is on list', () => {
      let microscope = {
        condenser1ApertureDiameter: 10,
        condenser2ApertureDiameter: 20,
        condenser3ApertureDiameter: 30,
        condenser4ApertureDiameter: 40
      }
      expect(vm.isApplicableCondenser2ApertureDiameter(microscope, 20)).to.be.true
    })

    it('should pass when value condenser2ApertureDiameter is not defined as well as one from microscope', () => {
      let microscope = {
        condenser1ApertureDiameter: 10,
        condenser2ApertureDiameter: 20,
        condenser3ApertureDiameter: null,
        condenser4ApertureDiameter: 40
      }
      expect(vm.isApplicableCondenser2ApertureDiameter(microscope, null)).to.be.true
    })
  })

  describe('getDefaultObjectiveAperture', () => {
    it('should first when index is not defined', () => {
      let microscope = {
        objectiveAperture1: {diameter: '1'},
        objectiveAperture2: {diameter: '2'},
        objectiveAperture3: {diameter: '3'},
        objectiveAperture4: {diameter: '4'},
        defaultObjectiveApertureIndex: null
      }
      expect(vm.getDefaultObjectiveAperture(microscope)).to.be.deep.equal({diameter: '1'})
    })

    it('should return base on index', () => {
      let microscope = {
        objectiveAperture1: {diameter: '1'},
        objectiveAperture2: {diameter: '2'},
        objectiveAperture3: {diameter: '3'},
        objectiveAperture4: {diameter: '4'},
        defaultObjectiveApertureIndex: 1
      }
      expect(vm.getDefaultObjectiveAperture(microscope)).to.be.deep.equal({diameter: '1'})
      microscope.defaultObjectiveApertureIndex = 3
      expect(vm.getDefaultObjectiveAperture(microscope)).to.be.deep.equal({diameter: '3'})
    })
  })

  describe('getDefaultCondenser2ApertureDiameter', () => {
    it('should first when index is not defined', () => {
      let microscope = {
        condenser1ApertureDiameter: 10,
        condenser2ApertureDiameter: 20,
        condenser3ApertureDiameter: 30,
        condenser4ApertureDiameter: 40,
        defaultCondenserApertureIndex: null
      }
      expect(vm.getDefaultCondenser2ApertureDiameter(microscope)).to.be.equal(10)
    })

    it('should return base on index', () => {
      let microscope = {
        condenser1ApertureDiameter: 10,
        condenser2ApertureDiameter: 20,
        condenser3ApertureDiameter: 30,
        condenser4ApertureDiameter: 40,
        defaultCondenserApertureIndex: 1
      }
      expect(vm.getDefaultCondenser2ApertureDiameter(microscope)).to.be.equal(10)
      microscope.defaultCondenserApertureIndex = 3
      expect(vm.getDefaultCondenser2ApertureDiameter(microscope)).to.be.equal(30)
    })
  })
})
