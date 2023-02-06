<template>
  <div class="mian">
    <div v-if="type == 'description'" class="description">
      <div class="title">Description</div>
      <div class="text">
        <el-input
          type="textarea"
          :rows="7"
          placeholder="Please enter the content"
          v-model="textareaDescription"
          style="width: 80%; margin-top: 15px"
        ></el-input>
      </div>
    </div>
    <div v-if="type == 'other'" class="other">
      <div class="title">Other</div>
      <div class="text">
        <el-input type="textarea" :rows="7" placeholder="Please enter the content" v-model="textareaOther" style="width: 80%; margin-top: 15px"></el-input>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    type: {
      type: String
    }
  },
  data() {
    return {
      textareaDescription:
        '研究历史时期年代际气候变化为年代际气候预测提供了自然背景约束，对于完善气候学基础理论、理解地球气候多尺度变化规律、推动古气候与现代气候的交叉融合具有重要的科学意义。该研究借助通用地球系统模式CESM、区域气候模式RegCM和WRF，开展了不同外强驱动下的全新世气候模拟试验(NNU-Holocene)，探究了历史时期多尺度气候变化的史实和特征，阐明了过去1万年全球气候的多时间尺度变化特征，以及主控年代尺度气候变化的关键因子；揭示了自然外强迫与气候系统内部自然反馈过程共同驱动下的历史时期东亚年代际气候变化的驱动机制。',
      textareaOther: ''
    };
  }
};
</script>

<style lang="scss" scoped>
.description {
  height: 100%;
  width: 100%;
}
.title {
  font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Helvetica, Arial, sans-serif, Apple Color Emoji, Segoe UI Emoji;
  font-size: 16px;
  font-weight: 700;
  margin-top: 40px;
  margin-left: 20px;
}
.text {
  text-align: center;
}
</style>
