<template>
  <div class="hello">
    <h1>Dashboard</h1>
    <button @click="callRestService()">Run!!!!</button>
    <p>{{ response}}</p>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
  name: 'Dashboard',
  data () {
    return {
      response: [],
      errors: []
    }
  },
  methods: {
    callRestService () {
      axios.get('api/v1/logs/ping_google/last')
        .then(response => {
          this.response = response.data
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
