<template>
  <div>
    <div>
      <b-form @submit="onSubmit" @reset="onReset">
        <b-form-group id="field"
                      label="Field name:"
                      label-for="fieldInput">
          <b-form-select id="fieldInput"
                         :options="fieldOptions"
                        v-model="form.field"
                        required>
          </b-form-select>
        </b-form-group>
        <b-form-group id="field"
                      label="Field name:"
                      label-for="fieldInput">
          <b-form-select id="fieldInput"
                         :options="operators"
                         v-model="form.operator"
                         required>
          </b-form-select>
        </b-form-group>
        <b-form-group id="value"
                      label="Expected value:"
                      label-for="valueInput">
          <b-form-input id="valueInput"
                        type="text"
                        v-model="form.value"
                        required
                        placeholder="Enter the value">
          </b-form-input>
        </b-form-group>
        <b-form-group id="exampleGroup4" v-if="chooseType">
          <b-form-radio-group v-model="form.type" id="exampleChecks">
            <b-form-radio value="primary">Primary Filter</b-form-radio>
            <b-form-radio value="secondary">Secondary Filter</b-form-radio>
          </b-form-radio-group>
        </b-form-group>
        <b-button type="submit" variant="primary">Add</b-button>
        <b-button type="reset" variant="danger">Reset</b-button>
      </b-form>
    </div>
  </div>
</template>

<script>
import basicApi from '../../rest/basic-api'

export default {
  name: 'FilterCriteriaForm',
  data () {
    return {
      form: {
        field: '',
        value: '',
        type: 'primary',
        operator: 'equals'
      },
      fieldData: [],
      fieldOptions: [],
      operators: [
        {value: 'equals', text: 'equals'},
        {value: 'gte', text: 'greater than or equal to'},
        {value: 'gt', text: 'greater than'},
        {value: 'lte', text: 'less than or equal to'},
        {value: 'lt', text: 'less than'}
      ]
    }
  },
  props: {
    chooseType: Boolean
  },
  created () {
    this.loadFieldNames()
  },
  methods: {
    onSubmit (evt) {
      evt.preventDefault()
      this.$emit('filterAdded', this.form)
      this.form = {
        field: '',
        value: '',
        type: 'primary'
      }
    },
    onReset (evt) {
      evt.preventDefault()
      /* Reset our form values */
      this.form.field = ''
      this.form.value = ''
      this.form.checked = []
      /* Trick to reset/clear native browser form validation state */
      this.show = false
      this.$nextTick(() => { this.show = true })
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
