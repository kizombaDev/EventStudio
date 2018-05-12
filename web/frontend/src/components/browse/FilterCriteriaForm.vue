<template>
  <div>
    <div>
      <b-form @submit="onSubmit" @reset="onReset">
        <b-form-group id="field"
                      label="Field name:"
                      label-for="fieldInput"
                      description="Description ">
          <b-form-input id="fieldInput"
                        type="text"
                        v-model="form.field"
                        required
                        placeholder="Enter the field name">
          </b-form-input>
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
export default {
  name: 'FilterCriteriaForm',
  data () {
    return {
      form: {
        field: '',
        value: '',
        type: 'primary'
      }
    }
  },
  props: {
    chooseType: Boolean
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
    }
  }
}
</script>
