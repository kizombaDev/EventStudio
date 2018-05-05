<template>
  <div class="hello">
    <h1>Ping-Dashboard</h1>
      <div v-for="(ping, index) in pings" v-bind:key="index">
        <PingLight v-bind:ping="ping"></PingLight>
      </div>
  </div>
</template>

<script>
  import axios from 'axios'
  import PingLight from './PingLight'

  export default {
  name: 'PingDashboard',
  components: {
    PingLight
  },
  data () {
    return {
      pings: [],
      errors: []
    }
  },
  beforeMount () {
    this.loadAllPingTypes()
  },
  methods: {
    loadAllPingTypes () {
      axios.get('api/v1/logs?type=ping&group-by=id')
        .then(response => {
          response.data.forEach(ping => {
            if (this.pings.filter(e => e.key === ping.key).length === 0) {
              this.pings.push({
                'key': ping.key,
                'source': null
              });
              this.loadLastPing(ping.key)
            }
          })
        })
        .catch(e => {
          this.errors.push(e)
        })
    },
    loadLastPing (id) {
      axios.get('api/v1/logs/' + id + '/last')
        .then(response => {
          this.pings.filter(e => e.key === response.data.id).forEach(ping => {
            ping.source = response.data
          })
        })
        .catch(e => {
          this.errors.push(e)
        })
    }
  }
}
</script>

<style scoped>
</style>
