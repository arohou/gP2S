import Vue from 'vue'
import * as sinon from 'sinon'

import ProteinRegistrationSystemCommons from '@/components/App/Protein/registration_system_ext/ProteinRegistrationSystemCommons'

const sandbox = sinon.sandbox.create()

describe('ProteinRegistrationSystemCommons', () => {
  let vm = null

  const DATA = {
    projectSlugOrId: 'project-1',
    protein: {
      id: 1,
      slug: 'pur-label',
      label: 'Pur label',
      purificationId: '333',
      tubeLabel: 'T333',
      concentration: '0.2'
    }
  }

  beforeEach(() => {
    const Constructor = Vue.extend(ProteinRegistrationSystemCommons)
    vm = new Constructor({})
    vm.protein = DATA.protein
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  describe('interpretProteinRegistrationSystemResult', () => {
    it('should reject when protein registration system response is missing data', async () => {
      //given
      let purId = vm.protein.purificationId
      const spy = sandbox.spy(vm.$events, '$emit')
      let response = null

      //when
      await vm.interpretProteinRegistrationSystemResult(purId, vm.protein, response)
      //then
        .catch(error => expect(error).to.be.deep.equal({purId: purId}))

      spy.should.have.been.calledWith('proteinRegistrationSystemPurInvalid', purId)
    })

    it('should prevent overlapping', async () => {
      //given
      let purId = 'NEW-PUR-ID'
      let response = {
        data: {
          purId: purId
        }
      }

      //when
      await vm.interpretProteinRegistrationSystemResult(purId, vm.protein, response)
      //then
        .then(() => {
          return Promise.reject('Expected method to reject.')
        })
        .catch(error => expect(error).to.be.undefined)
    })

    it('should prevent using pur without molecular weight', async () => {
      //given
      let purId = vm.protein.purificationId
      let response = {
        data: {
          purId: purId,
          molecularWeightDa: null
        }
      }
      const spy = sandbox.spy(vm.$events, '$emit')

      //when
      await vm.interpretProteinRegistrationSystemResult(purId, vm.protein, response)
      //then
        .then(() => {
          return Promise.reject('Expected method to reject.')
        })
        .catch(error => expect(error).to.be.undefined)
      spy.should.have.been.calledOnce
      spy.should.have.been.calledWith('purHasNoMolecularWeight', purId)
    })

    it('should emit Protein registration system pur loaded event', async () => {
      //given
      let purId = vm.protein.purificationId
      const spy = sandbox.spy(vm.$events, '$emit')
      let response = {
        data: {
          purId: purId,
          molecularWeightDa: 70000
        }
      }

      //when
      await vm.interpretProteinRegistrationSystemResult(purId, vm.protein, response)
      //then

      spy.should.have.been.calledOnce
      spy.should.have.been.calledWith('proteinRegistrationSystemPurLoaded', purId)
    })
  })
})
