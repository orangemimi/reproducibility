<template>
  <div>
    <el-row>
      <el-col :span="18" :offset="3">
        <el-row :gutter="20">
          <el-col :span="16">
            <h2 style="margin-left: 10px; margin-top: 10px">Abstract:</h2>
            <mavon-editor :value="value" language="en" style="height: 700px; margin-left: 10px" :toolbars="toolbars" ref="md"/>
          </el-col>
          <el-col :span="8">
            <div class="right">
              <el-button icon="el-icon-edit" circle style="margin-right: 3px" @click="titleClick"></el-button>
              <span v-if="editTitle == false" class="title">{{ title }}</span>
              <el-input
                v-model="title"
                placeholder="Please enter the content"
                v-if="editTitle == true"
                style="width: 200px"
                @blur="blur"
                @keyup.enter.native="enter"
                ref="input"
              ></el-input>
              <div>
                <el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)">
                  {{ tag }}
                </el-tag>
                <el-input
                  class="input-new-tag"
                  v-if="inputVisible"
                  v-model="inputValue"
                  ref="saveTagInput"
                  size="small"
                  @keyup.enter.native="handleInputConfirm"
                  @blur="handleInputConfirm"
                ></el-input>
                <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
              </div>
              <el-divider><i class="el-icon-mobile-phone"></i></el-divider>
              <el-form label-position="top" label-width="80px" :model="form">
                <el-form-item label="Purpose">
                  <el-input type="textarea" :rows="3" placeholder="Please enter the purpose" v-model="form.purpose" resize="none"></el-input>
                </el-form-item>
              </el-form>
              <el-divider><i class="el-icon-mobile-phone"></i></el-divider>
            </div>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="24" style="text-align: center;margin-top:24px">
        <el-button type="primary" round @click="updateEssentialInformation">Submit</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getContextByProject, updateEssentialInformation } from '@/api/request';
export default {
  data() {
    return {
      projectId: this.$route.params.projectId,
      dynamicTags: [],
      value: '',
      inputVisible: false,
      inputValue: '',
      title: '',
      editTitle: false,
      form: {
        purpose: ''
      },
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
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.dynamicTags.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = '';
    },

    async getContextByProject() {
      let data = await getContextByProject(this.projectId);
      this.title = data.essentialInformation.name;
      this.form.purpose = data.essentialInformation.purpose;
      this.dynamicTags = data.essentialInformation.keyWords;
      this.value = data.essentialInformation.abstractText;
    },
    async updateEssentialInformation() {
      let jsonData = {
        name: this.title,
        purpose: this.form.purpose,
        keyWords: this.dynamicTags,
        abstractText: this.$refs.md.d_value,
        abstractRender: this.$refs.md.d_render,
      };
      console.log(jsonData);
      await updateEssentialInformation(this.projectId, jsonData);
    },

    init() {
      this.getContextByProject();
    },
  },
  mounted() {
    this.init();
  },
};
</script>

<style lang="scss" scoped>
.el-tag + .el-tag {
  margin-left: 10px;
  margin-top: 10px;
}
.button-new-tag {
  margin-top: 10px;
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  margin-top: 10px;
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
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