<template>
  <div class="courses-page">
    <div class="courses-header">
      <h1>全部课程</h1>
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索课程"
          prefix-icon="Search"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
      </div>
    </div>

    <div class="courses-content">
      <div class="category-filter">
        <el-tag 
          :class="{ active: !selectedCategory }"
          @click="selectCategory(null)"
        >
          全部
        </el-tag>
        <el-tag 
          v-for="cat in categories" 
          :key="cat.id"
          :class="{ active: selectedCategory === cat.id }"
          @click="selectCategory(cat.id)"
        >
          {{ cat.name }}
        </el-tag>
      </div>

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

      <div class="pagination">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          :total="total"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCourseList } from '../api/course'
import { getCategories } from '../api/category'

export default {
  name: 'Courses',
  setup() {
    const router = useRouter()
    const courses = ref([])
    const categories = ref([])
    const searchKeyword = ref('')
    const selectedCategory = ref(null)
    const page = ref(1)
    const size = ref(12)
    const total = ref(0)

    const loadCourses = async () => {
      try {
        const res = await getCourseList({
          page: page.value,
          size: size.value,
          keyword: searchKeyword.value,
          categoryId: selectedCategory.value
        })
        courses.value = res.data.records
        total.value = res.data.total
      } catch (error) {
        console.error('获取课程失败', error)
      }
    }

    const loadCategories = async () => {
      try {
        const res = await getCategories()
        categories.value = res.data
      } catch (error) {
        console.error('获取分类失败', error)
      }
    }

    const handleSearch = () => {
      page.value = 1
      loadCourses()
    }

    const selectCategory = (id) => {
      selectedCategory.value = id
      page.value = 1
      loadCourses()
    }

    const handlePageChange = () => {
      loadCourses()
    }

    const goToCourse = (id) => {
      router.push(`/course/${id}`)
    }

    onMounted(() => {
      loadCourses()
      loadCategories()
    })

    return {
      courses,
      categories,
      searchKeyword,
      selectedCategory,
      page,
      size,
      total,
      handleSearch,
      selectCategory,
      handlePageChange,
      goToCourse
    }
  }
}
</script>

<style scoped>
.courses-page {
  padding-top: 60px;
  min-height: 100vh;
  background: #f5f7fa;
}

.courses-header {
  background: #fff;
  padding: 40px;
  text-align: center;
  border-bottom: 1px solid #e4e7ed;
}

.courses-header h1 {
  margin-bottom: 20px;
  color: #303133;
}

.search-bar {
  max-width: 500px;
  margin: 0 auto;
}

.courses-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 40px;
}

.category-filter {
  display: flex;
  gap: 10px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.category-filter .el-tag {
  cursor: pointer;
  transition: all 0.3s;
}

.category-filter .el-tag.active {
  background: #409EFF;
  color: #fff;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
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
  height: 160px;
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
  font-size: 16px;
  margin-bottom: 8px;
  color: #303133;
}

.course-desc {
  color: #606266;
  font-size: 13px;
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
  font-size: 12px;
}

.rating {
  color: #f7ba2a;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.pagination {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}
</style>
