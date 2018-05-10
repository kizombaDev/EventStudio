<template>
  <div>
    <h1>Ping Detail</h1>
    <p>
      <router-link :to="{ name: 'pingDetail', params: { id: id } }">Details</router-link>
      <router-link :to="{ name: 'pingDiagram', params: { id: id } }">Diagram</router-link>
    </p>
    <div v-show="ping !== null">
      <b-table striped hover :items="pingKeyValues"></b-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Ping',
  data () {
    return {
      id: this.$route.params.id,
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
    this.loadLastPing(this.id)
  },
  methods: {
    loadLastPing (id) {
      axios.get('api/v1/logs/' + id + '/last')
        .then(response => {
          this.ping = response.data
        })
        .catch(e => {
          console.error(e)
        })
    }
  }
}
</script>
