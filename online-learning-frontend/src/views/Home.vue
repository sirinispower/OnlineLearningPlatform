<template>
  <div class="home">
    <div class="hero">
      <div class="hero-content">
        <h1>在线学习平台</h1>
        <p>海量优质课程，随时随地学习</p>
        <el-button type="primary" size="large" @click="$router.push('/courses')">
          浏览课程
        </el-button>
      </div>
    </div>
    
    <div class="featured-courses">
      <h2>热门课程</h2>
      <div class="course-grid">
        <div 
          v-for="course in courses" 
          :key="course.id" 
          class="course-card"
          @click="goToCourse(course.id)"
        >
          <div class="course-cover">
            <img :src="course.cover || '/default-cover.jpg'" :alt="course.title">
          </div>
          <div class="course-info">
            <h3>{{ course.title }}</h3>
            <p class="course-desc">{{ course.description }}</p>
            <div class="course-meta">
              <span class="instructor">{{ course.instructorName }}</span>
              <span class="rating" v-if="course.rating">
                <el-icon><StarFilled /></el-icon>
                {{ course.rating.toFixed(1) }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAllCourses } from '../api/course'

export default {
  name: 'Home',
  setup() {
    const router = useRouter()
    const courses = ref([])

    const loadCourses = async () => {
      try {
        const res = await getAllCourses()
        courses.value = res.data.slice(0, 6)
      } catch (error) {
        console.error('获取课程失败', error)
      }
    }

    const goToCourse = (id) => {
      router.push(`/course/${id}`)
    }

    onMounted(() => {
      loadCourses()
    })

    return {
      courses,
      goToCourse
    }
  }
}
</script>

<style scoped>
.home {
  padding-top: 60px;
}

.hero {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 100px 40px;
  text-align: center;
}

.hero-content h1 {
  font-size: 48px;
  margin-bottom: 20px;
}

.hero-content p {
  font-size: 20px;
  margin-bottom: 30px;
  opacity: 0.9;
}

.featured-courses {
  padding: 60px 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.featured-courses h2 {
  text-align: center;
  font-size: 32px;
  margin-bottom: 40px;
  color: #303133;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.course-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.course-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

.course-cover {
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.course-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.course-info {
  padding: 16px;
}

.course-info h3 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #303133;
}

.course-desc {
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.course-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.instructor {
  color: #909399;
  font-size: 13px;
}

.rating {
  color: #f7ba2a;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
