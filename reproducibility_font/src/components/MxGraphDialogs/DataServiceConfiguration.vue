<template>
  <div class="main">
    <div class="info">
      <div>Information:</div>
    </div>
    <div class="event">
      <div>Input:</div>
      <el-row :gutter="10">
        <div class="event-desc" v-for="(modelInEvent, inEventIndex) in stateListInput" :key="inEventIndex" ref="inputItemList">
          <el-col :span="6" :class="modelInEvent.isSelect != undefined && modelInEvent.isSelect ? 'selectCard' : 'unselectCard'">
            <el-card :title="modelInEvent.name" @click.native="addSelectItem(modelInEvent)">
              <div class="event_option">*</div>
              <div class="event_name">
                {{ modelInEvent.name }}
              </div>
            </el-card>
          </el-col>
        </div>
      </el-row>
      <div v-if="stateListParameter.length > 0">Parameter:</div>
      <el-row :gutter="10" v-if="stateListParameter.length > 0">
        <div class="event-desc" v-for="(modelInEvent, inEventIndex) in stateListParameter" :key="inEventIndex" ref="inputItemList">
          <el-col :span="6" :class="modelInEvent.isSelect != undefined && modelInEvent.isSelect ? 'selectCard' : 'unselectCard'">
            <el-card :title="modelInEvent.name" @click.native="addSelectItem(modelInEvent)">
              <div class="event_option">*</div>
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
        <select-card :currentItem="item" :type="'dataService'" @removeItem="removeItem"></select-card>
      </div>
    </div>

    <el-button size="mini" @click="submit">Submit</el-button>
  </div>
</template>

<script>
import selectCard from '_com/Cards/SelectCard';
import { getDataServiceInfo1, getDataServiceInfoByPortal } from '@/api/request';
import { hasProperty } from '@/utils/utils';
// import { initSetTimeOut } from '@/utils/utils';
export default {
  props: {
    cell: {
      type: Object
    }
  },
  components: { selectCard },
  // watch: {
  //   cell: {
  //     handler(val) {
  //       console.log(2)
  //       if (hasProperty(val, 'id') && val.type == 'dataService') {
  //         this.init();
  //       }
  //     },
  //     deep: true,
  //     immediate: true
  //   }
  // },

  computed: {},
  data() {
    return {
      doi: '',
      modelIntroduction: {},
      modelInstance: {},
      md5: '',
      stateListInput: [],
      stateListOutput: [],
      stateListParameter: [],
      activeName: 'input',
      selectItemListToGraph: []
    };
  },

  methods: {
    async init() {
      // await this.getServiceInfo();
      await this.getDataServiceInfoByPortal();
    },

    async getDataServiceInfoByPortal() {
      let data = await getDataServiceInfoByPortal(this.cell.nodeAttribute.oid, this.cell.nodeAttribute.dataServiceId);
      console.log(data);
      this.stateListInput = data.input;
      this.stateListParameter = data.parameter;
      this.stateListOutput = data.output;
      this.stateListInput.forEach(item => {
        item.nodeType = 'input';
      });
      this.stateListParameter.forEach(item => {
        item.nodeType = 'parameter';
      });
      this.stateListOutput.forEach(item => {
        item.nodeType = 'output';
      });
    },

    async getServiceInfo() {
      let form = {
        // dataServiceId:this.cell.nodeAttribute.dataServiceId,
        token: this.cell.nodeAttribute.token,
        name: this.cell.name
        // type: this.cell.nodeAttribute.type
      };
      let data = await getDataServiceInfo1(form); //获得模型所有信息

      let json = JSON.parse(data);

      let stateListInput = json[0].list.metaDetailJSON.Input;
      let stateListOutput = json[0].list.metaDetailJSON.Output;
      let stateListParameter = json[0].list.metaDetailJSON.Parameter;

      this.stateListInput = stateListInput.map(element => {
        element.nodeType = 'input';
        element.dataServiceId = this.cell.nodeAttribute.dataServiceId;
        element.token = this.cell.nodeAttribute.token;
        element.type = this.cell.nodeAttribute.type; //Processing
        element.stateName = this.cell.name;
        return element;
      });
      this.stateListOutput = stateListOutput.map(element => {
        element.nodeType = 'output';
        element.dataServiceId = this.cell.nodeAttribute.dataServiceId;
        element.token = this.cell.nodeAttribute.token;
        element.type = this.cell.nodeAttribute.type; //Processing
        element.stateName = this.cell.name;
        return element;
      });
      this.stateListParameter = stateListParameter.map(element => {
        element.nodeType = 'parameter';
        element.dataServiceId = this.cell.nodeAttribute.dataServiceId;
        element.token = this.cell.nodeAttribute.token;
        element.type = this.cell.nodeAttribute.type; //Processing
        element.stateName = this.cell.name;
        return element;
      });
    },

    addSelectItem(item) {
      console.log('selecty', item);
      if (hasProperty(item, 'isSelect') && item.isSelect) {
        this.selectItemListToGraph.splice(this.selectItemListToGraph.findIndex(arrItem => arrItem.name == item.name));
        item.isSelect = false;
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
  },
  created() {
    // if (this.cell != undefined) {
    //   this.init();
    // }
    console.log(1);
    this.init();
  }
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
