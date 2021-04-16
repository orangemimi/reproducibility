import mxgraph from '_com/MxGraph/index';

const { mxConstants } = mxgraph;
const rhombus = require('./icon/lingxing.png');

export const generalToolbar = [
  {
    icon: rhombus,
    name: 'Condition',
    width: 200,
    height: 50,
    style: {
      fillColor: 'transparent', // 填充色
      strokeColor: '#000000', // 线条颜色
      strokeWidth: '1', // 线条粗细
      shape: 'rhombus', // 形状
      align: mxConstants.ALIGN_CENTER, // 水平方向对其方式
      verticalAlign: mxConstants.ALIGN_BOTTOM, // 垂直方向对其方式
      imageAlign: mxConstants.ALIGN_CENTER, // 图形水平方向对其方式
      imageVerticalAlign: mxConstants.ALIGN_TOP, // 图形方向对其方式
      image: rhombus
    }
  }
];
