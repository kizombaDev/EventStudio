<template>
  <div>
    <h1>Term Diagram</h1>
    <b-button :pressed.sync="showResults" variant="primary">{{showResults ?  'Edit basic filter' : 'Show results'}}</b-button>
    <div v-if="showResults" class="pb-4" />
    <b-form-group id="field"
                  label="Field name:"
                  label-for="fieldInput"
                  v-if="showResults">
      <b-form-select id="fieldInput"
                     :options="fieldOptions"
                     v-model="fieldName"
                     required>
      </b-form-select>
    </b-form-group>
    <b-form-group id="value"
                  label="Count:"
                  label-for="valueInput"
                  v-if="showResults">
      <b-form-input id="valueInput"
                    type="number"
                    v-model="count"
                    required
                    placeholder="Enter the count">
      </b-form-input>
    </b-form-group>
      <div class="pb-4" />
    <FilterCriteria v-if="!showResults" :filters="filters" :choose-types="false"/>
    <BarDiagram v-if="showResults && reloadDiagram" ref="diagram" :lineDiagramData="lineDiagramData"
    />
  </div>
</template>

<script>
import BarDiagram from './BarDiagram'
import FilterCriteria from '../common/FilterCriteria'
import basicApi from '../../rest/basic-api'

export default {
  name: 'TermDiagram',
  components: {
    BarDiagram,
    FilterCriteria
  },
  data () {
    return {
      lineDiagramData: {
        labels: [],
        primaryData: [],
        primaryLabel: 'Primary Filter'
      },
      filters: [
        {field: 'type', value: 'access_log', type: 'primary', operator: 'equals'},
        {field: 'source_id', value: 'fcs_webaccess', type: 'primary', operator: 'equals'}
      ],
      fieldName: 'ip',
      fieldData: [],
      fieldOptions: [],
      count: 15,
      showResults: true,
      reloadDiagram: true
    }
  },
  created () {
    this.loadData(this.filters)
    this.loadFieldNames()
  },
  watch: {
    showResults: function (value) {
      if (value) {
        this.loadData(this.filters)
      }
    },
    fieldName: function (value) {
      if (value) {
        this.reloadDiagram = false
        this.loadData(this.filters)
        this.reloadDiagram = true
      }
    },
    count: function (value) {
      if (value) {
        this.reloadDiagram = false
        this.loadData(this.filters)
        this.reloadDiagram = true
      }
    }
  },
  methods: {
    loadData (filters) {
      basicApi.getTermDiagram(filters, this.fieldName, this.count).then(response => {
        this.lineDiagramData.labels = []
        this.lineDiagramData.primaryData = []
        response.data.forEach(item => {
          this.lineDiagramData.labels.push(item.key)
          this.lineDiagramData.primaryData.push(item.count)
        })
        this.$refs.diagram.doRenderChart()
      }).catch(e => {
        this.$events.emit('error', e)
      })
    },
    loadFieldNames () {
      basicApi.getAllFieldNames().then(response => {
        this.fieldData = response.data
        this.fieldData.forEach(data => {
          this.fieldOptions.push({value: data.field, text: data.field})
        })
      }).catch(e => {
        this.$events.emit('error', e)
      })
    }
  }
}
</script>
