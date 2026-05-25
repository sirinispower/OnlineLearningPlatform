<template>
  <div class="video-player-page" v-loading="pageLoading">
    <header class="player-header">
      <el-button text class="back-btn" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回课程
      </el-button>
      <div class="header-meta" v-if="course">
        <span class="course-name">{{ course.title }}</span>
        <el-divider direction="vertical" />
        <span class="video-name">{{ video?.title || '加载中...' }}</span>
      </div>
    </header>

    <div class="player-layout">
      <main class="player-main">
        <div class="video-wrapper">
          <video
            v-if="videoUrl"
            ref="videoPlayer"
            :key="videoId"
            :src="videoUrl"
            controls
            controlslist="nodownload"
            @loadedmetadata="applyPlaybackRate"
          ></video>
          <div v-else-if="!pageLoading" class="video-placeholder">
            <el-icon :size="48"><VideoCamera /></el-icon>
            <p>暂无视频资源</p>
          </div>
        </div>

        <div class="player-toolbar">
          <div class="toolbar-group">
            <span class="toolbar-label">倍速</span>
            <el-select v-model="playbackRate" size="small" style="width: 88px" @change="changePlaybackRate">
              <el-option v-for="rate in playbackRates" :key="rate" :label="`${rate}x`" :value="rate" />
            </el-select>
          </div>
          <div class="toolbar-group nav-group">
            <el-button :disabled="!prevVideo" @click="switchVideo(prevVideo.id)">
              <el-icon><ArrowLeft /></el-icon>
              上一节
            </el-button>
            <el-button type="primary" :disabled="!nextVideo" @click="switchVideo(nextVideo.id)">
              下一节
              <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
        </div>

        <section class="video-info-card" v-if="video">
          <h1>{{ video.title }}</h1>
          <p class="course-link" v-if="course" @click="goBack">{{ course.title }}</p>
        </section>
      </main>

      <aside class="course-sidebar">
        <div class="sidebar-header" v-if="course">
          <img :src="course.cover || defaultCover" :alt="course.title" class="sidebar-cover" />
          <h3>{{ course.title }}</h3>
          <p>{{ flatVideoList.length }} 个视频 · {{ chapters.length }} 个章节</p>
        </div>

        <div class="sidebar-body">
          <el-empty v-if="!chapters.length && !pageLoading" description="暂无课程目录" :image-size="64" />
          <el-collapse v-else v-model="activeChapters">
            <el-collapse-item
              v-for="chapter in chapters"
              :key="chapter.id"
              :name="chapter.id"
              :title="chapter.title"
            >
              <div
                v-for="v in getVideosByChapter(chapter.id)"
                :key="v.id"
                class="sidebar-video-item"
                :class="{ active: Number(v.id) === Number(videoId) }"
                @click="switchVideo(v.id)"
              >
                <div class="item-icon">
                  <el-icon v-if="Number(v.id) === Number(videoId)"><VideoPause /></el-icon>
                  <el-icon v-else><VideoPlay /></el-icon>
                </div>
                <div class="item-content">
                  <span class="item-title">{{ v.title }}</span>
                </div>
              </div>
              <div v-if="!getVideosByChapter(chapter.id).length" class="empty-chapter">本章暂无视频</div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </aside>
    </div>
  </div>
</template>

<script>
import { ref, computed, watch, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getVideoById, getVideosByCourseId } from '../api/video'
import { getChaptersByCourseId } from '../api/chapter'
import { getCourseById } from '../api/course'

export default {
  name: 'VideoPlayer',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const videoPlayer = ref(null)
    const video = ref(null)
    const course = ref(null)
    const chapters = ref([])
    const videos = ref([])
    const pageLoading = ref(true)
    const playbackRate = ref(1)
    const activeChapters = ref([])
    const defaultCover = 'https://via.placeholder.com/320x180/409EFF/ffffff?text=Course'

    const playbackRates = [0.5, 0.75, 1, 1.25, 1.5, 2]

    const videoId = computed(() => route.params.id)
    const videoUrl = computed(() => video.value?.url || '')

    const flatVideoList = computed(() => {
      return chapters.value.flatMap(chapter =>
        getVideosByChapter(chapter.id).map(v => ({ ...v, chapterId: chapter.id }))
      )
    })

    const currentIndex = computed(() =>
      flatVideoList.value.findIndex(v => Number(v.id) === Number(videoId.value))
    )

    const prevVideo = computed(() =>
      currentIndex.value > 0 ? flatVideoList.value[currentIndex.value - 1] : null
    )

    const nextVideo = computed(() =>
      currentIndex.value >= 0 && currentIndex.value < flatVideoList.value.length - 1
        ? flatVideoList.value[currentIndex.value + 1]
        : null
    )

    const getVideosByChapter = (chapterId) => {
      return videos.value.filter(v => v.chapterId === chapterId)
    }

    const loadVideo = async () => {
      pageLoading.value = true
      const targetVideoId = route.params.id
      try {
        const res = await getVideoById(targetVideoId)
        video.value = res.data
        const courseId = res.data.courseId

        const [courseRes, chaptersRes, videosRes] = await Promise.all([
          getCourseById(courseId),
          getChaptersByCourseId(courseId),
          getVideosByCourseId(courseId)
        ])

        course.value = courseRes.data
        chapters.value = chaptersRes.data || []
        videos.value = videosRes.data || []

        const currentChapterId = res.data.chapterId
        activeChapters.value = currentChapterId
          ? [currentChapterId]
          : chapters.value.length ? [chapters.value[0].id] : []

        await nextTick()
        applyPlaybackRate()
      } catch (error) {
        console.error('加载视频失败', error)
      } finally {
        pageLoading.value = false
      }
    }

    const applyPlaybackRate = () => {
      if (videoPlayer.value) {
        videoPlayer.value.playbackRate = playbackRate.value
      }
    }

    const changePlaybackRate = () => {
      applyPlaybackRate()
    }

    const switchVideo = (id) => {
      if (Number(id) === Number(videoId.value)) return
      router.push(`/video/${id}`)
    }

    const goBack = () => {
      if (course.value?.id) {
        router.push(`/course/${course.value.id}`)
      } else {
        router.push('/courses')
      }
    }

    onMounted(() => {
      loadVideo()
    })

    watch(() => route.params.id, (newId, oldId) => {
      if (route.name !== 'VideoPlayer') return
      if (!newId || newId === oldId) return
      playbackRate.value = 1
      video.value = null
      loadVideo()
    })

    return {
      videoPlayer,
      video,
      course,
      chapters,
      videoId,
      videoUrl,
      pageLoading,
      playbackRate,
      playbackRates,
      activeChapters,
      defaultCover,
      flatVideoList,
      prevVideo,
      nextVideo,
      getVideosByChapter,
      switchVideo,
      goBack,
      applyPlaybackRate,
      changePlaybackRate
    }
  }
}
</script>

<style scoped>
.video-player-page {
  min-height: 100vh;
  padding-top: 60px;
  background: #f0f2f5;
}

.player-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 24px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
}

.back-btn {
  color: #606266;
  font-size: 14px;
}

.header-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
  color: #909399;
  font-size: 14px;
}

.course-name {
  color: #409eff;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 240px;
}

.video-name {
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.player-layout {
  display: flex;
  gap: 20px;
  padding: 20px 24px 32px;
  max-width: 1440px;
  margin: 0 auto;
}

.player-main {
  flex: 1;
  min-width: 0;
}

.video-wrapper {
  background: #000;
  border-radius: 12px;
  overflow: hidden;
  aspect-ratio: 16 / 9;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.video-wrapper video {
  width: 100%;
  height: 100%;
  display: block;
  background: #000;
}

.video-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
  background: #1a1a1a;
}

.video-placeholder p {
  margin-top: 12px;
  font-size: 14px;
}

.player-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 16px;
  padding: 12px 16px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.toolbar-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.toolbar-label {
  font-size: 13px;
  color: #909399;
}

.nav-group .el-button + .el-button {
  margin-left: 8px;
}

.video-info-card {
  margin-top: 16px;
  padding: 20px 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.video-info-card h1 {
  font-size: 22px;
  color: #303133;
  line-height: 1.4;
}

.course-link {
  margin-top: 8px;
  font-size: 14px;
  color: #409eff;
  cursor: pointer;
}

.course-link:hover {
  text-decoration: underline;
}

.course-sidebar {
  width: 340px;
  flex-shrink: 0;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  overflow: hidden;
  max-height: calc(100vh - 140px);
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid #f0f2f5;
}

.sidebar-cover {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 12px;
  background: #f5f7fa;
}

.sidebar-header h3 {
  font-size: 16px;
  color: #303133;
  line-height: 1.4;
  margin-bottom: 6px;
}

.sidebar-header p {
  font-size: 12px;
  color: #909399;
}

.sidebar-body {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0 16px;
}

.sidebar-body :deep(.el-collapse) {
  border: none;
}

.sidebar-body :deep(.el-collapse-item__header) {
  padding: 0 16px;
  height: 44px;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  border-bottom: 1px solid #f5f7fa;
}

.sidebar-body :deep(.el-collapse-item__wrap) {
  border-bottom: none;
}

.sidebar-body :deep(.el-collapse-item__content) {
  padding: 4px 8px 8px;
}

.sidebar-video-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 10px 12px;
  margin-bottom: 4px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}

.sidebar-video-item:hover {
  background: #f5f7fa;
}

.sidebar-video-item.active {
  background: #ecf5ff;
  color: #409eff;
}

.item-icon {
  flex-shrink: 0;
  margin-top: 2px;
  font-size: 16px;
  color: #909399;
}

.sidebar-video-item.active .item-icon {
  color: #409eff;
}

.item-content {
  flex: 1;
  min-width: 0;
}

.item-title {
  display: block;
  font-size: 13px;
  line-height: 1.5;
  color: #303133;
}

.sidebar-video-item.active .item-title {
  color: #409eff;
  font-weight: 500;
}

.empty-chapter {
  padding: 8px 12px;
  font-size: 12px;
  color: #c0c4cc;
}

@media (max-width: 992px) {
  .player-layout {
    flex-direction: column;
    padding: 16px;
  }

  .course-sidebar {
    width: 100%;
    max-height: none;
  }

  .header-meta {
    display: none;
  }
}
</style>
