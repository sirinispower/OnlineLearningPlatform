import request from '../utils/request'

export const getFavorites = () => {
  return request.get('/api/favorites')
}

export const addFavorite = (courseId) => {
  return request.post(`/api/favorites/${courseId}`)
}

export const removeFavorite = (courseId) => {
  return request.delete(`/api/favorites/${courseId}`)
}

export const checkFavorite = (courseId) => {
  return request.get(`/api/favorites/check/${courseId}`)
}
