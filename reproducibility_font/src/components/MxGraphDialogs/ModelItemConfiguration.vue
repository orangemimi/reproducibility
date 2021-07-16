<template>
  <div class="main">
    <div class="info">
      <div>Information:</div>
      {{ modelIntroduction.name }}
    </div>
    <div class="event">
      <div>Input:</div>
      <el-row :gutter="10">
        <div class="event-desc" v-for="(modelInEvent, inEventIndex) in stateListInput" :key="inEventIndex" ref="inputItemList">
          <el-col :span="6" :class="modelInEvent.isSelect != undefined && modelInEvent.isSelect ? 'selectCard' : 'unselectCard'">
            <el-card :title="modelInEvent.name" @click.native="addSelectItem(modelInEvent)">
              <div v-show="modelInEvent.optional == 'false'" class="event_option">
                *
              </div>
              <div class="event_name">
                {{ modelInEvent.name }}
              </div>
            </el-card>
          </el-col>
        </div>
      </el-row>
      <div>Output:</div>
      <el-row :gutter="10">
        <div class="event-desc" v-for="(modelOutEvent, outEventIndex) in stateListOutput" :key="outEventIndex" ref="outputItemList">
          <el-col :span="6" :class="modelOutEvent.isSelect != undefined && modelOutEvent.isSelect ? 'selectCard' : 'unselectCard'">
            <el-card :title="modelOutEvent.name" @click.native="addSelectItem(modelOutEvent)">
              <div class="event_name">
                {{ modelOutEvent.name }}
              </div>
            </el-card>
          </el-col>
        </div>
      </el-row>
    </div>
    <div>
      <div v-for="(item, index) in selectItemListToGraph" :key="index">
        <select-card :currentItem="item" @removeItem="removeItem"></select-card>
      </div>
    </div>

    <el-button size="mini" @click="submit">Submit</el-button>
  </div>
</template>

<script>
import selectCard from '_com/Cards/SelectCard';
import { getModelInfo } from '@/api/request';
import { hasProperty } from '@/utils/utils';
// import { initSetTimeOut } from '@/utils/utils';
export default {
  props: {
    cell: {
      type: Object
    }
  },
  components: { selectCard },
  watch: {
    cell: {
      handler(val, oldVla) {
        if (val != undefined && val != oldVla) {
          console.log('cell', val);
          debugger;
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
      activeName: 'input',
      selectItemListToGraph: []
    };
  },

  methods: {
    async init() {
      await this.getModelInfo();
    },

    async getModelInfo() {
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
      // await initSetTimeOut();
      // this.$emit('getInAndOut', this.stateListInput, this.stateListOutput);
    },
    addSelectItem(item) {
      if (hasProperty(item, 'isSelect') && item.isSelect) {
        this.selectItemListToGraph.splice(this.selectItemListToGraph.findIndex(arrItem => arrItem.eventId == item.eventId));
      } else {
        item.isSelect = true;
        this.selectItemListToGraph.push(item);
      }
    },
    removeItem(item) {
      let index = this.selectItemListToGraph.findIndex(arrItem => arrItem.eventId == item.eventId);
      console.log('index', index);
      this.selectItemListToGraph.splice(index, 1);
      item.isSelect = false;
    },
    submit() {
      this.$emit('selectItemListToGraph', this.selectItemListToGraph);
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
  width: 100%;

  .event {
    width: 100%;
    .event-desc {
      margin: 5px 0;
      .unselectCard {
        /deep/ .el-card {
          box-shadow: 0 1px 5px 0 rgba(161, 161, 161, 0.1);
          .el-card__body {
            padding: 5px;
            width: 200px;
            height: 40px;
          }
        }
      }

      .selectCard {
        /deep/ .el-card {
          box-shadow: 0 1px 5px 0 rgba(161, 161, 161, 0.1);
          background-color: #9ed4aa34;
          .el-card__body {
            padding: 5px;
            width: 200px;
            height: 40px;
          }
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
}
</style>
