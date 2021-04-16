<template>
  <div class="main">
    <el-tabs v-model="activeName" type="card" class="tabs">
      <el-tab-pane label="Input" name="input" class="tab">
        <div class="events">
          <div class="event">
            <div class="event-desc" v-for="(modelInEvent, inEventIndex) in stateListInput" :key="inEventIndex" ref="inputItemList">
              <el-card :title="modelInEvent.name">
                <div v-show="modelInEvent.optional == 'false'" class="event_option">
                  *
                </div>
                <div class="event_name">
                  {{ modelInEvent.name }}
                </div>
              </el-card>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Output" name="output" class="tab">
        <div class="events">
          <div class="event">
            <div class="event-desc" v-for="(modelOutEvent, outEventIndex) in stateListOutput" :key="outEventIndex" ref="outputItemList">
              <el-card :title="modelOutEvent.name" class="event-card">
                <div class="event_name">
                  {{ modelOutEvent.name }}
                </div>
              </el-card>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
// import file from '@/components/dataTemplate/File';
import { getModelInfo } from '@/api/request';
import { initSetTimeOut } from '@/utils/utils';
export default {
  props: {
    cell: {
      type: Object
    }
  },
  watch: {
    cell: {
      handler(val) {
        if (val != null) {
          this.doi = val.doi;
          this.init();
        }
      },
      deep: true
    }
  },

  computed: {},
  data() {
    return {
      doi: '',
      modelIntroduction: {},
      modelInstance: {},
      md5: '',
      stateList: [],
      stateListInput: [],
      stateListOutput: [],
      activeName: 'input'
    };
  },

  methods: {
    async init() {
      await this.getModelInformation();
    },

    async getModelInformation() {
      let data = await getModelInfo(this.doi); //获得模型所有信息
      console.log(data);
      this.md5 = data.md5;
      this.modelIntroduction = data;
      this.stateList = data.convertMdlJson;
      await this.getInAndOut();
    },
    async getInAndOut() {
      let stateList = this.stateList;
      let input = [];
      let output = [];
      stateList.forEach(state => {
        state.Event.forEach(event => {
          if (event.type == 'input') {
            input.push(event);
          } else if (event.type == 'output') {
            output.push(event);
          }
        });
      });
      this.stateListInput = input;
      this.stateListOutput = output;
      console.log('dataitemtoolbar', this.stateListInput, this.stateListOutput);
      await initSetTimeOut();
      this.$emit('getInAndOut', this.stateListInput, this.stateListOutput);
    }
  }
  // created() {
  //   this.$set(this.cell);
  // }
};
</script>

<style lang="scss" scoped>
.main {
  position: relative;
  width: 200px;

  .tabs {
    width: 100%;
    .tab {
      .events {
        .event {
          .event-desc {
            margin: 5px 0;

            /deep/ .el-card {
              box-shadow: 0 1px 5px 0 rgba(161, 161, 161, 0.1);
              .el-card__body {
                padding: 5px;
                width: 200px;
                height: 40px;
              }
            }

            .el-card:hover {
              background-color: #3067d61c;
              border: 1px solid #25252534;
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
            }
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
    }
  }
}
</style>
