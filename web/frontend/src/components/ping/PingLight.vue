<template>
  <p><strong>{{ ping.key }}</strong> Status: {{ pingStatus }} <router-link :to="{ name: 'pingDetail', params: { id: ping.key }}">Show</router-link>
  </p>
</template>

<script>
export default {
  name: 'PingLight',
  computed: {
    pingStatus: function () {
      if (this.ping !== null && this.ping.source !== null) {
        return this.ping.source.status
      } else {
        return 'unknown'
      }
    }
  },
  props: {
    ping: Object
  }
}
</script>

<style scoped>
</style>
