<template>
  <div class="mainContain" v-if="JSON.stringify(currentEvent) != '{}'">
    <el-row>
      <div class="data">
        <div class="dataTitle">State name:</div>
        <div class="dataDetail">{{ currentEvent.state.name }}</div>
      </div>
      <div class="data">
        <div class="dataTitle">State description:</div>
        <div class="dataDetail">{{ currentEvent.state.description }}</div>
      </div>
      <div class="data">
        <div class="dataTitle">State type:</div>
        <div class="dataDetail">{{ currentEvent.state.type }}</div>
      </div>
      <div class="data">
        <div class="dataTitle">Event name:</div>
        <div class="dataDetail">{{ currentEvent.name }}</div>
      </div>
      <div class="data">
        <div class="dataTitle">Event description:</div>
        <div class="dataDetail">{{ currentEvent.description }}</div>
      </div>
      <div class="data">
        <div class="dataTitle">Event type:</div>
        <div class="dataDetail">{{ currentEvent.type }}</div>
      </div>
    </el-row>

    <el-row>
      <el-divider class="eventDivider"></el-divider>
    </el-row>
    <el-row>
      <div v-if="currentCell.type == 'output'">
        <div v-if="currentCell.value != '' && currentCell.value != undefined">
          <el-button>DownLoad</el-button>
        </div>
        <div v-else>Please run this task to get the output!</div>
      </div>
      <div v-else-if="currentCell.type == 'link'">
        Link to the output
      </div>
      <div v-else>
        <div v-if="currentEvent.datasetItem.type == `internal`" class="uploadContent">
          <vue-scroll style="height: 100%" :ops="ops">
            <div v-if="filterEvent">
              <el-table
                border
                :data="filterEvent[0].UdxNode"
                size="mini"
                class="table"
                style="width: 100%"
              >
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
              </el-table>
            </div>
            <div v-else>
              <!-- {{ dataItemList }} -->
<<<<<<< HEAD:reproducibility_font/src/components/DataCellInfo/Info.vue
              <el-select v-if="role == 'builder'" v-model="selectDataId" clearable placeholder="Please select data" class="uploadContent" @change="changeSelectResource">
                <el-option v-for="(item, dataIndex) in dataItemList" :key="dataIndex" :label="item.name" :value="item.id"></el-option>
=======
              <el-select
                v-model="selectDataId"
                clearable
                placeholder="Please select data"
                class="uploadContent"
                @change="changeSelectResource"
              >
                <el-option
                  v-for="(item, dataIndex) in dataItemList"
                  :key="dataIndex"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/DataItemInfo.vue
              </el-select>
              <div v-else>
                <el-button>{{ selectDataItem.name }}</el-button>
              </div>
            </div>
          </vue-scroll>
        </div>
        <div v-else>
<<<<<<< HEAD:reproducibility_font/src/components/DataCellInfo/Info.vue
          <el-select v-if="role == 'builder'" v-model="selectDataId" clearable placeholder="Please select data" class="uploadContent" @change="changeSelectResource">
            <el-option v-for="(item, dataIndex) in dataItemList" :key="dataIndex" :label="item.name" :value="item.id"></el-option>
=======
          <el-select
            v-model="selectDataId"
            clearable
            placeholder="Please select data"
            class="uploadContent"
            @change="changeSelectResource"
          >
            <el-option
              v-for="(item, dataIndex) in dataItemList"
              :key="dataIndex"
              :label="item.name"
              :value="item.id"
            ></el-option>
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/DataItemInfo.vue
          </el-select>
          <div v-else>
            <el-button>{{ selectDataItem.name }}</el-button>
          </div>
        </div>
        <!-- <div>
          <el-button size="small" type="success" round @click="submitResource">Submit</el-button>
        </div> -->
      </div>
    </el-row>
  </div>
</template>

<script>
<<<<<<< HEAD:reproducibility_font/src/components/DataCellInfo/Info.vue
import { mapState } from 'vuex';
import { getProjectResourcesById } from '@/api/request';
=======
// import file from '@/components/dataTemplate/File';
import { get } from '@/axios';

import { getResourcesById } from '@/api/request';
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/DataItemInfo.vue

export default {
  props: {
    cell: {
      type: Object
    }
  },
  watch: {
    cell: {
      handler(val) {
<<<<<<< HEAD:reproducibility_font/src/components/DataCellInfo/Info.vue
        if (val != '') {
          this.currentEvent = val;
          console.log(this.currentEvent);
          this.selectDataId = this.currentEvent.fileId;
          this.selectDataItem = {
            id: this.currentEvent.fileId,
            name: this.currentEvent.fileName,
            url: this.currentEvent.value
          };
=======
        if (val != '' && val.doi != undefined) {
          this.doi = val.doi;
          this.currentCell = val;
          this.selectDataId = this.currentCell.fileId;
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/DataItemInfo.vue
          this.init();
        }
      },
      deep: true
    }
  },

  computed: {
    filterEvent() {
      let datasetItem = this.currentDatasetItem;
      if (Object.prototype.hasOwnProperty.call(datasetItem, 'UdxDeclaration')) {
        if (datasetItem.UdxDeclaration[0].UdxNode != '') {
          return false;
        } else {
          let udxNode = datasetItem.UdxDeclaration[0].UdxNode;
          return udxNode;
        }
      } else {
        return false;
      }
    },
    ...mapState({
      role: state => state.permission.role
    })
  },

  // created() {
  //   this.$set(this.cell);
  // },

  data() {
    return {
      doi: '',
      modelIntroduction: {},
      modelInstance: {},
      md5: '',
      stateList: [],
      stateInView: {},
      currentCell: this.cell,
      dataItemList: [],
      projectId: this.$route.params.id,

      currentEvent: {},
      currentDatasetItem: {},
      ops: {
        bar: {
          background: '#808695',
          keepShow: true
        }
      },
      selectDataId: '',
      selectDataItem: {}
    };
  },

  methods: {
    async init() {
      await this.getModelInfo();
      await this.getResources();
    },

    async getModelInfo() {
      let data = await get(`/portal/modelBehavior/${this.doi}`); //获得模型所有信息
      this.md5 = data.md5;
      this.modelIntroduction = data;
      this.stateList = data.convertMdlJson;
      console.log('stateList', this.stateList);

      this.currentEvent = this.convertStateList();
      if (this.currentCell.value != undefined) {
        this.currentEvent.value = this.currentCell.value;
        // this.$set(this.currentEvent, "value", this.currentCell.value);
      }
      if (Object.prototype.hasOwnProperty.call(this.currentCell, 'datasetItem')) {
        this.currentDatasetItem = this.currentCell.datasetItem;
      } else {
        this.currentDatasetItem = this.currentEvent.datasetItem;
      }
    },

    convertStateList() {
      if (this.stateList == '') {
        return;
      }
      let stateList = this.stateList;
      let currentCell = this.currentCell;
      let current;
      stateList.forEach(state => {
        if (state.name == currentCell.stateName) {
          let events = state.Event;
          current = events.filter(event => {
            return event.name == currentCell.name;
          });
          current = { state, Event, ...current[0] };
        }
      });
      console.log(current);

      return current;
    },

    async getResources() {
      let data = await getProjectResourcesById(this.projectId);
      console.log('resource', data);
      this.dataItemList = data; //id list
    },

    async changeSelectResource(id) {
      this.selectDataId = id;
      let dataSelect = this.dataItemList.filter(e => e.id == id);
      this.selectDataItem = dataSelect[0];
      // this.selectDataId = this.selectDataItem.fileName;

      this.$forceUpdate();
      await this.submitResource();
    },

    async submitResource() {
      // console.log(this.currentEvent);

<<<<<<< HEAD:reproducibility_font/src/components/DataCellInfo/Info.vue
      this.currentEvent.fileName = this.selectDataItem.name;
      this.currentEvent.fileId = this.selectDataItem.id;
      this.currentEvent.value = this.selectDataItem.address;
      this.currentEvent.fileDescription = this.selectDataItem.description;
      // console.log(this. currentEvent);
      this.currentEvent.datasetItem = this.currentDatasetItem;
      // this. currentEvent.name = this.currentEvent.name;
=======
      this.currentCell.fileName = this.selectDataItem.name;
      this.currentCell.fileId = this.selectDataItem.id;
      this.currentCell.value = this.selectDataItem.url;
      this.currentCell.fileDescription = this.selectDataItem.description;
      // console.log(this.currentCell);
      this.currentCell.datasetItem = this.currentDatasetItem;
      // this.currentCell.name = this.currentEvent.name;
      this.currentCell.description = this.currentEvent.description;
>>>>>>> parent of f11cd19 (mxgraph):reproducibility_font/src/views/project/components/Construction/DataItemInfo.vue

      this.$emit('currentEventWithFile', this.currentCell);
    }
  }
};
</script>

<style lang="scss" scoped>
.mainContain {
  width: 100%;
  height: 100%;

  .uploadContent {
    // height: 390px;
    width: 100%;
  }
}
.data {
  font-size: 15px;
  .dataTitle {
    font-weight: 600;
    width: 150px;
    float: left;
  }
  .dataDetail {
    float: left;
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
