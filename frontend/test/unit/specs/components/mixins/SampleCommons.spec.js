import SampleCommons from '@/components/mixins/SampleCommons'

describe('SampleCommons', () => {

  it('should match sample ID', () => {
    //given
    const sample = {
      id: 123
    }
    const slugOrId = 123

    //when
    const result = SampleCommons.methods.doesSampleHaveSlugOrId(slugOrId, sample)

    //then
    expect(result)
      .to.be.true
  })

  it('should match sample slug', () => {
    //given
    const sample = {
      slug: "abc"
    }
    const slugOrId = "abc"

    //when
    const result = SampleCommons.methods.doesSampleHaveSlugOrId(slugOrId, sample)

    //then
    expect(result)
      .to.be.true
  })

  it('should not match sample', () => {
    //given
    const sample = {
      id: 123,
      slug: "abc"
    }
    const slugOrId = "xyz"

    //when
    const result = SampleCommons.methods.doesSampleHaveSlugOrId(slugOrId, sample)

    //then
    expect(result)
      .to.be.false
  })

  it('should not find sample in empty array', () => {
    //given
    const samples = []
    const slugOrId = "xyz"

    //when
    const result = SampleCommons.methods.findSampleInArray(slugOrId, samples)

    //then
    expect(result)
      .to.be.falsy
  })

  it('should not find sample', () => {
    //given
    const samples = [
      {
        id: 123,
        slug: "abc"
      },
      {
        id: 124,
        slug: "abd"
      }]
    const slugOrId = "syz"

    //when
    const result = SampleCommons.methods.findSampleInArray(slugOrId, samples)

    //then
    expect(result)
      .to.be.falsy
  })

  it('should find sample', () => {
    //given
    const sample1 = {
      id: 123,
      slug: "abc"
    }
    const sample2 = {
      id: 124,
      slug: "abd"
    }
    const samples = [sample1, sample2]
    const slugOrId = "abd"

    //when
    const result = SampleCommons.methods.findSampleInArray(slugOrId, samples)

    //then
    expect(result)
      .to.equal(sample2)
  })

})
