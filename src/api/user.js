import request from '../utils/request'

export const getUsers = () => request.get('/api/users/all')

export const getUserPage = (page, size) => request.get('/api/users', { params: { page, size } })

export const getUserById = (id) => request.get(`/api/users/${id}`)

export const updateUser = (id, data) => request.put(`/api/users/${id}`, data)

export const deleteUser = (id) => request.delete(`/api/users/${id}`)