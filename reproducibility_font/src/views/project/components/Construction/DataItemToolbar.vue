<template>
  <div class="main">
    <el-col class="dataContainer" :span="22" :offset="1">
      <div class="params-group">
        <el-row v-if="stateListInput !== undefined" class="stateTitle"
          >Input</el-row
        >
        <div class="event">
          <div
            class="event-desc"
            v-for="(modelInEvent, inEventIndex) in stateListInput"
            :key="inEventIndex"
            ref="inputItemList"
          >
            <el-card :title="modelInEvent.name">
              <div
                v-show="
                  modelInEvent.optional == 'False' ||
                  modelInEvent.optional == 'false'
                "
                class="event_option"
              >
                *
              </div>
              <div class="event_name">
                {{ modelInEvent.name }}
              </div>
            </el-card>
          </div>
        </div>
      </div>

      <div class="params-group">
        <el-row v-if="stateListOutput !== undefined" class="stateTitle"
          >Output</el-row
        >
        <div class="event">
          <div
            class="event-desc"
            v-for="(modelOutEvent, outEventIndex) in stateListOutput"
            :key="outEventIndex"
            ref="outputItemList"
          >
            <el-card :title="modelOutEvent.name">
              <div class="event_name">
                {{ modelOutEvent.name }}
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </el-col>
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
          this.init();
        }
      },
      deep: true,
    },
  },

  computed: {},
  data() {
    return {
      doi: "",
      modelIntroduction: {},
      modelInstance: {},
      md5: "",
      stateList: [],
      stateListInput: [],
      stateListOutput: [],
    };
  },

  methods: {
    initSetTimeOut() {
      return new Promise(function (reslove, reject) {
        // 模拟异步，加载组件
        setTimeout(function () {
          console.log("等了1秒钟");
          reslove(1000);
        }, 1000);
      });
    },

    async init() {
      await this.getModelInfo();
    },

    async getModelInfo() {
      let data = await get(
        `/GeoProblemSolving/modelTask/ModelBehavior/${this.doi}`
      ); //获得模型所有信息
      this.md5 = data.md5;
      this.modelIntroduction = data;
      this.stateList = data.convertMdlJson;
      await this.getInAndOut();
    },
    async getInAndOut() {
      let stateList = this.stateList;
      let input = [];
      let output = [];
      stateList.forEach((state) => {
        state.Event.forEach((event) => {
          if (event.type == "response") {
            input.push(event);
          } else if (event.type == "noresponse") {
            output.push(event);
          }
        });
      });
      this.stateListInput = input;
      this.stateListOutput = output;
      // console.log(this.stateListInput, this.stateListOutput);
      await this.initSetTimeOut();
      this.$emit("getInAndOut", this.stateListInput, this.stateListOutput);
    },
  },
};
</script>

<style lang="scss" scoped>
.main {
  position: relative;
  width: 200px;

  .state-desc {
    margin: 0px 0px 15px 0px;
    padding: 4px 0px;
    line-height: 2;
    background-color: #f3f3f3;
    font-size: 16px;
    font-style: italic;
  }
  .el-tabs__item {
    font-size: 16px;
  }
  .el-tabs__item:hover {
    color: #00bbd8;
    background-color: #b5dce244;
  }
  .el-tabs__item.is-active {
    color: #00bbd8;
  }
  .el-tabs__active-bar {
    background-color: #00bbd8;
  }

  .leftContainer {
    background-color: rgba(142, 200, 255, 0.2);
    border-radius: 5px;
    // box-shadow: 0px 0px 4px rgb(203, 207, 212);
    width: 100%;
    .modelState {
      color: rgb(37, 44, 66);
      font-size: 14px;
    }
  }

  .stateTitle {
    font-size: 16px;
    font-weight: 600;
    color: rgb(87, 173, 253);
    font-style: italic;
  }

  .event {
    .event:hover {
      background-color: #c4d9f734;
    }
    .event_option {
      color: red;
      float: left;
      width: 10px;
      font-size: 14px;
      font-weight: 600;
    }
    .event_name {
      font-size: 14px;
      font-weight: 600;
      padding-left: 10px;

      /* padding: 10px 0; */
    }
    >>> .el-card__body {
      padding: 5px;
      width: 200px;
    }
    .event-desc:hover {
      cursor: pointer;
    }
  }

  .des {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    /* !autoprefixer: off */
    -webkit-box-orient: vertical;
    font-size: 14px;
  }
  .title {
    font-weight: 600;
    font-size: 20px;
    margin: 20px 0 10px 0;
  }
}
</style>