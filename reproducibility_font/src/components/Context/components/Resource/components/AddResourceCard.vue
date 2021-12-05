<template>
  <div>
    <el-row>
      <el-col :span="16" :offset="4" style="margin-top: 30px">
        <el-row :gutter="20">
          <el-col :span="16">
            <h2>Supplement</h2>
            <mavon-editor :value="value" language="en" style="height: 700px; margin-top: 10px" ref="md" :toolbars="toolbars" />
          </el-col>
          <el-col :span="8">
            <div class="right">
              <el-form label-position="top" label-width="80px" :model="form" :rules="rules" ref="form">
                <el-form-item label="Name" prop="name">
                  <el-input placeholder="Please enter the name" v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="Type" prop="type">
                  <el-select v-model="form.type" placeholder="Please enter the type" @change="change">
                    <el-option label="input" value="input"></el-option>
                    <el-option label="parameter" value="parameter"></el-option>
                    <el-option label="output" value="output"></el-option>
                    <el-option label="model" value="model"></el-option>
                    <el-option label="dataservice" value="dataservice"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="Event Name" prop="eventName">
                  <el-input
                    placeholder="Please enter the event name"
                    v-model="form.eventName"
                    :disabled="form.type == '' || form.type == 'model' || form.type == 'dataservice'"
                  ></el-input>
                </el-form-item>
                
                <el-form-item label="Data Type" prop="dataType">
                  <el-radio v-model="form.dataType" label="FILE" :disabled="form.type != 'output'"></el-radio>
                  <el-radio v-model="form.dataType" label="STRING" :disabled="!(form.type == 'output' || form.type == 'parameter')"></el-radio>
                  <el-radio v-model="form.dataType" label="INT" :disabled="!(form.type == 'output' || form.type == 'parameter')"></el-radio>
                </el-form-item>

                <el-form-item label="Description">
                  <el-input type="textarea" :rows="3" placeholder="Please enter the event description" v-model="form.description" resize="none"></el-input>
                </el-form-item>
              </el-form>
              <el-divider><i class="el-icon-mobile-phone"></i></el-divider>
            </div>
          </el-col>
        </el-row>
        <el-col :span="24" style="text-align: center; margin-top: 24px">
          <el-button type="primary" round @click="create">Submit</el-button>
        </el-col>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { addResourceCard } from '@/api/request'
export default {
  data() {
    return {
      value: '',
      form: {
        name: '',
        type: '',
        dataType: '',
        eventName: '',
        description: '',
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
      rules: {
        name: [{ required: true, message: 'Please enter the name', trigger: 'blur' }],
        type: [{ required: true, message: 'Please select the type', trigger: 'change' }],
      },
      projectId: this.$route.params.projectId,
    };
  },
  methods: {
    async addResourceCard() {
      if(this.form.type == 'input') {
        let form = {
          projectId: this.projectId,
          resourceType: this.form.type,
          info: {
            name: this.form.name,
            eventName: this.form.eventName,
            description: this.form.description,
            markDown: this.$refs.md.d_value,
            markDownHtml: this.$refs.md.d_render
          }
        }
        await addResourceCard(form)
      } else if(this.form.type == 'parameter') {
        let form = {
          projectId: this.projectId,
          resourceType: this.form.type,
          info: {
            name: this.form.name,
            eventName: this.form.eventName,
            type: this.form.dataType,
            description: this.form.description,
            markDown: this.$refs.md.d_value,
            markDownHtml: this.$refs.md.d_render
          }
        }
        await addResourceCard(form)
      } else if(this.form.type == 'output') {
        let form = {
          projectId: this.projectId,
          resourceType: this.form.type,
          info: {
            name: this.form.name,
            eventName: this.form.eventName,
            type: this.form.dataType,
            description: this.form.description,
            markDown: this.$refs.md.d_value,
            markDownHtml: this.$refs.md.d_render
          }
        }
        await addResourceCard(form)
      } else if(this.form.type == 'model' || this.form.type == 'dataservice') {
        let form = {
          projectId: this.projectId,
          resourceType: this.form.type,
          info: {
            name: this.form.name,
            description: this.form.description,
            markDown: this.$refs.md.d_value,
            markDownHtml: this.$refs.md.d_render
          }
        }
        await addResourceCard(form)
      }
    },
    create() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          await this.addResourceCard()
        } else {
          return false;
        }
      });
    },
    change(val) {
      if (val == 'parameter') {
        this.form.dataType = 'STRING'
        this.$refs.form.clearValidate();
        this.rules = {
          name: [{ required: true, message: 'Please enter the name', trigger: 'blur' }],
          type: [{ required: true, message: 'Please select the type', trigger: 'change' }],
          eventName: [{ required: true, message: 'Please enter the eventName', trigger: 'blur' }],
        };
      } else if (val == 'input') {
        this.form.value = ''
        this.form.dataType = ''
        this.$refs.form.clearValidate();
        this.rules = {
          name: [{ required: true, message: 'Please enter the name', trigger: 'blur' }],
          type: [{ required: true, message: 'Please select the type', trigger: 'change' }],
        };
      } else if (val == 'model' || val == 'dataservice') {
        this.form.eventName = ''
        this.form.value = ''
        this.form.dataType = ''
        this.$refs.form.clearValidate();
        this.rules = {
          name: [{ required: true, message: 'Please enter the name', trigger: 'blur' }],
          type: [{ required: true, message: 'Please select the type', trigger: 'change' }],
        };
      } else if (val == 'output') {
        this.form.value = ''
        this.form.dataType = 'FILE'
        this.$refs.form.clearValidate();
        this.rules = {
          name: [{ required: true, message: 'Please enter the name', trigger: 'blur' }],
          type: [{ required: true, message: 'Please select the type', trigger: 'change' }],
          eventName: [{ required: true, message: 'Please enter the eventName', trigger: 'blur' }],
        };
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.el-radio {
  margin-top: 10px;
}
.right /deep/ .el-divider__text {
  background-color: #f6f6f6;
}
</style>