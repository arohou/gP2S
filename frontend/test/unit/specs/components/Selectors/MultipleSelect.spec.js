import Vue from 'vue'
import * as sinon from 'sinon'
import MultipleSelect from '@/components/Selectors/MultipleSelect'

const sandbox = sinon.sandbox.create()

describe('MultipleSelect', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(MultipleSelect)
    vm = new Constructor({})
    vm.selectedEntities = []
    vm.allEntities = []
    vm.entityLabel = 'entity'
    vm.selectedEntitiesName = 'entities'
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('availableEntities', () => {
    it('should compute available entities', async () => {
      // given
      vm.allEntities = [
        {id: 1}, {id: 2}, {id: 3}
      ]
      vm.selectedEntities = [vm.allEntities[1]]

      // when
      vm.updateAvailableEntities()
      await vm.$nextTick()

      // then
      expect(vm.availableEntities).to.be.of.length(2)
    })
  }),

    describe('updateEntities', () => {
      it('update entities list', () => {
        // given
        vm.selectedEntities = [{}, {}]
        vm.allEntities = [{id: 1}, {id: 2}, {id: 3}]
        const chosenEntityId = vm.allEntities[1].id

        // when
        vm.updateEntities(chosenEntityId, 0)

        // then
        expect(vm.selectedEntities[0]).to.be.deep.equal(vm.allEntities[1])
      })
    })

  describe('createFilteredSelectionList', () => {
    it('update entities list', () => {
      // given
      vm.allEntities = [{id: 1, label: 'e-1', createdDate: 0}, {id: 2, label: 'e-2', createdDate: 1},
        {id: 3, label: 'e-3', createdDate: 2}]
      vm.chosenEntitiesIds = []
      const entity = {id: 4, label: 'e-4', createdDate: 3}

      // when
      const orderedEntities = vm.createFilteredSelectionList(entity)

      // then
      expect(orderedEntities).to.be.of.length(4)
      expect(orderedEntities[0]).to.be.deep.equal(entity)
      expect(orderedEntities[1]).to.be.deep.equal(vm.availableEntities[2])
      expect(orderedEntities[3]).to.be.deep.equal(vm.availableEntities[0])
    })
  })

  describe('removeEntity', () => {
    it('update entities list', async () => {
      // given
      vm.allEntities = [{id: 1, label: 'e-1', createdDate: 2}, {id: 2, label: 'e-2', createdDate: 1},
        {id: 3, label: 'e-3', createdDate: 0}]
      vm.selectedEntities = Object.assign([], vm.allEntities)
      const entity = {id: 2}

      // when
      vm.removeEntity(entity)
      await vm.$nextTick()

      // then
      expect(vm.selectedEntities).to.be.of.length(2)
    })
  })
})
