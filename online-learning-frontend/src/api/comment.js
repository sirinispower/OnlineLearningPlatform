import request from '../utils/request'

export const getCommentsByCourseId = (courseId) => {
  return request.get(`/api/comments/course/${courseId}`)
}

export const createComment = (data) => {
  return request.post('/api/comments', data)
}

export const deleteComment = (id) => {
  return request.delete(`/api/comments/${id}`)
}

export const getAverageRating = (courseId) => {
  return request.get(`/api/comments/rating/${courseId}`)
}
