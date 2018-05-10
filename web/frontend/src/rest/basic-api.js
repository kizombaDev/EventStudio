import {HTTP} from './http-common'

export default {
  getAllTypes () {
    var types = []
    HTTP.get('/v1/logs/structure').then(response => {
      response.data.forEach(item => {
        types.push(item.type)
      })
    }).catch(e => {
      console.log(e)
    })
    return types
  }
}
