<template>
  <div>
    <h1>Browse {{ type }} Logs</h1>
  </div>
</template>

<script>
export default {
  name: 'Home',
  // data () {
  //   return {
  //     test: this.$route.params.type
  //   }
  // },
  computed: {
    type: function () {
      return this.$route.params.type
    }
  }
}
</script>
