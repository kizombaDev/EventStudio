<template>
  <div>
    <h1>Ping Diagram</h1>
    <p>
      <router-link :to="{ name: 'pingDetail', params: { id: id }}">Go to the details</router-link>
    </p>
    <p v-show="ping !== null">Diagram</p>
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
