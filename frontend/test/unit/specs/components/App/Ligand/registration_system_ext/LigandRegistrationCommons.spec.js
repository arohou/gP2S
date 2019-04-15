import Vue from 'vue'
import * as sinon from 'sinon'

import LigandRegistrationCommons from '@/components/App/Ligand/registration_system_ext/LigandRegistrationCommons'

const sandbox = sinon.sandbox.create()

describe('LigandRegistrationCommons', () => {
  let vm = null

  const DATA = {
    projectSlugOrId: 'project-1',
    ligand: {
      id: 1,
      slug: 'ligand-label',
      label: 'Lignad Label',
      tubeLabel: 'Tube label',
      concentration: '0.3',
      conceptId: 'CONCEPT1',
      batchId: 'G99999999.9-9',
      solvent: 'DMSO'
    }
  }

  beforeEach(() => {
    const Constructor = Vue.extend(LigandRegistrationCommons)
    vm = new Constructor({})
    vm.ligand = DATA.ligand
  })

  afterEach(() => {
    sandbox.restore()
    vm.$destroy()
    7
    vm = null
  })

  it('should not call save on CONCEPT error', done => {
    let saveLignadCallback = sandbox.spy()
    const spy = sandbox.spy(vm.$events, '$emit')

    Vue.nextTick().then(() => {
      let response = null
      vm.interpretLigandRegistrationResult(vm.ligand.conceptId, null, vm.ligand, response, saveLignadCallback)
      return Vue.nextTick()
    }).then(() => {
      spy.should.have.been.calledWith('conceptIdentifierInvalid')
      expect(saveLignadCallback).to.not.have.been.called
    }).then(() => {
      let response = null
      vm.interpretLigandRegistrationResult(null, vm.ligand.batchId, vm.ligand, response, saveLignadCallback)
      return Vue.nextTick()
    }).then(() => {
      spy.should.have.been.calledWith('conceptIdentifierInvalid')
      expect(saveLignadCallback).to.not.have.been.called
    }).then(done, done)
  })

  it('should interpret CONCEPT positive results properly', async () => {

    let conceptId = vm.ligand.conceptId
    let response = {conceptId: conceptId}

    await vm.$nextTick()

    vm.interpretLigandRegistrationResult(conceptId, null, response).then((result) => {
      expect(result).to.deep.equal(response)
    })
  })

  it('should emit ligandHasNoMolecularWeight when response has no molecularWeight', () => {
    // given
    const response = {
      data: {
        conceptId: 'whatever'
      }
    }
    const spy = sandbox.stub(vm.$events, '$emit')

    // then
    expect(vm.interpretLigandRegistrationResult(null, null, response)).to.be.rejected
    spy.should.have.been.calledWith('ligandHasNoMolecularWeight')
  })
})
