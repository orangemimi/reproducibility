<template>
  <div class="mainContain" v-if="currentEvent != null">
    <el-row class="dataInfo">
      <div class="data">
        <div class="dataTitle">State name:</div>
        <div class="dataDetail">{{ currentEvent.nodeAttribute.stateName }}</div>
      </div>
      <div class="data">
        <div class="dataTitle">State description:</div>
        <div class="dataDetail">{{ currentEvent.nodeAttribute.stateDescription }}</div>
      </div>

      <div class="data">
        <div class="dataTitle">Event name:</div>
        <div class="dataDetail">{{ currentEvent.nodeAttribute.eventName }}</div>
      </div>
      <div class="data">
        <div class="dataTitle">Event description:</div>
        <div class="dataDetail">{{ currentEvent.nodeAttribute.eventDescription }}</div>
      </div>
      <div class="data">
        <div class="dataTitle">Event type:</div>
        <div class="dataDetail">{{ currentEvent.nodeAttribute.type }}</div>
      </div>
    </el-row>

    <el-row>
      <el-divider class="eventDivider"></el-divider>
    </el-row>
    <el-row>
      <div v-if="currentEvent.type == 'modelServiceOutput' || currentEvent.type == 'dataServiceOutput'">
        <div v-if="currentEvent.value != '' && currentEvent.value != undefined">
          <el-button>DownLoad</el-button>
        </div>
        <div v-else>Please run this task to get the output!</div>
        <el-switch v-model="value1.upload" active-text="Upload" inactive-text="Don't upload" @change="changeSwitch"></el-switch>
      </div>
      <div v-else-if="currentEvent.type == 'modelServiceLink'">Link to the output</div>
      <div v-else-if="currentEvent.type == 'modelServiceInput'">
        <div v-if="currentEvent.nodeAttribute.datasetItem != undefined && currentEvent.nodeAttribute.datasetItem.type == `internal`" class="uploadContent">
          <vue-scroll style="height: 100%" :ops="ops">
            <div v-if="currentEvent.isParameter">
              <!-- <el-table border :data="filterEvent[0].UdxNode" size="mini" class="table" style="width: 100%">
                <el-table-column type="expand" width="20">
                  <template slot-scope="props">
                    <el-form label-position="top" inline class="table-expand" size="mini">
                      <el-form-item label="Parameter">
                        <span>{{ props.row.name }}</span>
                      </el-form-item>
                      <el-form-item label="Type">
                        <span>{{ props.row.type }}</span>
                      </el-form-item>
                      <el-form-item label="Description">
                        <span>{{ props.row.description }}</span>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column prop="name" label="Parameter" width="120"></el-table-column>
                <el-table-column label="Value" width="140">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.value"></el-input>
                  </template>
                </el-table-column>
              </el-table> -->
              <el-select
                v-if="role == 'builder'"
                v-model="selectDataItem.name"
                placeholder="Please select data"
                class="uploadContent"
                @change="changeSelectResource"
              >
                <el-option v-for="(item, dataIndex) in paramsDataList" :key="dataIndex" :label="item.name" :value="item.id"></el-option>
              </el-select>
              <div v-else>
                <el-button>{{ selectDataItem.name }}</el-button>
              </div>
            </div>
            <div v-else>
              <!-- {{ dataItemList }} -->
              <el-select
                v-if="role == 'builder'"
                v-model="selectDataItem.name"
                placeholder="Please select data"
                class="uploadContent"
                @change="changeSelectResource"
              >
                <el-option v-for="(item, dataIndex) in fileDataList" :key="dataIndex" :label="item.name" :value="item.id"></el-option>
              </el-select>
              <div v-else>
                <el-button>{{ selectDataItem.name }}</el-button>
              </div>
            </div>
          </vue-scroll>
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
          <div v-else>
            <el-button>{{ selectDataItem.name }}</el-button>
          </div>
        </div>
        <!-- <div>
          <el-button size="small" type="success" round @click="submitResource">Submit</el-button>
        </div> -->
      </div>

      <div v-else-if="currentEvent.type == 'dataServiceInput'">
        <el-select v-if="role == 'builder'" v-model="selectDataId" placeholder="Please select data" class="uploadContent" @change="changeSelectResource" v-show="currentEvent.isParameter">
          <el-option v-for="(item, dataIndex) in paramsDataList" :key="dataIndex" :label="item.name" :value="item.id"></el-option>
        </el-select>
        <el-select v-if="role == 'builder'" v-model="selectDataId" placeholder="Please select data" class="uploadContent" @change="changeSelectResource" v-show="currentEvent.isParameter == false">
          <el-option v-for="(item, dataIndex) in fileDataList" :key="dataIndex" :label="item.name" :value="item.id"></el-option>
        </el-select>
        <div v-else>
          <el-button>{{ selectDataItem.name }}</el-button>
        </div>

        <!-- <div>
          <el-button size="small" type="success" round @click="submitResource">Submit</el-button>
        </div> -->
      </div>
    </el-row>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { getDataItemsByProjectId } from '@/api/request';
import { hasProperty } from '@/utils/utils';

export default {
  props: {
    cell: {
      type: Object,
    },
  },
  watch: {
    cell: {
      // immediate: true,
      handler(val, oldVal) {
        if (val != '' && val != oldVal) {
          console.log(val)
          this.currentEvent = val;
          this.selectDataId = '';
          this.selectDataItem = {};
          this.$set(this.value1, 'upload', val.upload)
          //this.init();
          if (hasProperty(val, 'dataResourceRelated')) {
            this.selectDataId = this.currentEvent.dataResourceRelated.dataSelectId;
            this.selectDataItem = this.currentEvent.dataResourceRelated;
          }
          // this.init();
        }
      },
      deep: true,
      immediate: true,
    },
  },

  computed: {
    filterEvent() {
      let datasetItem = this.currentEvent.nodeAttribute.datasetItem;
      if (Object.prototype.hasOwnProperty.call(datasetItem, 'UdxDeclaration')) {
        console.log(datasetItem.UdxDeclaration[0].UdxNode);
        return datasetItem.UdxDeclaration[0].UdxNode;
        // if (datasetItem.UdxDeclaration[0].UdxNode != '') {
        //   return false;
        // } else {
        //   let udxNode = datasetItem.UdxDeclaration[0].UdxNode;

        //   return udxNode;
        // }
      } else {
        return false;
      }
    },
    ...mapState({
      role: (state) => state.permission.role,
    }),
  },

  data() {
    return {
      value1: {},

      currentEvent: { nodeAttribute: '' },
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
      selectDataId: '',
      selectDataItem: {},
    };
  },

  methods: {
    changeSwitch() {
      this.$emit("isUpload", this.value1.upload)
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
      
      let dataSelect = this.dataItemList.find((e) => e.id == id);
      this.selectDataId = dataSelect.name;
      dataSelect.id = id + this.cell.id
      dataSelect.dataSelectId = id
      this.selectDataItem = dataSelect;
      // console.log(dataSelect)
      // this.currentEvent.dataResourceRelated = { name: dataSelect.name, value: dataSelect.value, id: dataSelect.id };
      
      this.currentEvent.dataResourceRelated = dataSelect;
      // this.selectDataId = this.selectDataItem.fileName;
      this.$forceUpdate();
    },
  },
  mounted() {
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
