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
        <!-- 课程管理 -->
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
            <el-table-column label="操作" width="400">
              <template #default="scope">
                <el-button type="primary" size="small" @click="editCourse(scope.row)">编辑</el-button>
                <el-button type="warning" size="small" @click="manageChapters(scope.row)">章节管理</el-button>
                <el-button type="success" size="small" @click="manageVideos(scope.row)">视频管理</el-button>
                <el-button type="danger" size="small" @click="deleteCourse(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 分类管理 -->
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

        <!-- 用户管理 -->
        <div v-if="activeMenu === 'users'" class="user-management">
          <div class="section-header">
            <h2>用户管理</h2>
            <el-button type="primary" @click="showAddUserDialog">添加用户</el-button>
          </div>
          <div class="search-bar">
            <el-input 
              v-model="userSearchKeyword" 
              placeholder="搜索用户名/昵称/邮箱" 
              style="width: 250px; margin-right: 10px;"
              clearable
              @clear="loadUserPage"
            />
            <el-select v-model="userSearchRole" placeholder="选择角色" style="width: 150px; margin-right: 10px;" clearable @clear="loadUserPage">
              <el-option label="普通用户" value="user" />
              <el-option label="管理员" value="admin" />
              <el-option label="讲师" value="instructor" />
            </el-select>
            <el-button type="primary" @click="loadUserPage">搜索</el-button>
          </div>
          <el-table :data="users" style="width: 100%">
            <el-table-column prop="username" label="用户名" sortable />
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column prop="email" label="邮箱" />
            <el-table-column prop="role" label="角色">
              <template #default="scope">
                <el-tag :type="scope.row.role === 'admin' ? 'danger' : scope.row.role === 'instructor' ? 'warning' : 'info'">
                  {{ scope.row.role === 'admin' ? '管理员' : scope.row.role === 'instructor' ? '讲师' : '普通用户' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-switch 
                  v-model="scope.row.status" 
                  :active-value="1" 
                  :inactive-value="0"
                  @change="(val) => toggleUserStatus(scope.row, val)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="注册时间" sortable />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button type="primary" size="small" @click="editUser(scope.row)">编辑</el-button>
                <el-button type="danger" size="small" @click="deleteUser(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-container">
            <el-pagination
              v-model:current-page="userPage.current"
              v-model:page-size="userPage.size"
              :page-sizes="[10, 20, 50, 100]"
              :total="userPage.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="loadUserPage"
              @current-change="loadUserPage"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- 用户编辑对话框 -->
    <el-dialog v-model="showUserDialog" :title="userForm.id ? '编辑用户' : '添加用户'" width="500px">
      <el-form :model="userForm" label-width="100px" :rules="userRules" ref="userFormRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" :disabled="!!userForm.id" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!userForm.id">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="userForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" placeholder="选择角色">
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
            <el-option label="讲师" value="instructor" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showUserDialog = false">取消</el-button>
        <el-button type="primary" @click="saveUser">保存</el-button>
      </template>
    </el-dialog>

    <!-- 课程编辑对话框 -->
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

    <!-- 分类编辑对话框 -->
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

    <!-- 章节管理对话框 -->
    <el-dialog v-model="showChapterDialog" :title="`章节管理 - ${currentCourse?.title || ''}`" width="700px">
      <div class="chapter-management">
        <div class="section-header">
          <h3>章节列表</h3>
          <el-button type="primary" @click="showAddChapterDialog">添加章节</el-button>
        </div>
        <el-table :data="chapters" style="width: 100%">
          <el-table-column type="index" label="序号" width="80" />
          <el-table-column prop="title" label="章节标题" />
          <el-table-column prop="sort" label="排序" width="100" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button type="primary" size="small" @click="editChapter(scope.row)">编辑</el-button>
              <el-button type="danger" size="small" @click="deleteChapter(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <!-- 添加/编辑章节对话框 -->
    <el-dialog v-model="showChapterEditDialog" :title="chapterForm.id ? '编辑章节' : '添加章节'" width="500px">
      <el-form :model="chapterForm" label-width="100px">
        <el-form-item label="章节标题">
          <el-input v-model="chapterForm.title" placeholder="请输入章节标题" />
        </el-form-item>
        <el-form-item label="章节描述">
          <el-input v-model="chapterForm.description" type="textarea" :rows="3" placeholder="请输入章节描述" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="chapterForm.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showChapterEditDialog = false">取消</el-button>
        <el-button type="primary" @click="saveChapter">保存</el-button>
      </template>
    </el-dialog>

    <!-- 视频管理对话框 -->
    <el-dialog v-model="showVideoDialog" :title="`视频管理 - ${currentCourse?.title || ''}`" width="900px">
      <div class="video-management">
        <div class="section-header">
          <h3>视频列表 ({{ videos.length }}/100)</h3>
          <el-button type="primary" @click="showAddVideoDialog" :disabled="videos.length >= 100">添加视频</el-button>
        </div>
        <el-table :data="videos" style="width: 100%">
          <el-table-column type="index" label="集数" width="80" />
          <el-table-column prop="title" label="视频标题" />
          <el-table-column prop="chapterTitle" label="所属章节" width="150" />
          <el-table-column prop="duration" label="时长" width="100" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button type="primary" size="small" @click="editVideo(scope.row)">编辑</el-button>
              <el-button type="danger" size="small" @click="deleteVideo(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <!-- 添加/编辑视频对话框 -->
    <el-dialog v-model="showVideoEditDialog" :title="videoForm.id ? '编辑视频' : '添加视频'" width="600px">
      <el-form :model="videoForm" label-width="100px">
        <el-form-item label="视频标题">
          <el-input v-model="videoForm.title" placeholder="请输入视频标题" />
        </el-form-item>
        <el-form-item label="所属章节">
          <el-select v-model="videoForm.chapterId" placeholder="选择章节">
            <el-option 
              v-for="ch in chapters" 
              :key="ch.id" 
              :label="ch.title" 
              :value="ch.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="视频文件">
          <el-upload
            class="video-uploader"
            action="/api/videos/upload"
            :headers="uploadHeaders"
            :on-success="handleVideoUploadSuccess"
            :before-upload="beforeVideoUpload"
            accept="video/*"
          >
            <el-button type="primary">选择视频文件</el-button>
            <template #tip>
              <div class="el-upload__tip">支持 mp4, avi, mov 等格式，单个文件不超过 500MB</div>
            </template>
          </el-upload>
          <div v-if="videoForm.url" class="video-url">
            已上传: {{ videoForm.url }}
          </div>
        </el-form-item>
        <el-form-item label="视频时长">
          <el-input v-model="videoForm.duration" placeholder="如: 15:30" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="videoForm.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showVideoEditDialog = false">取消</el-button>
        <el-button type="primary" @click="saveVideo" :disabled="!videoForm.url">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllCourses, createCourse, updateCourse, deleteCourse } from '../api/course'
import { getCategories, createCategory, updateCategory, deleteCategory } from '../api/category'
import { getUserPage, createUser, updateUser, deleteUser as deleteUserApi } from '../api/user'
import { getChaptersByCourseId, createChapter, updateChapter, deleteChapter } from '../api/chapter'
import { getVideosByCourseId, createVideo, updateVideo, deleteVideo } from '../api/video'

export default {
  name: 'Admin',
  setup() {
    const activeMenu = ref('courses')
    const courses = ref([])
    const categories = ref([])
    const users = ref([])
    const chapters = ref([])
    const videos = ref([])
    const currentCourse = ref(null)
    const showCourseDialog = ref(false)
    const showCategoryDialog = ref(false)
    const showUserDialog = ref(false)
    const showChapterDialog = ref(false)
    const showChapterEditDialog = ref(false)
    const showVideoDialog = ref(false)
    const showVideoEditDialog = ref(false)
    const courseForm = ref({})
    const categoryForm = ref({})
    const userForm = ref({
      username: '',
      password: '',
      nickname: '',
      email: '',
      phone: '',
      role: 'user',
      status: 1
    })
    const userFormRef = ref(null)
    const userSearchKeyword = ref('')
    const userSearchRole = ref('')
    const userPage = ref({
      current: 1,
      size: 10,
      total: 0
    })
    const chapterForm = ref({
      title: '',
      description: '',
      sort: 0,
      courseId: null
    })
    const videoForm = ref({
      title: '',
      url: '',
      duration: '',
      sort: 0,
      courseId: null,
      chapterId: null
    })

    const uploadHeaders = computed(() => {
      const token = localStorage.getItem('token')
      return token ? { Authorization: 'Bearer ' + token } : {}
    })

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

    const loadUserPage = async () => {
      try {
        const res = await getUserPage(userPage.value.current, userPage.value.size, userSearchKeyword.value, userSearchRole.value)
        users.value = res.data.records
        userPage.value.total = res.data.total
      } catch (error) {
        console.error('获取用户失败', error)
      }
    }

    const loadUsers = async () => {
      await loadUserPage()
    }

    const loadChapters = async (courseId) => {
      try {
        const res = await getChaptersByCourseId(courseId)
        chapters.value = res.data || []
      } catch (error) {
        console.error('获取章节失败', error)
        chapters.value = []
      }
    }

    const loadVideos = async (courseId) => {
      try {
        const res = await getVideosByCourseId(courseId)
        videos.value = res.data || []
      } catch (error) {
        console.error('获取视频失败', error)
        videos.value = []
      }
    }

    const handleMenuSelect = (index) => {
      activeMenu.value = index
      if (index === 'courses') loadCourses()
      if (index === 'categories') loadCategories()
      if (index === 'users') loadUsers()
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

    // 章节管理
    const manageChapters = (course) => {
      currentCourse.value = course
      loadChapters(course.id)
      showChapterDialog.value = true
    }

    const showAddChapterDialog = () => {
      chapterForm.value = {
        title: '',
        description: '',
        sort: chapters.value.length,
        courseId: currentCourse.value.id
      }
      showChapterEditDialog.value = true
    }

    const editChapter = (chapter) => {
      chapterForm.value = { ...chapter }
      showChapterEditDialog.value = true
    }

    const saveChapter = async () => {
      try {
        if (chapterForm.value.id) {
          await updateChapter(chapterForm.value.id, chapterForm.value)
        } else {
          await createChapter(chapterForm.value)
        }
        ElMessage.success('保存成功')
        showChapterEditDialog.value = false
        loadChapters(currentCourse.value.id)
      } catch (error) {
        console.error('保存章节失败', error)
      }
    }

    const deleteChapterHandler = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除该章节吗？', '提示', { type: 'warning' })
        await deleteChapter(id)
        ElMessage.success('删除成功')
        loadChapters(currentCourse.value.id)
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除章节失败', error)
        }
      }
    }

    // 视频管理
    const manageVideos = (course) => {
      currentCourse.value = course
      loadChapters(course.id)
      loadVideos(course.id)
      showVideoDialog.value = true
    }

    const showAddVideoDialog = () => {
      videoForm.value = {
        title: '',
        url: '',
        duration: '',
        sort: videos.value.length,
        courseId: currentCourse.value.id,
        chapterId: chapters.value.length > 0 ? chapters.value[0].id : null
      }
      showVideoEditDialog.value = true
    }

    const editVideo = (video) => {
      videoForm.value = { ...video }
      showVideoEditDialog.value = true
    }

    const saveVideo = async () => {
      try {
        if (videoForm.value.id) {
          await updateVideo(videoForm.value.id, videoForm.value)
        } else {
          await createVideo(videoForm.value)
        }
        ElMessage.success('保存成功')
        showVideoEditDialog.value = false
        loadVideos(currentCourse.value.id)
      } catch (error) {
        console.error('保存视频失败', error)
      }
    }

    const deleteVideoHandler = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除该视频吗？', '提示', { type: 'warning' })
        await deleteVideo(id)
        ElMessage.success('删除成功')
        loadVideos(currentCourse.value.id)
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除视频失败', error)
        }
      }
    }

    const handleVideoUploadSuccess = (res) => {
      if (res.code === 200) {
        videoForm.value.url = res.data
        ElMessage.success('视频上传成功')
      } else {
        ElMessage.error(res.message || '上传失败')
      }
    }

    const beforeVideoUpload = (file) => {
      const isVideo = file.type.startsWith('video/')
      const isLt500M = file.size / 1024 / 1024 < 500

      if (!isVideo) {
        ElMessage.error('请上传视频文件!')
        return false
      }
      if (!isLt500M) {
        ElMessage.error('视频大小不能超过 500MB!')
        return false
      }
      return true
    }

    // 用户管理
    const userRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      nickname: [
        { required: true, message: '请输入昵称', trigger: 'blur' }
      ],
      email: [
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ]
    }

    const showAddUserDialog = () => {
      userForm.value = {
        username: '',
        password: '',
        nickname: '',
        email: '',
        phone: '',
        role: 'user',
        status: 1
      }
      showUserDialog.value = true
    }

    const editUser = (user) => {
      userForm.value = { 
        id: user.id,
        username: user.username,
        nickname: user.nickname,
        email: user.email,
        phone: user.phone,
        role: user.role,
        status: user.status
      }
      showUserDialog.value = true
    }

    const saveUser = async () => {
      try {
        const valid = await userFormRef.value.validate()
        if (!valid) return

        if (userForm.value.id) {
          await updateUser(userForm.value.id, userForm.value)
        } else {
          await createUser(userForm.value)
        }
        ElMessage.success('保存成功')
        showUserDialog.value = false
        loadUserPage()
      } catch (error) {
        console.error('保存用户失败', error)
      }
    }

    const deleteUser = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除该用户吗？', '提示', { type: 'warning' })
        await deleteUserApi(id)
        ElMessage.success('删除成功')
        loadUserPage()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除用户失败', error)
        }
      }
    }

    const toggleUserStatus = async (user, status) => {
      try {
        await updateUser(user.id, { status })
        ElMessage.success('状态更新成功')
      } catch (error) {
        console.error('更新状态失败', error)
        user.status = status === 1 ? 0 : 1
      }
    }

    onMounted(() => {
      loadCourses()
      loadCategories()
      loadUsers()
    })

    return {
      activeMenu,
      courses,
      categories,
      users,
      chapters,
      videos,
      currentCourse,
      showCourseDialog,
      showCategoryDialog,
      showUserDialog,
      showChapterDialog,
      showChapterEditDialog,
      showVideoDialog,
      showVideoEditDialog,
      courseForm,
      categoryForm,
      userForm,
      userFormRef,
      userSearchKeyword,
      userSearchRole,
      userPage,
      userRules,
      chapterForm,
      videoForm,
      uploadHeaders,
      handleMenuSelect,
      editCourse,
      saveCourse,
      deleteCourse: deleteCourseHandler,
      editCategory,
      saveCategory,
      deleteCategory: deleteCategoryHandler,
      showAddUserDialog,
      editUser,
      saveUser,
      deleteUser,
      toggleUserStatus,
      loadUserPage,
      manageChapters,
      showAddChapterDialog,
      editChapter,
      saveChapter,
      deleteChapter: deleteChapterHandler,
      manageVideos,
      showAddVideoDialog,
      editVideo,
      saveVideo,
      deleteVideo: deleteVideoHandler,
      handleVideoUploadSuccess,
      beforeVideoUpload
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

.section-header h2,
.section-header h3 {
  color: #303133;
}

.chapter-management,
.video-management {
  padding: 10px 0;
}

.video-url {
  margin-top: 10px;
  padding: 8px;
  background: #f5f7fa;
  border-radius: 4px;
  word-break: break-all;
  font-size: 12px;
  color: #606266;
}

.search-bar {
  display: flex;
  margin-bottom: 20px;
  align-items: center;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>