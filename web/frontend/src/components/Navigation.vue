<template>
  <b-navbar toggleable="md" type="dark" variant="primary">

    <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

    <b-navbar-brand href="#">NavBar</b-navbar-brand>

    <b-collapse is-nav id="nav_collapse">

      <b-navbar-nav>
        <b-nav-item :to="{ name: 'home' }">Home</b-nav-item>
        <b-nav-item :to="{ name: 'pingDashboard' }">Ping Dashboard</b-nav-item>
        <b-nav-item-dropdown text="Browse Logs" right >
          <b-dropdown-item v-for="(type, index) in types" v-bind:key="index" :to="{ name: 'browse', params: { type: type } }" >{{ type }}</b-dropdown-item>
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
      this.types = basicApi.getAllTypes()
    }
  }
}
</script>
