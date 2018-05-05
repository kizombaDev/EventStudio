import Vue from 'vue'
import Router from 'vue-router'
import PingDashboard from '@/components/ping/PingDashboard'
import PingElement from '@/components/ping/PingElement'
import Foo from '@/components/Foo'

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/ping/dashboard',
      name: 'PingDashboard',
      component: PingDashboard
    },
    {
      path: '/foo',
      name: 'Foo',
      component: Foo
    },
    {
      path: '/ping/:id',
      name: 'PingElement',
      component: PingElement
    }
  ]
})
