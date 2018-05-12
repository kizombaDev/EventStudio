<template>
  <div>
    <h1>Browse {{ type }} Logs</h1>
    <b-button :pressed.sync="showResults" variant="primary">{{showResults ? 'Edit' : 'Show results'}}</b-button>
    <div class="pb-4"></div>
    <FilterCriteria v-if="!showResults" :filters="filters"/>
    <b-table v-if="showResults" responsive striped hover :items="pings" :fields="fields">
      <template slot="show_details" slot-scope="row">
        <b-button size="sm" @click.stop="row.toggleDetails" class="mr-2">
          {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
        </b-button>
      </template>
      <template slot="row-details" slot-scope="row">
        <b-card>
          <b-row class="mb-2" v-for="(keyValue, index) in convertToPair(row.item)" v-bind:key="index">
            <b-col sm="3" class="text-sm-right"><b>{{keyValue.key}}:</b></b-col>
            <b-col>{{ keyValue.value }}</b-col>
          </b-row>
        </b-card>
      </template>
    </b-table>
  </div>
</template>

<script>
import FilterCriteria from './FilterCriteria'
import basicApi from '../../rest/basic-api'

export default {
  name: 'Browse',
  data: function () {
    return {
      filters: [
        { 'field': 'type', 'value': 'ping', 'type': 'primary' }
      ],
      showResults: true,
      pings: [],
      fields: ['id', 'type', 'timestamp', 'show_details']
    }
  },
  components: {FilterCriteria},
  computed: {
    type: function () {
      return this.$route.params.type
    }
  },
  created () {
    this.loadData()
  },
  watch: {
    '$route': 'fetchData2'
  },
  methods: {
    fetchData () {
      console.log('test')
    },
    fetchData2 () {
      console.log('fetchData2')
    },
    loadData () {
      basicApi.getLogsByFilter(this.filters, 100, 0).then(response => {
        this.pings = response.data
      }).catch(e => {
        console.error(e)
      })
    },
    convertToPair (ping) {
      console.log(ping)
      if (ping === null || ping === undefined) {
        return null
      }
      var result = []
      for (var key in ping) {
        if (key.startsWith('_')) {
          continue
        }
        result.push({'key': key, 'value': ping[key]})
      }
      return result
    }
  }
}
</script>
