<template>
  <div>
    <div class="top" style="position: relative">
      <div class="callBack" @click="handlerClose">
        <i class="el-icon-arrow-right"></i>
      </div>
      <div :class="select == 1 ? 'icon select' : 'icon'" title="Models" @click="change(1)">
        <el-badge :value="modelList.length" class="item" :max="10">
          <i class="iconfont icon-model" style="font-size: 35px"></i>
        </el-badge>
      </div>
      <div :class="select == 2 ? 'icon select' : 'icon'" title="DataServices" @click="change(2)">
        <el-badge :value="dataServiceList.length" class="item" :max="10">
          <i class="iconfont icon-service" style="font-size: 35px"></i>
        </el-badge>
      </div>
      <div class="clear">
        <i class="iconfont icon-yijianqingli"></i>
      </div>
    </div>
    <div class="main">
      <div v-if="select == 1">
        <el-empty description="No Data" v-if="modelList.length == 0"></el-empty>
        <vue-scroll style="height: 350px" v-else>
          <div style="padding: 0 10px">
            <el-row :gutter="20">
              <el-col :span="6" v-for="(item, index) in modelList" :key="index">
                <div class="modelInfo" :title="item.name">
                  <img :src="'http://geomodeling.njnu.edu.cn' + item.image" v-if="item.image != ''" height="60" width="60" />
                  <img src="http://geomodeling.njnu.edu.cn/static/img/model/model.png" alt="" v-else height="60" width="60" />
                  <p class="name">{{ item.name }}</p>
                </div>
              </el-col>
            </el-row>
          </div>
        </vue-scroll>
      </div>
      <div v-else>
        <el-empty description="No Data" v-if="dataServiceList.length == 0"></el-empty>
        <vue-scroll style="height: 350px" v-else>
          <div style="padding: 0 10px">
            <el-row :gutter="20">
              <el-col :span="6" v-for="(item, index) in dataServiceList" :key="index">
                <div class="modelInfo" :title="item.name">
                  <img :src="imgPath(item.snapshot, item.name)" height="60" width="60" />
                  <p class="name">{{ item.name }}</p>
                </div>
              </el-col>
            </el-row>
          </div>
        </vue-scroll>
      </div>
    </div>
    <div class="bottom">
      <el-button type="primary" round size="mini" class="btn" plain @click="projectFlag = true">Add to project</el-button>
    </div>
    <div class="project" v-if="projectFlag">
      <div style="margin-top: 10px; margin-left: 10px; margin-bottom: 10px">Add project</div>
      <div>
        <vue-scroll style="height: 200px">
          <el-row>
            <el-col :span="24" v-for="(item, index) in projectList" :key="index">
              <div :class="selectNum == index ? 'project-item select' : 'project-item'" @click="selectClick(index)">
                <i class="el-icon-folder"></i>
                {{ item.name }}
              </div>
            </el-col>
          </el-row>
        </vue-scroll>
      </div>
      <div class="bottom-btn">
        <el-button size="mini" type="primary" round style="margin-top: 10px; margin-left: 120px" @click="confirm" :disabled="selectNum == -1">
          confirm
        </el-button>
        <el-button size="mini" type="primary" round @click="projectFlag = false">cancle</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getProjectsCreatedByMe, saveModelsToProject, saveDataServicesToProject } from '@/api/request';
import { imgBase64 } from '@/lib/utils';
export default {
  data() {
    return {
      modelList: [],
      dataServiceList: [],
      projectList: [],
      projectFlag: false,
      selectNum: -1,
      select: 1,
    };
  },
  methods: {
    imgPath(snapshot, name) {
      if (snapshot != undefined) {
        return snapshot;
      } else {
        return imgBase64(name);
      }
    },
    handlerClose() {
      this.$emit('close', false);
    },
    async init() {
      this.select = 1;
      if (localStorage.selectModels != undefined) {
        let temp = JSON.parse(localStorage.selectModels);
        this.modelList = temp.computableModels;
      }
      if (localStorage.selectDataServices != undefined) {
        let temp = JSON.parse(localStorage.selectDataServices);
        this.dataServiceList = temp.dataServices;
      }
      await this.getProjectsCreatedByMe();
    },
    async getProjectsCreatedByMe() {
      this.projectList = await getProjectsCreatedByMe();
    },
    selectClick(index) {
      if (this.selectNum == index) {
        this.selectNum = -1;
      } else {
        this.selectNum = index;
      }
    },
    async saveModelsToProject() {
      let jsonData = {
        models: this.modelList,
        projectId: this.projectList[this.selectNum].projectId,
      };
      await saveModelsToProject(jsonData);
    },
    async saveDataServicesToProject() {
      let jsonData = {
        dataServices: this.dataServiceList,
        projectId: this.projectList[this.selectNum].projectId
      }
      await saveDataServicesToProject(jsonData)
    },
    async confirm() {
      if (this.modelList.length == 0 && this.dataServiceList.length == 0) {
        this.$notify({
          type: 'warning',
          message: 'No models!',
          title: 'Warning',
        });
      } else {
        if (this.modelList.length != 0) {
          await this.saveModelsToProject();
          let temp = {
            computableModels: [],
            modelItem: [],
          };
          localStorage.selectModels = JSON.stringify(temp);
        }
        if(this.dataServiceList.length != 0) {
          await this.saveDataServicesToProject()
          let temp = {
            dataServiceItem: {},
            dataServices: []
          }
          localStorage.selectDataServices = JSON.stringify(temp)
        }
        this.modelList = [];
        this.dataServiceList = []
        this.$emit('success', 0);
      }
    },
    change(val) {
      this.select = val;
      // if(val == 1) {

      // }
    },
  },
  mounted() {
    this.init();
  },
};
</script>

<style lang="scss" scoped>
.top {
  background-color: #0a0f30;
  height: 50px;
  width: 100%;
  display: flex;
  .icon {
    height: 50px;
    line-height: 50px;
    padding: 0px 5px;
    .el-badge {
      /deep/ .el-badge__content {
        margin-top: 12px;
        right: 19px;
      }
    }
  }
  .select {
    background-color: rgba($color: #fff, $alpha: 0.5);
  }
  .icon:hover {
    cursor: pointer;
  }
  .icon:active {
    // background-color: rgba($color: #fff, $alpha: 0.5);
    box-shadow: inset 0 3px 5px rgba($color: #fff, $alpha: 0.5);
  }
  .callBack {
    height: 100%;
    width: 25px;
    background-color: #ff0000;
    color: white;
    line-height: 50px;
    text-align: center;
  }
  .clear {
    line-height: 50px;
    text-align: center;
    margin-left: 100px;
  }
  .callBack:hover {
    cursor: pointer;
  }
}
.main {
  height: 350px;
  width: 100%;
  color: black;
  .modelInfo {
    margin-top: 10px;
    .name {
      max-height: 50px;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 1;
      overflow: hidden;
      font-size: 10px;
    }
  }
  .modelInfo:hover {
    cursor: pointer;
  }
}
.bottom {
  background-color: #f3f3f3;
  width: 100%;
  height: calc(100vh - 400px);
  .btn {
    margin-top: 25px;
    width: 70%;
  }
}
.project {
  position: absolute;
  text-align: left;
  bottom: 0px;
  height: 300px;
  background-color: #fafafa;
  width: 100%;
  color: black;
  z-index: 9;
  .select {
    background-color: #fbe7e6;
    border: solid 1px red;
  }
  .project-item {
    margin: 0px 15px;
    padding: 10px 15px;
    border-radius: 4px;
    font-size: 12px;
  }
  .project-item:hover {
    cursor: pointer;
    background-color: #fbe7e6;
    border: solid 1px red;
  }
  .bottom-btn {
    background-color: #f3f3f3;
    height: 59px;
  }
}
</style>