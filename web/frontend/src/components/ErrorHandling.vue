<template>
  <div>
    <b-alert show variant="danger" dismissible v-for="(error, index) in errors" :key="index">
      {{error.message}}
    </b-alert>
  </div>
</template>

<script>
export default {
  name: 'ErrorHandling',
  data () {
    return {
      errors: []
    }
  },
  watch: {
    '$route': 'clearErrors'
  },
  created () {
    console.log(this)
    this.$events.on('error', this.errorListener)
  },
  methods: {
    errorListener (e) {
      this.errors.push(e)
      console.error(e)
    },
    clearErrors () {
      this.errors = []
    }
  },
  beforeDestroy () {
    this.$events.off('error')
  }
}
</script>
