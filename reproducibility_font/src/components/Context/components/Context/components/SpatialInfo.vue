<template>
  <div class="spatialInfo" ref="spatialInfo">
    <p style="margin-bottom: 10px">SpatialInfo</p>
    <el-select v-model="value1" placeholder="请选择" filterable @change="change1" size="mini" :disabled="visible ? true : false">
      <el-option v-for="(item, index) in options1" :key="index" :label="item.name" :value="index"></el-option>
    </el-select>
    <el-select v-model="value2" placeholder="请选择" v-if="flag2" @change="change2" size="mini" :disabled="visible ? true : false">
      <el-option v-for="(item, index) in options2" :key="index" :label="item.name" :value="index"></el-option>
    </el-select>
    <el-select v-model="value3" placeholder="请选择" v-if="flag3" @change="change3" size="mini" :disabled="visible ? true : false">
      <el-option v-for="(item, index) in options3" :key="index" :label="item.name" :value="index"></el-option>
    </el-select>
    <el-dropdown trigger="click" @command="handleCommand">
      <span class="el-dropdown-link">
        <el-button icon="el-icon-edit" circle size="mini" :type="editFlag ? 'warning' : 'info'" :disabled="visible ? true : false"></el-button>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="Region">Region</el-dropdown-item>
        <el-dropdown-item command="Manual">Manual</el-dropdown-item>
        <el-dropdown-item command="Stop" v-if="editFlag">Stop editing</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <span v-if="editFlag" style="margin-left: 5px">{{ editMethod }}</span>

    <el-popover placement="bottom" width="200" v-model="visible">
      <el-input size="mini" placeholder="Name" v-model="input"></el-input>
      <div style="text-align: right; margin-top: 5px">
        <el-button size="mini" type="text" @click="visible = false">cancel</el-button>
        <el-button type="primary" size="mini" @click="confirm">confirm</el-button>
      </div>
    </el-popover>
    <el-button icon="el-icon-finished" circle size="mini" style="margin-left: 5px" @click="handerClick" :disabled="visible ? true : false"></el-button>
    <el-dropdown trigger="click" @command="delCommand">
      <span class="el-dropdown-link">
        <el-button icon="el-icon-delete" circle size="mini" style="margin-left: 5px" :disabled="visible ? true : false"></el-button>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-for="(item, index) in selectData" :key="index" :command="{ data: item, index: index }">
          <i class="el-icon-delete"></i>
          {{ item.name }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <!-- <el-button icon="el-icon-close" circle size="mini" style="float: right" @click="close"></el-button> -->

    <baidu-map class="map" :center="center" :scroll-wheel-zoom="true" @click="selectPoint" @ready="handleMap">
      <bm-control anchor="BMAP_ANCHOR_TOP_RIGHT">
        <el-dropdown style="margin-top: 10px; margin-right: 5px" trigger="click" @command="selectCommand">
          <span class="el-dropdown-link">
            <el-button type="primary" size="mini" :disabled="visible ? true : false">
              Selected Data
              <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-for="(item, index) in selectData" :key="index" :command="item">
              {{ item.name }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </bm-control>

      <bm-map-type :map-types="['BMAP_NORMAL_MAP', 'BMAP_HYBRID_MAP']" anchor="BMAP_ANCHOR_TOP_LEFT"></bm-map-type>
      <bm-scale anchor="BMAP_ANCHOR_BOTTOM_LEFT"></bm-scale>
      <bm-boundary :name="boundaryName" :strokeWeight="1" strokeColor="blue" />
      <bm-boundary :name="item" :strokeWeight="2" strokeColor="black" v-for="(item, index) in selectBoundary" :key="'index1' + index" />
      <bm-polygon :path="polygonPath" stroke-color="blue" :stroke-weight="1" :editing="visible ? false : true" @lineupdate="updatePolygonPath" />
      <bm-polygon :path="path" v-for="(path, index) in paths" :key="'index2' + index" stroke-color="black" :stroke-weight="2" />
    </baidu-map>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      map: '',
      BMap: '',
      options1: [],
      options2: [],
      options3: [],
      value1: '',
      value2: '',
      value3: '',
      flag2: false,
      flag3: false,
      center: '',
      boundaryName: '',
      editFlag: false,
      editMethod: '',
      selectBoundary: [],
      polygonPath: [],
      selectData: [],
      paths: [],
      visible: false,
      input: '',
      type: ''
    };
  },

  props: {
    spatialInfo: {
      type: Array
    }
  },

  watch: {
    spatialInfo: {
      handler(val) {
        this.selectData = JSON.parse(JSON.stringify(val));
      },
      deep: true
    }
  },

  methods: {
    handleMap({ BMap, map }) {
      this.BMap = BMap;
      this.map = map;
      if (this.selectData.length > 0) {
        if (this.selectData[0].type == 'polygon') {
          this.map.centerAndZoom(new this.BMap.Point(this.selectData[0].points[0].lng, this.selectData[0].points[0].lat), 11);
        } else if (this.selectData[0].type == 'boundary') {
          this.map.centerAndZoom(this.selectData[0].value);
        }
      }
    },
    async getCityList() {
      await axios.get('/json/Region.json').then(res => {
        this.options1 = res.data.districts[0].districts;
      });
    },
    change1(val) {
      if (
        this.options1[val].name == '北京市' ||
        this.options1[val].name == '天津市' ||
        this.options1[val].name == '上海市' ||
        this.options1[val].name == '重庆市'
      ) {
        this.options2 = this.options1[val].districts[0].districts;
      } else {
        this.options2 = this.options1[val].districts;
      }
      this.flag3 = false;
      this.value2 = '';
      if (this.options2.length > 0) {
        this.flag2 = true;
      } else {
        this.flag2 = false;
      }
      this.center = this.options1[val].name;
      if (this.editFlag && this.editMethod == 'Region') {
        this.boundaryName = this.options1[val].name;
      }
    },
    change2(val) {
      this.options3 = this.options2[val].districts;
      this.value3 = '';
      if (this.options3.length > 0) {
        this.flag3 = true;
      } else {
        this.flag3 = false;
      }
      this.center = this.options1[this.value1].name + this.options2[val].name;
      if (this.editFlag && this.editMethod == 'Region') {
        this.boundaryName = this.options1[this.value1].name + this.options2[val].name;
      }
    },
    change3(val) {
      this.center = this.options1[this.value1].name + this.options2[this.value2].name + this.options3[val].name;
      if (this.editFlag && this.editMethod == 'Region') {
        this.boundaryName = this.options1[this.value1].name + this.options2[this.value2].name + this.options3[val].name;
      }
    },
    handleCommand(val) {
      if (val == 'Region' || val == 'Manual') {
        this.editFlag = true;
        if (val == 'Region' && this.editMethod == 'Manual') {
          this.polygonPath = [];
          this.boundaryName = '';
        }
        if (val == 'Manual' && this.editMethod == 'Region') {
          this.boundaryName = '';
          this.polygonPath = [];
        }
        this.editMethod = val;
      }
      if (val == 'Stop') {
        this.editFlag = false;
        this.boundaryName = '';
        this.polygonPath = [];
      }
    },
    selectCommand(val) {
      if (val.type == 'boundary') {
        this.map.centerAndZoom(val.value);
      } else if (val.type == 'polygon') {
        this.map.panTo(new this.BMap.Point(val.points[0].lng, val.points[0].lat));
      }
    },
    delCommand(val) {
      console.log(val);
      if (val.data.type == 'boundary') {
        for (let i = 0; i < this.selectBoundary.length; i++) {
          if (JSON.stringify(this.selectBoundary[i]) == JSON.stringify(val.data.value)) {
            this.selectBoundary.splice(i, 1);
            break;
          }
        }
      } else if (val.data.type == 'polygon') {
        for (let i = 0; i < this.paths.length; i++) {
          if (JSON.stringify(this.paths[i]) == JSON.stringify(val.data.points)) {
            this.paths.splice(i, 1);
            break;
          }
        }
      }
      this.selectData.splice(val.index, 1);
      this.$emit('selectData', this.selectData);
    },
    handerClick() {
      if (this.editMethod == 'Region') {
        if (this.boundaryName == '') {
          this.$notify({
            type: 'error',
            message: 'Please select data!'
          });
          this.visible = false;
        } else {
          this.input = this.boundaryName;
          this.type = 'boundary';
          this.visible = true;
        }
      }
      if (this.editMethod == 'Manual') {
        if (this.polygonPath.length > 2) {
          this.input = 'polygon';
          this.visible = true;
          this.type = 'polygon';
        } else {
          this.$notify({
            type: 'error',
            message: 'Polygons need to select at least three points!'
          });
          this.visible = false;
        }
      }
    },
    selectPoint(e) {
      if (this.editFlag && this.editMethod == 'Manual') {
        this.polygonPath.push({
          lng: e.Bg.lng,
          lat: e.Bg.lat
        });
      }
    },
    updatePolygonPath(e) {
      this.polygonPath = e.target.getPath();
    },
    confirm() {
      if (this.type == 'boundary') {
        this.selectBoundary.push(this.boundaryName);
        this.selectData.push({
          name: this.input,
          type: this.type,
          value: this.boundaryName
        });
        this.boundaryName = '';
      } else if (this.type == 'polygon') {
        this.paths.push(this.polygonPath);
        this.selectData.push({
          name: this.input,
          type: this.type,
          points: this.polygonPath
        });
        this.polygonPath = [];
      }
      this.$emit('selectData', this.selectData);
      this.visible = false;
    },
    // close() {
    //   this.$emit('close');
    // },
    init() {
      this.selectData = JSON.parse(JSON.stringify(this.spatialInfo));
      console.log(this.selectData);
      this.selectData.forEach(item => {
        if (item.type == 'polygon') {
          this.paths.push(item.points);
        } else if (item.type == 'boundary') {
          this.selectBoundary.push(item.value);
        }
      });
    }
  },
  mounted() {
    this.getCityList();
    this.init();
  }
};
</script>

<style lang="scss" scoped>
.spatialInfo {
  color: #606266;
  .map {
    width: 100%;
    height: 400px;
    margin-top: 10px;
  }
  .el-select {
    width: 20%;
    margin-right: 7px;
  }
}
</style>
