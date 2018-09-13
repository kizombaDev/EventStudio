import Vue from 'vue'
import Router from 'vue-router'
import PingDashboard from '@/components/ping/PingDashboard'
import PingDetail from '@/components/ping/PingDetail'
import DateDiagram from '@/components/diagram/DateDiagram'
import TermDiagram from '@/components/diagram/TermDiagram'
import Search from '@/components/search/Search'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: { name: 'pingDashboard' }
    },
    {
      path: '/ping/dashboard',
      name: 'pingDashboard',
      component: PingDashboard
    },
    {
      path: '/search/:type',
      name: 'search',
      component: Search
    },
    {
      path: '/ping/:source_id/detail',
      name: 'pingDetail',
      component: PingDetail
    },
    {
      path: '/date-diagram',
      name: 'dateDiagram',
      component: DateDiagram
    },
    {
      path: '/term-diagram',
      name: 'termDiagram',
      component: TermDiagram
    }
  ]
})
