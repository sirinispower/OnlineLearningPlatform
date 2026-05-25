<template>
  <div class="profile-page">
    <div class="profile-container">
      <div class="profile-sidebar">
        <div class="user-info">
          <el-avatar :size="80" :src="user.avatar || '/default-avatar.jpg'" />
          <h3>{{ user.nickname || user.username }}</h3>
          <p>{{ user.email }}</p>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="profile-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="favorites">
            <el-icon><Star /></el-icon>
            <span>我的收藏</span>
          </el-menu-item>
          <el-menu-item index="settings">
            <el-icon><Setting /></el-icon>
            <span>账号设置</span>
          </el-menu-item>
        </el-menu>
      </div>
      <div class="profile-content">
        <div v-if="activeMenu === 'favorites'" class="favorites">
          <h2>我的收藏</h2>
          <el-empty v-if="!favorites.length" description="暂无收藏课程" />
          <div v-else class="favorite-grid">
            <div 
              v-for="item in favorites" 
              :key="item.id"
              class="favorite-card"
              @click="goToCourse(item.courseId)"
            >
              <img :src="item.course?.cover || '/default-cover.jpg'" :alt="item.course?.title || '课程封面'">
              <h4>{{ item.course?.title || '未知课程' }}</h4>
            </div>
          </div>
        </div>
        <div v-if="activeMenu === 'settings'" class="settings">
          <h2>账号设置</h2>
          <el-form :model="userForm" label-width="100px">
            <el-form-item label="昵称">
              <el-input v-model="userForm.nickname" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="userForm.email" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="userForm.phone" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCurrentUser } from '../api/auth'
import { getFavorites } from '../api/favorite'

export default {
  name: 'Profile',
  setup() {
    const router = useRouter()
    const activeMenu = ref('favorites')
    const user = ref({})
    const favorites = ref([])
    const userForm = ref({})

    const loadUserInfo = async () => {
      try {
        const res = await getCurrentUser()
        user.value = res.data
        userForm.value = { ...res.data }
      } catch (error) {
        console.error('获取用户信息失败', error)
      }
    }

    const loadFavorites = async () => {
      try {
        const res = await getFavorites()
        favorites.value = res.data
      } catch (error) {
        console.error('获取收藏失败', error)
      }
    }

    const handleMenuSelect = (index) => {
      activeMenu.value = index
      if (index === 'favorites') {
        loadFavorites()
      }
    }

    const goToCourse = (courseId) => {
      router.push(`/course/${courseId}`)
    }

    const updateProfile = async () => {
      ElMessage.success('修改成功')
    }

    onMounted(() => {
      loadUserInfo()
      loadFavorites()
    })

    return {
      activeMenu,
      user,
      favorites,
      userForm,
      handleMenuSelect,
      goToCourse,
      updateProfile
    }
  }
}
</script>

<style scoped>
.profile-page {
  padding-top: 60px;
  min-height: 100vh;
  background: #f5f7fa;
}

.profile-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  padding: 30px 40px;
  gap: 24px;
}

.profile-sidebar {
  width: 250px;
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.user-info {
  text-align: center;
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #e4e7ed;
}

.user-info h3 {
  margin: 12px 0 4px;
  color: #303133;
}

.user-info p {
  color: #909399;
  font-size: 13px;
}

.profile-menu {
  border: none;
}

.profile-content {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.profile-content h2 {
  margin-bottom: 20px;
  color: #303133;
}

.favorite-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}

.favorite-card {
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}

.favorite-card:hover {
  transform: translateY(-4px);
}

.favorite-card img {
  width: 100%;
  height: 120px;
  object-fit: cover;
}

.favorite-card h4 {
  padding: 12px;
  font-size: 14px;
  color: #303133;
}
</style>
