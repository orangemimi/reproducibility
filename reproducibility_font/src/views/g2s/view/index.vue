<template>
  <div class="mainContent">
    <div style="position:relative">
        <h1 class="title" :title="this.g2s.name">{{ this.g2s.name }}</h1>
      <el-button
        @click="folkVisible=true"
        style="position:absolute; right:65px;top:0px;"
        type="info"
        circle
        icon="el-icon-connection"
      ></el-button>
    </div>
    <el-row>
      <el-col :span="22" :offset="1">
        <el-row>
          <el-col :span="5">
            <el-card class="leftContent">
              <div class="single-info">
                <div class="info-tag">
                  <i class="el-icon-user-solid" />
                  <strong style="margin-left:5px">Creator</strong>
                </div>
                <span>{{ this.g2s.creator }}</span>
              </div>

              <div class="single-info">
                <div class="info-tag">
                  <i class="el-icon-time" />
                  <strong style="margin-left:5px">Create Time</strong>
                </div>
                <span>{{ this.g2s.createTime }}</span>
              </div>

              <el-collapse v-model="activeNamesLeft">
                <el-collapse-item title="goals" name="1">
                  <div>{{ g2s.goals }}</div>
                </el-collapse-item>
                <el-collapse-item title="background" name="2">
                  <div>{{ g2s.background }}</div>
                </el-collapse-item>
              </el-collapse>
            </el-card>
          </el-col>
          <el-col :span="18" :offset="1">
            <div class="rightContent">
              <el-row class="rightContentCard">
                <el-card>
                  <el-collapse v-model="activeNamesRight">
                    <el-collapse-item title="Context Definition" name="1">
                      <el-tabs v-model="activeContext" v-if="g2s.contextDefine">
                        <el-tab-pane label="Theme" name="theme">{{ g2s.contextDefine.theme }}</el-tab-pane>
                        <el-tab-pane
                          label="Geographic Object"
                          name="object"
                        >{{ g2s.contextDefine.object }}</el-tab-pane>
                        <el-tab-pane
                          label="Boundary"
                          name="boundary"
                        >{{ g2s.contextDefine.boundary }}</el-tab-pane>
                      </el-tabs>
                    </el-collapse-item>
                    <el-collapse-item title="Resource Collection" name="2">
                      <el-tabs v-model="activeResource">
                        <el-tab-pane label="Data Services" name="data">
                          <el-table :data="dataTable" style="width: 100%">
                            <el-table-column prop="name" label="Name" width="200"></el-table-column>
                            <el-table-column prop="createTime" label="CreateTime" width="200"></el-table-column>
                            <el-table-column prop="description" label="description" width="340"></el-table-column>
                            <el-table-column fixed="right" label="operation">
                              <template slot-scope="scope">
                                <el-button
                                  @click.native.prevent="download(scope.row)"
                                  type="text"
                                  icon="el-icon-download"
                                >Download</el-button>
                              </template>
                            </el-table-column>
                          </el-table>
                        </el-tab-pane>

                        <el-tab-pane label="Data Process Services" name="process">
                          <el-table :data="dataProcessTable" style="width: 100%">
                            <el-table-column prop="name" label="Name" width="180"></el-table-column>
                            <el-table-column prop="createTime" label="CreateTime" width="180"></el-table-column>
                            <el-table-column prop="description" label="description"></el-table-column>
                            <el-table-column fixed="right" label="operation">
                              <template slot-scope="scope">
                                <el-button
                                  @click.native.prevent="
                                  view(scope.row, 'process')
                                "
                                  type="text"
                                  size="small"
                                >view</el-button>

                                <el-button
                                  @click.native.prevent="
                                  invoke(scope.row, 'process')
                                "
                                  type="text"
                                  size="small"
                                >invoke</el-button>
                              </template>
                            </el-table-column>
                          </el-table>
                        </el-tab-pane>

                        <el-tab-pane label="Model Services" name="model">
                          <el-table :data="modelTable" style="width: 100%">
                            <el-table-column prop="name" label="Name" width="180"></el-table-column>
                            <el-table-column prop="createTime" label="CreateTime" width="180"></el-table-column>
                            <el-table-column prop="description" label="description"></el-table-column>

                            <el-table-column fixed="right" label="operation">
                              <template slot-scope="scope">
                                <el-button
                                  @click.native.prevent="view(scope.row, 'model')"
                                  type="text"
                                  size="small"
                                >view</el-button>

                                <el-button
                                  @click.native.prevent="
                                  invoke(scope.row, 'model')
                                "
                                  type="text"
                                  size="small"
                                >invoke</el-button>
                              </template>
                            </el-table-column>
                          </el-table>
                        </el-tab-pane>
                      </el-tabs>
                    </el-collapse-item>
                  </el-collapse>
                </el-card>
              </el-row>
              <el-row class="rightContentCard">
                <el-card>
                  <h1>Simulation Concept Graph</h1>
                  <div style="margin-top:20px;padding:15px;background-color:#f8f8f9">
                    <el-image
                      v-if="g2s.simulationConceptGraph.imgGraph!=null"
                      :src="'/api/'+g2s.simulationConceptGraph.imgGraph"
                      fit="fit"
                    ></el-image>
                  </div>
                </el-card>
              </el-row>
              <el-row class="rightContentCard">
                <el-card>
                  <el-tabs v-model="activeExpected" class="instanceCard">
                    <el-tab-pane label="Service Instances" name="instance">
                      <el-col
                        :span="7"
                        :offset="1"
                        v-for="(instance, index) in instanceCard"
                        :key="index"
                      >
                        <InstanceCard :cardData="instance"></InstanceCard>
                      </el-col>
                    </el-tab-pane>

                    <el-tab-pane label="Evaluation" name="evaluation">
                      <el-col
                        :span="7"
                        :offset="1"
                        v-for="(evaluation, index) in evaluationCard"
                        :key="index"
                      >
                        <InstanceCard :cardData="evaluation"></InstanceCard>
                      </el-col>
                    </el-tab-pane>

                    <el-tab-pane label="Workflow" name="workflow">
                      <el-button @click="fullScreen" icon="el-icon-full-screen" primary></el-button>
                      <workflow
                        id="fullScreenComponent"
                        :expectedInstances="g2s.computation.serviceInstances"
                      ></workflow>
                    </el-tab-pane>
                  </el-tabs>
                </el-card>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <el-dialog title="Folk Geographic Simulation" :visible.sync="folkVisible">
      <el-form :model="g2s_folk">
        <el-form-item label="Name">
          <el-input v-model="g2s_folk.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input type="textarea" :rows="2" v-model="g2s_folk.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Background">
          <el-input type="textarea" :rows="2" v-model="g2s_folk.background" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Goals">
          <el-input type="textarea" :rows="2" v-model="g2s_folk.goals" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="folk()">Create</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import workflow from "./components/workflow";
import { get, post } from "@/axios";
import InstanceCard from "_com/common/InstanceCard";
import config from "@/config";
export default {
  data() {
    return {
      id: this.$route.params.id,
      folkVisible: false,
      g2s_folk: {},
      g2s: {
        contextDefine: {
          theme: {},
          object: {},
          boundary: {}
        },
        resourceCollect: {
          dataServices: [],
          modelServices: [],
          dataProcessServices: []
        },
        simulationConceptGraph: {
          imgGraph: null
        },
        computation: {
          serviceInstances: []
        },
        evaluation: []
      },
      activeNamesLeft: ["1", "2"],
      activeNamesRight: ["1", "2"],
      activeResource: "data",
      activeContext: "theme",
      activeExpected: "instance",
      fullscreenFlag: false
    };
  },
  computed: {
    instanceCard() {
      let arr = [];
      this.g2s.computation.serviceInstances.forEach(
        ({ id, name, createTime, creator, instanceEnum, service }) => {
          let inner = {
            id,
            name,
            description: service.description,
            createTime,
            creator,
            serviceId: service.id
          };
          if (instanceEnum === "MODEL") {
            inner.type = "model";
          } else {
            inner.type = "process";
          }
          arr.push(inner);
        }
      );
      return arr;
    },
    evaluationCard() {
      let arr = [];
      this.g2s.evaluation.forEach(
        ({ id, name, description, createTime, creator }) => {
          let inner = {
            serviceId: id,
            name,
            description,
            createTime,
            creator
          };
          inner.type = "evaluation";
          arr.push(inner);
        }
      );
      return arr;
    },
    dataTable() {
      let arr = [];
      this.g2s.resourceCollect.dataServices.forEach(
        ({ id, name, description, createTime }) => {
          let inner = {
            id,
            name,
            description,
            createTime
          };
          arr.push(inner);
        }
      );
      return arr;
    },
    dataProcessTable() {
      let arr = [];
      this.g2s.resourceCollect.dataProcessServices.forEach(
        ({ id, name, description, createTime }) => {
          let inner = {
            id,
            name,
            description,
            createTime
          };
          arr.push(inner);
        }
      );
      return arr;
    },
    modelTable() {
      let arr = [];
      this.g2s.resourceCollect.modelServices.forEach(
        ({ id, name, description, createTime }) => {
          let inner = {
            id,
            name,
            description,
            createTime
          };
          arr.push(inner);
        }
      );
      return arr;
    }
  },

  methods: {
    download(row) {
      window.open(`${config.containerURL}/data_service/fetch/${row.id}`);
    },
    view(row, type) {
      this.$router.push({
        path: `/resource/${row.id}/${type}`
      });
    },
    invoke(row, type) {
      this.$router.push({
        path: `/resource/${row.id}/${type}/invoke`
      });
    },
    fullScreen() {
      let element = document.getElementById("fullScreenComponent");
      element.requestFullscreen();
      this.fullscreenFlag = true;
    },
    async folk() {
      this.g2s_folk.creator = this.$store.state.user.name;
      let { id } = await post(`/g2s/${this.id}/folk`, this.g2s_folk);
      this.$router.push({ path: `/g2s/${id}` });
      this.folkVisible = false;
    }
  },
  created() {
    document.addEventListener("keyup", el => {
      if (el.keyCode == 27) {
        this.fullscreenFlag = false;
      }
    });
  },
  async mounted() {
    this.g2s = await get("/g2s/{id}/view", null, {
      id: this.id
    });
  },
  components: {
    workflow,
    InstanceCard
  }
};
</script>

<style scoped>
.mainContent {
  /* background-color: rgba(116, 111, 111, 0.2); */
  height: 700px;
}
.title {
  text-align: center;
  margin-top: 30px;
  margin-bottom: 20px;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
  color: #0366d6;
}
.info {
  display: flex;
  align-items: center;
  margin-right: 10px;
}
.single-info {
  display: flex;
  align-items: flex-start;
  padding: 5px;
  /* height: 30px; */
  font-size: 12px;
  line-height: 15px;
}
.info-tag {
  display: flex;
  width: 120px;
  align-items: center;
}
.rightContent {
  /* margin-top: 20px; */
  flex: 1;
}
.leftContent /deep/.el-collapse-item__header {
  height: 40px;
  padding-left: 5px;
  font-weight: 600;
}
.rightContent /deep/.el-collapse-item__header {
  background-color: rgb(243, 243, 243);
  border-radius: 5px;
  border: 1px solid rgb(222, 222, 222);
  padding-left: 20px;
  height: 40px;
  font-weight: 600;
  font-size: 16px;
}
.rightContent /deep/.el-collapse-item__wrap {
  margin: 20px 20px;
  border-bottom: 0;
  line-height: 0;
}
.rightContent /deep/ .el-tabs__item {
  height: 35px;
}

.rightContent /deep/ .el-table th {
  background: rgb(243, 243, 243);
  height: 35px;
  border-radius: 5px;
  padding: 0;
}
.rightContentCard {
  margin-bottom: 20px;
}

.instanceCard /deep/ .el-card:hover {
  background-color: #f2f6fc;
}
</style>
