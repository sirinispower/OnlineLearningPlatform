<template>
  <div class="video-player-page">
    <div class="player-container">
      <div class="video-wrapper">
        <video 
          ref="videoPlayer"
          :src="videoUrl"
          controls
          @timeupdate="handleTimeUpdate"
          @pause="handlePause"
          @ended="handleEnded"
          style="width: 100%; max-height: 600px;"
        ></video>
      </div>
      <div class="video-info" v-if="video">
        <h2>{{ video.title }}</h2>
        <p v-if="course">{{ course.title }}</p>
      </div>
    </div>
    <div class="course-sidebar">
      <h3>课程目录</h3>
      <div class="chapter-list">
        <div 
          v-for="chapter in chapters" 
          :key="chapter.id"
          class="chapter-item"
        >
          <h4>{{ chapter.title }}</h4>
          <div 
            v-for="v in getVideosByChapter(chapter.id)" 
            :key="v.id"
            class="sidebar-video-item"
            :class="{ active: v.id == videoId }"
            @click="switchVideo(v.id)"
          >
            <el-icon><VideoPlay /></el-icon>
            <span>{{ v.title }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getVideoById, getVideosByCourseId } from '../api/video'
import { getChaptersByCourseId } from '../api/chapter'
import { getCourseById } from '../api/course'
import { getProgress, saveProgress } from '../api/progress'

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
    const progress = ref(null)
    const lastSaveTime = ref(0)

    const videoId = computed(() => route.params.id)
    const videoUrl = computed(() => {
      if (!video.value?.url) return ''
      return video.value.url.startsWith('http') ? video.value.url : video.value.url
    })

    const loadVideo = async () => {
      try {
        const res = await getVideoById(videoId.value)
        video.value = res.data
        loadCourse(res.data.courseId)
        loadChapters(res.data.courseId)
        loadVideos(res.data.courseId)
        loadVideoProgress()
      } catch (error) {
        console.error('获取视频失败', error)
      }
    }

    const loadCourse = async (courseId) => {
      try {
        const res = await getCourseById(courseId)
        course.value = res.data
      } catch (error) {
        console.error('获取课程失败', error)
      }
    }

    const loadChapters = async (courseId) => {
      try {
        const res = await getChaptersByCourseId(courseId)
        chapters.value = res.data
      } catch (error) {
        console.error('获取章节失败', error)
      }
    }

    const loadVideos = async (courseId) => {
      try {
        const res = await getVideosByCourseId(courseId)
        videos.value = res.data
      } catch (error) {
        console.error('获取视频列表失败', error)
      }
    }

    const loadVideoProgress = async () => {
      try {
        const res = await getProgress(videoId.value)
        progress.value = res.data
        if (res.data && videoPlayer.value) {
          videoPlayer.value.currentTime = res.data.progress || 0
        }
      } catch (error) {
        console.error('获取进度失败', error)
      }
    }

    const getVideosByChapter = (chapterId) => {
      return videos.value.filter(v => v.chapterId === chapterId)
    }

    const switchVideo = (id) => {
      if (id !== videoId.value) {
        router.push(`/video/${id}`)
      }
    }

    const handleTimeUpdate = () => {
      const currentTime = videoPlayer.value.currentTime
      const now = Date.now()
      if (now - lastSaveTime.value > 10000) {
        saveVideoProgress(currentTime)
        lastSaveTime.value = now
      }
    }

    const handlePause = () => {
      if (videoPlayer.value) {
        saveVideoProgress(videoPlayer.value.currentTime)
      }
    }

    const handleEnded = () => {
      if (videoPlayer.value) {
        saveVideoProgress(videoPlayer.value.currentTime, true)
      }
    }

    const saveVideoProgress = async (currentTime, completed = false) => {
      try {
        await saveProgress({
          videoId: videoId.value,
          courseId: course.value?.id,
          progress: Math.floor(currentTime),
          duration: Math.floor(videoPlayer.value?.duration || 0),
          completed: completed
        })
      } catch (error) {
        console.error('保存进度失败', error)
      }
    }

    onMounted(() => {
      loadVideo()
    })

    watch(() => route.params.id, (newId, oldId) => {
      if (newId && newId !== oldId) {
        loadVideo()
      }
    })

    return {
      videoPlayer,
      video,
      course,
      chapters,
      videos,
      videoId,
      videoUrl,
      getVideosByChapter,
      switchVideo,
      handleTimeUpdate,
      handlePause,
      handleEnded
    }
  }
}
</script>

<style scoped>
.video-player-page {
  padding-top: 60px;
  display: flex;
  min-height: 100vh;
  background: #1a1a1a;
}

.player-container {
  flex: 1;
  padding: 20px;
}

.video-wrapper {
  background: #000;
  border-radius: 8px;
  overflow: hidden;
}

.video-info {
  margin-top: 16px;
  color: #fff;
}

.video-info h2 {
  font-size: 20px;
  margin-bottom: 8px;
}

.video-info p {
  color: #aaa;
  font-size: 14px;
}

.course-sidebar {
  width: 300px;
  background: #2a2a2a;
  padding: 20px;
  overflow-y: auto;
}

.course-sidebar h3 {
  color: #fff;
  margin-bottom: 16px;
  font-size: 16px;
}

.chapter-item {
  margin-bottom: 16px;
}

.chapter-item h4 {
  color: #ccc;
  font-size: 14px;
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px solid #444;
}

.sidebar-video-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  color: #aaa;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s;
  font-size: 13px;
}

.sidebar-video-item:hover {
  background: #3a3a3a;
  color: #fff;
}

.sidebar-video-item.active {
  background: #409EFF;
  color: #fff;
}
</style>
