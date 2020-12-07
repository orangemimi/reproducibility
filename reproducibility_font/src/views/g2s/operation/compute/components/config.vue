<template>
  <div class="main">
    <el-row class="title">
      <el-col :span="12">
        <i class="el-icon-setting"></i>
        {{ service.name }}
      </el-col>
      <el-col
        :span="4"
        :offset="8"
      >
        <el-button
          class="btn"
          plain
          type="primary"
          @click="showDialogInvoke()"
        >
          invoke</el-button>
        <el-button
          class="btn"
          plain
          type="primary"
          @click="showDialogInstances()"
        >
          instances</el-button>
      </el-col>
    </el-row>

    <el-row>
      <el-col>
        <p>{{ service.description }}</p>
      </el-col>
    </el-row>

    <el-row>
      <div class="group">
        <el-row class="title">Input</el-row>
        <div class="items">
          <el-row
            v-for="(input, inputIndex) in service.behavior.inputs"
            :key="inputIndex"
            class="item"
          >
            <el-row>
              <el-col :span="16">
                <span class="event-name">
                  <span
                    v-show="!input.isOptional"
                    style="color:red"
                  >*</span>{{ input.name }}</span>
              </el-col>
              <el-col
                :span="3"
                :offset="2"
              >
                <el-select
                  v-model="input.dataServiceId"
                  placeholder="请绑定对应的数据服务"
                >
                  <el-option-group
                    key="originalDataServices"
                    label="原始数据集"
                  >
                    <el-option
                      v-for="dataService in dataServices.originalDataServices"
                      :key="dataService.id"
                      :label="dataService.name"
                      :value="dataService.id"
                    >
                      <span style="float: left">{{ dataService.name }}</span>
                    </el-option>
                  </el-option-group>

                  <el-option-group
                    key="intermediateDataServices"
                    label="中间数据集"
                  >
                    <el-option
                      v-for="dataService in dataServices.intermediateDataServices"
                      :key="dataService.id"
                      :label="dataService.name"
                      :value="dataService.id"
                    >
                      <span style="float: left">{{ dataService.name }}</span>
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-col>
            </el-row>
            <el-row>{{ input.description }}</el-row>
          </el-row>
        </div>
      </div>

      <div
        class="group"
        v-show="service.behavior.parameters"
      >
        <el-row class="title">params</el-row>
        <div class="items">
          <el-row
            v-for="(parameter, parameterIndex) in service.behavior.parameters"
            :key="parameterIndex"
            class="item"
          >
            <el-row>
              <el-col :span="16">
                <span class="event-name"> {{ parameter.name }}</span>
              </el-col>
              <el-col
                :span="3"
                :offset="2"
              >
                <el-tooltip
                  :content="parameter.tooltip"
                  placement="top"
                  effect="light"
                >
                  <component
                    :is="typeMapping(parameter.type)"
                    :initParameter="parameter"
                  ></component>
                </el-tooltip>
              </el-col>
            </el-row>
            <el-row>{{ parameter.description }}</el-row>
          </el-row>
        </div>
      </div>

      <div class="group">
        <el-row class="title">Output</el-row>
        <div class="items">
          <el-row
            v-for="(output, outputIndex) in service.behavior.outputs"
            :key="outputIndex"
            class="item"
          >
            <el-row>
              <el-col :span="16">
                <span class="event-name"> {{ output.name }}</span>
              </el-col>
              <el-col
                :span="3"
                :offset="2"
              >
                <div>
                  null
                </div>
              </el-col>
            </el-row>
            <el-row>{{ output.description }}</el-row>
          </el-row>
        </div>
      </div>
    </el-row>

    <el-dialog
      title="运行实例"
      :visible.sync="dialogInvokeVisible"
    >
      <el-input
        v-model="instanceName"
        autocomplete="off"
      >
        <template slot="prepend">运行实例名称:</template></el-input>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogInvokeVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="invoke()"
        >Invoke</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="实例集合"
      :visible.sync="dialogInstancesVisible"
    >
      <el-table
        :data="instancesData"
        stripe
        style="width: 100%"
      >
        <el-table-column type="expand">
          <template slot-scope="props">
            <h1>Model Configuration</h1>
            <el-divider></el-divider>
            <innerTable :behavior="props.row.service.behavior"></innerTable>
          </template>
        </el-table-column>

        <el-table-column
          prop="name"
          label="name"
          width="150"
        ></el-table-column>
        <el-table-column
          prop="createTime"
          label="createTime"
          width="200"
        ></el-table-column>
        <el-table-column
          prop="statusEnum"
          label="statusEnum"
          :filters="[
            { text: 'success', value: 'FINISH' },
            { text: 'error', value: 'ERROR' },
            { text: 'run', value: 'RUN' }
          ]"
          :filter-method="filterTag"
          filter-placement="bottom-end"
        >
          <template slot-scope="scope">
            <el-tag
              :type="statusEnumTag(scope.row.statusEnum)"
              disable-transitions
            >{{ scope.row.statusEnum }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="small"
              round
              @click="handleBind(scope.row)"
            >Bind</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import InputParameter from "_com/behaviorUI/InputParameter";
import RangeParameter from "_com/behaviorUI/RangeParameter";
import SelectParameter from "_com/behaviorUI/SelectParameter";
import SliderParameter from "_com/behaviorUI/SliderParameter";
import TableParameter from "_com/behaviorUI/TableParameter";
import { dateFormat } from "@/lib/utils";
import { get, post } from "@/axios";
import innerTable from "_com/innerTable";
export default {
  props: ["service", "type", "dataServices"],
  data() {
    return {
      id: this.$route.params.id,
      dialogInvokeVisible: false,
      dialogInstancesVisible: false,
      instancesData: [],
      instanceName: ""
    };
  },
  methods: {
    async handleBind(row) {
      await post(
        "/g2s/{id}/bind/{instanceId}?instanceEnum=" + row.instanceEnum,
        null,
        {
          id: this.id,
          instanceId: row.id
        }
      );

      this.$message({
        message: "绑定成功",
        type: "success"
      });
      this.dialogInstancesVisible = false;
    },
    statusEnumTag(status) {
      if (status === "FINISH") {
        return "success";
      } else if (status === "ERROR") {
        return "danger";
      } else {
        return "info";
      }
    },
    filterTag(value, row) {
      return row.statusEnum === value;
    },
    showDialogInvoke() {
      this.dialogInvokeVisible = true;
      this.instanceName = this.service.name + "_" + dateFormat(new Date());
    },
    showDialogInstances() {
      this.dialogInstancesVisible = true;
      this.refreshInstances();
    },

    async refreshInstances() {
      this.instancesData = await get(
        "/g2s/{id}/getInstances/{type}/{serviceId}",
        null,
        {
          id: this.id,
          type: this.type,
          serviceId: this.service.id
        },
        false
      );
    },
    async invoke() {
      this.service.details = null;
      await post(
        "/g2s/{id}/invoke",
        {
          name: this.instanceName,
          statusEnum: "READY",
          instanceEnum: this.type,
          service: this.service,
          creator:this.$store.state.user.name
        },
        {
          id: this.id
        }
      );
      this.dialogInvokeVisible = false;
    },
    typeMapping(type) {
      let vueType;
      switch (type) {
        case "input_parameter":
          {
            vueType = "InputParameter";
          }
          break;
        case "range_parameter":
          {
            vueType = "RangeParameter";
          }
          break;
        case "slider_parameter":
          {
            vueType = "SelectParameter";
          }
          break;
        case "select_parameter":
          {
            vueType = "SliderParameter";
          }
          break;
        case "table_parameter":
          {
            vueType = "TableParameter";
          }
          break;
      }
      return vueType;
    }
  },
  components: {
    InputParameter,
    RangeParameter,
    SelectParameter,
    SliderParameter,
    TableParameter,
    innerTable
  }
};
</script>

<style>
.table-expand {
  font-size: 0;
}
.table-expand label {
  width: 90px;
  color: #99a9bf;
}
.table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.main {
  position: relative;
}
.btn {
  margin-top: 20px;
}

.title {
  position: relative;
  font-size: 18px;
  padding: 0px 0px 30px 0px;
}
.title i {
  font-size: 30px !important;
}
.group {
  position: relative;
  padding-bottom: 30px;
}
.group > .title {
  font-style: italic;
  font-size: 12px;
  padding-bottom: 10px;
  border-bottom: solid 2px #999;
}
.group > .items {
  padding: 10px 0px 6px 50px;
}
.group > .items > .item {
  padding: 20px 10px 20px 0px;
  border-bottom: dotted 1px #999999;
  line-height: 2;
}
</style>
