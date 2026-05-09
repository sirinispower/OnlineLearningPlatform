<template>
  <div id="app">
    <nav class="navbar">
      <div class="nav-brand">
        <router-link to="/">
          <el-icon><VideoCamera /></el-icon>
          在线学习平台
        </router-link>
      </div>
      <div class="nav-menu">
        <router-link to="/">首页</router-link>
        <router-link to="/courses">课程</router-link>
        <router-link to="/profile" v-if="isLoggedIn">个人中心</router-link>
        <router-link to="/admin" v-if="isAdmin">管理后台</router-link>
      </div>
      <div class="nav-user">
        <template v-if="isLoggedIn">
          <span class="username">{{ userInfo.nickname || userInfo.username }}</span>
          <el-button type="text" @click="logout">退出</el-button>
        </template>
        <template v-else>
          <router-link to="/login">登录</router-link>
          <router-link to="/register">注册</router-link>
        </template>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCurrentUser } from './api/auth'

export default {
  name: 'App',
  setup() {
    const router = useRouter()
    const userInfo = ref({})
    const isLoggedIn = computed(() => !!localStorage.getItem('token'))
    const isAdmin = computed(() => userInfo.value.role === 'admin')

    const loadUserInfo = async () => {
      if (isLoggedIn.value) {
        try {
          const res = await getCurrentUser()
          userInfo.value = res.data
        } catch (error) {
          console.error('获取用户信息失败', error)
        }
      }
    }

    const logout = () => {
      localStorage.removeItem('token')
      userInfo.value = {}
      router.push('/login')
    }

    onMounted(() => {
      loadUserInfo()
    })

    return {
      userInfo,
      isLoggedIn,
      isAdmin,
      logout
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 40px;
  height: 60px;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.nav-brand a {
  font-size: 20px;
  font-weight: bold;
  color: #409EFF;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-menu {
  display: flex;
  gap: 30px;
}

.nav-menu a {
  color: #606266;
  text-decoration: none;
  font-size: 15px;
  transition: color 0.3s;
}

.nav-menu a:hover,
.nav-menu a.router-link-active {
  color: #409EFF;
}

.nav-user {
  display: flex;
  align-items: center;
  gap: 15px;
}

.nav-user a {
  color: #606266;
  text-decoration: none;
  font-size: 14px;
}

.nav-user a:hover {
  color: #409EFF;
}

.username {
  color: #409EFF;
  font-weight: 500;
}

.main-content {
  padding-top: 60px;
}
</style>
