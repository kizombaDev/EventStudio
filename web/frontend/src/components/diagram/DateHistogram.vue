<template>
  <div>
    <h1>Date Histogram</h1>
    <b-button :pressed.sync="toggleButtonState" variant="primary">{{toggleButtonState ? 'Show results' : 'Edit' }}</b-button>
    <div class="pb-4" />
    <div v-if="showEdit">
      <div v-if="this.filters.length > 0">
        <h3>The current filter criteria</h3>
        <b-table responsive striped hover :items="filters" :fields="fields">
          <template slot="action" slot-scope="row">
            <b-button size="sm" @click="deleteFilterCriteria(row.item)">
              Delete
            </b-button>
          </template>
        </b-table>
        <div class="pb-4" />
      </div>
      <h3>Add new filter criteria</h3>
      <DateHistogramForm :show="showEdit" v-on:filterAdded="filterAdded"/>
    </div>
    <LineDiagram v-if="showDiagram"
                  ref="diagram"
                 :lineDiagramData="lineDiagramData"
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
      lineDiagramData: {
        labels: [],
        primaryData: [],
        secondaryData: [],
        primaryLabel: 'Primary Filter',
        secondaryLabel: 'Secondary Filter'
      },
      filters: [
        { 'field': 'type', 'value': 'ping', 'type': 'primary' },
        { 'field': 'id', 'value': 'ping_localhost', 'type': 'primary' },
        { 'field': 'status', 'value': 'failed', 'type': 'secondary' }
      ],
      fields: [ 'field', 'value', 'type', 'action' ],
      toggleButtonState: false
    }
  },
  computed: {
    showEdit () {
      return this.toggleButtonState
    },
    showDiagram () {
      return !this.toggleButtonState
    }
  },
  created () {
    this.loadData(this.filters)
  },
  watch: {
    showDiagram: function (value) {
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
    },
    filterAdded (filter) {
      this.filters.push(filter)
    },
    deleteFilterCriteria (filter) {
      this.filters.splice(this.filters.indexOf(filter), 1)
    }
  }
}
</script>
