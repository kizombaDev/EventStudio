<template>
  <b-navbar toggleable="md" type="dark" variant="primary">

    <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

    <b-navbar-brand href="#">Event-Studio</b-navbar-brand>

    <b-collapse is-nav id="nav_collapse">

      <b-navbar-nav>
        <b-nav-item :to="{ name: 'pingDashboard' }">Ping Dashboard</b-nav-item>
        <b-nav-item :to="{ name: 'dateDiagram' }">Date Diagram</b-nav-item>
        <b-nav-item :to="{ name: 'termDiagram' }">Term Diagram</b-nav-item>
        <b-nav-item-dropdown text="Search Events" right >
          <b-dropdown-item :to="{ name: 'search', params: { type: 'all' }}" >All</b-dropdown-item>
          <b-dropdown-divider></b-dropdown-divider>
          <b-dropdown-item v-for="(type, index) in types" v-bind:key="index" :to="{ name: 'search', params: { type: type } }" >{{ type }}</b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script>
import basicApi from '../rest/basic-api'

export default {
  name: 'Navigation',
  data () {
    return {
      types: []
    }
  },
  beforeMount () {
    this.loadNavigationData()
  },
  methods: {
    loadNavigationData () {
      basicApi.getAllTypes().then(response => {
        response.data.forEach(item => {
          this.types.push(item.key)
        })
      }).catch(e => {
        this.$events.emit('error', e)
      })
    }
  }
}
</script>
