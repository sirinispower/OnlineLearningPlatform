import request from '../utils/request'

export const getVideosByChapterId = (chapterId) => {
  return request.get(`/api/videos/chapter/${chapterId}`)
}

export const getVideosByCourseId = (courseId) => {
  return request.get(`/api/videos/course/${courseId}`)
}

export const getVideoById = (id) => {
  return request.get(`/api/videos/${id}`)
}

export const createVideo = (data) => {
  return request.post('/api/videos', data)
}

export const updateVideo = (id, data) => {
  return request.put(`/api/videos/${id}`, data)
}

export const deleteVideo = (id) => {
  return request.delete(`/api/videos/${id}`)
}

export const uploadVideo = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/videos/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
