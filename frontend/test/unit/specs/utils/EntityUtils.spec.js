import { isEntityCreated } from '@/utils/EntityUtils'

describe('utils/EntityUtils.js', () => {
  describe('isEntityCreated', () => {
    it('should return false for null', () => {
      expect(isEntityCreated(null)).to.be.false
    })

    it('should return false for undefined', () => {
      expect(isEntityCreated(null)).to.be.false
    })

    it('should return false when no id in entity', () => {
      // given
      const entity = {createdDate: 1337}

      // then
      expect(isEntityCreated(entity)).to.be.false
    })

    it('should return false when no createdDate in entity', () => {
      // given
      const entity = {id: 1337}

      // then
      expect(isEntityCreated(entity)).to.be.false
    })

    it('should return true when both id and createdDate exist in entity', () => {
      // given
      const entity = {
        id: 1337,
        createdDate: 1337
      }

      // then
      expect(isEntityCreated(entity)).to.be.true
    })
  })
})
