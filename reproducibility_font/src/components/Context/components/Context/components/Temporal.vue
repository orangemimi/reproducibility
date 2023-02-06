<template>
  <div>
    <el-row>
      <el-col :span="18" :offset="3">
        <el-row :gutter="20">
          <el-col :span="16">
            <h2 style="margin-left: 10px; margin-top: 10px">Description:</h2>
            <mavon-editor :value="value" language="en" style="height: 700px; margin-left: 10px" :toolbars="toolbars" ref="md" />
          </el-col>
          <el-col :span="8">
            <div class="plus">
              <div class="temporal">
                <temporal-info @date="getTemporalInfo" :temporalInfo="temporalInfo" />
              </div>
            </div>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="24" style="text-align: center; margin-top: 24px; margin-bottom: 24px">
        <el-button type="primary" round @click="updateTemporal">Submit</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getTemporal, updateTemporal } from '@/api/request';
// import { dateFormat } from '@/utils/utils';
import temporalInfo from './TemporalInfo.vue';

export default {
  data() {
    return {
      projectId: this.$route.params.projectId,
      value: '',
      temporalInfo: [],
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: false, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true // 预览
      }
    };
  },

  components: {
    temporalInfo
  },
  methods: {
    async getTemporal() {
      let data = await getTemporal(this.projectId);
      console.log(data);
      if (data.mdText != null && data.mdText != undefined) {
        this.value = data.mdText;
      } else {
        this.value = '';
      }
      if (data.end != undefined && data.end != null) {
        this.temporalInfo.push(data.start);
        this.temporalInfo.push(data.end);
      }
    },

    async updateTemporal() {
      let jsonData = {};
      if (this.temporalInfo == null) {
        jsonData = {
          start: null,
          end: null,
          mdText: this.$refs.md.d_value,
          htmlText: this.$refs.md.d_render
        };
      } else {
        jsonData = {
          start: this.temporalInfo[0],
          end: this.temporalInfo[1],
          mdText: this.$refs.md.d_value,
          htmlText: this.$refs.md.d_render
        };
      }

      await updateTemporal(jsonData, this.projectId);
    },

    getTemporalInfo(val) {
      this.temporalInfo = val;
    },

    async init() {
      await this.getTemporal();
    }
  },
  async mounted() {
    await this.init();
  }
};
</script>

<style lang="scss" scoped>
.plus {
  .temporal {
    margin-top: 30px;
  }
}
</style>
