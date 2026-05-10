import request from '../utils/request'

export const getChaptersByCourseId = (courseId) => {
  return request.get(`/api/chapters/course/${courseId}`)
}

export const createChapter = (data) => {
  return request.post('/api/chapters', data)
}

export const updateChapter = (id, data) => {
  return request.put(`/api/chapters/${id}`, data)
}

export const deleteChapter = (id) => {
  return request.delete(`/api/chapters/${id}`)
}
