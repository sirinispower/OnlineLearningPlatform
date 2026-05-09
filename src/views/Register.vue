<template>
  <div class="register-page">
    <div class="register-box">
      <h2>用户注册</h2>
      <el-form :model="form" :rules="rules" ref="registerForm" label-width="0">
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input 
            v-model="form.nickname" 
            placeholder="昵称"
            prefix-icon="UserFilled"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="email">
          <el-input 
            v-model="form.email" 
            placeholder="邮箱"
            prefix-icon="Message"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="phone">
          <el-input 
            v-model="form.phone" 
            placeholder="手机号（可选）"
            prefix-icon="Phone"
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
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input 
            v-model="form.confirmPassword" 
            type="password" 
            placeholder="确认密码"
            prefix-icon="Lock"
            size="large"
            @keyup.enter="handleRegister"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            style="width: 100%"
            :loading="loading"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <div class="register-footer">
        <span>已有账号？</span>
        <router-link to="/login">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../api/auth'

export default {
  name: 'Register',
  setup() {
    const router = useRouter()
    const registerForm = ref(null)
    const loading = ref(false)
    
    const form = reactive({
      username: '',
      nickname: '',
      email: '',
      phone: '',
      password: '',
      confirmPassword: ''
    })

    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (form.confirmPassword !== '') {
          registerForm.value.validateField('confirmPassword')
        }
        callback()
      }
    }

    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== form.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }

    const rules = {
      username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
      email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ],
      password: [{ validator: validatePass, trigger: 'blur' }],
      confirmPassword: [{ validator: validatePass2, trigger: 'blur' }]
    }

    const handleRegister = async () => {
      const valid = await registerForm.value.validate().catch(() => false)
      if (!valid) return

      loading.value = true
      try {
        await register({
          username: form.username,
          nickname: form.nickname,
          email: form.email,
          phone: form.phone,
          password: form.password
        })
        ElMessage.success('注册成功')
        router.push('/login')
      } catch (error) {
        console.error('注册失败', error)
      } finally {
        loading.value = false
      }
    }

    return {
      form,
      rules,
      registerForm,
      loading,
      handleRegister
    }
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding-top: 60px;
}

.register-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

.register-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.register-footer {
  text-align: center;
  margin-top: 20px;
  color: #606266;
}

.register-footer a {
  color: #409EFF;
  text-decoration: none;
  margin-left: 5px;
}
</style>
