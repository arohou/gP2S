import comments from '@/components/App/Comment/Comments'

describe('Comments', () => {

  it('should indicate that comment was edited', () => {
    //given
    const updatedComment = {
      id: 1,
      content: 'x',
      createdDate: 1521105969000,
      modifiedDate: 1521105969001
    }

    //when
    const result = comments.wasEdited(updatedComment)

    //then
    expect(result)
      .to
      .be
      .true
  })

  it('should indicate that comment is brand new - never edited', () => {
    //given
    const updatedComment = {
      id: 1,
      content: 'x',
      createdDate: 1521105969000,
      modifiedDate: 1521105969000
    }

    //when
    const result = comments.wasEdited(updatedComment)

    //then
    expect(result)
      .to
      .be
      .false
  })

  it('should fail when getting undefined comment ID', (done) => {
    //given
    const commentsList = [
      {
        id: 1,
        content: 'a'
      },
      {
        id: 2,
        content: 'b'
      }
    ]
    const badComment = {
      id: undefined,
      content: 'x'
    }

    //when
    try {
      comments.replaceUpdatedComment(commentsList, badComment)
      //then
      done('Failed to trow error!')
    } catch (e) {
      done()
    }
  })

  it('should fail when getting undefined comment', (done) => {
    //given
    const commentsList = [
      {
        id: 1,
        content: 'a'
      },
      {
        id: 2,
        content: 'b'
      }
    ]

    //when
    try {
      comments.replaceUpdatedComment(commentsList, undefined)
      //then
      done('Failed to trow error!')
    } catch (e) {
      done()
    }
  })

  it('should replace in comments array existing comment', () => {
    //given
    const commentsList = [
      {
        id: 1,
        content: 'a'
      },
      {
        id: 2,
        content: 'b'
      }
    ]
    const updatedComment = {
      id: 1,
      content: 'x'
    }

    //when
    const result = comments.replaceUpdatedComment(commentsList, updatedComment)

    //then
    expect(result)
      .to
      .have
      .lengthOf(2)

    expect(result[0])
      .to
      .be
      .eql(updatedComment)
  })

  it('should not replace in comments array not existing comment', () => {
    //given
    const commentsList = [
      {
        id: 1,
        content: 'a'
      },
      {
        id: 2,
        content: 'b'
      }
    ]
    const updatedComment = {
      id: 3,
      content: 'x'
    }

    //when
    const result = comments.replaceUpdatedComment(commentsList, updatedComment)

    //then
    expect(result)
      .to
      .have
      .lengthOf(2)

    expect(result)
      .not
      .to
      .contain(updatedComment)
  })

  it('should preserve comments order', () => {
    //given
    const commentsList = [
      {
        id: 1,
        content: 'a'
      },
      {
        id: 2,
        content: 'b'
      }
    ]
    const updatedComment = {
      id: 1,
      content: 'x'
    }

    //when
    const result = comments.replaceUpdatedComment(commentsList, updatedComment)

    //then
    expect(result[0])
      .to
      .be
      .eql(updatedComment)
  })

})
