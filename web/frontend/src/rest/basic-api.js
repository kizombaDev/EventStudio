import {HTTP} from './http-common'

export default {
  getAllTypes () {
    var types = []
    HTTP.get('/v1/logs/structure').then(response => {
      response.data.forEach(item => {
        types.push(item.type)
      })
    }).catch(e => {
      console.error(e)
    })
    return types
  },
  getPingIds () {
    var ids = []
    HTTP.get('/v1/logs?type=ping&group-by=id').then(response => {
      response.data.forEach(item => {
        ids.push(item.key)
      })
    }).catch(e => {
      console.error(e)
    })
    return ids
  },
  loadLastLogById (id) {
    var data = null
    HTTP.get('/v1/logs/' + id + '/last').then(response => {
      return response.data
    }).catch(e => {
      this.errors.push(e)
    })
    return data
  }
}
