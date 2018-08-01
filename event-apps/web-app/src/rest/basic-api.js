import {HTTP} from './http-common'

export default {
  getAllTypes () {
    return HTTP.get('/v1/events/structure')
  },
  getPingSourceIds () {
    return HTTP.get('/v1/events?type=ping&group-by=source_id')
  },
  getLastPingBySourceId (sourceId) {
    return HTTP.get('/v1/events/' + sourceId + '/?size=1&from=0')
  },
  getDateHistogram (filters) {
    return HTTP.post('/v1/events/date-histogram', filters)
  },
  getTermDiagram (filters, termName, count) {
    return HTTP.post('/v1/events/term-diagram?term-name=' + termName + '&count=' + count, filters)
  },
  getLogsByFilter (filters, size, from) {
    return HTTP.post('/v1/events?from=' + from + '&size=' + size, filters)
  },
  getAllFieldNames () {
    return HTTP.get('v1/events/structure/fields')
  }
}
