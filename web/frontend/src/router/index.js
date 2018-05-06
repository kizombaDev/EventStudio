import Vue from 'vue'
import Router from 'vue-router'
import PingDashboard from '@/components/ping/PingDashboard'
import PingDetail from '@/components/ping/PingDetail'
import PingDiagram from '@/components/ping/PingDiagram'
import Foo from '@/components/Foo'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/ping/dashboard',
      name: 'pingDashboard',
      component: PingDashboard
    },
    {
      path: '/foo',
      name: 'foo',
      component: Foo
    },
    {
      path: '/ping/:id/detail',
      name: 'pingDetail',
      component: PingDetail
    },
    {
      path: '/ping/:id/diagram',
      name: 'pingDiagram',
      component: PingDiagram
    }
  ]
})
