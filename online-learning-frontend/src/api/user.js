import request from '../utils/request'

export const getUsers = () => request.get('/api/users/all')

export const getUserPage = (page, size, keyword, role) => request.get('/api/users', { params: { page, size, keyword, role } })

export const getUserById = (id) => request.get(`/api/users/${id}`)

export const createUser = (data) => request.post('/api/users', data)

export const updateUser = (id, data) => request.put(`/api/users/${id}`, data)

export const deleteUser = (id) => request.delete(`/api/users/${id}`)