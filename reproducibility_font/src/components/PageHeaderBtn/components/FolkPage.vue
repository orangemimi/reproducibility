<template>
  <div>
    <el-col :xs="24" :sm="24" :md="{ span: 22, offset: 1 }" :lg="{ span: 16, offset: 4 }">
      <div class="folk">
        <el-form ref="form" :model="form" label-width="90px" size="mini">
          <el-form-item label="Name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="Purpose">
            <el-input v-model="form.purpose"></el-input>
          </el-form-item>
          <el-form-item label="Description">
            <el-input type="textarea" v-model="form.description" resize="none"></el-input>
          </el-form-item>
          <el-form-item label="Privacy">
            <el-select v-model="form.privacy" placeholder="请选择">
              <el-option label="public" value="public"></el-option>
              <el-option label="private" value="private"></el-option>
            </el-select>
            <!-- <el-switch v-model="form.privacy" active-text="public" inactive-text="private"></el-switch> -->
          </el-form-item>
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
        </el-form>
        <div class="trans">
          <el-transfer v-model="value" :data="data" filterable :titles="['Source', 'Target']">
            <span slot-scope="{ option }" :title="option.label">
              {{ option.label }}
            </span>
            <el-button class="transfer-footer" slot="left-footer" size="small">操作</el-button>
            <el-button class="transfer-footer" slot="right-footer" size="small">操作</el-button>
          </el-transfer>
        </div>
        <div class="btn">
          <el-button type="primary" @click="forkProject">Submit</el-button>
          <el-button type="primary" @click="test">test</el-button>
        </div>
      </div>
    </el-col>
  </div>
</template>

<script>
import { getSelectedInstancesByProjectId, getContextByProject, forkProject } from '@/api/request';
import X2js from 'x2js';
import { dateFormat } from '@/utils/utils';
export default {
  data() {
    return {
      projectId: this.$route.params.projectId,
      form: {
        name: '',
        description: '',
        purpose: '',
        privacy: 'public'
      },
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
      value: [],
      data: []
    };
  },
  methods: {
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    },
    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (this.dynamicTags.indexOf(inputValue) != -1) {
        this.$notify({
          title: 'warning',
          message: 'The same label exists!',
          type: 'warning'
        });
      } else {
        if (inputValue) {
          this.dynamicTags.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      }
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    async getTaskContent() {
      let data = await getSelectedInstancesByProjectId(this.projectId);

      let x2js = new X2js();
      let json = x2js.xml2js(data.taskContent).mxGraphModel.root.mxCell;
      console.log(json);
      json.forEach(item => {
        if (item._type == 'modelService') {
          this.data.push({
            key: this.data.length,
            label: item._name,
            type: item._type,
            doi: item.Object._doi,
            md5: item.Object._md5
          });
        } else if (item._type == 'modelServiceInput' || item._type == 'dataServiceInput') {
          this.data.push({
            key: this.data.length,
            label: item.Object.Object._name,
            type: item.Object._isParameter == 'true' ? 'parameter' : 'input',
            value: item.Object.Object._value
          });
        } else if (item._type == 'dataService') {
          console.log('dataService暂时还没写！');
        }
      });
    },
    async getContextByProject() {
      let data = await getContextByProject(this.projectId);
      if (data.essentialInformation != null) {
        this.data.push({
          key: this.data.length,
          label: 'essentialInformation',
          obj: data.essentialInformation
        });
      }
      if (data.spatialInfos != null) {
        this.data.push({
          key: this.data.length,
          label: 'spatialInfos',
          obj: data.spatialInfos
        });
      }
      if (data.temporalInfo != null) {
        this.data.push({
          key: this.data.length,
          label: 'temporalInfo',
          obj: data.temporalInfo
        });
      }
    },

    async forkProject() {
      let jsonData = {
        name: this.form.name,
        purpose: this.form.purpose,
        description: this.form.description,
        tags: this.dynamicTags,
        privacy: this.form.privacy,
        forkingProjectId: this.projectId,
        essentialInformation: {},
        temporalInfo: {},
        spatialInfos: [],
        resources: [],
        completionJson: {
          completion: {
            context: { content: 'Context Definition', degree: '0%', type: 'warning', icon: 'el-icon-edit' },
            resource: { content: 'Resource Collection', degree: '0%', type: 'warning', icon: 'el-icon-folder' },
            scenario: { content: 'Simulation Scenario', degree: '0%', type: 'warning', icon: 'el-icon-sunny' },
            results: { content: 'Excepted Results', degree: '0%', type: 'warning', icon: 'el-icon-document' }
          }
        }
      };
      this.value.forEach(item => {
        if (this.data[item].label == 'essentialInformation') {
          jsonData['essentialInformation'] = this.data[item].obj;
        } else if (this.data[item].label == 'temporalInfo') {
          jsonData['temporalInfo'] = this.data[item].obj;
          jsonData.temporalInfo.start = dateFormat(jsonData.temporalInfo.start);
          jsonData.temporalInfo.end = dateFormat(jsonData.temporalInfo.end);
        } else if (this.data[item].label == 'spatialInfos') {
          jsonData['spatialInfos'] = this.data[item].obj;
        } else {
          jsonData.resources.push(this.data[item]);
        }
      });
      let forkProjectId = await forkProject(jsonData);
      console.log(forkProjectId);
      this.$router.push({
        path: `/project/${forkProjectId}/info`
      });
    },
    test() {
      console.log(this.value);
      console.log(this.data);
    },

    async init() {
      await this.getContextByProject();
      await this.getTaskContent();
    }
  },
  async mounted() {
    console.log(this.$route.params);
    await this.init();
  }
};
</script>

<style lang="scss" scoped>
.folk {
  background-color: white;
  min-height: calc(100vh - 92px);
  padding: 20px 170px;
  margin-top: 10px;
  .trans {
    margin-left: 20px;
    margin-top: 20px;
    .el-transfer /deep/ .el-transfer-panel {
      width: 250px;
      height: 336px;
    }
    .transfer-footer {
      margin-left: 20px;
      padding: 6px 5px;
    }
  }
  .btn {
    text-align: center;
    margin-top: 20px;
  }
}
</style>
