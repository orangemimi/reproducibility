import mxgraph from './index';
import { get } from '@/axios';

const { mxConstants } = mxgraph;

const outputIcon = './images/1.jpg';
const inputIcon = './images/2.jpg';

export const toolbarItems = [
  {
    icon: outputIcon,
    title: 'Data node',
    width: 150,
    height: 100,

    style: {
      fillColor: '#f8f5ec',
      strokeColor: 'rgb(200, 200, 200)',
      strokeWidth: '1',
      shape: 'ellipse',
      align: mxConstants.ALIGN_CENTER,
      // verticalAlign: mxConstants.ALIGN_,
      imageAlign: mxConstants.ALIGN_CENTER,
      imageVerticalAlign: mxConstants.ALIGN_TOP
      // image: outputIcon,
    }
  },
  {
    icon: inputIcon,
    title: 'Model node',
    width: 150,
    height: 100,
    style: {
      fillColor: 'transparent', // 填充色
      strokeColor: '#000000', // 线条颜色
      strokeWidth: '1', // 线条粗细
      shape: 'rectangle', // 形状
      align: mxConstants.ALIGN_CENTER, // 水平方向对齐方式
      // verticalAlign: mxConstants.ALIGN_CENTER, // 垂直方向对齐方式
      imageAlign: mxConstants.ALIGN_CENTER, // 图形水平方向对齐方式
      imageVerticalAlign: mxConstants.ALIGN_TOP // 图形垂直方向对齐方式
      // image: inputIcon, // 图形
      // rounded:true,// 圆角
      // arcSize:'6'// 设置圆角程度
    }
  }
];
