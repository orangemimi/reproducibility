<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App",
  props: [
    //继承父组件数据
  ],
  data() {
    return {
      //该组件的数据
    };
  },
  watch: {
    //观察数据的变化
  },
  computed: {
    //属性的一个实时计算，当属性发生改变，会进行计算
  },
  methods: {
    //组件的方法
  },
  filter: {
    //过滤器
  },
  beforeRouteUpdate() {
    //在当前路由改变和该组件被复用时调用
  },
  created() {
    //调用该函数时，data和method已经构建完成
    //在Vue1.0中起到初始化数据的作用
    //在Vue2.0之后推荐使用computed进行数据初始化
    console.log(
      "%cOpenGMS",
      " text-shadow: 0 1px 0 #ccc,0 2px 0 #c9c9c9,0 3px 0 #bbb,0 4px 0 #b9b9b9,0 5px 0 #aaa,0 6px 1px rgba(0,0,0,.1),0 0 5px rgba(0,0,0,.1),0 1px 3px rgba(0,0,0,.3),0 3px 5px rgba(0,0,0,.2),0 5px 10px rgba(0,0,0,.25),0 10px 10px rgba(0,0,0,.2),0 20px 20px rgba(0,0,0,.15);font-size:5em"
    );
  },
  mounted() {
    //调用该函数时，el已经构建完成
    //在这里才能获取到dom元素，因此在一些插件的使用或者组件的使用中进行操作
    //在这里发起后端请求，拿回数据
  },
  components: {}
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
html,
body {
  width: 100%;
  height: 100%;
  font-size: 100%;
}

.el-tabs__item {
  max-width: 200px;
  text-overflow: ellipsis;
  overflow: hidden;
}
.el-input-group__append {
  background: #ffffff;
}
.scrollbar::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  background-color: #f5f5f5;
  border-radius: 4px;
}

.scrollbar::-webkit-scrollbar {
  width: 6px;
  height: 5px !important;
  background-color: #f5f5f5;
}

.scrollbar::-webkit-scrollbar-thumb {
  background-color: #666666;
  border-radius: 4px;
}

.noselect {
  -webkit-touch-callout: none;
  /* iOS Safari */
  -webkit-user-select: none;
  /* Chrome/Safari/Opera */
  -khtml-user-select: none;
  /* Konqueror */
  -moz-user-select: none;
  /* Firefox */
  -ms-user-select: none;
  /* Internet Explorer/Edge */
  user-select: none;
  /* Non-prefixed version, currently
    not supported by any browser */
}
body {
  background-image: radial-gradient(
      farthest-corner at 120px 420px,
      #fff,
      rgba(255, 255, 255, 0.5)
    ),
    url("./assets/topo.svg");
  background-repeat: no-repeat;
  background-size: 100%, 1500px;
  width: 100%;
}
</style>
