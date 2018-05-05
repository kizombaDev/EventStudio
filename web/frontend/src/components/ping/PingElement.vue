<template>
  <div>
    <h1>Ping</h1>
    <p><strong>{{ ping.key}}</strong> Status: {{ ping.source.status }}</p>
  </div>
</template>

<script>
export default {
  name: 'PingElement',
  props: {
    ping: []
  }
}
</script>

<style scoped>
</style>
