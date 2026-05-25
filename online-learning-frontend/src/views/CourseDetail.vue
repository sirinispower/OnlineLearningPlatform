<template>
  <div class="course-detail">
    <div class="course-header" v-if="course">
      <div class="course-header-content">
        <div class="course-cover-large">
          <img :src="course.cover || '/default-cover.jpg'" :alt="course.title">
        </div>
        <div class="course-header-info">
          <h1>{{ course.title }}</h1>
          <p class="course-description">{{ course.description }}</p>
          <div class="course-stats">
            <span><el-icon><User /></el-icon> {{ course.instructorName }}</span>
            <span><el-icon><View /></el-icon> {{ course.viewCount }} 次观看</span>
            <span v-if="rating">
              <el-icon><StarFilled /></el-icon> {{ rating.toFixed(1) }}
            </span>
          </div>
          <div class="course-actions">
            <el-button type="primary" size="large" @click="startLearning">
              开始学习
            </el-button>
            <el-button 
              :type="isFavorite ? 'danger' : 'default'" 
              size="large"
              @click="toggleFavorite"
            >
              <el-icon><Star /></el-icon>
              {{ isFavorite ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <div class="course-body">
      <div class="course-content">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="课程目录" name="chapters">
            <div class="chapter-list">
              <div 
                v-for="chapter in chapters" 
                :key="chapter.id"
                class="chapter-item"
              >
                <h3>{{ chapter.title }}</h3>
                <div class="video-list">
                  <div 
                    v-for="video in getVideosByChapter(chapter.id)" 
                    :key="video.id"
                    class="video-item"
                    @click="playVideo(video.id)"
                  >
                    <el-icon><VideoPlay /></el-icon>
                    <span class="video-title">{{ video.title }}</span>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="课程评价" name="comments">
            <div class="comments-section">
              <div class="comment-form" v-if="isLoggedIn">
                <el-rate v-model="newComment.rating" show-score />
                <el-input
                  v-model="newComment.content"
                  type="textarea"
                  :rows="3"
                  placeholder="写下你的评价..."
                />
                <el-button type="primary" @click="submitComment">提交评价</el-button>
              </div>
              <div class="comment-list">
                <div 
                  v-for="comment in comments" 
                  :key="comment.id"
                  class="comment-item"
                >
                  <div class="comment-header">
                    <span class="username">{{ comment.username }}</span>
                    <el-rate 
                      v-model="comment.rating" 
                      disabled 
                      show-score
                      text-color="#ff9900"
                    />
                  </div>
                  <p class="comment-content">{{ comment.content }}</p>
                  <span class="comment-time">{{ comment.createdAt }}</span>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCourseById } from '../api/course'
import { getChaptersByCourseId } from '../api/chapter'
import { getVideosByCourseId } from '../api/video'
import { getCommentsByCourseId, createComment, getAverageRating } from '../api/comment'
import { checkFavorite, addFavorite, removeFavorite } from '../api/favorite'

export default {
  name: 'CourseDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const course = ref(null)
    const chapters = ref([])
    const videos = ref([])
    const comments = ref([])
    const rating = ref(0)
    const isFavorite = ref(false)
    const activeTab = ref('chapters')
    const newComment = ref({ rating: 5, content: '' })

    const isLoggedIn = computed(() => !!localStorage.getItem('token'))
    const courseId = computed(() => route.params.id)

    const loadCourse = async () => {
      try {
        const res = await getCourseById(courseId.value)
        course.value = res.data
      } catch (error) {
        console.error('获取课程失败', error)
      }
    }

    const loadChapters = async () => {
      try {
        const res = await getChaptersByCourseId(courseId.value)
        chapters.value = res.data
      } catch (error) {
        console.error('获取章节失败', error)
      }
    }

    const loadVideos = async () => {
      try {
        const res = await getVideosByCourseId(courseId.value)
        videos.value = res.data
      } catch (error) {
        console.error('获取视频失败', error)
      }
    }

    const loadComments = async () => {
      try {
        const res = await getCommentsByCourseId(courseId.value)
        comments.value = res.data
      } catch (error) {
        console.error('获取评论失败', error)
      }
    }

    const loadRating = async () => {
      try {
        const res = await getAverageRating(courseId.value)
        rating.value = res.data
      } catch (error) {
        console.error('获取评分失败', error)
      }
    }

    const checkIsFavorite = async () => {
      if (!isLoggedIn.value) return
      try {
        const res = await checkFavorite(courseId.value)
        isFavorite.value = res.data
      } catch (error) {
        console.error('检查收藏状态失败', error)
      }
    }

    const getVideosByChapter = (chapterId) => {
      return videos.value.filter(v => v.chapterId === chapterId)
    }

    const playVideo = (videoId) => {
      router.push(`/video/${videoId}`)
    }

    const startLearning = () => {
      if (videos.value.length > 0) {
        playVideo(videos.value[0].id)
      } else {
        ElMessage.warning('暂无视频内容')
      }
    }

    const toggleFavorite = async () => {
      if (!isLoggedIn.value) {
        ElMessage.warning('请先登录')
        router.push('/login')
        return
      }
      try {
        if (isFavorite.value) {
          await removeFavorite(courseId.value)
          isFavorite.value = false
          ElMessage.success('取消收藏成功')
        } else {
          await addFavorite(courseId.value)
          isFavorite.value = true
          ElMessage.success('收藏成功')
        }
      } catch (error) {
        console.error('操作失败', error)
      }
    }

    const submitComment = async () => {
      if (!newComment.value.content.trim()) {
        ElMessage.warning('请输入评价内容')
        return
      }
      try {
        await createComment({
          courseId: courseId.value,
          rating: newComment.value.rating,
          content: newComment.value.content
        })
        ElMessage.success('评价成功')
        newComment.value = { rating: 5, content: '' }
        loadComments()
        loadRating()
      } catch (error) {
        console.error('提交评价失败', error)
      }
    }

    onMounted(() => {
      loadCourse()
      loadChapters()
      loadVideos()
      loadComments()
      loadRating()
      checkIsFavorite()
    })

    return {
      course,
      chapters,
      videos,
      comments,
      rating,
      isFavorite,
      activeTab,
      newComment,
      isLoggedIn,
      getVideosByChapter,
      playVideo,
      startLearning,
      toggleFavorite,
      submitComment
    }
  }
}
</script>

<style scoped>
.course-detail {
  padding-top: 60px;
  min-height: 100vh;
  background: #f5f7fa;
}

.course-header {
  background: #fff;
  padding: 40px;
  border-bottom: 1px solid #e4e7ed;
}

.course-header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  gap: 40px;
}

.course-cover-large {
  width: 400px;
  height: 250px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.course-cover-large img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.course-header-info {
  flex: 1;
}

.course-header-info h1 {
  font-size: 28px;
  margin-bottom: 16px;
  color: #303133;
}

.course-description {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 20px;
}

.course-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 24px;
  color: #909399;
  font-size: 14px;
}

.course-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.course-actions {
  display: flex;
  gap: 12px;
}

.course-body {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 40px;
}

.course-content {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}

.chapter-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.chapter-item {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
}

.chapter-item h3 {
  font-size: 16px;
  margin-bottom: 12px;
  color: #303133;
}

.video-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.video-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.video-item:hover {
  background: #f5f7fa;
}

.video-title {
  flex: 1;
  color: #606266;
  font-size: 14px;
}

.comments-section {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comment-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.comment-item {
  padding: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.username {
  font-weight: 500;
  color: #303133;
}

.comment-content {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 8px;
}

.comment-time {
  color: #909399;
  font-size: 12px;
}
</style>
