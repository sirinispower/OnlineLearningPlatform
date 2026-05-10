import request from '../utils/request'

export const getProgress = (videoId) => {
  return request.get(`/api/progress/${videoId}`)
}

export const saveProgress = (data) => {
  return request.post('/api/progress', data)
}
