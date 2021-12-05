<template>
  <div>
    <el-row>
      <el-col :span="16" :offset="4" style="margin-top: 30px">
        <el-row :gutter="20">
          <el-col :span="16">
            <h2>Supplement</h2>
            <mavon-editor :value="resourceItem.markDown" language="en" style="height: 700px; margin-top: 10px" ref="md" :toolbars="toolbars" :ishljs="true" />
          </el-col>
          <el-col :span="8">
            <div class="right">
              <el-button icon="el-icon-edit" circle style="margin-right: 3px" @click="titleClick"></el-button>
              <span v-if="editTitle == false" class="title">{{ resourceItem.name }}</span>
              <el-input
                v-model="resourceItem.name"
                placeholder="Please enter the content"
                v-if="editTitle == true"
                style="width: 200px"
                @blur="blur"
                @keyup.enter.native="enter"
                ref="input"
              ></el-input>
              <div style="margin-top: 20px">
                <el-tag :type="getFormat" effect="dark">
                  {{ resourceItem.format }}
                </el-tag>
                <el-tag v-if="resourceItem.type != undefined" style="margin-left: 10px" :type="getType">
                  {{ resourceItem.type }}
                </el-tag>
              </div>
              <el-divider><i class="el-icon-mobile-phone" style="color: "></i></el-divider>

              <el-form label-position="top" label-width="80px" :model="resourceItem">
                <el-form-item label="Event Name" v-if="resourceItem.eventName != undefined">
                  <el-input placeholder="Please enter the event name" v-model="resourceItem.eventName"></el-input>
                </el-form-item>
                <el-form-item label="Description">
                  <el-input type="textarea" :rows="3" placeholder="Please enter the description" v-model="resourceItem.description" resize="none"></el-input>
                </el-form-item>
              </el-form>
              <el-divider><i class="el-icon-mobile-phone"></i></el-divider>
            </div>
          </el-col>
        </el-row>
        <el-col :span="24" style="text-align: center; margin-top: 24px">
          <el-button type="primary" round @click="update">Submit</el-button>
        </el-col>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { updateResourceOfContent } from '@/api/request';
export default {
  data() {
    return {
      projectId: this.$route.params.projectId,
      editTitle: false,
      resourceItem: '',
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
        preview: true, // 预览
      },
    };
  },
  computed: {
    getFormat() {
      if (this.resourceItem.format == 'input') {
        return '';
      } else if (this.resourceItem.format == 'parameter') {
        return 'info';
      } else if (this.resourceItem.format == 'output') {
        return 'success';
      } else if (this.resourceItem.format == 'model') {
        return 'danger';
      } else {
        return 'warning';
      }
    },
    getType() {
      if (this.resourceItem.type == 'INT') {
        return '';
      } else if (this.resourceItem.type == 'STRING') {
        return 'success';
      } else {
        return 'warning';
      }
    },
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
    async update() {
      let form = {
        projectId: this.projectId,
        format: this.resourceItem.format,
        bean: JSON.parse(JSON.stringify(this.resourceItem)),
      };
      form.bean.markDown = this.$refs.md.d_value;
      form.bean.markDownHtml = this.$refs.md.d_render;

      await updateResourceOfContent(form);
    },
  },
  created() {
    let obj = decodeURIComponent(this.$route.query.resourceItem);
    this.resourceItem = JSON.parse(obj);
    console.log(this.resourceItem);
  },
};
</script>

<style lang="scss" scoped>
.right {
  margin-top: 40px;
  .title {
    font-size: 22px;
  }
}
.right /deep/ .el-divider__text {
  background-color: #f6f6f6;
}
</style>