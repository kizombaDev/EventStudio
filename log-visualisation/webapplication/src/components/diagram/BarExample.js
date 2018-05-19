import { Bar } from 'vue-chartjs'

export default {
  extends: Bar,
  data () {
    return {
      gradient: null
    }
  },
  mounted () {
    this.gradient = this.$refs.canvas.getContext('2d').createLinearGradient(0, 0, 0, 450)
    this.gradient.addColorStop(0, 'rgba(255, 0,0, 0.5)') // show this color at 0%;
    this.gradient.addColorStop(0.5, 'rgba(255, 0, 0, 0.25)') // show this color at 50%
    this.gradient.addColorStop(1, 'rgba(145, 67, 204, 0.46)') // show this color at 100%

    this.renderChart({
      labels: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
      datasets: [
        {
          label: 'Data One',
          backgroundColor: this.gradient,
          data: [40, 20, 12, 39, 10, 40, 39, 80, 40, 20, 12, 11, 40, 20, 12, 39, 10, 40, 39, 80, 40, 20, 12, 11]
        },
        {
          label: 'Data Two',
          backgroundColor: this.gradient,
          data: [1, 2, 3, 3, 1, 4, 3, 8, 4, 2, 1, 1, 1, 2, 3, 3, 1, 4, 3, 8, 4, 2, 1, 1]
        }
      ]
    }, {responsive: true, maintainAspectRatio: false})
  }
}
