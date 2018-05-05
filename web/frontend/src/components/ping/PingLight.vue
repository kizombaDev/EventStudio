<template>
  <p><strong>{{ ping.key }}</strong> Status: {{ ping.source.status }} <router-link to="/ping/ping_google">Details</router-link></p>
</template>

<script>
export default {
  name: 'PingLight',
  props: {
    ping: []
  }
}
</script>

<style scoped>
</style>
