<template>
  <div>
    <h1>Browse events</h1>
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
import FilterCriteria from '../common/FilterCriteria'
import basicApi from '../../rest/basic-api'

export default {
  name: 'Browse',
  data: function () {
    return {
      filters: [],
      showResults: true,
      pings: [],
      fields: ['source_id', 'type', 'timestamp', 'show_details']
    }
  },
  components: {FilterCriteria},
  created () {
    this.initDefaultFilter()
    this.loadData()
  },
  watch: {
    '$route': 'initDefaultFilter',
    showResults: function (value) {
      if (value) {
        this.loadData()
      }
    }
  },
  methods: {
    initDefaultFilter () {
      let type = this.$route.params.type
      if (type === 'all') {
        this.filters = []
      } else {
        this.filters = [
          { field: 'type', value: type, type: 'primary', operator: 'equals' }
        ]
      }
      this.loadData()
    },
    loadData () {
      basicApi.getLogsByFilter(this.filters, 100, 0).then(response => {
        this.pings = response.data
      }).catch(e => {
        this.$events.emit('error', e)
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
