<template>
  <div>
    <el-card class="box-card" v-if="type != 'Model'">
      <img :src="imgPath(item.snapshot, item.name)" />
      <div class="content">
        <h3 class="title" :title="item.name">{{ item.name }}</h3>
        <p class="desc" :title="item.des">{{ item.description }}</p>
      </div>
      <el-button class="config-btn" type="text" @click="view(item.id)">View</el-button>
    </el-card>

    <el-card class="box-card" v-if="type == 'Model'" :style="select ? 'box-shadow: 0px 0px 20px #666;' : ''">
      <img :src="'http://geomodeling.njnu.edu.cn' + item.image" v-if="item.image != ''" />
      <img src="http://geomodeling.njnu.edu.cn/static/img/model/model.png" alt="" v-else />
      <div class="content">
        <h3 class="title" :title="item.name">{{ item.name }}</h3>
        <p class="desc" :title="item.des">{{ item.description }}</p>
      </div>
      <el-button class="config-btn" type="text" @click="modelView">View</el-button>
      <el-button type="text primary" @click="selectClick" :style="select ? 'float: right;color: red;' : 'float: right'">
        <i class="iconfont icon-gouwuche" v-if="select == false"></i>
        <el-badge :value="badgeNum" :max="10" class="item" v-else>
          <i class="iconfont icon-gouwuche"></i>
        </el-badge>
      </el-button>
    </el-card>
    <el-dialog :visible.sync="dialogVisible" width="30%">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(model, index) in computableModels" :key="index">
          <div
            :class="selectNum == index ? 'modelCard select' : 'modelCard'"
            @click="getDescription(index)"
            :style="selectNumArr[index] == 1 ? 'background-color: #D0FF00;' : ''"
          >
            {{ model.name }}
          </div>
        </el-col>
      </el-row>
      <div class="des">
        {{ description }}
      </div>
      <div class="btn">
        <el-button type="primary" plain size="mini" @click="confirm">confirm</el-button>
        <el-button type="primary" plain size="mini" @click="cancel">cancel</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { imgBase64 } from '@/lib/utils';
import { getComputableModels } from '@/api/request';
export default {
  props: ['item', 'type'],
  data() {
    return {
      select: false,
      computableModels: [],
      dialogVisible: false,
      description: '',
      selectNum: -1,
      selectNumArr: [],
      selectArr: [],
      badgeNum: 0
    };
  },
  watch: {
    item: {
      handler(val) {
        let temp = JSON.parse(localStorage.selectModels);
        for (let i = 0; i < temp.length; i++) {
          if (val.oid == temp[i].oid) {
            this.select = true;
            break;
          } else {
            this.select = false;
          }
        }
      }
    },
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
    init() {
      if (localStorage.selectModels != undefined) {
        let temp = JSON.parse(localStorage.selectModels);
        for (let i = 0; i < temp.modelItem.length; i++) {
          if (this.item.oid == temp.modelItem[i].id && temp.modelItem[i].count > 0) {
            this.select = true;
            this.badgeNum = temp.modelItem[i].count;
            break;
          }
        }
      }
    },
    imgPath(snapshot, name) {
      if (snapshot != undefined) {
        return snapshot;
      } else {
        return imgBase64(name);
      }
    },
    view(id) {
      this.$router.push({
        path: `/resource/${id}/${this.type}`
      });
    },
    modelView() {
      window.open('http://geomodeling.njnu.edu.cn/modelItem/' + this.item.oid, '_blank');
    },
    async selectClick() {
      await this.getComputableModels();
      console.log(this.computableModels);
      if (this.select) {
        let temp = JSON.parse(localStorage.selectModels);
        for (let i = 0; i < this.computableModels.length; i++) {
          this.selectNumArr[i] = -1;
        }
        for (let i = 0; i < temp.computableModels.length; i++) {
          for (let j = 0; j < this.computableModels.length; j++) {
            if (temp.computableModels[i].doi == this.computableModels[j].doi) {
              this.selectNumArr[j] = 1;
              break;
            }
          }
        }
        this.dialogVisible = true;
      } else {
        if (this.computableModels.length > 0) {
          for (let i = 0; i < this.computableModels.length; i++) {
            this.selectNumArr[i] = -1;
          }
          this.dialogVisible = true;
        } else {
          this.$notify({
            title: 'Warning',
            message: 'The model has no computing resources!',
            type: 'warning'
          });
        }
      }
    },
    async getComputableModels() {
      let data = await getComputableModels(this.item.oid);
      this.computableModels = data;
    },
    getDescription(index) {
      this.selectNum = index;
      if (this.selectNumArr[index] == 1) {
        this.selectNumArr.splice(index, 1, -1);
        this.description = '';
      } else {
        this.selectNumArr.splice(index, 1, 1);
        this.description = this.computableModels[index].description;
      }
    },
    confirm() {
      this.selectArr = [];
      this.selectNumArr.forEach((item, index) => {
        if (item == 1) {
          this.selectArr.push(this.computableModels[index]);
        }
      });
      let temp = '';
      if (localStorage.selectModels == undefined) {
        temp = {
          modelItem: [],
          computableModels: []
        };
      } else {
        temp = JSON.parse(localStorage.selectModels);
      }
      if (this.select) {
        for (let i = 0; i < temp.computableModels.length; i++) {
          for (let j = 0; j < this.computableModels.length; j++) {
            if (this.computableModels[j].doi == temp.computableModels[i].doi) {
              temp.computableModels.splice(i, 1);
              console.log(temp.computableModels.length);
              i--;
              break;
            }
          }
        }
      }
      if (this.selectArr.length == 0) {
        temp.modelItem.forEach((item, index) => {
          if (item.id == this.item.oid) {
            temp.modelItem.splice(index, 1);
          }
        });
      } else {
        for (let i = 0; i < this.selectArr.length; i++) {
          temp.computableModels.push({
            name: this.selectArr[i].name,
            doi: this.selectArr[i].doi,
            image: this.item.image,
            md5: this.selectArr[i].md5
          });
        }
        if (this.badgeNum == 0) {
          temp.modelItem.push({
            id: this.item.oid,
            count: this.selectArr.length
          });
        } else {
          temp.modelItem.forEach(item => {
            if (item.id == this.item.oid) {
              item.count = this.selectArr.length;
            }
          });
        }
      }
      localStorage.setItem('selectModels', JSON.stringify(temp));
      this.$store.state.user.selectNum = this.$store.state.user.selectNum + this.selectArr.length - this.badgeNum;
      if (this.selectArr.length > 0) {
        this.select = true;
        this.badgeNum = this.selectArr.length;
      } else {
        this.select = false;
        this.badgeNum = 0;
      }

      this.dialogVisible = false;
    },
    cancel() {
      this.dialogVisible = false;
    }
  },
  mounted() {
    this.init();
    // console.log(this.item)
    // console.log(this.item.snapshot)
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
      max-height: 44px;
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
.modelCard:hover {
  cursor: pointer;
}
.des {
  margin-top: 20px;
}
.select {
  border: solid 1px red;
}
.btn {
  text-align: center;
  margin-top: 15px;
}
</style>
