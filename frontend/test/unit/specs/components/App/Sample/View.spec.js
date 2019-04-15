import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'
import moment from 'moment'

import View from '@/components/App/Sample/View'
import VueRouter from 'vue-router'
import { HTTP } from '@/utils/http-common'

const sandbox = sinon.sandbox.create()

const router = new VueRouter({
  routes: [
    {name: 'sample-view', path: ''},
    {name: 'protein-view', path: ''},
    {name: 'ligand-view', path: ''},
    {name: 'sample-copy', path: ''}
  ]
})

describe('Sample/View.vue', () => {
  let vm = null

  const LIGAND_COMPONENTS = [
    {
      'id': 1,
      'finalConcentration': 4,
      'finalDrop': null,
      'aliquot': {
        'id': 1,
        'slug': 'ligand-no-1',
        'label': 'Ligand no.1',
        'tubeLabel': 'tube no.1',
        'concentration': 1.337,
        'conceptId': 'CONCEPT5',
        'solvent': 'solvent no.1',
        'createdDate': moment('2017-02-13 16:45:23')
      }
    }]

  const PROTEIN_COMPONENTS = [{
    'id': 1,
    'finalConcentration': 1.3,
    'finalDrop': null,
    'aliquot': {
      'id': 1,
      'slug': 'protein-no-1',
      'label': 'PROTEIN no.1',
      'tubeLabel': 'tube no.1',
      'concentration': 1.337,
      'purificationId': 'PUR1AH1',
      'createdDate': moment('2016-11-05 08:05:01')
    }
  }]

  const DATA = {
    projectSlugOrId: 'project-1',
    sample: {
      id: 1,
      slug: 'sample-label',
      label: 'Sample Label',
      incubationTime: 13,
      incubationTemperature: 137,
      availableForGridMaking: true,
      otherBufferComponents: 'other components',
      protocolDescription: 'protocol description',
      ligandComponent: LIGAND_COMPONENTS,
      proteinComponent: PROTEIN_COMPONENTS
    }
  }
  const LIGAND_COMPONENTS_EMPTY = [{
    'id': 2,
    'finalConcentration': null,
    'finalDrop': null,
    'aliquot': {
      'id': 2,
      'slug': 'ligand-empty',
      'label': 'Ligand Empty',
      'tubeLabel': 'tube no.1',
      'concentration': null,
      'conceptId': null,
      'solvent': ''
    }
  }]

  const PROTEIN_COMPONENTS_EMPTY = [{
    'id': 2,
    'finalConcentration': null,
    'finalDrop': null,
    'aliquot': {
      'id': 2,
      'slug': 'protein-empty',
      'label': 'PROTEIN Empty',
      'tubeLabel': 'tube no.1',
      'concentration': null,
      'purificationId': null
    }
  }]

  const DATA_EMPTY = {
    projectSlugOrId: 'project-2',
    sample: {
      id: 2,
      slug: 'sample-empty',
      label: 'Sample Empty',
      incubationTime: 13,
      incubationTemperature: 137,
      otherBufferComponents: 'other components',
      ligandComponent: LIGAND_COMPONENTS_EMPTY,
      proteinComponent: PROTEIN_COMPONENTS_EMPTY
    }
  }

  const PROJECTS = [
    {
      id: 1,
      slug: 'project-1',
      label: 'Project 1',
      samples: [DATA.sample]
    },
    {id: 2, label: 'Project 2', slug: 'project-2', samples: [DATA_EMPTY.sample]},
    {id: 3, label: 'Project 3', slug: 'project-3'}
  ]

  beforeEach(() => {
    sandbox.stub(router, 'push')
    const Constructor = Vue.extend(View)
    vm = new Constructor({router})

    moxios.install(HTTP)
    moxios.stubRequest(process.env.API_URL + 'sample/' + DATA.sample.slug, {
      status: 200,
      response: DATA.sample
    })
    moxios.stubRequest(process.env.API_URL + 'sample/' + DATA_EMPTY.sample.slug, {
      status: 200,
      response: DATA_EMPTY.sample
    })
    moxios.stubRequest(process.env.API_URL + 'sample/' + DATA.sample.slug + '/projects', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'sample/' + DATA_EMPTY.sample.slug + '/projects', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'project/', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'sample/', {
      status: 200,
      response: DATA.sample
    })

    vm.checkIsSampleView = function () {
      return true
    }
    // vm.$route.name = 'sample-view'

    // this.$route.name === 'sample-view' && (this.entity.proteinComponent || this.entity.proteinComponent)
  })

  afterEach(() => {
    moxios.uninstall()
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should download and show sample', done => {
    // Initial list of project is empty
    expect(vm.sample).to.be.eql({
      label: null,
      incubationTime: null,
      incubationTemperature: null,
      availableForGridMaking: true,
      otherBufferComponents: null,
      protocolDescription: null,
      ligandComponent: [],
      proteinComponent: [],
      components: []
    })

    vm.id = DATA.sample.slug
    vm = vm.$mount()
    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      expect(vm.sample).to.be.eql(DATA.sample)
      expect(vm.$el.querySelector('.view-header__content__title').textContent.trim())
        .to.be.eql(DATA.sample.label)
    }).then(done, done)
  })

  it('should display the edit button', () => {
    vm = vm.$mount()

    expect(vm.$el.querySelector('#action-buttons__edit').title).to.be.eql('Edit')
  })

  it('should edit button switch to edit form', done => {
    vm.id = DATA.sample.slug
    vm.projectId = DATA.projectSlugOrId
    vm.$mount()
    Vue.nextTick().then(() => {
      vm.$el.querySelector('#action-buttons__edit').click()

      const expectedPush = {
        name: 'sample-edit',
        params: {
          id: DATA.sample.slug,
          projectId: DATA.projectSlugOrId
        }
      }

      expect(router.push).to.have.been.calledWith(expectedPush)
    }).then(done, done)
  })

  it('should go to copy screen on copy button click', done => {
    vm.id = DATA.sample.slug
    vm.projectId = DATA.projectSlugOrId
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve))
      .then(() => {
        vm.$el.querySelector('#action-buttons__copy').click()
        router.push.should.have.been.calledWith({
          name: 'sample-copy',
          params: {
            projectId: DATA.projectSlugOrId,
            id: DATA.sample.slug
          }
        })
      }).then(done, done)
  })

  it('displays component titles', done => {
    vm.id = DATA.sample.slug
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      const componentTitles = [...vm.$el.querySelectorAll('.list-view__link-label')]
      componentTitles.map(label => label.textContent.trim()).splice(0, 2)
        .should.be.deep.equal(
        [
          PROTEIN_COMPONENTS[0].aliquot.label,
          LIGAND_COMPONENTS[0].aliquot.label
        ])
    }).then(done, done)
  })

  it('displays component tag based on type', done => {
    vm.id = DATA.sample.slug
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      const componentTags = [...vm.$el.querySelectorAll('.component-tag-ligand')]
      componentTags.map(el => el.textContent)[0].should.be.equal('Ligand')
    }).then(done, done)
  })

  it('displays PROTEIN component labels', done => {
    vm.id = DATA.sample.slug
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      const proteinLabels = [...vm.$el.querySelectorAll('.view__components__protein .list-view__label')]
      proteinLabels.map(label => label.textContent)
        .should.be.deep.equal(['ID', 'Final concentration'])
    }).then(done, done)
  })

  it('displays default PROTEIN values', done => {
    vm.id = DATA_EMPTY.sample.slug
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      const proteinValues = [...vm.$el.querySelectorAll('.view__components__protein .list-view__value')]
      proteinValues.map(label => label.textContent.trim())
        .should.be.deep.equal(
        ['—', '—'])
    }).then(done, done)
  })

  it('displays default ligand values', done => {
    vm.id = DATA_EMPTY.sample.slug
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      const proteinValues = [...vm.$el.querySelectorAll('.view__components__ligand .list-view__value')]
      proteinValues.map(label => label.textContent.trim())
        .should.be.deep.equal(
        ['—', '—', '—', '—'])
    }).then(done, done)
  })

  it('displays PROTEIN component values', done => {
    vm.id = DATA.sample.slug
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      const proteinValues = [...vm.$el.querySelectorAll('.view__components__protein .list-view__value')]
      proteinValues.map(value => value.textContent.trim())
        .should.be.deep.equal(
        [
          PROTEIN_COMPONENTS[0].aliquot.purificationId,
          PROTEIN_COMPONENTS[0].finalConcentration + ' μM'
        ])
    }).then(done, done)
  })

  it('displays ligand component values', done => {
    vm.id = DATA.sample.slug
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      const gValues = [...vm.$el.querySelectorAll('.view__components__ligand .list-view__value')]

      gValues.map(value => value.textContent.trim())
        .should.be.deep.equal(
        [LIGAND_COMPONENTS[0].aliquot.conceptId,
          '—',
          LIGAND_COMPONENTS[0].finalConcentration + ' μM',
          LIGAND_COMPONENTS[0].aliquot.solvent
        ])
    }).then(done, done)
  })

  it('displays components metadata', done => {
    vm.id = DATA.sample.slug
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      const metadataValues = [...vm.$el.querySelectorAll('.list-view__list-row__right-pane')]
      metadataValues.map(value => value.textContent.trim())
        .should.be.deep.equal(
        [
          PROTEIN_COMPONENTS[0].aliquot.createdDate.format('YYYY.MM.DD') + ' ' +
          PROTEIN_COMPONENTS[0].aliquot.createdDate.format('HH:mm:ss') + ' GP2S User',
          LIGAND_COMPONENTS[0].aliquot.createdDate.format('YYYY.MM.DD') + ' ' +
          LIGAND_COMPONENTS[0].aliquot.createdDate.format('HH:mm:ss') + ' GP2S User'
        ])
    }).then(done, done)
  })

  it('displays component metadata labels', done => {
    vm.id = DATA.sample.slug
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      const labels = [...vm.$el.querySelectorAll('.view__metadata .view__metadata__label')]
      labels.map(label => label.textContent).splice(0, 4)
        .should.be.deep.equal(
        ['Incubation time', 'Incubation temperature', 'Buffer', 'Protocol description'])
    }).then(done, done)
  })

  it('displays component metadata default values', done => {
    vm = vm.$mount()

    Vue.nextTick().then(() => {
      const labels = [...vm.$el.querySelectorAll('.view__metadata .view__metadata__value')]
      labels.map(label => label.textContent.trim())
        .should.be.deep.equal(['—', '—', '—', '—'])
    }).then(done, done)
  })

  it('displays component metadata values', done => {
    vm.id = DATA.sample.slug
    vm = vm.$mount()

    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      const labels = [...vm.$el.querySelectorAll('.view__metadata .view__metadata__value')]
      labels.map(label => label.textContent.trim()).splice(0, 4)
        .should.be.deep.equal([
        DATA.sample.incubationTime.toString() + ' min',
        DATA.sample.incubationTemperature.toString() + ' °C',
        DATA.sample.otherBufferComponents,
        DATA.sample.protocolDescription
      ])
    }).then(done, done)
  })

})
