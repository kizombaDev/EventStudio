<template>
  <b-card :border-variant="borderVariant"
          :header="pingId"
          class="text-center">
    <p class="card-text">Status: {{status}}<br>Time: {{ time }}<br>Date: {{date}}</p>
    <b-button :to="{ name: 'pingDetail', params:  { id: pingId }}" variant="primary">Details</b-button>
  </b-card>
</template>

<script>
import basicApi from '../../rest/basic-api'
import moment from 'moment'

export default {
  name: 'PingCard',
  data () {
    return {
      ping: null
    }
  },
  props: {
    pingId: String
  },
  computed: {
    borderVariant: function () {
      if (this.ping === null) {
        return ''
      }

      if (this.ping.status === 'ok') {
        return 'success'
      } else if (this.ping.status === 'failed') {
        return 'danger'
      } else {
        return 'warning'
      }
    },
    status: function () {
      if (this.ping === null) {
        return 'loading...'
      }
      if (this.ping.status === undefined) {
        return 'undefined'
      }
      return this.ping.status
    },
    time: function () {
      if (this.ping === null) {
        return 'loading...'
      }
      if (this.ping.timestamp === undefined) {
        return 'undefined'
      }
      return moment(this.ping.timestamp).format('HH:mm:ss')
    },
    date: function () {
      if (this.ping === null) {
        return 'loading...'
      }
      if (this.ping.timestamp === undefined) {
        return 'undefined'
      }
      return moment(this.ping.timestamp).format('DD.MM.YYYY')
    }
  },
  created () {
    this.loadLastLogById(this.pingId)
  },
  methods: {
    loadLastLogById (id) {
      basicApi.getLastPingById(id).then(response => {
        this.ping = response.data
      }).catch(e => {
        console.error(e)
      })
    }
  }
}
</script>
