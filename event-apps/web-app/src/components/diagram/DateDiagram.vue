<template>
  <div>
    <h1>Date Diagram</h1>
    <b-button :pressed.sync="showResults" variant="primary">{{showResults ?  'Edit' : 'Show results'}}</b-button>
    <div class="pb-4" />
    <FilterCriteria v-if="!showResults" :filters="filters" :choose-types="true"/>
    <LineDiagram v-if="showResults" ref="diagram" :lineDiagramData="lineDiagramData"
    />
  </div>
</template>

<script>
import LineDiagram from './LineDiagram'
import BarExample from './BarDiagram'
import FilterCriteria from '../common/FilterCriteria'
import basicApi from '../../rest/basic-api'

export default {
  name: 'DateDiagram',
  components: {
    LineDiagram,
    BarExample,
    FilterCriteria
  },
  data () {
    return {
      lineDiagramData: {
        labels: [],
        primaryData: [],
        secondaryData: [],
        primaryLabel: 'Primary Filter',
        secondaryLabel: 'Secondary Filter'
      },
      filters: [],
      showResults: true
    }
  },
  created () {
    this.loadData(this.filters)
  },
  watch: {
    showResults: function (value) {
      if (value) {
        this.loadData(this.filters)
      }
    }
  },
  methods: {
    loadData (filters) {
      basicApi.getDateHistogram(filters).then(response => {
        this.lineDiagramData.labels = []
        this.lineDiagramData.primaryData = []
        this.lineDiagramData.secondaryData = []
        response.data.forEach(item => {
          this.lineDiagramData.labels.push(item.key)
          this.lineDiagramData.primaryData.push(item.primary_count)
          this.lineDiagramData.secondaryData.push(item.secondary_count)
        })
        this.$refs.diagram.doRenderChart()
      }).catch(e => {
        this.$events.emit('error', e)
      })
    }
  }
}
</script>
