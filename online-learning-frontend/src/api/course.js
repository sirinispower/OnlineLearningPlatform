import request from '../utils/request'

export const getCourseList = (params) => {
  return request.get('/api/courses', { params })
}

export const getCourseById = (id) => {
  return request.get(`/api/courses/${id}`)
}

export const getAllCourses = () => {
  return request.get('/api/courses/all')
}

export const createCourse = (data) => {
  return request.post('/api/courses', data)
}

export const updateCourse = (id, data) => {
  return request.put(`/api/courses/${id}`, data)
}

export const deleteCourse = (id) => {
  return request.delete(`/api/courses/${id}`)
}
