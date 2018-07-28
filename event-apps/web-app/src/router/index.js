import Vue from 'vue'
import Router from 'vue-router'
import PingDashboard from '@/components/ping/PingDashboard'
import PingDetail from '@/components/ping/PingDetail'
import Home from '@/components/Home'
import DateHistogram from '@/components/diagram/DateHistogram'
import TermDiagram from '@/components/diagram/TermDiagram'
import Browse from '@/components/browse/Browse'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/ping/dashboard',
      name: 'pingDashboard',
      component: PingDashboard
    },
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/browse/:type',
      name: 'browse',
      component: Browse
    },
    {
      path: '/ping/:id/detail',
      name: 'pingDetail',
      component: PingDetail
    },
    {
      path: '/date-histogram',
      name: 'dateHistogram',
      component: DateHistogram
    },
    {
      path: '/term-diagram',
      name: 'termDiagram',
      component: TermDiagram
    }
  ]
})
