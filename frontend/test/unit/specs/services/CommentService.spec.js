import CommentService from '@/services/CommentService'
import { HTTP } from '@/utils/http-common'

import sinon from 'sinon'

const sandbox = sinon.sandbox.create()

describe('CommentService', () => {
  let vm = null

  beforeEach(() => {
    vm = new CommentService()
  })

  afterEach(() => {
    sandbox.restore()
    vm = null
  })

  it('should GET comment/ on listComments', () => {
    // given
    const spy = sandbox.stub(HTTP, 'get')
    const expected = 1337

    // when
    vm.listComments(expected, expected)

    // then
    expect(spy).to.have.been.calledWith('comment/' + expected + '/' + expected)
  })

  it('should GET comment/../count on countComments', () => {
    // given
    const spy = sandbox.stub(HTTP, 'get')
    const expected = 1337

    // when
    vm.countComments(expected, expected)

    // then
    expect(spy).to.have.been.calledWith(
      'comment/' + expected + '/' + expected + '/count')
  })

  it('should POST comment/ on listComments', () => {
    // given
    const spy = sandbox.stub(HTTP, 'post')
    const expected = 1337

    // when
    vm.addComment(expected, expected, expected)

    // then
    expect(spy).to.have.been.calledWith(
      'comment/' + expected + '/' + expected, expected)
  })

  it('should DELETE comment/ on deleteComment', () => {
    // given
    const expected = 1337

    // when
    vm.deleteComment(expected)

    // then
    HTTP.delete.should.have.been.calledWith('comment/' + expected)
  })

  describe('updateComment', () => {
    it('should PUT comment/ on updateComment', () => {
      // given
      const spy = sandbox.stub(HTTP, 'put')
      const expected = 1337

      // when
      vm.updateComment({
          id: expected,
          content: expected
        },
        false)

      // then
      expect(spy).to.have.been.calledWith('comment/' + expected)
    })

    it('should add modified=true query parameter if attachmentsModified is true', () => {
      // given
      const spy = sandbox.stub(HTTP, 'put')
      const expected = 1337

      // when
      vm.updateComment({
          id: expected,
          content: expected
        },
        true)

      // then
      expect(spy).to.have.been.calledWith('comment/' + expected + '?modified=true')
    })
  })

})
