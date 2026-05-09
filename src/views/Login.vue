<template>
  <div class="login-page">
    <div class="login-box">
      <h2>用户登录</h2>
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="0">
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="密码"
            prefix-icon="Lock"
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            style="width: 100%"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <span>还没有账号？</span>
        <router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api/auth'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const loginForm = ref(null)
    const loading = ref(false)
    
    const form = reactive({
      username: '',
      password: ''
    })

    const rules = {
      username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
    }

    const handleLogin = async () => {
      const valid = await loginForm.value.validate().catch(() => false)
      if (!valid) return

      loading.value = true
      try {
        const res = await login(form)
        localStorage.setItem('token', res.data.token)
        ElMessage.success('登录成功')
        router.push('/')
      } catch (error) {
        console.error('登录失败', error)
      } finally {
        loading.value = false
      }
    }

    return {
      form,
      rules,
      loginForm,
      loading,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding-top: 60px;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

.login-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  color: #606266;
}

.login-footer a {
  color: #409EFF;
  text-decoration: none;
  margin-left: 5px;
}
</style>
