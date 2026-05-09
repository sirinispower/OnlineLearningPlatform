<template>
  <div class="admin-page">
    <div class="admin-container">
      <div class="admin-sidebar">
        <el-menu
          :default-active="activeMenu"
          class="admin-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="courses">
            <el-icon><Reading /></el-icon>
            <span>课程管理</span>
          </el-menu-item>
          <el-menu-item index="categories">
            <el-icon><Folder /></el-icon>
            <span>分类管理</span>
          </el-menu-item>
          <el-menu-item index="users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </el-menu>
      </div>
      <div class="admin-content">
        <div v-if="activeMenu === 'courses'" class="course-management">
          <div class="section-header">
            <h2>课程管理</h2>
            <el-button type="primary" @click="showCourseDialog = true">添加课程</el-button>
          </div>
          <el-table :data="courses" style="width: 100%">
            <el-table-column prop="title" label="课程名称" />
            <el-table-column prop="categoryName" label="分类" />
            <el-table-column prop="instructorName" label="讲师" />
            <el-table-column prop="viewCount" label="观看次数" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button type="primary" size="small" @click="editCourse(scope.row)">编辑</el-button>
                <el-button type="danger" size="small" @click="deleteCourse(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-if="activeMenu === 'categories'" class="category-management">
          <div class="section-header">
            <h2>分类管理</h2>
            <el-button type="primary" @click="showCategoryDialog = true">添加分类</el-button>
          </div>
          <el-table :data="categories" style="width: 100%">
            <el-table-column prop="name" label="分类名称" />
            <el-table-column prop="sort" label="排序" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button type="primary" size="small" @click="editCategory(scope.row)">编辑</el-button>
                <el-button type="danger" size="small" @click="deleteCategory(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-if="activeMenu === 'users'" class="user-management">
          <h2>用户管理</h2>
          <el-table :data="users" style="width: 100%">
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column prop="email" label="邮箱" />
            <el-table-column prop="role" label="角色" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button type="primary" size="small" @click="editUser(scope.row)">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>

    <el-dialog v-model="showCourseDialog" title="课程信息" width="600px">
      <el-form :model="courseForm" label-width="100px">
        <el-form-item label="课程名称">
          <el-input v-model="courseForm.title" />
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="courseForm.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="courseForm.categoryId" placeholder="选择分类">
            <el-option 
              v-for="cat in categories" 
              :key="cat.id" 
              :label="cat.name" 
              :value="cat.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="封面">
          <el-input v-model="courseForm.cover" placeholder="封面URL" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCourseDialog = false">取消</el-button>
        <el-button type="primary" @click="saveCourse">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showCategoryDialog" title="分类信息" width="400px">
      <el-form :model="categoryForm" label-width="100px">
        <el-form-item label="分类名称">
          <el-input v-model="categoryForm.name" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="categoryForm.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCategoryDialog = false">取消</el-button>
        <el-button type="primary" @click="saveCategory">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllCourses, createCourse, updateCourse, deleteCourse } from '../api/course'
import { getCategories, createCategory, updateCategory, deleteCategory } from '../api/category'

export default {
  name: 'Admin',
  setup() {
    const activeMenu = ref('courses')
    const courses = ref([])
    const categories = ref([])
    const users = ref([])
    const showCourseDialog = ref(false)
    const showCategoryDialog = ref(false)
    const courseForm = ref({})
    const categoryForm = ref({})

    const loadCourses = async () => {
      try {
        const res = await getAllCourses()
        courses.value = res.data
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

    const handleMenuSelect = (index) => {
      activeMenu.value = index
      if (index === 'courses') loadCourses()
      if (index === 'categories') loadCategories()
    }

    const editCourse = (course) => {
      courseForm.value = { ...course }
      showCourseDialog.value = true
    }

    const saveCourse = async () => {
      try {
        if (courseForm.value.id) {
          await updateCourse(courseForm.value.id, courseForm.value)
        } else {
          await createCourse(courseForm.value)
        }
        ElMessage.success('保存成功')
        showCourseDialog.value = false
        loadCourses()
      } catch (error) {
        console.error('保存课程失败', error)
      }
    }

    const deleteCourseHandler = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除该课程吗？', '提示', { type: 'warning' })
        await deleteCourse(id)
        ElMessage.success('删除成功')
        loadCourses()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除课程失败', error)
        }
      }
    }

    const editCategory = (category) => {
      categoryForm.value = { ...category }
      showCategoryDialog.value = true
    }

    const saveCategory = async () => {
      try {
        if (categoryForm.value.id) {
          await updateCategory(categoryForm.value.id, categoryForm.value)
        } else {
          await createCategory(categoryForm.value)
        }
        ElMessage.success('保存成功')
        showCategoryDialog.value = false
        loadCategories()
      } catch (error) {
        console.error('保存分类失败', error)
      }
    }

    const deleteCategoryHandler = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除该分类吗？', '提示', { type: 'warning' })
        await deleteCategory(id)
        ElMessage.success('删除成功')
        loadCategories()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除分类失败', error)
        }
      }
    }

    onMounted(() => {
      loadCourses()
      loadCategories()
    })

    return {
      activeMenu,
      courses,
      categories,
      users,
      showCourseDialog,
      showCategoryDialog,
      courseForm,
      categoryForm,
      handleMenuSelect,
      editCourse,
      saveCourse,
      deleteCourse: deleteCourseHandler,
      editCategory,
      saveCategory,
      deleteCategory: deleteCategoryHandler
    }
  }
}
</script>

<style scoped>
.admin-page {
  padding-top: 60px;
  min-height: 100vh;
  background: #f5f7fa;
}

.admin-container {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px 40px;
  gap: 24px;
}

.admin-sidebar {
  width: 220px;
  background: #fff;
  border-radius: 8px;
  padding: 20px 0;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.admin-menu {
  border: none;
}

.admin-content {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  color: #303133;
}
</style>
