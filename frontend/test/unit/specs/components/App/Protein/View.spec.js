import Vue from 'vue'
import moxios from 'moxios'
import * as sinon from 'sinon'

import ProteinView from '@/components/App/Protein/registration_system_ext/View'
import VueRouter from 'vue-router'
import { HTTP } from '@/utils/http-common'

const sandbox = sinon.sandbox.create()

const router = new VueRouter()
describe('Protein/registration_system_ext/View.vue', () => {
  let vm = null

  const DATA = {
    projectSlugOrId: 'project-1',
    protein: {
      id: 1,
      slug: 'protein-label',
      label: 'Protein label',
      purificationId: '333',
      tubeLabel: 'T333',
      concentration: {
        isDilutedOrConcentrated: false,
        concentrationType: 'Concentration',
        dilutionFactor: null,
        concentrationFactor: null
      }
    }
  }

  const PROJECTS = [
    {id: 1, label: 'Project 1', proteins: [DATA.protein]},
    {id: 2, label: 'Project 2'},
    {id: 3, label: 'Project 3'}
  ]

  beforeEach(() => {
    sandbox.stub(router, 'push')
    const Constructor = Vue.extend(ProteinView)
    vm = new Constructor({router})

    moxios.install(HTTP)
    moxios.stubRequest(process.env.API_URL + 'protein/' + DATA.protein.slug, {
      status: 200,
      response: DATA.protein
    })
    moxios.stubRequest(process.env.API_URL + 'protein/' + DATA.protein.slug + '/projects', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'project/', {
      status: 200,
      response: PROJECTS
    })
    moxios.stubRequest(process.env.API_URL + 'protein/', {
      status: 200,
      response: DATA.protein
    })
    vm.id = DATA.protein.slug
    vm.projectId = DATA.projectSlugOrId
    vm = vm.$mount()
  })

  afterEach(() => {
    moxios.uninstall()
    sandbox.restore()
    vm.$destroy()
    vm = null
  })

  it('should download and show protein aliquot', done => {
    new Promise((resolve, reject) => moxios.wait(resolve)).then(() => {
      expect(vm.protein).to.be.eql(DATA.protein)
      expect(vm.$el.querySelector('.view-header__content__title').innerHTML).to.be.eql(DATA.protein.label)
      expect(vm.$el.querySelector('a#purificationId').textContent).to.be.eql(DATA.protein.purificationId)
      expect(vm.$el.querySelector('#protein-tube-labeling').innerHTML.trim().split(' ')[0]).to.be.eql(DATA.protein.tubeLabel)
      expect(vm.$el.querySelector('#concentration-factor-fallback').innerHTML.trim().split(' ')[0]).to.be.eql('1')
    }).then(done, done)
  })

  it('should display the edit button', () => {
    expect(vm.$el.querySelector('#action-buttons__edit').title).to.be.eql('Edit')
  })

  it('should edit button switch to edit form', done => {
    Vue.nextTick().then(() => {
      vm.$el.querySelector('#action-buttons__edit').click()

      const expectedPush = {
        name: 'protein-edit',
        params: {
          id: DATA.protein.slug,
          projectId: DATA.projectSlugOrId
        }
      }

      expect(router.push).to.have.been.calledOnce
      expect(router.push).to.have.been.calledWith(expectedPush)
    }).then(done, done)
  })
})
