import Vue from 'vue'
import * as sinon from 'sinon'

import RelatedEntitiesList from '@/components/App/RelatedEntitiesList'

describe('RelatedEntitiesList.vue', () => {
  let vm = null
  const sandbox = sinon.sandbox.create()

  let model = {
    maps: [{
      id: 1,
      processingSession: {
        id: 1,
        microscopySessions: [
          {
            id: 1,
            grid: {
              id: 1,
              sample: {
                id: 1,
                proteinComponent: [{id: 1}, {id: 2}],
                ligandComponent: [{id: 1}, {id: 2}],
              }
            },
            gridReturnedToStorage: true
          },
          {
            id: 2,
            grid: {
              id: 2,
              sample: {
                id: 2,
                proteinComponent: [{id: 3}, {id: 4}],
                ligandComponent: [{id: 3}, {id: 4}],
              }
            },
            gridReturnedToStorage: false
          },
        ]
      }
    },
      {
        id: 2,
        processingSession: {
          id: 2,
          microscopySessions: [
            {
              id: 3,
              grid: {
                id: 3,
                sample: {
                  id: 1,
                  proteinComponent: [{id: 1}, {id: 2}],
                  ligandComponent: [{id: 1}, {id: 2}],
                }
              }
            },
            {
              id: 4,
              grid: {
                id: 4,
                sample: {
                  id: 2,
                  proteinComponent: [{id: 3}, {id: 4}],
                  ligandComponent: [{id: 3}, {id: 4}],
                }
              }
            },
          ]
        }
      },
      {id: 1, processingSession: {id: 1}}] // duplicate
  }

  beforeEach(() => {
    const Constructor = Vue.extend(RelatedEntitiesList)
    vm = new Constructor().$mount()
    vm.entity = {}
  })

  afterEach(() => {
    sandbox.restore()
    vm = vm.$destroy()
    vm = null
  })

  it('should create entity name', () => {
    // given
    let list = [{}, {}]
    let expectedLabel = 'my label (2)'
    // when
    let label = vm.createEntityLabel('my label', list)
    // then
    expect(label).to.be.equal(expectedLabel)
  })

  it('should load child maps from a parent entity', async () => {
    // given
    vm.entity = {maps: Object.assign([], model.maps)}
    // when
    await vm.$nextTick()
    // then
    expect(vm.maps.length).to.be.equal(2)
  })

  describe('loadProcessingSessions', () => {
    it('should load child processing sessions from a model', async () => {
      // given
      vm.entity = {maps: Object.assign([], model.maps)}
      // when
      await vm.$nextTick()
      // then
      expect(vm.processingSessions.length).to.be.equal(2)
    })
    it('should load child processing sessions from a map', async () => {
      // given
      vm.entity = Object.assign({}, model.maps[0])
      // when
      await vm.$nextTick()
      // then
      expect(vm.processingSessions.length).to.be.equal(1)
    })
  })

  describe('loadMicroscopySessions', () => {
    it('should load child microscopy sessions from a model', async () => {
      // given
      vm.entity = {maps: Object.assign([], model.maps)}
      // when
      await vm.$nextTick()
      // vm.loadMicroscopySessions()
      // then
      expect(vm.microscopySessions.length).to.be.equal(4)
    })
    it('should load child microscopy sessions from a processing session', async () => {
      // given
      vm.entity = Object.assign({}, model.maps[0].processingSession)
      // when
      await vm.$nextTick()
      // then
      expect(vm.microscopySessions.length).to.be.equal(2)
    })
  })

  describe('loadGrids', () => {
    it('should load child grids from a model', async () => {
      // given
      vm.entity = {maps: Object.assign([], model.maps)}
      // when
      await vm.$nextTick()
      // then
      expect(vm.grids.length).to.be.equal(4)
    })
    it('should load child grids from a microscopy session', async () => {
      // given
      vm.entity = Object.assign({}, model.maps[0].processingSession.microscopySessions[0])
      // when
      await vm.$nextTick()
      // then
      expect(vm.grids.length).to.be.equal(1)
    })
  })

  describe('loadSamples', () => {
    it('should load child samples from a model', async () => {
      // given
      vm.entity = {maps: Object.assign([], model.maps)}
      // when
      await vm.$nextTick()
      // then
      expect(vm.samples.length).to.be.equal(2)
    })
    it('should load child samples from a grid', async () => {
      // given
      vm.entity = Object.assign({}, model.maps[0].processingSession.microscopySessions[0].grid)
      // when
      await vm.$nextTick()
      // then
      expect(vm.samples.length).to.be.equal(1)
    })
  })

  describe('loadProteins', () => {
    it('should load child proteins from a model', async () => {
      // given
      vm.entity = {maps: Object.assign([], model.maps)}
      // when
      await vm.$nextTick()
      // then
      expect(vm.proteins.length).to.be.equal(4)
    })
    it('should load child proteins from a sample', async () => {
      // given
      vm.entity = Object.assign({}, model.maps[0].processingSession.microscopySessions[0].grid.sample)
      // when
      await vm.$nextTick()
      // then
      expect(vm.proteins.length).to.be.equal(2)
    })
  })

  describe('loadLigands', () => {
    it('should load child ligands from a model', async () => {
      // given
      vm.entity = {maps: Object.assign([], model.maps)}
      // when
      await vm.$nextTick()
      // then
      expect(vm.ligands.length).to.be.equal(4)
    })
    it('should load child ligands from a sample', async () => {
      // given
      vm.entity = Object.assign({}, model.maps[0].processingSession.microscopySessions[0].grid.sample)
      // when
      await vm.$nextTick()
      // then
      expect(vm.ligands.length).to.be.equal(2)
    })
  })

  describe('updateGridsAvailability', () => {
    it('should update grids availability', async () => {
      // given
      vm.entity = Object.assign({}, model.maps[0].processingSession)
      // when
      await vm.$nextTick()
      // then
      expect(vm.grids[0].isAvailable).to.be.true
      expect(vm.grids[1].isAvailable).to.be.false
    })
  })
})
