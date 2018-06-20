import Vue from 'vue'
import moxios from 'moxios'
import shallowMount from '@vue/test-utils'
import PingDetail from '@/components/ping/PingDetail'

describe('PingDetail.vue', () => {
  beforeEach(() => {
    moxios.install()
  })

  afterEach(() => {
    moxios.uninstall()
  })

  it('should render the title correct', () => {
    const Constructor = Vue.extend(PingDetail)
    const vm = new Constructor().$mount()
    expect(vm.$el.querySelector('h1').textContent)
      .to.equal('Ping Detail')
  })

  // it('foo', () => {
  //   const $route = {params: {'id': 1}}
  //   shallowMount(PingDetail, {
  //     mocks: {
  //       $route
  //     }
  //   })
  //   // expect(wrapper.props().color).toBe('red')
  // })
})
