import {HTTP} from './http-common'

export default {
  getAllTypes () {
    return HTTP.get('/v1/logs/structure')
  },
  getPingIds () {
    return HTTP.get('/v1/logs?type=ping&group-by=id')
  },
  getLastPingById (id) {
    return HTTP.get('/v1/logs/' + id + '/?size=1&from=0')
  },
  getDateHistogram (filters) {
    return HTTP.post('/v1/logs/date-histogram', filters)
  },
  getLogsByFilter (filters, size, from) {
    return HTTP.post('/v1/logs?from=' + from + '&size=' + size, filters)
  },
  getAllFieldNames () {
    return HTTP.get('v1/logs/structure/fields')
  }
}