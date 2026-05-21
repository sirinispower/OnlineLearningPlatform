import request from '../utils/request'

export const getChaptersByCourseId = (courseId) => {
  return request({
    url: `/api/chapters/course/${courseId}`,
    method: 'get'
  })
}

export const createChapter = (data) => {
  return request({
    url: '/api/chapters',
    method: 'post',
    data
  })
}

export const updateChapter = (id, data) => {
  return request({
    url: `/api/chapters/${id}`,
    method: 'put',
    data
  })
}

export const deleteChapter = (id) => {
  return request({
    url: `/api/chapters/${id}`,
    method: 'delete'
  })
}