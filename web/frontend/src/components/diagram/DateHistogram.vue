<template>
  <div>
    <h1>Date Histogram</h1>
    <line-example :labels="labels" :primaryData="primaryData" :secondaryData="secondaryData" :primaryLabel="primaryLabel" :secondaryLabel="secondaryLabel"></line-example>
  </div>
</template>

<script>
import LineExample from './LineExample'
import BarExample from './BarExample'
import basicApi from '../../rest/basic-api'

export default {
  name: 'DateHistogram',
  components: {
    LineExample,
    BarExample
  },
  data () {
    return {
      labels: [],
      primaryData: [],
      secondaryData: [],
      primaryLabel: 'Primary Filter',
      secondaryLabel: 'Secondary Filter'
    }
  },
  created () {
    this.loadData()
  },
  methods: {
    loadData () {
      basicApi.getDateHistogram().then(response => {
        response.data.forEach(item => {
          this.labels.push(item.key)
          this.primaryData.push(item.primary_count)
          this.secondaryData.push(item.secondary_count)
        })
      }).catch(e => {
        console.error(e)
      })
    }
  }
}
</script>
