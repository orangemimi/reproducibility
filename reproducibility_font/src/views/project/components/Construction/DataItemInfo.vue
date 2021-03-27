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
      <div v-if="currentCell.type == 'noresponse'">
        <el-button>DownLoad</el-button>
      </div>
      <div v-else>
        <div
          v-if="currentEvent.datasetItem.type == `internal`"
          class="uploadContent"
        >
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
                    <el-form
                      label-position="top"
                      inline
                      class="table-expand"
                      size="mini"
                    >
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
                <el-table-column
                  prop="name"
                  label="Parameter"
                  width="120"
                ></el-table-column>
                <el-table-column label="Value" width="140">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.value"></el-input>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div v-else>
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
              </el-select>
            </div>
          </vue-scroll>
        </div>
        <div v-else>
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
          </el-select>
        </div>
        <div>
          <el-button size="small" type="success" round @click="submitResource"
            >Submit</el-button
          >
        </div>
      </div>
    </el-row>
  </div>
</template>

<script>
import file from "@/components/dataTemplate/File";
import { get, del, post, put, patch } from "@/axios";
export default {
  props: {
    cell: {
      type: Object,
    },
  },
  watch: {
    cell: {
      handler(val) {
        if (val != "") {
          this.doi = val.doi;
          this.currentCell = val;
          this.selectDataId = this.currentCell.fileId;
          this.init();
        }
      },
      deep: true,
    },
  },

  computed: {
    filterEvent() {
      let datasetItem = this.currentDatasetItem;
      if (datasetItem.hasOwnProperty("UdxDeclaration")) {
        if (datasetItem.UdxDeclaration[0].UdxNode != "") {
          if (
            datasetItem.UdxDeclaration[0].UdxNode[0].UdxNode[0].hasOwnProperty(
              "UdxNode"
            )
          ) {
            return false;
          } else {
            let udxNode = datasetItem.UdxDeclaration[0].UdxNode;
            return udxNode;
          }
        }
      }
    },
  },

  data() {
    return {
      doi: "",
      modelIntroduction: {},
      modelInstance: {},
      md5: "",
      stateList: [],
      stateInView: {},
      currentCell: this.cell,
      dataItemList: [],
      projectId: this.$route.params.projectId,
      userInfo: this.$store.getters.userInfo,
      currentEvent: {},
      currentDatasetItem: {},
      ops: {
        bar: {
          background: "#808695",
          keepShow: true,
        },
      },
      selectDataId: "",
      selectDataItem: {},
    };
  },

  methods: {
    async init() {
      await this.getModelInfo();
      await this.getResources();
    },

    async getModelInfo() {
      let data = await get(
        `/GeoProblemSolving/modelTask/ModelBehavior/${this.doi}`
      ); //获得模型所有信息
      this.md5 = data.md5;
      this.modelIntroduction = data;
      this.stateList = data.convertMdlJson;

      this.currentEvent = this.convertStateList();
      if (this.currentCell.value != undefined) {
        this.currentEvent.value = this.currentCell.value;
        // this.$set(this.currentEvent, "value", this.currentCell.value);
      }
      if (this.currentCell.hasOwnProperty("datasetItem")) {
        this.currentDatasetItem = this.currentCell.datasetItem;
      } else {
        this.currentDatasetItem = this.currentEvent.datasetItem;
      }
    },

    convertStateList() {
      if (this.stateList == "") {
        return;
      }
      let stateList = this.stateList;
      let currentCell = this.currentCell;
      let current;
      let event = stateList.forEach((state) => {
        if (state.name == currentCell.stateName) {
          let events = state.Event;
          current = events.filter((event) => {
            return event.name == currentCell.name;
          });
          current = { state, Event, ...current[0] };
        }
      });
      // console.log(current);

      return current;
    },

    async getResources() {
      let data = await get(`/GeoProblemSolving/r/dataItems/${this.projectId}`);
      this.dataItemList = data;
    },

    changeSelectResource(id) {
      this.selectDataId = id;
      let dataSelect = this.dataItemList.filter((e) => e.id == id);
      this.selectDataItem = dataSelect[0];
      // this.selectDataId = this.selectDataItem.fileName;

      this.$forceUpdate();
    },

    async submitResource() {
      // console.log(this.currentEvent);

      let event = this.currentEvent;
      if (
        event.type == "response" &&
        event.datasetItem.hasOwnProperty("UdxDeclaration") &&
        event.datasetItem.UdxDeclaration[0].UdxNode != "" &&
        !event.datasetItem.UdxDeclaration[0].UdxNode[0].UdxNode[0].hasOwnProperty(
          "UdxNode"
        )
      ) {
        let content = "";
        let uploadFileForm = new FormData();

        let udxNodeList = this.currentDatasetItem.UdxDeclaration[0].UdxNode[0]
          .UdxNode;
        console.log(udxNodeList);
        udxNodeList.forEach((udx) => {
          if (udx.hasOwnProperty("value")) {
            content += `<XDO name="${udx.name}" kernelType="string" value="${udx.value}" />`;
          }
        });

        if (content != "") {
          content = "<Dataset> " + content + " </Dataset>";
          let file = new File([content], event.name + ".xml", {
            type: "text/plain",
          });
          uploadFileForm.append("datafile", file); //http://111.229.14.128:8082/data

          // this.createConfigFile();
          let uploadedData = await this.submitUpload(uploadFileForm);
          this.currentCell.fileName = uploadedData.name;
          this.currentCell.value = uploadedData.url;
          this.currentCell.fileId = uploadedData.id;

          this.currentCell.datasetItem = this.currentDatasetItem;
        }
      } else {
        this.currentCell.fileName = this.selectDataItem.name;
        this.currentCell.fileId = this.selectDataItem.id;
        this.currentCell.value = this.selectDataItem.url;

        this.currentCell.datasetItem = this.currentDatasetItem;
        if (this.selectDataItem != undefined) {
          this.$message({
            message: "You have submit the file successfully",
            type: "success",
          });
        }
      }

      this.$emit("currentEventWithFile", this.currentCell);
    },

    //上传文件到服务器
    async submitUpload(uploadFileForm) {
      let uid = await post(
        `/GeoProblemSolving/dataContainer/uploadSingle`,
        uploadFileForm
      );

      let url = `http://221.226.60.2:8082/data?uid=${uid}`;

      let list = {
        userId: this.userInfo.userId,
        pid: this.projectId,
        url: url,
        name: uploadFileForm.get("datafile").name,
        isDirect: false, //if true -- 是直接上传的数据    --false是中间数据
      };
      console.log(list);

      let data = await post(`/GeoProblemSolving/r/dataItems`, list);
      if (data != undefined) {
        this.$message({
          message: "You have submit the parameter successfully",
          type: "success",
        });
      }
      return data;
    },

    async uploadResource() {},
  },
};
</script>

<style lang="scss" scoped>
.mainContain {
  width: 100%;
  height: 100%;

  .uploadContent {
    height: 390px;
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