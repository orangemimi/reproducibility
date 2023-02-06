<template>
  <div>
    <el-row>
      <el-col :span="18" :offset="3">
        <el-row :gutter="20">
          <el-col :span="16">
            <h2 style="margin-left: 10px; margin-top: 10px">Abstract:</h2>
            <mavon-editor :value="value" language="en" style="height: 700px; margin-left: 10px" :toolbars="toolbars" ref="md" />
          </el-col>
          <el-col :span="8">
            <!-- <div>
              <el-dropdown trigger="click" style="float: right; margin-top: 10px" @command="handleCommand">
                <span class="el-dropdown-link">
                  <el-button icon="el-icon-plus" circle size="mini"></el-button>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item icon="iconfont icon-Tool" command="temporalInfo">TemporalInfo</el-dropdown-item>
                  <el-dropdown-item icon="iconfont icon-Tool" command="spatialInfo">SpatialInfo</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div> -->
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
            <!-- <div class="plus">
              <div v-if="temporalInfoFlag">
                <temporal-info @close="temporalInfoClose" @date="getTemporalInfo" :temporalInfo="temporalInfo"/>
              </div>
              <div v-if="spatialInfoFlag" class="spatialInfo">
                <spatial-info @close="spatialInfoClose" @selectData="getSpatialInfo" :spatialInfo="spatialInfo"/>
              </div>
            </div> -->
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="24" style="text-align: center; margin-top: 24px; margin-bottom: 24px">
        <el-button type="primary" round @click="updateContext">Submit</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getContextByProject, updateContext, updatePerformanceById } from '@/api/request';
import { dateFormat } from '@/utils/utils';
// import temporalInfo from './TemporalInfo.vue';
// import spatialInfo from './SpatialInfo.vue';
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

      temporalInfoFlag: false,
      spatialInfoFlag: false,
      temporalInfo: [],
      spatialInfo: [],

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

  // components: {
  //   temporalInfo,
  //   spatialInfo,
  // },
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
      console.log(data);
      this.title = data.essentialInformation.name;
      this.form.purpose = data.essentialInformation.purpose;
      this.dynamicTags = data.essentialInformation.keyWords;
      this.value = data.essentialInformation.abstractText;
    },

    async updateContext() {
      let jsonData = {
        projectId: this.projectId,
        context: {
          essentialInformation: {
            name: this.title,
            purpose: this.form.purpose,
            keyWords: this.dynamicTags,
            abstractText: this.$refs.md.d_value,
            abstractRender: this.$refs.md.d_render
          },
          spatialInfos: [],
          temporalInfo: {}
        }
      };
      if (this.spatialInfo.length > 0 && this.spatialInfoFlag != false) {
        this.spatialInfo.forEach(item => {
          if (item.type == 'polygon') {
            jsonData.context.spatialInfos.push({
              name: item.name,
              type: item.type,
              points: item.points
            });
          } else {
            jsonData.context.spatialInfos.push({
              name: item.name,
              type: item.type,
              value: item.value
            });
          }
        });
      }
      if (this.temporalInfo.length > 0 && this.temporalInfoFlag != false) {
        jsonData.context.temporalInfo.start = dateFormat(this.temporalInfo[0]);
        jsonData.context.temporalInfo.end = dateFormat(this.temporalInfo[1]);
      }
      await updateContext(jsonData);
      await updatePerformanceById('context', this.projectId, { content: 'update context' });
    },

    handleCommand(val) {
      console.log(val);
      if (val == 'temporalInfo') {
        this.temporalInfoFlag = true;
      } else if (val == 'spatialInfo') {
        this.spatialInfoFlag = true;
      }
    },

    temporalInfoClose() {
      this.temporalInfoFlag = false;
    },

    spatialInfoClose() {
      this.spatialInfoFlag = false;
    },
    getTemporalInfo(val) {
      this.temporalInfo = val;
      console.log(dateFormat(val[0]));
      console.log(val[0].getDate());
    },
    getSpatialInfo(val) {
      this.spatialInfo = val;
      console.log(val);
    },

    init() {
      this.getContextByProject();
    }
  },
  mounted() {
    this.init();
  }
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

.plus {
  .spatialInfo {
    margin-top: 20px;
  }
}
</style>
