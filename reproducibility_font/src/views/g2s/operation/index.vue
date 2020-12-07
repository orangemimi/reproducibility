<template>
  <div>
    <el-switch
      v-model="g2s.isPublish"
      active-text="public"
      inactive-text="private"
      @change="publish"
      active-color="#13ce66"
      inactive-color="#ff4949"
    >
    </el-switch>

    <div
      style="width:90%;margin-top:20px;padding:15px;background-color:#f8f8f9"
      :style="{ height: contentHeight - 140 + 'px' }"
      ref="steps"
    ></div>

    <el-dialog
      title="上下文定义"
      :visible.sync="contextVisible"
    >
      <el-form :model="contextDefine">
        <el-form-item label="主题名称">
          <el-input
            v-model="contextDefine.theme.name"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="地理对象名称">
          <el-input
            v-model="contextDefine.object.name"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="边界名称">
          <el-input
            v-model="contextDefine.boundary.name"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="contextVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="setContext()"
        >确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="资源收集"
      :visible.sync="resourceCollectVisible"
    >
      <el-form :model="resourceCollect">
        <el-form-item label="数据资源">
          <el-select
            v-model="resourceCollect.dataServices"
            multiple
            allow-create
            placeholder="请输入数据资源ID"
            filterable
            default-first-option
          >
          </el-select>
        </el-form-item>

        <el-form-item label="数据处理资源">
          <el-select
            v-model="resourceCollect.dataProcessServices"
            multiple
            allow-create
            placeholder="请输入数据处理资源ID"
            filterable
            default-first-option
          >
          </el-select>
        </el-form-item>

        <el-form-item label="模型资源">
          <el-select
            v-model="resourceCollect.modelServices"
            multiple
            allow-create
            placeholder="请输入模型资源ID"
            filterable
            default-first-option
          >
          </el-select>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="resourceCollectVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="setResourceCollect()"
        >确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="模拟验证"
      :visible.sync="evaluationVisible"
    >
      <el-tabs v-model="avtiveTab">
        <el-tab-pane
          label="管理"
          name="manage"
        >
          <el-table
            :data="evaluationServices"
            style="width: 100%"
          >
            <el-table-column
              prop="name"
              label="Name"
              width="180"
            >
            </el-table-column>
            <el-table-column
              prop="createTime"
              label="CreateTime"
              width="180"
            >
            </el-table-column>
            <el-table-column
              prop="description"
              label="description"
            >
            </el-table-column>
            <el-table-column
              fixed="right"
              label="operation"
            >
              <template slot-scope="scope">
                <el-button
                  @click="view(scope.row)"
                  type="text"
                  size="small"
                >
                  view
                </el-button>
                <el-button
                  @click="edit(scope.row)"
                  type="text"
                  size="small"
                >
                  edit
                </el-button>

                <el-button
                  @click="remove(scope.row)"
                  type="text"
                  size="small"
                >
                  remove
                </el-button>

              </template>
            </el-table-column>
          </el-table>

        </el-tab-pane>
        <el-tab-pane
          label="创建"
          name="create"
        >
          <el-row>
            <div>
              <el-steps
                finish-status="success"
                :active="active"
              >
                <el-step title="基本信息"></el-step>
                <el-step title="详情"></el-step>
                <el-step title="行为绑定"></el-step>
              </el-steps>
            </div>

            <div v-show="active == 0">
              name:<el-input v-model="evaluationService.name"></el-input>
              description:<el-input v-model="evaluationService.description"></el-input>
              tags:<el-select
                v-model="evaluationService.tags"
                multiple
                allow-create
                placeholder="请输入tags"
                filterable
                default-first-option
              ></el-select>
              <br />

            </div>

            <div v-show="active == 1">
              details:<el-input
                v-model="evaluationService.details"
                type="textarea"
              ></el-input>

              <br />

              <el-select
                v-model="evaluationService.evaluationTypeEnum"
                clearable
                placeholder="请选择"
              >
                <el-option
                  key="HTML"
                  label="HTML"
                  value="HTML"
                > </el-option>
                <el-option
                  key="JUPYTER"
                  label="Jupter"
                  value="JUPYTER"
                >
                </el-option>
                <el-option
                  key="RMARKDOWN"
                  label="Rmarkdown"
                  value="RMARKDOWN"
                >
                </el-option>
              </el-select>
            </div>

            <div v-show="active == 2">
              <el-transfer
                v-model="choosenDataServices"
                :data="dataServices"
              ></el-transfer>
            </div>
            <el-button @click="active--">back</el-button>
            <el-button
              @click="active++"
              v-show="active < 2"
            >next</el-button>
            <el-button
              @click="createEvaluationService"
              v-show="active == 2"
            >create</el-button>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import config from "@/config";
import { get, put, post, del } from "@/axios";
import echarts from "echarts";
import { mapState } from "vuex";
export default {
  data() {
    return {
      id: this.$route.params.id,
      g2s: {},
      active: 0,
      avtiveTab: "manage",
      resourceCollect: {},
      resourceCollectVisible: false,
      evaluationVisible: false,
      evaluationService: {},
      evaluationServices: [],
      contextDefine: {
        theme: {
          name: null
        },
        object: {
          name: null
        },
        boundary: {
          name: null
        }
      },
      contextVisible: false,
      typeList: [
        "Context definition",
        "Geographic simulation construction",
        "Resource collection",
        "Geographical simulation compute",
        "Simulation  evaluation"
      ],
      chart: {},
      contentHeight: window.innerHeight - 200,
      dataServices: [],
      choosenDataServices: []
    };
  },
  computed: {
    ...mapState(["user"])
  },
  methods: {
    view({ id }) {
      window.open(`${config.tomcatURL}/evaluation/#/gist/${id}`, "_blank");
    },
    edit({ id }) {
      window.open(
        `${config.tomcatURL}/evaluation/#/gist/${id}?name=${this.user.name}&token=${this.user.token}`,
        "_blank"
      );
    },
    async remove(row) {
      console.log(row);
      let evaluationId = row.id;
      await del("/g2s/{id}/evaluationService/{evaluationId}", null, {
        id: this.id,
        evaluationId
      });
      this.getEvaluationServices();
    },
    initEchart() {
      let option = {
        animationDurationUpdate: 500,
        animationEasingUpdate: "quinticInOut",
        legend: {
          show: true,
          data: [
            {
              name: "Context definition",
              icon: "circle"
            },
            {
              name: "Geographic simulation construction",
              icon: "circle"
            },
            {
              name: "Resource collection",
              icon: "circle"
            },
            {
              name: "Geographical simulation compute",
              icon: "circle"
            },
            {
              name: "Simulation  evaluation",
              icon: "circle"
            }
          ]
        },
        series: [
          {
            type: "graph",
            layout: "none",
            legendHoverLink: true,
            roam: this.procedureDrag,
            label: {
              normal: {
                show: true
              }
            },
            edgeSymbol: ["circle", "arrow"],
            edgeSymbolSize: [4, 10],
            focusNodeAdjacency: true,
            data: [
              {
                name: "Context definition",
                category: "Context definition",
                label: "模拟上下文特征定义",
                symbolSize: 45,
                x: 500,
                y: 500
              },
              {
                name: "Geographic simulation construction",
                category: "Geographic simulation construction",
                label: "模拟概念逻辑图绘制",
                x: 600,
                y: 500,
                symbolSize: 45
              },
              {
                name: "Resource collection",
                category: "Resource collection",
                label: "资源收集",
                symbolSize: 45,
                x: 700,
                y: 500
              },
              {
                name: "Geographical simulation compute",
                category: "Geographical simulation compute",
                label: "模拟计算",
                symbolSize: 45,
                x: 800,
                y: 500
              },
              {
                name: "Simulation  evaluation",
                category: "Simulation  evaluation",
                label: "模拟验证",
                symbolSize: 45,
                x: 900,
                y: 500
              }
            ],
            categories: [
              {
                name: "Context definition"
              },
              {
                name: "Geographic simulation construction"
              },
              {
                name: "Resource collection"
              },
              {
                name: "Geographical simulation compute"
              },
              {
                name: "Simulation  evaluation"
              }
            ],
            links: [
              {
                source: "Context definition",
                target: "Geographic simulation construction"
              },
              {
                source: "Geographic simulation construction",
                target: "Resource collection"
              },
              {
                source: "Resource collection",
                target: "Geographical simulation compute"
              },
              {
                source: "Geographical simulation compute",
                target: "Simulation  evaluation"
              }
            ],
            lineStyle: {
              normal: {
                opacity: 1,
                width: 5,
                curveness: 0.1
              }
            }
          }
        ]
      };
      this.chart = echarts.init(this.$refs.steps);
      this.chart.setOption(option);
      this.chart.on("dblclick", ({ data }) => {
        let type = data.name;
        switch (type) {
          case "Context definition":
            {
              this.contextVisible = true;
            }
            break;
          case "Geographic simulation construction":
            {
              window.open(`${config.tomcatURL}/graph/index.html?id=${this.id}`);
            }
            break;
          case "Resource collection":
            {
              this.resourceCollectVisible = true;
            }
            break;
          case "Geographical simulation compute":
            {
              let routeUrl = this.$router.resolve({
                path: `/g2s/${this.id}/compute`
              });
              window.open(routeUrl.href, "_blank");
            }
            break;
          case "Simulation  evaluation":
            {
              this.evaluationVisible = true;
            }
            break;
        }
      });
    },
    async getEvaluationServices() {
      this.evaluationServices = await get("g2s/{id}/evaluationServices", null, {
        id: this.id
      });
    },
    async getDataServices() {
      //Todo 后面直接g2s就应该包含这些信息
      let data = await get("/g2s/{id}/dataServices", null, {
        id: this.id
      });

      if (data.originalDataServices != null) {
        data.originalDataServices.forEach(el => {
          this.dataServices.push({
            key: el.id,
            label: el.name,
            id: el.id
          });
        });
      }

      if (data.intermediateDataServices != null) {
        data.intermediateDataServices.forEach(el => {
          this.dataServices.push({
            key: el.id,
            label: el.name,
            id: el.id
          });
        });
      }
    },
    async publish() {
      this.g2s = await put(
        "/g2s/{id}",
        {
          isPublish: this.g2s.isPublish
        },
        {
          id: this.id
        }
      );
    },
    async createEvaluationService() {
      let inputs = [];
      this.choosenDataServices.forEach(choosen => {
        let findData = this.dataServices.find(el => {
          return el.id == choosen;
        });
        inputs.push({
          name: findData.label,
          isOptional: false,
          dataServiceId: findData.id
        });
      });
      this.evaluationService.behavior = {
        inputs
      };
      this.evaluationService.creator = this.user.name;
      let serviceId = await post(
        "/g2s/{id}/evaluationService",
        this.evaluationService,
        {
          id: this.id
        }
      );
      this.getEvaluationServices();
      this.resourceCollectVisible = false;
      window.open(
        `${config.tomcatURL}/evaluation/#/gist/${serviceId}?name=${this.user.name}&token=${this.user.token}`,
        "_blank"
      );
    },
    async update(type) {
      if (type == "context") {
        this.g2s = await put(
          "/g2s/{id}",
          {
            contextDefine: this.contextDefine
          },
          {
            id: this.id
          }
        );
      } else {
        this.g2s = await put(
          "/g2s/{id}",
          {
            resourceCollect: this.resourceCollect
          },
          {
            id: this.id
          }
        );
      }
    },
    setContext() {
      this.update("context");
      this.contextVisible = false;
    },
    setResourceCollect() {
      this.update("resourceCollect");
      this.resourceCollectVisible = false;
    }
  },
  async mounted() {
    this.g2s = await get("/g2s/{id}", null, {
      id: this.id
    });
    if (this.g2s.contextDefine !== null) {
      this.contextDefine = this.g2s.contextDefine;
    }
    if (this.g2s.resourceCollect != null) {
      this.resourceCollect = this.g2s.resourceCollect;
    }
    this.initEchart();
    this.getDataServices();
    this.getEvaluationServices();
  }
};
</script>

<style>
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
</style>
