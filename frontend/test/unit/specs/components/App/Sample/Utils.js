/**
 * Check the samples' components table
 * @param vm The controller to check
 * @param expectedComponents A list of fields to expect
 */
export function checkComponents (vm, expectedComponents, header) {
  // Title
  vm.$el.querySelector('.sample-base-form__components .el-form-item__label').textContent
    .should.be.equal('Components')

  // Component rows
  const components = [...vm.$el.querySelectorAll('.sample-base-form__components__component-row.el-row')]
  components.should.have.lengthOf(expectedComponents.length)

  for (let i = 0; i < expectedComponents.length; ++i) {
    const expectedRow = expectedComponents[i]
    const componentRow = components[i]

    if(header) {
      Array.from(componentRow.querySelectorAll('.el-form-item__label'))
           .map(label => label.textContent.trim())
           .should.be.deep.equal([header, 'Final conc. (μM)', 'Used (μL)'])
    }

    // First two fields should be required
    componentRow.querySelector('.sample-base-form__components__component')
      .should.have.class('is-required')
    componentRow.querySelector('.sample-base-form__components__concentration')
      .should.have.class('is-required')

    // There's a last drop checkbox unchecked by default
    componentRow.querySelector('.el-checkbox__input')
      .should.not.have.class('is-checked')
    componentRow.querySelector('.el-checkbox__label')
      .textContent.trim().should.be.equal('last drop')

    // There's a remove danger button
    const buttons = componentRow.querySelectorAll('.el-button')
    buttons[buttons.length - 1].should.have.class('el-button--danger')
    buttons[buttons.length - 1].textContent.should.be.equal('Remove')

    // Compare input values with the expected list
    Array.from(componentRow.querySelectorAll('input[type=text]'))
      .map(input => input.textContent)
      .should.be.deep.equal(expectedRow)
  }
}
