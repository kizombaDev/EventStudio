<template>
  <div>
    <h1>Date Histogram</h1>
    <p>{{filters}}</p>
    <DateHistogramForm :show="true" v-on:filterAdded="filterAdded"/>
    <b-button @click="updateDiagram">Show diagram</b-button>
    <LineDiagram ref="diagram"
                 :labels="labels"
                 :primaryData="primaryData" :secondaryData="secondaryData"
                 :primaryLabel="primaryLabel" :secondaryLabel="secondaryLabel"
    />
  </div>
</template>

<script>
import LineDiagram from './LineDiagram'
import BarExample from './BarExample'
import DateHistogramForm from './DateHistogramForm'
import basicApi from '../../rest/basic-api'

export default {
  name: 'DateHistogram',
  components: {
    LineDiagram,
    BarExample,
    DateHistogramForm
  },
  data () {
    return {
      labels: [],
      primaryData: [],
      secondaryData: [],
      primaryLabel: 'Primary Filter',
      secondaryLabel: 'Secondary Filter',
      filters: [
        { 'field': 'type', 'value': 'ping', 'type': 'primary' },
        { 'field': 'id', 'value': 'ping_localhost', 'type': 'primary' },
        { 'field': 'status', 'value': 'failed', 'type': 'secondary' }
      ]
    }
  },
  methods: {
    loadData (filters) {
      basicApi.getDateHistogram(filters).then(response => {
        this.labels = []
        this.primaryData = []
        this.secondaryData = []
        response.data.forEach(item => {
          this.labels.push(item.key)
          this.primaryData.push(item.primary_count)
          this.secondaryData.push(item.secondary_count)
        })
        this.$refs.diagram.doRenderChart()
      }).catch(e => {
        console.error(e)
      })
    },
    filterAdded (filter) {
      this.filters.push(filter)
    },
    updateDiagram () {
      this.loadData(this.filters)
    }
  }
}
</script>
