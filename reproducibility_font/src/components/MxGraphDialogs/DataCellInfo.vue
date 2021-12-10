<template>
  <div class="mainContain" v-if="cell != null">
    <el-row class="dataInfo">
      <div class="data">
        <div class="dataTitle">State name:</div>
        <div class="dataDetail" v-if="cell.type == 'modelServiceInput' || cell.type == 'modelServiceOutput'">
          {{ cell.nodeAttribute.stateName }}
        </div>
        <div v-else class="dataDetail">{{ cell.name }}</div>
      </div>
      <div class="data">
        <div class="dataTitle">State description:</div>
        <div class="dataDetail" v-if="cell.type == 'modelServiceInput' || cell.type == 'modelServiceOutput'">
          {{ cell.nodeAttribute.stateDescription }}
        </div>
        <div v-else class="dataDetail">{{ cell.description }}</div>
      </div>

      <div class="data" v-if="cell.type == 'modelServiceInput' || cell.type == 'modelServiceOutput'">
        <div class="dataTitle">Event name:</div>
        <div class="dataDetail">{{ cell.nodeAttribute.eventName }}</div>
      </div>
      <div class="data" v-if="cell.type != 'dataServiceOutput'">
        <div class="dataTitle">Event description:</div>
        <div class="dataDetail">{{ cell.nodeAttribute.eventDescription }}</div>
      </div>
      <div class="data">
        <div class="dataTitle">Event type:</div>
        <div class="dataDetail">{{ cell.nodeAttribute.type }}</div>
      </div>
    </el-row>

    <el-row>
      <el-divider class="eventDivider"></el-divider>
    </el-row>
    <el-row>
      <div v-if="cell.type == 'modelServiceOutput' || cell.type == 'dataServiceOutput'">
        <div v-if="cell.nodeAttribute.value != '' && cell.nodeAttribute.value != undefined">
          <el-button>DownLoad</el-button>
        </div>
        <div v-else>Please run this task to get the output!</div>
        <el-switch
          v-model="upload"
          active-text="Upload"
          inactive-text="Don't upload"
          @change="changeSwitch"
          v-if="cell.type == 'dataServiceOutput'"
        ></el-switch>
      </div>
      <div v-else-if="cell.type == 'modelServiceLink'">Link to the output</div>
      <div v-else-if="cell.type == 'modelServiceInput'">
        <div class="uploadContent">
          <div v-if="cell.nodeAttribute.isParameter == 'true'">
            <el-select
              v-if="role == 'builder'"
              v-model="selectDataItem.name"
              placeholder="Please select data"
              class="uploadContent"
              @change="changeSelectResource"
            >
              <el-option v-for="(item, dataIndex) in paramsDataList" :key="dataIndex" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </div>
          <div v-else>
            <el-select
              v-if="role == 'builder'"
              v-model="selectDataItem.name"
              placeholder="Please select data"
              class="uploadContent"
              @change="changeSelectResource"
            >
              <el-option v-for="(item, dataIndex) in fileDataList" :key="dataIndex" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </div>
        </div>
      </div>

      <div v-else-if="cell.type == 'dataServiceInput'">
        <el-select
          v-if="role == 'builder'"
          v-model="selectDataItem.name"
          placeholder="Please select data"
          class="uploadContent"
          @change="changeSelectResource"
          v-show="cell.isParameter"
        >
          <el-option v-for="(item, dataIndex) in paramsDataList" :key="dataIndex" :label="item.name" :value="item.id"></el-option>
        </el-select>
        <el-select
          v-if="role == 'builder'"
          v-model="selectDataItem.name"
          placeholder="Please select data"
          class="uploadContent"
          @change="changeSelectResource"
          v-show="cell.isParameter == false"
        >
          <el-option v-for="(item, dataIndex) in fileDataList" :key="dataIndex" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </div>
    </el-row>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { getDataItemsByProjectId } from '@/api/request';
// import { hasProperty } from '@/utils/utils';

export default {
  props: {
    cell: {
      type: Object,
    },
  },

  computed: {
    ...mapState({
      role: (state) => state.permission.role,
    }),
  },

  data() {
    return {
      upload: false,
      dataItemList: [],
      fileDataList: [],
      paramsDataList: [],
      projectId: this.$route.params.id,

      ops: {
        bar: {
          background: '#808695',
          keepShow: true,
        },
      },
      selectDataName: '',
      selectDataItem: {},
    };
  },

  methods: {
    changeSwitch() {
      this.$emit('isUpload', this.upload);
    },
    async init() {
      await this.getResources();
    },

    async getResources() {
      let data = await getDataItemsByProjectId(this.projectId);
      this.dataItemList = data; //id list
      for (let i = 0; i < data.length; i++) {
        if (data[i].format != 'parameter') {
          this.fileDataList.push(data[i]);
        } else {
          // data[i].numValue =data[i].value;
          this.paramsDataList.push(data[i]);
        }
      }
    },

    async changeSelectResource(id) {
      let dataSelect = {
        value: '',
        dataSelectId: '',
        name: '',
      };
      this.dataItemList.forEach((item) => {
        if (item.id == id) {
          dataSelect.value = item.value;
          dataSelect.dataSelectId = id;
          dataSelect.name = item.name;
        }
      });

      this.selectDataItem = dataSelect;

      this.$emit('dataSelect', dataSelect);
    },
  },
  mounted() {
    console.log(this.cell);
    if (this.cell.type == 'modelServiceInput' || this.cell.type == 'dataServiceInput') {
      this.selectDataItem = JSON.parse(JSON.stringify(this.cell.nodeAttribute.dataSelect));
    }

    if (this.cell.type == 'dataServiceOutput') {
      if (this.cell.nodeAttribute.upload) {
        this.upload = true;
      } else {
        this.upload = false;
      }
    }
    this.init();
  },
};
</script>

<style lang="scss" scoped>
.mainContain {
  width: 100%;
  height: 100%;
  .dataInfo {
    .data {
      font-size: 16px;
      line-height: 20px;
      .dataTitle {
        clear: both;
        font-weight: 600;
        width: 150px;
        float: left;
      }
      .dataDetail {
        float: left;
      }
    }
  }

  .uploadContent {
    // height: 390px;
    width: 100%;
  }
}

.eventDivider {
  >>> .el-divider--horizontal {
    margin: 5px 0;
  }
}
.table {
  >>> .el-table__expanded-cell {
    background-color: rgba(230, 239, 255, 0.4);
    padding: 0 10px;
    font-size: 14px;
  }
  >>> .el-form-item__label {
    font-weight: 600;
    font-style: italic;
    color: rgb(58, 63, 73);
    padding: 0;
  }
  >>> .el-form-item {
    margin-bottom: 5px;
  }
}
</style>
