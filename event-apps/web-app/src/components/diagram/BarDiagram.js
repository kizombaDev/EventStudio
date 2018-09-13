import { Bar, mixins } from 'vue-chartjs'
const { reactiveData } = mixins

export default {
  extends: Bar,
  mixins: [reactiveData],
  data () {
    return {
      gradient: null,
      options: {responsive: true, maintainAspectRatio: false, legend: { display: false }}
    }
  },
  props: {
    lineDiagramData: Object
  },
  mounted () {
    this.gradient = this.$refs.canvas.getContext('2d').createLinearGradient(0, 0, 0, 450)
    this.gradient.addColorStop(0, 'rgba(255, 0,0, 0.5)') // show this color at 0%;
    this.gradient.addColorStop(0.5, 'rgba(255, 0, 0, 0.25)') // show this color at 50%
    this.gradient.addColorStop(1, 'rgba(145, 67, 204, 0.46)') // show this color at 100%
  },
  methods: {
    doRenderChart () {
      if (this.chartData == null) {
        this.renderChart(this.chartData, this.options)
      }
      this.chartData = {
        labels: this.lineDiagramData.labels,
        datasets: [
          {
            borderColor: '#FC2525',
            pointBackgroundColor: 'white',
            borderWidth: 1,
            pointBorderColor: 'white',
            backgroundColor: this.gradient,
            data: this.lineDiagramData.primaryData
          }
        ]
      }
    }
  }
}
