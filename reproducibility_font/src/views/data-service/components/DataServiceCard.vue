<template>
  <div>
    <el-card class="box-card" :style="select ? 'box-shadow: 0px 0px 20px #666;' : ''">
      <img :src="imgPath(item.snapshot, item.name)" />
      <div class="content">
        <h3 class="title" :title="item.name">{{ item.name }}</h3>
        <p class="desc" :title="item.des">{{ item.description }}</p>
      </div>
      <el-button class="config-btn" type="text" @click="view(item.oid)">View</el-button>
      <el-button type="text primary" @click="selectClick" :style="select ? 'float: right;color: red;' : 'float: right'">
        <i class="iconfont icon-gouwuche" v-if="select == false"></i>
        <el-badge :value="badgeNum" :max="10" class="item" v-else>
          <i class="iconfont icon-gouwuche"></i>
        </el-badge>
      </el-button>
    </el-card>

    <el-dialog :visible.sync="dialogVisible" width="30%">
      <div v-if="item.invokeServices != undefined">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(service, index) in item.invokeServices" :key="index">
            <div
              :class="selectNum == index ? 'modelCard select' : 'modelCard'"
              @click="getDescription(index)"
              :style="selectNumArr[index] == 1 ? 'background-color: #D0FF00;' : ''"
            >
              {{ service.name }}
            </div>
          </el-col>
        </el-row>
        <div class="btn">
          <el-button type="primary" plain size="mini" @click="confirm">confirm</el-button>
          <el-button type="primary" plain size="mini" @click="cancel">cancel</el-button>
        </div>
      </div>
      <div v-else>
        <el-empty description="There is no computing service. Click here to download the service to run locally!"></el-empty>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { imgBase64 } from '@/lib/utils';
export default {
  props: ['item'],
  data() {
    return {
      select: false,
      badgeNum: 0,
      dialogVisible: false,
      selectNum: -1,
      selectNumArr: [],
      description: ''
    };
  },

  watch: {
    '$store.state.user.selectNum': {
      handler(val) {
        if (val == 0) {
          this.select = false;
          this.badgeNum = 0;
        }
      }
    }
  },

  methods: {
    imgPath(snapshot, name) {
      if (snapshot != undefined) {
        return snapshot;
      } else {
        return imgBase64(name);
      }
    },

    init() {
      if (localStorage.selectDataServices != undefined) {
        let temp = JSON.parse(localStorage.selectDataServices).dataServiceItem;
        if (temp[this.item.oid] == undefined || temp[this.item.oid].length == 0) {
          this.select = false;
          this.badgeNum = 0;
        } else {
          this.select = true;
          this.badgeNum = temp[this.item.oid].length;
        }
      }
    },

    view(oid) {
      window.open('http://geomodeling.njnu.edu.cn/dataApplication/' + oid, '_blank');
    },

    selectClick() {
      if (localStorage.selectDataServices != undefined) {
        if (this.item.invokeServices != undefined) {
          let temp = JSON.parse(localStorage.selectDataServices).dataServiceItem;
          this.item.invokeServices.forEach((item, index) => {
            this.selectNumArr[index] = -1;
          });
          if (temp[this.item.oid] != undefined && temp[this.item.oid].length != 0) {
            this.item.invokeServices.forEach((item, index) => {
              for (let i = 0; i < temp[this.item.oid].length; i++) {
                if (item.serviceId == temp[this.item.oid][i]) {
                  this.selectNumArr[index] = 1;
                  break;
                }
              }
            });
          }
        }
      }

      this.dialogVisible = true;
    },
    getDescription(index) {
      this.selectNum = index;
      if (this.selectNumArr[index] == 1) {
        this.selectNumArr.splice(index, 1, -1);
      } else {
        this.selectNumArr.splice(index, 1, 1);
      }
    },

    cancel() {
      this.dialogVisible = false;
    },

    confirm() {
      if (localStorage.selectDataServices != undefined) {
        let temp = JSON.parse(localStorage.selectDataServices).dataServices;
        let dataServiceItem = JSON.parse(localStorage.selectDataServices).dataServiceItem;
        if (this.badgeNum != 0) {
          for (let i = 0; i < this.item.invokeServices.length; i++) {
            for (let j = 0; j < temp.length; j++) {
              if (this.item.invokeServices[i].serviceId == temp[j].serviceId) {
                temp.splice(j, 1);
              }
            }
          }
        }
        let tempArr = [];
        this.selectNumArr.forEach((item, index) => {
          if (item == 1) {
            temp.push({
              serviceId: this.item.invokeServices[index].serviceId,
              token: this.item.invokeServices[index].token,
              name: this.item.invokeServices[index].name,
              oid: this.item.oid
            });
            tempArr.push(this.item.invokeServices[index].serviceId);
          }
        });
        if (dataServiceItem[this.item.oid] != undefined && tempArr.length == 0) {
          delete dataServiceItem[this.item.oid];
        } else {
          dataServiceItem[this.item.oid] = tempArr;
        }
        localStorage.selectDataServices = JSON.stringify({
          dataServiceItem: dataServiceItem,
          dataServices: temp
        });

        this.$store.state.user.selectNum = this.$store.state.user.selectNum + tempArr.length - this.badgeNum;
        this.badgeNum = tempArr.length;
      }
      if (this.badgeNum != 0) {
        this.select = true;
        console.log(1);
      } else {
        this.select = false;
      }
      this.dialogVisible = false;
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style lang="scss" scoped>
.box-card {
  margin: 10px 2%;
  width: 300px;
  min-height: 275px;
  transition: box-shadow 0.1s ease;
  .el-card__body {
    padding: 0;
  }
  .content {
    padding: 10px 18px;
    word-wrap: break-word;
    min-height: 115px;
    max-height: 115px;
    .title,
    .desc {
      max-height: 50px;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }
  }

  // &:hover {
  //   box-shadow: 0px 0px 20px #666;
  // }
  img {
    width: 100%;
    object-fit: cover;
    height: 200px;
  }
  .config-btn {
    float: right;
    margin: 0px 20px;
  }
}

.modelCard {
  width: 100%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  border: solid 1px;
  // max-height: 50px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  overflow: hidden;
  font-size: 10px;
  padding: 0px 5px;
}
.select {
  border: solid 1px red;
}
.btn {
  text-align: center;
  margin-top: 15px;
}
</style>
