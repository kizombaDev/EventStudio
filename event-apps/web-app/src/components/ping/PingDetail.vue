<template>
  <div>
    <h1>Ping Details</h1>
    <div v-show="ping !== null">
      <b-table striped hover :items="pingKeyValues"></b-table>
    </div>
  </div>
</template>

<script>
import basicApi from '../../rest/basic-api'

export default {
  name: 'Ping',
  data () {
    return {
      sourceId: this.$route.params.source_id,
      ping: null
    }
  },
  computed: {
    pingKeyValues: function () {
      if (this.ping === null || this.ping === undefined) {
        return null
      }
      var result = []
      for (var key in this.ping) {
        result.push({'key': key, 'value': this.ping[key]})
      }
      return result
    }
  },
  beforeMount () {
    this.loadLastPing(this.sourceId)
  },
  methods: {
    loadLastPing (sourceId) {
      basicApi.getLastPingBySourceId(sourceId)
        .then(response => {
          if (response.data.length > 0) {
            this.ping = response.data[0]
          }
        })
        .catch(e => {
          this.$events.emit('error', e)
        })
    }
  }
}
</script>
