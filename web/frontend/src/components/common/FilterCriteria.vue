<template>
  <div>
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
    <h3>Add a new filter criteria</h3>
    <FilterCriteriaForm :chooseType="chooseTypes" v-on:filterAdded="filterAdded"/>
  </div>
</template>

<script>
import FilterCriteriaForm from './FilterCriteriaForm'

export default {
  name: 'FilterCriteria',
  components: {FilterCriteriaForm},
  props: {
    filters: Array,
    chooseTypes: Boolean
  },
  computed: {
    fields () {
      if (this.chooseTypes) {
        return [ 'field', 'operator', 'value', 'type', 'action' ]
      } else {
        return [ 'field', 'operator', 'value', 'action' ]
      }
    }
  },
  methods: {
    deleteFilterCriteria (filter) {
      this.filters.splice(this.filters.indexOf(filter), 1)
    },
    filterAdded (filter) {
      this.filters.push(filter)
    }
  }
}
</script>
