<template>
  <div class="main">
    <el-row style="width: 100%;">
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
                <FileUpload
                  :input="input"
                  :disabled="disabled"
                ></FileUpload>
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
                    :disabled="disabled"
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
                <div v-if="output.dataServiceId == undefined">null</div>
                <el-button
                  v-else
                  icon="el-icon-download"
                  plain
                  @click="download(output.dataServiceId)"
                ></el-button>
              </el-col>
            </el-row>
            <el-row>{{ output.description }}</el-row>
          </el-row>
        </div>
      </div>
    </el-row>
  </div>
</template>

<script>
import config from "@/config";
import InputParameter from "_com/behaviorUI/InputParameter";
import FileUpload from "_com/behaviorUI/FileUpload";
import RangeParameter from "_com/behaviorUI/RangeParameter";
import SelectParameter from "_com/behaviorUI/SelectParameter";
import SliderParameter from "_com/behaviorUI/SliderParameter";
import TableParameter from "_com/behaviorUI/TableParameter";
export default {
  props: ["service", "disabled"],
  methods: {
    download(dataServiceId) {
      window.open(`${config.containerURL}/data_service/fetch/${dataServiceId}`);
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
    FileUpload
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
