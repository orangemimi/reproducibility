<template>
  <div class="main">
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 18, offset: 3 }">
      <div class="main-card">
        <el-tabs type="border-card" style="min-height: calc(100vh - 265px)">
          <el-tab-pane>
            <el-dropdown split-button type="text" slot="label" trigger="click">
              <i class="el-icon-document"></i>
              Context
              <el-dropdown-menu slot="dropdown">
                <el-cascader-panel :options="options"></el-cascader-panel>
              </el-dropdown-menu>
            </el-dropdown>
            <step-card :cardInfo="{ btnType: 'Context Definition' }"></step-card>

          </el-tab-pane>
          <el-tab-pane>
            <span slot="label">
              <i class="iconfont icon-resource-manage"></i>
              Resource
            </span>
            <el-row>
              <el-col :span="24">
                <step-card :cardInfo="{ btnType: 'Resource Definition' }"></step-card>
              </el-col>
              <el-col :span="24">
                <step-card :cardInfo="{ btnType: 'Resource Collection' }" style="height: 450px; width: 100%; margin-top: 20px"></step-card>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane>
            <span slot="label">
              <i class="iconfont icon-changjingguanli"></i>
              Scenario
            </span>
            <!-- <el-col :span="24">
              <step-card :cardInfo="{ btnType: 'Scenario Definition' }"></step-card>
            </el-col> -->
            <el-col :span="24">
              <step-card :cardInfo="{ btnType: 'Simulation Scenario' }" style="height: 950px; width: 100%;"></step-card>
            </el-col>
          </el-tab-pane>
          <el-tab-pane>
            <span slot="label">
              <i class="iconfont icon-result"></i>
              Result analysis
            </span>
            <div style="padding-top: 100px">
              <wang-editor />
            </div>
          </el-tab-pane>
          <el-tab-pane>
            <span slot="label">
              <i class="iconfont icon-comparison-analysis"></i>
              Comparision analysis
            </span>
            定时任务补偿
          </el-tab-pane>
        </el-tabs>
      </div>
      <div class="record">
        <div class="block">
          <el-timeline>
            <el-timeline-item
              :icon="completion.context.icon"
              :type="completion.context.type"
              size="large"
              :timestamp="dateFormat(completion.context.updateTime)"
            >
              {{ completion.context.content }}
            </el-timeline-item>

            <el-timeline-item
              :icon="completion.resource.icon"
              :type="completion.resource.type"
              size="large"
              :timestamp="dateFormat(completion.resource.updateTime)"
            >
              {{ completion.resource.content }}
            </el-timeline-item>

            <el-timeline-item
              :icon="completion.scenario.icon"
              :type="completion.scenario.type"
              size="large"
              :timestamp="dateFormat(completion.scenario.updateTime)"
            >
              {{ completion.scenario.content }}
            </el-timeline-item>

            <el-timeline-item
              :icon="completion.results.icon"
              :type="completion.results.type"
              size="large"
              :timestamp="dateFormat(completion.results.updateTime)"
            >
              {{ completion.results.content }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-col>
  </div>
</template>

<script>
import stepCard from '_com/StepCard';
// import tinymceEdit from '_com/TinymceEdit/TinymceEdit.vue';
import wangEditor from '_com/WangEditor/WangEditor.vue';
// import tinymce from '_com/Tinymce/Tinymce.vue'
import { getPerformanceByProjectId } from '@/api/request';
import { dateFormat } from '@/utils/utils';

export default {
  components: {
    stepCard,
    // tinymceEdit,
    wangEditor,
    // tinymce
  },
  data() {
    return {
      projectId: this.$route.params.id,
      completion: {
        context: { icon: '', type: '', updateTime: '' },
        resource: { icon: '', type: '', updateTime: '' },
        scenario: { icon: '', type: '', updateTime: '' },
        results: { icon: '', type: '', updateTime: '' },
      },
      options: [
        {
          value: 'zhinan',
          label: '指南',
          children: [
            {
              value: 'shejiyuanze',
              label: '设计原则',
              children: [
                {
                  value: 'yizhi',
                  label: '一致',
                },
                {
                  value: 'fankui',
                  label: '反馈',
                },
                {
                  value: 'xiaolv',
                  label: '效率',
                },
                {
                  value: 'kekong',
                  label: '可控',
                },
              ],
            },
            {
              value: 'daohang',
              label: '导航',
              children: [
                {
                  value: 'cexiangdaohang',
                  label: '侧向导航',
                },
                {
                  value: 'dingbudaohang',
                  label: '顶部导航',
                },
              ],
            },
          ],
        },
        {
          value: 'zujian',
          label: '组件',
          children: [
            {
              value: 'basic',
              label: 'Basic',
              children: [
                {
                  value: 'layout',
                  label: 'Layout 布局',
                },
                {
                  value: 'color',
                  label: 'Color 色彩',
                },
                {
                  value: 'typography',
                  label: 'Typography 字体',
                },
                {
                  value: 'icon',
                  label: 'Icon 图标',
                },
                {
                  value: 'button',
                  label: 'Button 按钮',
                },
              ],
            },
            {
              value: 'form',
              label: 'Form',
              children: [
                {
                  value: 'radio',
                  label: 'Radio 单选框',
                },
                {
                  value: 'checkbox',
                  label: 'Checkbox 多选框',
                },
                {
                  value: 'input',
                  label: 'Input 输入框',
                },
                {
                  value: 'input-number',
                  label: 'InputNumber 计数器',
                },
                {
                  value: 'select',
                  label: 'Select 选择器',
                },
                {
                  value: 'cascader',
                  label: 'Cascader 级联选择器',
                },
                {
                  value: 'switch',
                  label: 'Switch 开关',
                },
                {
                  value: 'slider',
                  label: 'Slider 滑块',
                },
                {
                  value: 'time-picker',
                  label: 'TimePicker 时间选择器',
                },
                {
                  value: 'date-picker',
                  label: 'DatePicker 日期选择器',
                },
                {
                  value: 'datetime-picker',
                  label: 'DateTimePicker 日期时间选择器',
                },
                {
                  value: 'upload',
                  label: 'Upload 上传',
                },
                {
                  value: 'rate',
                  label: 'Rate 评分',
                },
                {
                  value: 'form',
                  label: 'Form 表单',
                },
              ],
            },
            {
              value: 'data',
              label: 'Data',
              children: [
                {
                  value: 'table',
                  label: 'Table 表格',
                },
                {
                  value: 'tag',
                  label: 'Tag 标签',
                },
                {
                  value: 'progress',
                  label: 'Progress 进度条',
                },
                {
                  value: 'tree',
                  label: 'Tree 树形控件',
                },
                {
                  value: 'pagination',
                  label: 'Pagination 分页',
                },
                {
                  value: 'badge',
                  label: 'Badge 标记',
                },
              ],
            },
            {
              value: 'notice',
              label: 'Notice',
              children: [
                {
                  value: 'alert',
                  label: 'Alert 警告',
                },
                {
                  value: 'loading',
                  label: 'Loading 加载',
                },
                {
                  value: 'message',
                  label: 'Message 消息提示',
                },
                {
                  value: 'message-box',
                  label: 'MessageBox 弹框',
                },
                {
                  value: 'notification',
                  label: 'Notification 通知',
                },
              ],
            },
            {
              value: 'navigation',
              label: 'Navigation',
              children: [
                {
                  value: 'menu',
                  label: 'NavMenu 导航菜单',
                },
                {
                  value: 'tabs',
                  label: 'Tabs 标签页',
                },
                {
                  value: 'breadcrumb',
                  label: 'Breadcrumb 面包屑',
                },
                {
                  value: 'dropdown',
                  label: 'Dropdown 下拉菜单',
                },
                {
                  value: 'steps',
                  label: 'Steps 步骤条',
                },
              ],
            },
            {
              value: 'others',
              label: 'Others',
              children: [
                {
                  value: 'dialog',
                  label: 'Dialog 对话框',
                },
                {
                  value: 'tooltip',
                  label: 'Tooltip 文字提示',
                },
                {
                  value: 'popover',
                  label: 'Popover 弹出框',
                },
                {
                  value: 'card',
                  label: 'Card 卡片',
                },
                {
                  value: 'carousel',
                  label: 'Carousel 走马灯',
                },
                {
                  value: 'collapse',
                  label: 'Collapse 折叠面板',
                },
              ],
            },
          ],
        },
        {
          value: 'ziyuan',
          label: '资源',
          children: [
            {
              value: 'axure',
              label: 'Axure Components',
            },
            {
              value: 'sketch',
              label: 'Sketch Templates',
            },
            {
              value: 'jiaohu',
              label: '组件交互文档',
            },
          ],
        },
      ],
      dialogVisible: false,
    };
  },
  methods: {
    async getPerformance() {
      let data = await getPerformanceByProjectId(this.projectId);
      this.completion = data.completion;
    },
    dateFormat(time) {
      if (time == null) {
        return 'You have not do any operation';
      }
      return dateFormat(time);
    },
    init() {
      this.getPerformance();
    },
  },
  mounted() {
    this.init();
  },
};
</script>

<style lang="scss" scoped>
.main {
  width: 100%;

  min-height: calc(100vh - 240px);
  .main-card {
    background-color: white;
    min-height: calc(100vh - 242px);
    padding: 20px 32px;
  }
  .record {
    position: fixed;
    right: 30px;
    bottom: 75px;
    z-index: 100;
    .block {
      /deep/ .el-timeline-item__tail {
        left: 8px;
      }
      /deep/.el-timeline-item__wrapper {
        padding-left: 35px;
      }
      /deep/ .el-timeline-item__node--large {
        height: 25px;
        width: 25px;
      }
    }
  }
}
</style>