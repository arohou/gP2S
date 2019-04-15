import '@/global-imports'

// This gets replaced by karma webpack with the updated files on rebuild
let __karmaWebpackManifest__ = []

// require all test files (files that ends with .spec.js)
const testsContext = require.context('./specs', true, /\.spec$/)

function inManifest (path) {
  return __karmaWebpackManifest__.indexOf(path) >= 0
}

let runnable = testsContext.keys().filter(inManifest)
// Run all tests if we didn't find any changes
if (!runnable.length) {
  runnable = testsContext.keys()
}
runnable.forEach(testsContext)

// require all src files except main.js for coverage.
// you can also change this to match only the subset of files that
// you want coverage for.
const srcContext = require.context('../../src', true, /^\.\/(?!main(\.js)?$)/)
srcContext.keys().forEach(srcContext)
