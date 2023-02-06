<template>
    <div class="result-conclusion">
      <div class="result-validation">
      <div class="small-title">
        <div class="icon"></div>
        <h4>3.1 结果验证</h4>
      </div>
      <div>
      <el-table :data="resultValidationList" style="width: 100%" max-height="250">
        <el-table-column align='center' prop="name" label="步骤名称" width="250" />
        <el-table-column align='center' prop="stepType"  label="验证指标" width="250" />
        <!-- 判断 -->
        <el-table-column align='center'  label="输入数据" >
          <template #default="scope">
            <div style="display: flex;justify-content: center;align-items: center;">
              <div style="margin-left:10px" v-for="(item,index) in scope.row.dataResource" :key="index">
                <el-tag v-if="item.id">{{item.name}}</el-tag>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column align='center' fixed="right" label="操作" width="250">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="updateClick(scope.$index)">
              查看
            </el-button>
            <el-button link type="danger" size="small" @click="deleteClick(scope.$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button class="mt-4" style="width: 100%" @click="addResultValidationClick()">添加结果验证</el-button>
      <el-dialog v-if="addDialog" :visible.sync="addDialog" width="50%"  title="结果验证">
        <div style="height: 60vh;overflow: auto;">
          <h3>验证信息</h3>
            <el-form  v-if="addDialog" :model="resultValidationItem" label-width="100px" >
            <el-form-item label="步骤名称" prop="name">
              <el-input v-model="resultValidationItem.name" />
            </el-form-item>
            <el-form-item label="步骤描述" prop="description">
              <el-input
                v-model="resultValidationItem.description"
                type="textarea"
                :rows="3"
                placeholder="说明利用哪个验证指标对结果进行验证，以及验证的结果"
              />
            </el-form-item>
            <el-form-item label="验证指标" prop="stepType" >
              <el-select v-model="resultValidationItem.stepType" placeholder="请选择验证指标" filterable allow-create clearable>
                <el-option label="MSE" value="MSE" />
                <el-option label="RMSE" value="RMSE" />
                <el-option label="MAE" value="MAE" />
                <el-option label="MAPE" value="MAPE" />
                <el-option label="其他(请直接输入)" value=" " disabled/>
              </el-select>
            </el-form-item>

            <el-form-item label="输入数据" prop="dataResource" >
              <h5 style="color:rgb(150,150,150);margin-left:10px">从“资源”中选择此步骤中的输入数据</h5>
              <div class="mess" v-for="(item2,index2) in resultValidationItem.dataResource" :key="item2.id">
                <el-select style="margin-bottom:5px" value-key="id" v-model="resultValidationItem.dataResource[index2]" :placeholder="'输入信息 '+(index2+1)">
                  <el-option v-for="item in dataItemList" :key="item.id" :label="item.name" :value="item" />
                </el-select>
                <el-button style="margin-left:10px" type="danger" icon="el-icon-delete" size="small" plain @click="handleCloseInput2(index2)"></el-button>
              </div>
              <el-button class="button-new-tag ml-1" size="small" @click="showInput2">
                + 添加输入数据
              </el-button>
            </el-form-item>
          <h5 class="mh5a"><el-icon><CaretBottom /></el-icon>输出数据:</h5>
            <el-form-item label="名称" prop="resultOutput">
              <el-input v-model="resultValidationItem.resultOutput.name" />
            </el-form-item>
            <el-form-item label="描述" prop="resultOutput">
              <el-input v-model="resultValidationItem.resultOutput.description" />
            </el-form-item>
            <el-form-item label="发布时间" prop="resultOutput">
              <el-input v-model="resultValidationItem.resultOutput.time" />
            </el-form-item>
            <el-form-item label="数据格式" prop="resultOutput">
              <el-input v-model="resultValidationItem.resultOutput.format" />
            </el-form-item>
            <el-form-item label="存储位置" prop="resultOutput">
                <div style="display:flex;width: 450px;"><el-input v-model="resultValidationItem.resultOutput.storage" placeholder="详细说明获取该数据的原始路径（下载链接）" :readonly="resultValidationItem.resultOutput.isUpload==='true'"/>
                <!-- <a style="margin-left:10px">或</a><el-button style="margin-left:10px" type="primary" @click="storageUpload">上传</el-button>-->
                </div> 
            </el-form-item>
            
            <!-- <el-form-item label="添加图片">
                <picture-upload
                  :pictureList="pictureList"
                  @returnPictureList="handlePictures"
                ></picture-upload>
              </el-form-item>
              <el-form-item label="添加视频" style="display: flex;align-items:flex-end;">
              <div style="display: flex;align-items:center;">
                <el-button v-if="!(VideoItem && VideoItem.id!='')" style="margin-top: 20px;margin-right: 10px;" type="primary" @click="uploadClick" >视频上传</el-button>
                <div v-if="VideoItem && VideoItem.id!=''">已上传文件：<el-tag>{{VideoItem.name}}</el-tag></div>
                <el-button v-if="VideoItem && VideoItem.id!=''" @click="deleteVideo" style="margin-left: 10px;" type="danger" size="small" plain>删除</el-button>
                </div>
              </el-form-item> -->

          <el-form-item>
              <el-button type="primary" v-if="operateType==='add'" @click="submitForm()">提交</el-button>
              <el-button type="primary" v-else @click="submitUpdate()">修改</el-button>
            </el-form-item>
          </el-form>
        <!-- <el-dialog v-if="upload":visible.sync="upload" width="500px" title="数据上传" >
            <el-form :model="uploadForm" label-width="100px">
            <el-form-item label="上传文件" prop="file">
              <el-upload
                ref="uploadRef"
                class="upload-demo"
                :auto-upload="false"
                :on-change="changeupload"
                :on-remove="removeupload"
                :limit="1"
              >
                <template #trigger>
                  <el-button type="primary">选择文件</el-button>
                </template>
                <template #tip>
                  <div class="el-upload__tip text-red">
                      最多上传一个文件，请勿重复上传，上传状态变更后请及时保存
                  </div>
              </template>
                <el-button :disabled="uploadForm.location!=''" style="margin-left:10px" class="ml-3" type="success" @click="submitUpload">
                  上传
                </el-button>
                </el-upload>
              </el-form-item>
              <el-form-item v-if="uploadForm.location" label="下载地址" prop="location">
                <el-input v-model="uploadForm.location" :disabled="true" type="textarea"/>
                <el-button style="margin-top:10px" class="ml-3" type="danger" @click="deleteUpload">
                  删除
                </el-button>
              </el-form-item>
            </el-form>
          </el-dialog> -->
        <!-- <el-dialog v-if="uploadVideo" :visible.sync="uploadVideo">
            <ResultValidationVideoUpload :type="type" :videoItem="VideoItem" :resultValidation="resultValidationItem" @returnVideo="returnVideo"></ResultValidationVideoUpload>
        </el-dialog> -->
        </div>
      </el-dialog>
    </div>
    </div>
      <div style="margin-top:20px;margin-bottom:20px">
        <div class="icon"></div>
        <h4>3.2 结果可视化</h4>
      </div>
      <el-form :model="resultVisualizationForm" label-width="100px">
      <el-form-item label="步骤名称" prop="name">
        <el-input v-model="resultVisualizationForm.name" />
      </el-form-item>
      <el-form-item label="步骤描述" prop="description">
        <el-input
          v-model="resultVisualizationForm.description"
          type="textarea"
          :rows="3"
          placeholder="说明利用什么方法/软件对结果数据进行可视化分析"
        />
      </el-form-item>
      <el-form-item label="步骤类型" prop="stepType" >
        <el-select v-model="resultVisualizationForm.stepType" placeholder="请选择步骤类型" clearable>
          <el-option label="可视化方法" value="可视化方法" />
          <el-option label="可视化软件" value="可视化软件" />
        </el-select>
      </el-form-item>
      <div v-if="resultVisualizationForm.stepType==='可视化软件'">
        <el-form-item label="软件需求" prop="softName" >
          <el-input v-model="resultVisualizationForm.softName" placeholder="需要说明所需的软件名称" />
        </el-form-item>
        <el-form-item label="软件版本" prop="softVersion" >
          <el-input v-model="resultVisualizationForm.softVersion" placeholder="需要说明所需的软件版本"/>
        </el-form-item>
        <el-form-item label="存储位置" prop="softStorage">
          <el-input v-model="resultVisualizationForm.softStorage" placeholder="详细说明获取该软件的原始路径（下载链接）"/>
        </el-form-item>
        <el-form-item label="软件依赖" prop="softDepend" >
          <el-input v-model="resultVisualizationForm.softDepend" placeholder="详细说明软件涉及的依赖"/>
        </el-form-item>
      </div>
      <div v-if="resultVisualizationForm.stepType==='可视化方法'">
        <el-form-item label="代码内容" prop="codeContent">
        <div style="display:flex;width: 900px;"><el-input v-model="resultVisualizationForm.codeContent" type="textarea" placeholder="代码内容" :readonly="resultVisualizationForm.isCodeUpload==='true'"/>
        <!-- <a style="margin-left:10px">或</a><el-button style="margin-left:10px" type="primary" @click="codeContentUpload">上传</el-button>-->
        </div> 
      </el-form-item>
        <el-form-item label="编程语言" prop="codeLanguage" >
          <el-input v-model="resultVisualizationForm.codeLanguage" placeholder="例如：Fortran77, Fortran90, C, C++, Python, Java, IDL, Matlab等"/>
        </el-form-item>
        <el-form-item label="模型依赖库" prop="codeDepend" >
          <el-input v-model="resultVisualizationForm.codeDepend" placeholder="部署或运行代码所需要引入的依赖库，例如Scrapy、Urllib3等"/>
        </el-form-item>
      </div>
      <el-form-item label="输入信息：">
        <h5 style="color:rgb(150,150,150);margin-left:10px">从“资源”中选择此步骤中的输入数据</h5>
        <div class="mess" v-for="(item1,index1) in resultVisualizationForm.dataResource" :key="item1.id">
          <el-select style="margin-bottom:10px" value-key="id" v-model="resultVisualizationForm.dataResource[index1]" :placeholder="'输入信息 '+(index1+1)">
            <el-option v-for="item in dataItemList" :key="item.id" :label="item.name" :value="item" />
          </el-select>
          <el-button style="margin-left:5px" type="danger" icon="el-icon-delete" size="small" plain @click="handleCloseInput1(index1)"></el-button>
        </div>
        <el-button class="button-new-tag ml-1" size="small" @click="showInput1">
          + 添加输入信息
        </el-button>
    </el-form-item>

      <h5 class="mh5a"><el-icon><CaretBottom /></el-icon>输出数据:</h5>
    <ul class="mul">
      <li>
        <div class="mflexa">
          <div class="text"><div class="necess">*</div>名称</div><el-input class="liinput" v-model="resultVisualizationForm.resultOutput.name"  />
        </div> 
      </li>
      <li>
        <div class="mflexa">
          <div class="text"><div class="necess">*</div>描述</div><el-input class="liinput" v-model="resultVisualizationForm.resultOutput.description"/>
        </div>
      </li>
      <li>
        <div class="mflexa">
          <div class="text"><div class="necess">*</div>发布时间</div><el-input class="liinput" v-model="resultVisualizationForm.resultOutput.time" />
        </div>
      </li>
      <li>
        <div class="mflexa">
          <div class="text" style="width:135px"><div class="necess">*</div>数据存储位置</div>
            <el-input v-model="resultVisualizationForm.resultOutput.storage" placeholder="详细说明获取该数据的原始路径（下载链接）" :readonly="resultVisualizationForm.resultOutput.isUpload==='true'"/>
            <!-- <a style="margin-left:10px">或</a><el-button style="margin-left:10px" type="primary" @click="storageUpload2">上传</el-button> -->
        </div>
      </li>
      <li>
        <div class="mflexa">
          <div class="text"><div class="necess">*</div>数据格式</div><el-input class="liinput" v-model="resultVisualizationForm.resultOutput.format" />
        </div>
      </li>
    </ul>
    </el-form>
      <div class="small-title">
        <div class="icon"></div>
        <h4>3.3 结果分析</h4>
      </div>
        <div class="inputbody">
        <el-form class="input" :model="conclusionForm">
          <el-form-item  prop="conclusionText">
        <div style="display:flex"><div class="necess">*</div>结果分析及结论：</div>
            <el-input v-model="conclusionForm.text" :rows="3" type="textarea" placeholder="具体说明对该实验结果的分析以及得出的结论"/>
          </el-form-item>
          <el-form-item prop="conclusionPaper" > 
        相关文献：
            <el-input v-model="conclusionForm.paper" :rows="3" type="textarea" placeholder="与本实验相关的实验人员发表的论文"/>
          </el-form-item>
        </el-form>
      </div>
      <div style="display: flex;justify-content: center;align-items: center;">
        <el-button type="primary" @click="saveResult">保存</el-button>
      </div>
    </div>
</template>
  
  <script>
import { getResultByTaskId,getDataItemsByProjectId,saveResult,deleteResultValidation,saveResultValidation,updateResultValidation } from '@/api/request';
import { successNot, errorNotification } from '@/utils/notification';
  export default {
    components: {

    },
    data() {
        return {
            projectId: this.$route.params.id,
            taskInfoInit: null,
            dataItemList: [],
            conclusionForm:{
              text:"",
              paper:"",
            },
            resultVisualizationForm:{
              name:"",
              description:"",
              stepType:"",
              dataResource:[],
              resultOutput:{},
              softName:"",
              softVersion:"",
              softStorage:"",
              softDepend:"",
              codeContent:"",
              isCodeUpload:"",
              codeLanguage:"",
              codeDepend:"",
            },
            resultValidationList:[],
            addDialog:false,
            resultValidationItem:{},
            operateType:"add",
            resultValidationIndex:-1
        }
    },
    methods: {
      async saveResult(){
        await saveResult(
          this.projectId,
          {conclusion:this.conclusionForm, resultValidations:this.resultValidationList, resultVisualization:this.resultVisualizationForm}
        ).then(() =>{
          successNot('保存成功')
        }).catch((err) =>{
          console.log(err)
          errorNotification('保存失败')
        })
      },
      async getDataCollection() {
        let data = await getDataItemsByProjectId(this.projectId);
        this.dataItemList = data;
      },
      showInput1() {
        this.resultVisualizationForm.dataResource.push({});
      },
      handleCloseInput1(index) {
        this.resultVisualizationForm.dataResource.splice(index, 1);
      },
      showInput2() {
        this.resultValidationItem.dataResource.push({});
        console.log(this.resultValidationItem.dataResource)
      },
      handleCloseInput2(index) {
        this.resultValidationItem.dataResource.splice(index, 1);
      },
      updateClick(scope){
        this.resultValidationItem = this.resultValidationList[scope]
        this.operateType = "update"
        this.addDialog = true
        this.resultValidationIndex = scope
      },
      async deleteClick(scope){
        await deleteResultValidation(
          this.projectId,
          this.resultValidationList[scope]
        ).then(() =>{
          this.resultValidationList.splice(scope, 1)
          successNot('删除成功')
        }).catch((err) =>{
          console.log(err)
          errorNotification('删除失败')
        })
      },
      addResultValidationClick(){
        this.resultValidationItem = {
          name:'',
          description:'',
          stepType:'',
          softName:'',
          softVersion:'',
          softStorage:'',
          softDepend:'',
          codeContent:'',
          codeLanguage:'',
          codeDepend:'',
          pictures:[],
          video:{
            id:'',
            name:''
          },
          dataResource:[],
          resultOutput: {
            name: '',
            description: '',
            time: '',
            storage: '',
            format: '',
            isUpload:'false',
          },
        };
        this.operateType = "add"
        this.addDialog = true;
      },
      async submitForm(){
        await saveResultValidation(
          this.projectId,
          this.resultValidationItem
        ).then(() =>{
          this.resultValidationList.push(this.resultValidationItem)
          this.addDialog=false
          successNot('添加成功')
        }).catch((err) =>{
          console.log(err)
          errorNotification('添加失败')
        })
      },
      async submitUpdate(){
        await updateResultValidation(
          this.projectId,
          {data:this.resultValidationItem, index:this.resultValidationIndex}
        ).then(() =>{
          this.resultValidationList[this.resultValidationIndex]=this.resultValidationItem
          this.addDialog=false
          successNot('更新成功')
        }).catch((err) =>{
          console.log(err)
          errorNotification('更新失败')
        })
      }
    },
    async mounted() {
        await this.getDataCollection();
        this.taskInfoInit = await getResultByTaskId(this.projectId);
        console.log("taskInfo",this.taskInfoInit)
        this.conclusionForm.text=this.taskInfoInit.conclusion.text
        this.conclusionForm.paper=this.taskInfoInit.conclusion.paper

        this.resultVisualizationForm.name=this.taskInfoInit.resultVisualization.name
        this.resultVisualizationForm.description=this.taskInfoInit.resultVisualization.description
        this.resultVisualizationForm.stepType=this.taskInfoInit.resultVisualization.stepType
        this.resultVisualizationForm.dataResource=this.taskInfoInit.resultVisualization.dataResource
        this.resultVisualizationForm.resultOutput=this.taskInfoInit.resultVisualization.resultOutput
        this.resultVisualizationForm.softName=this.taskInfoInit.resultVisualization.softName
        this.resultVisualizationForm.softVersion=this.taskInfoInit.resultVisualization.softVersion
        this.resultVisualizationForm.softStorage=this.taskInfoInit.resultVisualization.softStorage
        this.resultVisualizationForm.softDepend=this.taskInfoInit.resultVisualization.softDepend
        this.resultVisualizationForm.codeContent=this.taskInfoInit.resultVisualization.codeContent
        this.resultVisualizationForm.isCodeUpload=this.taskInfoInit.resultVisualization.isCodeUpload
        this.resultVisualizationForm.codeLanguage=this.taskInfoInit.resultVisualization.codeLanguage
        this.resultVisualizationForm.codeDepend=this.taskInfoInit.resultVisualization.codeDepend

        this.resultValidationList = this.taskInfoInit.resultValidations
    }
  }
  </script>
  <style lang="scss" scoped>
  .mul{
    margin-left: 70px;
  }
  .mflexa{
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-top: 10px;
        .text{
          display: flex;
          font-size: small;
          font-weight: 350;
          width: 120px;
        }
  }
  .necess{
    margin-right: 5px;
    color:lightcoral;
  }
  .mh5a{
    margin-left: 20px;
    display: flex;
  }
  </style>
  