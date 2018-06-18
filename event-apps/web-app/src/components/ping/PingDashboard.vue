<template>
  <div class="row">
    <div class="col-12 mb-4">
      <h1>Ping Dashboard</h1>
    </div>
    <div v-for="(pingId, index) in pingIds" v-bind:key="index" class="col-md-3 mb-4">
      <PingCard v-bind:pingId="pingId"></PingCard>
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
      pingIds: []
    }
  },
  created: function () {
    this.loadAllPingIds()
  },
  methods: {
    loadAllPingIds () {
      basicApi.getPingIds().then(response => {
        response.data.forEach(item => {
          if (!this.pingIds.includes(item.key)) {
            this.pingIds.push(item.key)
          }
        })
      }).catch(e => {
        this.$events.emit('error', e)
      })
    }
  }
}
</script>
