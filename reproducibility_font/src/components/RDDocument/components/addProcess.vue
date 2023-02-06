<template>
  <div >
    <el-form class="mform" :model="form" label-width="100px" :rules="rules">
      <el-form-item label="步骤名称" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="步骤类型" prop="stepType" >
        <el-select v-model="form.stepType" placeholder="请选择步骤类型" filterable allow-create>
          <el-option label="数据预处理" value="数据预处理" />
          <el-option label="模拟分析" value="模拟分析" />
          <el-option label="数据后处理" value="数据后处理" />
          <el-option label="误差评估" value="误差评估" />
          <el-option label="误差分析" value="误差分析" />
          <el-option label="不确定性分析" value="不确定性分析" />
          <el-option label="收敛性分析" value="收敛性分析" />
          <el-option label="其他(请直接输入)" value=" " disabled/>
        </el-select>
      </el-form-item>
      <el-form-item label="操作类型" prop="operateType">
        <el-select v-model="form.operateType" placeholder="请选择操作类型" >
          <el-option label="软件操作" value="软件操作" />
          <el-option label="服务运行" value="服务运行" />
          <el-option label="代码运行" value="代码运行" />
        </el-select>
      </el-form-item>
      <div style="display:flex;align-items:center ">
      <h5>从“资源”中选择此步骤中所使用的模型：</h5>
      <el-divider style="margin-left: 10px;width:250px" />
      </div>
      <el-form-item label="模型资源" prop="modelResource">
        <el-select value-key="id" v-model="form.modelResource" placeholder="请选择模型进行配置">
          <el-option v-for="item in dataItemList" :key="item.id" :label="item.name" :value="item" />
        </el-select>
      </el-form-item>
      <div style="display:flex;align-items:center ">
      <h5>选择“资源”匹配模型的输入、输出和参数：</h5>
      <el-divider style="margin-left: 10px;width:270px" />
      </div>
      <el-form-item label="输入信息：">
        <div class="mess" v-for="(item1,index1) in form.modelInputs" :key="item1.id">
          <el-select value-key="id" v-model="form.modelInputs[index1]" :placeholder="'输入信息 '+(index1+1)">
            <el-option v-for="item in dataItemList" :key="item.id" :label="item.name" :value="item" />
          </el-select>
          <el-button style="margin-left:10px" type="danger" icon="el-icon-delete" size="small" plain @click="handleCloseInput1(index1)"></el-button>
        </div>
        <el-button class="button-new-tag ml-1" size="small" @click="showInput1">
          + 添加输入信息
        </el-button>
    </el-form-item>
    <el-form-item label="输出信息：">
        <div class="mess" v-for="(item3,index3) in form.modelOutputs" :key="item3.id">
          <el-select value-key="id" v-model="form.modelOutputs[index3]" :placeholder="'输出信息 '+(index3+1)">
            <el-option v-for="item in dataItemList" :key="item.id" :label="item.name" :value="item" />
          </el-select>
          <el-button style="margin-left:10px" type="danger" icon="el-icon-delete" size="small" plain @click="handleCloseInput3(index3)"></el-button>
        </div>
        <el-button class="button-new-tag ml-1" size="small" @click="showInput3">
          + 添加输出信息
        </el-button>
    </el-form-item>
    <el-form-item label="参数信息：">
      <div class="mess" v-for="(item2,index2) in form.parameters" :key="item2.id">
          <el-select value-key="id" v-model="form.parameters[index2]" :placeholder="'参数信息 '+(index2+1)">
            <el-option v-for="item in dataItemList" :key="item.id" :label="item.name" :value="item" />
          </el-select>
          <el-button style="margin-left:10px" type="danger" icon="el-icon-delete" size="small" plain @click="handleCloseInput2(index2)"></el-button>
        </div>
        <el-button class="button-new-tag ml-1" size="small" @click="showInput2">
          + 添加参数信息
        </el-button>
    </el-form-item>
      <el-divider/>
      <el-form-item label="步骤描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
        />
      </el-form-item>
    <el-form-item label="参考文献">
        <el-tag
          v-for="tag in form.references"
          :key="tag"
          class="mx-1"
          closable
          :disable-transitions="false"
          @close="handleClose(tag)"
        >
          {{tag.length>33?tag.substring(0,22)+'......'+tag.substring(tag.length-10,tag.length):tag}}
        </el-tag>
        <el-input
          v-if="inputVisible"
          ref="saveTagInput"
          v-model="inputValue"
          class="ml-1 w-20"
          size="small"
          @keyup.enter.native="handleInputConfirm"
          @blur="handleInputConfirm"
        />
        <el-button v-else class="button-new-tag ml-1" size="small" @click="showInput">
          + 添加参考文献
        </el-button>
    </el-form-item>
    <h5 style="margin-left: 30px;color:rgb(150,150,150);">建议格式:[1]岳天祥,刘纪远.生态地理建模中的多尺度问题[J].第四纪研究,2003(03):256-261.</h5>
      <!-- <el-form-item label="添加图片">
        <picture-upload
          :pictureList="pictureList"
          @returnPictureList="handlePictures"
        ></picture-upload>
      </el-form-item> -->
      <!-- <el-form-item label="添加视频" style="display: flex;align-items:flex-end;">
      <div style="display: flex;align-items:center;">
        <el-button v-if="!(processVideoItem && processVideoItem.id!='')" style="margin-top: 20px;margin-right: 10px;" type="primary" @click="uploadClick" >视频上传</el-button>
        <div v-if="processVideoItem && processVideoItem.id!=''">已上传文件：<el-tag>{{processVideoItem.name}}</el-tag></div>
        <el-button v-if="processVideoItem && processVideoItem.id!=''" @click="deleteVideo" style="margin-left: 10px;" type="danger" size="small" plain>删除</el-button>
        </div>
      </el-form-item> -->
      <el-form-item label="其他补充">
        <el-input v-model="form.other"  type="textarea" />
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        plain
        @click="addProcessClick"
        v-if="operateType === 'add'"
        >确定</el-button>
      <el-button type="primary" plain @click="updateProcessClick" v-else
        >修改</el-button>
    </div>
  </div>
</template>

<script>
import { getDataItemsByProjectId, addProcess, editProcess } from '@/api/request';
export default {
  props: {
        processItem: {
            type: Object
        },
        operateType: {
            type: Object
        },
        processIndex:{
            type: Number
        }
    },
  data() {
    return {
      dataItemList: [],
      projectId: this.$route.params.id,
      inputVisible: false,
      inputValue: '',
      inputVisible1: false,
      inputValue1: {},
      inputVisible2: false,
      inputValue2: {},
      inputVisible3: false,
      inputValue3: {},
      form:{
            name: this.processItem.name,
            stepType: this.processItem.stepType,
            operateType: this.processItem.operateType,
            description: this.processItem.description,
            references: this.processItem.references,
            other: this.processItem.other,
            pictures: this.processItem.pictures,
            modelResource: this.processItem.modelResource,
            modelInputs: this.processItem.modelInputs,
            parameters: this.processItem.parameters,
            modelOutputs: this.processItem.modelOutputs
        }
    };
  },

  methods: {
    async addProcessClick(){
      await addProcess(this.projectId, this.form);
      this.$notify({
            title: 'Success',
            message: 'Successfully added!',
            type: 'success'
          });
    },

    async updateProcessClick(){
      await editProcess(this.projectId, {form:this.form, index:this.processIndex});
      this.$notify({
            title: 'Success',
            message: 'Successfully edited!',
            type: 'success'
          });
    },

    async getDataCollection() {
      let data = await getDataItemsByProjectId(this.projectId);
      this.dataItemList = data;
    },

    handleCloseInput(tag) {
      this.form.references.splice(this.form.references.indexOf(tag), 1);
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.form.references.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = '';
    },

    handleCloseInput1(index) {
      this.form.modelInputs.splice(index, 1);
    },
    showInput1() {
      this.form.modelInputs.push({});
    },

    handleCloseInput2(index) {
      this.form.parameters.splice(index, 1);
    },
    showInput2() {
      this.form.parameters.push({});
    },

    handleCloseInput3(index) {
      this.form.modelOutputs.splice(index, 1);
    },
    showInput3() {
      this.form.modelOutputs.push({});
    },

  },
  async mounted() {
      await this.getDataCollection();
  }
};
</script>
<style lang="scss" scoped>
  .el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
  .mess{
    display: flex;
    align-items:center;
    margin-top: 10px;
  }
  .mform{
    height: 600px;
    overflow: auto;
  }
</style>
