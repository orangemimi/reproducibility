<template>
    <div >
      <el-card>
      <h3>{{ stepInfo.name }}</h3>
      <div class="process-item">
        <strong>步骤类型：</strong>
        <el-tag  size="large">{{ stepInfo.stepType }}</el-tag>
      </div>
      <div class="process-item">
        <strong>操作类型：</strong>
        <el-tag size="large" >{{ stepInfo.operateType }}</el-tag>
      </div>
      <strong>模型资源：</strong>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <strong>{{ stepInfo.modelResource.name }}</strong>
        </div>
        <strong>模型描述：</strong>
        <div class="text" :title="stepInfo.modelResource.description">
          {{ stepInfo.modelResource.description }}
        </div>
        <strong>格式：</strong>
        <div class="text" :title="stepInfo.modelResource.format">
          {{ stepInfo.modelResource.format }}
        </div>
        <strong>类型：</strong>
        <div class="text" :title="stepInfo.modelResource.type">
          {{ stepInfo.modelResource.type }}
        </div>
      </el-card>  
      <strong>输入数据：</strong>
      <div  class="mcard">
        <div v-for="(item,index1) in stepInfo.modelInputs" :key="index1">
          <el-card class="box-card" >
            <div slot="header" class="clearfix">
              <strong>{{ item.name }}</strong>
            </div>
            <strong>模型描述：</strong>
            <div class="text" :title="item.description">
              {{ item.description }}
            </div>
            <strong>格式：</strong>
            <div class="text" :title="item.format">
              {{ item.format }}
            </div>
            <strong>类型：</strong>
            <div class="text" :title="item.type">
              {{ item.type }}
            </div>
          </el-card>  
        </div>
      </div>
      <strong>输出数据：</strong>
      <div  class="mcard">
        <div v-for="(item,index2) in stepInfo.modelOutputs" :key="index2">
          <el-card class="box-card" >
            <div slot="header" class="clearfix">
              <strong>{{ item.name }}</strong>
            </div>
            <strong>模型描述：</strong>
            <div class="text" :title="item.description">
              {{ item.description }}
            </div>
            <strong>格式：</strong>
            <div class="text" :title="item.format">
              {{ item.format }}
            </div>
            <strong>类型：</strong>
            <div class="text" :title="item.type">
              {{ item.type }}
            </div>
          </el-card>  
        </div>
      </div>
      <strong>参数数据：</strong>
      <div  class="mcard">
        <div v-for="(item,index3) in stepInfo.parameters" :key="index3">
          <el-card class="box-card" >
            <div slot="header" class="clearfix">
              <strong>{{ item.name }}</strong>
            </div>
            <strong>模型描述：</strong>
            <div class="text" :title="item.description">
              {{ item.description }}
            </div>
            <strong>格式：</strong>
            <div class="text" :title="item.format">
              {{ item.format }}
            </div>
            <strong>类型：</strong>
            <div class="text" :title="item.type">
              {{ item.type }}
            </div>
          </el-card>  
        </div>
      </div>
      <div class="item">
        <strong>步骤描述：</strong>
        <div class="text">
          {{ stepInfo.description }}
        </div>
      </div>
      <div class="tag-item" v-if="stepInfo.references.length > 0" >
      <strong >参考文献：</strong> 
        <el-tag
          v-for="tag in stepInfo.references"
          :key="tag"
        >
          {{tag.length>33?tag.substring(0,22)+'......'+tag.substring(tag.length-10,tag.length):tag}}
        </el-tag>
      </div>
      <div class="item" v-if="stepInfo.other != ''">
        <strong>其他补充：</strong>
        <div class="text">{{ stepInfo.other }}</div>
      </div>
      <!-- <div class="item" v-if="stepInfo.pictures.length > 0">
        <strong>图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;片：</strong>
        <div class="text">
          <PicturesDownload :fileList="stepInfo.pictures" :fileName="'实验步骤'+stepInfo.name"></PicturesDownload>
        </div>
      </div> -->
      <!-- <div style="display: flex;align-items:flex-end;">
        <div v-if="processVideoItem && processVideoItem.id!=''">已上传文件：<el-tag>{{processVideoItem.name}}</el-tag></div>
        <el-button v-if="processVideoItem && processVideoItem.id!=''" @click="downloadVideo(processVideoItem)" style="margin-left: 10px;" type="success" size="small" plain>下载</el-button>
        <el-button v-if="processVideoItem && processVideoItem.id!=''" @click="deleteVideo" style="margin-left: 10px;" type="danger" size="small" plain>删除</el-button>
      </div>
      <slot name="button"></slot> -->
    </el-card>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      stepInfo: {
        type: Object,
      },
      stepInfoId: {
        type: Number,
      }
    },
    data() {
      return {
  
      };
    },
  
    methods: {
     
    },
  
  };
  </script>
  <style lang="scss" scoped>
.box-card {
    width: 300px;
    margin-right: 10px;
  }
  .mcard{
    display: flex;
    flex-direction:row;
    
  }
  </style>
  