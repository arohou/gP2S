import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'

import { HTTP } from '@/utils/http-common'
import AliquotSelector from '@/components/Selectors/AliquotSelector'

const sandbox = sinon.sandbox.create()

describe('AliquotSelector.vue', () => {
  const ALIQUOTS = [
    {
      id: 1,
      label: 'Aliquot 1',
      projectSlug: 'project-slug-1',
      reference: 'S-111',
      slug: 'aliquot-slug-1',
      type: 'LIGAND'
    },
    {
      id: 2,
      label: 'Aliquot 2',
      projectSlug: 'project-slug-1',
      reference: 'CONCEPT2',
      slug: 'aliquot-slug-1',
      type: 'PROTEIN'
    }
  ]
  const SEARCH = [
    {
      id: 3,
      label: 'Aliquot 3',
      projectSlug: 'project-slug-1',
      reference: 'S-333',
      slug: 'aliquot-slug-3',
      type: 'PROTEIN'
    }
  ]

  let vm = null

  beforeEach(() => {
    moxios.install(HTTP)
    moxios.stubRequest(process.env.API_URL + 'project/1/aliquots?query=', {
      status: 200,
      response: ALIQUOTS
    })
    moxios.stubRequest(process.env.API_URL + 'project/1/aliquots?query=test', {
      status: 200,
      response: SEARCH
    })

    const Constructor = Vue.extend(AliquotSelector)
    vm = new Constructor()
  })

  afterEach(() => {
    moxios.uninstall()
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should download and populate the list of aliquots', done => {
    vm.projectId = 1
    vm = vm.$mount()
    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      expect([...vm.$el.querySelectorAll('.em-aliquot-selector__item-title')]
        .length).to.be.eql(2)
      expect([...vm.$el.querySelectorAll('.em-aliquot-selector__item-title')]
        .map(x => x.textContent.trim())).to.be.eql(['Aliquot 1', 'Aliquot 2'])
    }).then(done, done)
  })

  it('should download and populate the filtered list of aliquots ', done => {
    vm.projectId = 1
    vm = vm.$mount()
    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      expect([...vm.$el.querySelectorAll('.em-aliquot-selector__item-title')]
        .length).to.be.eql(2)
      expect([...vm.$el.querySelectorAll('.em-aliquot-selector__item-title')]
        .map(x => x.textContent.trim())).to.be.eql(['Aliquot 1', 'Aliquot 2'])

      vm.fetchAliquots('test')
      return new Promise((resolve, reject) => moxios.wait(resolve))
    }).then(() => {
      expect([...vm.$el.querySelectorAll('.em-aliquot-selector__item-title')]
        .length).to.be.eql(1)
      expect([...vm.$el.querySelectorAll('.em-aliquot-selector__item-title')]
        .map(x => x.textContent.trim())).to.be.eql(['Aliquot 3'])
    }).then(done, done)
  })

})
