import Vue from 'vue'
import * as sinon from 'sinon'
import MultipleSelectExtended from '@/components/Selectors/MultipleSelectWithAdditionalFields'

const sandbox = sinon.sandbox.create()

describe('MultipleSelectExtended', () => {
  let vm = null

  beforeEach(() => {
    const Constructor = Vue.extend(MultipleSelectExtended)
    vm = new Constructor({})
    vm.config = [
      {
        type: 'select-with-date',
        propertyName: 'propName1',
        label: 'label 1',
        required: true,
        errorMessage: 'error message 1',
        placeholder: 'placeholder 1'
      },
      {
        type: 'select',
        propertyName: 'propName2',
        listItemsName: 'propName1.itemsListName',
        label: 'label 2',
        required: true,
        errorMessage: 'error message 2',
        placeholder: 'placeholder 2'
      },
      {
        type: 'textarea',
        propertyName: 'propName3',
        label: 'label 3',
        required: false,
        placeholder: 'placeholder 3'
      }
    ]
    vm.selectedEntities = []
    vm.allEntities = [
      {id: 1, label: 'My label 1', itemsListName: ['v1', 'v2', 'v3', 'v4', 'v5'], createdDate: 1523563730000},
      {id: 2, label: 'My label 2', itemsListName: ['a1', 'a2', 'a3', 'a4'], createdDate: 1524566730000},
      {id: 3, label: 'My label 3', itemsListName: ['c1', 'c2', 'c3'], createdDate: 1525566730000}
    ]
    vm.entityLabel = 'entity'
    vm.selectedEntitiesName = 'entities'
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('addEntity', () => {
    it('should add a new entity', async () => {
      // given
      vm.selectedEntities = [{propName1: Object.assign(vm.allEntities[1])}]

      // when
      vm.setupMainSelectorPropertyName()
      vm.addEntity()

      // then
      expect(vm.selectedEntities).to.be.of.length(2)
      expect(vm.selectedEntities[1]).to.be.deep.equal({propName1: {}, propName2: ''})
    })
  })

  describe('createFilteredSelectionList', () => {
    it('should create options list', async () => {
      // given
      vm.selectedEntities = [{propName1: vm.allEntities[0]}, {propName1: vm.allEntities[1]}]

      // when
      let filteredList = vm.createFilteredSelectionList(vm.allEntities[1], {propertyName: 'propName1'})

      // then
      expect(filteredList).to.be.of.length(2)
      expect(filteredList[0]).to.be.deep.equal(vm.allEntities[2])
    })
  })

  describe('removeEntity', () => {
    it('should remove entity', async () => {
      const modelEntitties = [{propName1: vm.allEntities[0]}, {propName1: vm.allEntities[1]}]
      // given
      vm.selectedEntities = Object.assign([], modelEntitties)

      // when
      vm.setupMainSelectorPropertyName()
      vm.removeEntity(0)

      // then
      expect(vm.selectedEntities).to.be.of.length(1)
      expect(vm.selectedEntities[0]).to.be.deep.equal(modelEntitties[1])
    })
  })

  describe('updateSubSelectors', () => {
    it('should reset sub property if changed', async () => {
      // given
      let entity = {
        propName1: vm.allEntities[0],
        propName2: vm.allEntities[1].itemsListName[0]
      }

      // when
      vm.setupMainSelectorPropertyName()
      vm.updateSubSelectors(entity)

      // then
      expect(entity.propName2).to.be.undefined
    }),
      it('should keep sub property if not changed', async () => {
        // given
        let entity = {
          propName1: vm.allEntities[0],
          propName2: vm.allEntities[0].itemsListName[1]
        }
        // when
        vm.updateSubSelectors(entity, 'propName1')

        // then
        expect(entity.propName2).to.be.equal(vm.allEntities[0].itemsListName[1])
      })
  })

  describe('createOptions', () => {
    it('should create options', async () => {
      // given
      const entity = Object.assign({}, {propName1: vm.allEntities[2]})
      vm.mainSelectorPropertyName = vm.config[0].propertyName

      // when
      let options = vm.createOptions(entity, vm.config[1])

      // then
      expect(options).to.be.deep.equal(vm.allEntities[2].itemsListName)
    })
    it('should create options with historical values', async () => {
      // given
      const historicalValue = 'abcd'
      const entity = Object.assign({}, {propName1: vm.allEntities[2]})
      vm.mainSelectorPropertyName = vm.config[0].propertyName
      vm.historicalValues = [JSON.parse(JSON.stringify(Object.assign(entity)))]
      vm.historicalValues[0].propName2 = historicalValue

      // when
      let options = vm.createOptions(entity, vm.config[1])

      // then
      expect(options).to.be.deep.equal([historicalValue].concat(vm.allEntities[2].itemsListName))
    })
  })

  describe('setupMainSelectorPropertyName', () => {
    it('should set up main selector property name', async () => {
      // when
      vm.setupMainSelectorPropertyName()
      // then
      expect(vm.mainSelectorPropertyName).to.be.equal(vm.config[0].propertyName)
    })
  })
})
