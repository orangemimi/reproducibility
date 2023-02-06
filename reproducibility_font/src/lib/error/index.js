import ElementUI from 'element-ui';
import { ResError } from './ResError';

//所有的异常，都会在这里进行捕获
//error分为前台错误与后台错误
//前台错误，直接打印
//后台异常，通过Notification输出。
export const errorHandler = error => {
  ElementUI.Notification.close();
  if (!(error instanceof ResError)) {
    //打印前台错误
    console.error(error);
  } else {
    //抛出自定义的错误
    ElementUI.Notification.error(error.message);
  }
};
