<template>
  <div class="row">
    <div class="col-12 mb-4">
      <h1>Ping Dashboard</h1>
    </div>
    <div v-for="(pingSourceId, index) in pingSourceIds" v-bind:key="index" class="col-md-3 mb-4">
      <PingCard v-bind:pingSourceId="pingSourceId"></PingCard>
    </div>
  </div>
</template>

<script>
import PingCard from './PingCard'
import basicApi from '../../rest/basic-api'

export default {
  name: 'PingDashboard',
  components: {
    PingCard
  },
  data () {
    return {
      pingSourceIds: []
    }
  },
  created: function () {
    this.loadAllPingIds()
  },
  methods: {
    loadAllPingIds () {
      basicApi.getPingSourceIds().then(response => {
        response.data.forEach(item => {
          if (!this.pingSourceIds.includes(item.key)) {
            this.pingSourceIds.push(item.key)
          }
        })
      }).catch(e => {
        this.$events.emit('error', e)
      })
    }
  }
}
</script>
