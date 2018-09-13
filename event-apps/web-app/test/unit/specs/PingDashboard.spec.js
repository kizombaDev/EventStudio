import Vue from 'vue'
import PingDashboard from '@/components/ping/PingDashboard'

describe('PingDashboard.vue', () => {
  it('should render correct contents', () => {
    const Constructor = Vue.extend(PingDashboard)
    const vm = new Constructor().$mount()
    expect(vm.$el.querySelector('h1').textContent)
      .to.equal('Ping Dashboard')
  })
})
