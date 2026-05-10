import request from '../utils/request'

export const getCategories = () => {
  return request.get('/api/categories')
}

export const createCategory = (data) => {
  return request.post('/api/categories', data)
}

export const updateCategory = (id, data) => {
  return request.put(`/api/categories/${id}`, data)
}

export const deleteCategory = (id) => {
  return request.delete(`/api/categories/${id}`)
}
