<!-- card -->
<template>
  <div class>
    <el-card class="cardBody">
      <div class="card_avatar">
        <el-tooltip placement="bottom" max-width="600">
          <avatar
            :username="item.name"
            :size="38"
            style="margin-bottom: 6px"
            :rounded="false"
            v-if="item.thumbnail == ''"
          ></avatar>
          <div class="thumbnailOuter" v-else>
            <img :src="item.thumbnail" class="thumbnail" />
          </div>
          <div slot="content">
            <div style="text-align: center">{{ item.description }}</div>
            <!-- <div v-if="item.tags.length > 0">
                <span>
                  <i>{{ item.tags.join('|') }}</i>
                </span>
              </div> -->
          </div>
        </el-tooltip>
      </div>
      <div class="card_info">
        <h4 :title="item.name" class="card_info_name">
          {{ item.name }}
        </h4>
      </div>
    </el-card>
    <!-- 
    <div v-show="openTool" title="Tool preview" width="800" class="mask">
      <i class="el-icon-close maskBtn" title="Close the tool information"></i>

      <tool-preview :selectTool="item"></tool-preview>
    </div> -->
  </div>
</template>

<script>
import Avatar from 'vue-avatar';
// import toolPreview from '@/components/common/tools/toolPreview';
export default {
  props: {
    modelFrom: {
      type: Object
    }
  },

  watch: {
    modelFrom: {
      handler(val) {
        debugger;
        console.log(val);
        this.item = val;
      }
    }
  },

  components: {
    Avatar
    //   toolPreview
  },
  data() {
    return {
      item: this.modelFrom,
      isOpen: this.isOpenTool,
      openTool: false
      // toolPreview
    };
  },

  methods: {}
};
</script>
<style lang="scss" scoped>
.cardBody {
  /deep/ .el-card__body {
    padding: 5px;
  }

  background-color: ghostwhite;
  cursor: pointer;
  height: 50px;
  // width: 180px;

  .thumbnailOuter {
    height: 38px;
    width: 38px;
    .thumbnail {
      width: auto;
      height: auto;
      max-width: 100%;
      max-height: 100%;
    }
  }
  .card_avatar {
    width: 30px;
    margin-right: 10px;
    float: left;
  }
  .card_info {
    width: 150px;
    .card_info_name {
      display: block;
      width: 90px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
}

/* 遮盖罩 */
.mask {
  position: fixed;
  /* 最顶层 */
  z-index: 1;
  left: 0px;
  top: 0px;
  right: 0px;
  bottom: 0px;
  background-color: rgba(247, 241, 241, 0.938);
  padding: 3% 10% 0 10%;
  overflow-y: scroll;
  /* overflow-x:hidden; */

  .maskBtn {
    position: absolute;
    z-index: 2;
    right: 10%;
    font-size: 30px;
  }

  .maskBtn:hover {
    cursor: pointer;
  }
}
</style>
