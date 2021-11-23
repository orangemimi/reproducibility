<template>
  <div class="main">
    <div class="title">
      Essential Information
      <el-button type="primary" icon="iconfont icon-save-fill" size="mini" plain round>Save</el-button>
    </div>
    <div class="content">
      <div ref="div" v-if="editTitle == true"></div>
      <el-row>
        <el-col :span="12">
          <el-button size="mini" icon="el-icon-edit" circle @click="titleClick" style="margin-right: 3px"></el-button>
          <span v-if="editTitle == false">{{ title }}</span>
          <el-input
            v-model="title"
            placeholder="Please enter the content"
            v-if="editTitle == true"
            style="width: 200px"
            @blur="blur"
            @keyup.enter.native="enter"
            size="mini"
            ref="input"
          ></el-input>
        </el-col>
        <el-col :span="12">
          <!-- <div>
            Keywords:
            
          </div> -->
          <!-- <el-button size="mini" icon="el-icon-plus" style="margin-right: 3px">New Tag</el-button> -->
          <div class="tags">
            <div class="tag" ref="tag">
              <el-tag size="mini" type="info">+</el-tag>
              <el-tag size="mini" closable v-for="(item, index) in tagData" :key="index" class="tag-item" v-show="index < 3"><span>{{ item }}</span></el-tag>
            </div>
            <el-tag size="mini" v-if="tagData.length > 3">...</el-tag>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">Purpose:</el-col>
        <el-col :span="12" style="display: table">Abstract:</el-col>
        <el-col :span="12" style="display: table">
          <el-input
            type="textarea"
            :rows="5"
            placeholder="Please enter the content"
            v-model="textareaPurpose"
            style="width: 80%; margin-top: 5px"
            resize="none"
          ></el-input>
        </el-col>
        <el-col :span="12" style="display: table">
          <el-input
            type="textarea"
            :rows="5"
            placeholder="Please enter the content"
            v-model="textareaAbstract"
            style="width: 80%; margin-top: 5px"
            resize="none"
          ></el-input>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      textareaPurpose:
        '研究历史时期年代际气候变化为年代际气候预测提供了自然背景约束，对于完善气候学基础理论、理解地球气候多尺度变化规律、推动古气候与现代气候的交叉融合具有重要的科学意义',
      textareaAbstract:
        '该研究借助通用地球系统模式CESM、区域气候模式RegCM和WRF，开展了不同外强驱动下的全新世气候模拟试验(NNU-Holocene)，探究了历史时期多尺度气候变化的史实和特征，阐明了过去1万年全球气候的多时间尺度变化特征，以及主控年代尺度气候变化的关键因子；揭示了自然外强迫与气候系统内部自然反馈过程共同驱动下的历史时期东亚年代际气候变化的驱动机制。',
      editTitle: false,
      title: 'TEST',
      tagData: ['1', 'K-means','敏感性分析111111', 'dsadsa' ],
    };
  },
  methods: {
    async titleClick() {
      this.editTitle = !this.editTitle;
      //$nextTick()相当于setTime()函数，当dom元素全部渲染时执行，此处使用是因为ref无法获取v-if='false'的dom，需要在dom加载后再执行用ref获取（document.getElementById函数同理!）
      await this.$nextTick();
      this.$refs.input.focus();
    },
    enter() {
      this.$refs.input.blur();
    },
    blur() {
      this.editTitle = !this.editTitle;
    },
    test() {
      
    },
  },
};
</script>

<style lang="scss" scoped>
.main {
  height: 100%;
  width: 100%;

  .title {
    font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Helvetica, Arial, sans-serif, Apple Color Emoji, Segoe UI Emoji;
    font-size: 16px;
    font-weight: 700;
    margin-top: 40px;
    margin-left: 20px;
  }
  .content {
    margin-top: 15px;
    margin-left: 20px;
    .el-row {
      margin-bottom: 10px;
      .tags {
        width: 80%;
        display: flex;
        .tag {
          max-width: calc(100% - 23px);
          .tag-item {
            width: calc((100% - 35px) / 3);
            text-align: center;
          }
        }
      }
    }
  }
}
.el-tag {
  margin-right: 3px;
}
</style>