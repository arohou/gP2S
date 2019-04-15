import SettingsService from '@/services/SettingsService'
import { HTTP } from '@/utils/http-common'

import sinon from 'sinon'

const sandbox = sinon.sandbox.create()

describe('SettingsService', () => {
  let vm = null

  beforeEach(() => {
    vm = new SettingsService()
  })

  afterEach(() => {
    sandbox.reset()
    vm = null
  })

  it('should GET /settings on get', () => {
    // given
    const spy = sandbox.spy(HTTP, 'get')

    // when
    vm.get()

    // then
    expect(spy).to.have.been.calledWith('settings/')
  })

  it('should PUT /settings with entity on save', () => {
    // given
    const expected = 1337
    const spy = sandbox.spy(HTTP, 'put')

    // when
    vm.save(expected)

    // then
    expect(spy).to.have.been.calledWith('settings/', expected)
  })

})
