import Vue from 'vue'
import Foo from '@/components/Foo'

describe('Foo.vue', () => {
  it('should render correct contents', () => {
    const Constructor = Vue.extend(Foo)
    const vm = new Constructor().$mount()
    expect(vm.$el.querySelector('h1').textContent)
      .to.equal('Foo AAA')
  })
})
