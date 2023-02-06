export function initSetTimeOut() {
  return new Promise(function(reslove) {
    // 模拟异步，加载组件
    setTimeout(function() {
      console.log('等了1秒钟');
      reslove(1000);
    }, 2000);
  });
}

export const dateFormat = (date, format) => {
  let dateObj = new Date(Date.parse(date));
  let fmt = format || 'yyyy-MM-dd hh:mm:ss';
  //author: meizz
  var o = {
    'M+': dateObj.getMonth() + 1, //月份
    'd+': dateObj.getDate(), //日
    'h+': dateObj.getHours(), //小时
    'm+': dateObj.getMinutes(), //分
    's+': dateObj.getSeconds(), //秒
    'q+': Math.floor((dateObj.getMonth() + 3) / 3), //季度
    S: dateObj.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (dateObj.getFullYear() + '').substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length));
  return fmt;
};

export const getParams = url => {
  const keyValueArr = url.split('?')[1].split('&');
  let paramObj = {};
  keyValueArr.forEach(item => {
    const keyValue = item.split('=');
    paramObj[keyValue[0]] = keyValue[1];
  });
  return paramObj;
};

export const getTime = () => {
  let date = new Date();
  var Y = date.getFullYear();
  var M = date.getMonth() + 1;
  M = M < 10 ? '0' + M : M; // 不够两位补充0
  var D = date.getDate();
  D = D < 10 ? '0' + D : D;
  var H = date.getHours();
  H = H < 10 ? '0' + H : H;
  var Mi = date.getMinutes();
  Mi = Mi < 10 ? '0' + Mi : Mi;
  var S = date.getSeconds();
  S = S < 10 ? '0' + S : S;
  return Y + '-' + M + '-' + D + ' ' + H + ':' + Mi + ':' + S;
};

export const getUuid = () => {
  var s = [];
  var hexDigits = '0123456789abcdef';
  for (var i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = '4'; // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = '-';

  var uuid = s.join('');
  return uuid;
};
// export const getCurrentTime=()=>{
//   return Date()
// }

export const objToStrMap = obj => {
  let strMap = new Map();

  for (let k of Object.keys(obj)) {
    strMap.set(k, obj[k]);
  }

  return strMap;
};

export const getSuffix = filename => {
  let index = filename.lastIndexOf('.');
  return filename.substr(index + 1);
};

export const renderSize = value => {
  if (null == value || value == '') {
    return '0 Bytes';
  }
  var unitArr = new Array('Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB');
  var index = 0,
    srcsize = parseFloat(value);
  index = Math.floor(Math.log(srcsize) / Math.log(1024));
  var size = srcsize / Math.pow(1024, index);
  //  保留的小数位数
  size = size.toFixed(2);
  return size + unitArr[index];
};

/**
 * @param {Array} arr1
 * @param {Array} arr2
 * @description 得到两个数组的交集, 两个数组的元素为数值或字符串
 */
export const getIntersection = (arr1, arr2) => {
  let len = Math.min(arr1.length, arr2.length);
  let i = -1;
  let res = [];
  while (++i < len) {
    const item = arr2[i];
    if (arr1.indexOf(item) > -1) res.push(item);
  }
  return res;
};

/**
 * @param {Array} arr1
 * @param {Array} arr2
 * @description 得到两个数组的并集, 两个数组的元素为数值或字符串
 */
export const getUnion = (arr1, arr2) => {
  return Array.from(new Set([...arr1, ...arr2]));
};

/**
 * @param {Array} target 目标数组
 * @param {Array} arr 需要查询的数组
 * @description 判断要查询的数组是否至少有一个元素包含在目标数组中
 */
export const hasOneOf = (targetarr, arr) => {
  return targetarr.some(_ => arr.indexOf(_) > -1);
};

/**
 * @param {String|Number} value 要验证的字符串或数值
 * @param {*} validList 用来验证的列表
 */
export function oneOf(value, validList) {
  for (let i = 0; i < validList.length; i++) {
    if (value === validList[i]) {
      return true;
    }
  }
  return false;
}

/**
 * @description 绑定事件 on(element, event, handler)
 */
export const on = (function() {
  if (document.addEventListener) {
    return function(element, event, handler) {
      if (element && event && handler) {
        element.addEventListener(event, handler, false);
      }
    };
  } else {
    return function(element, event, handler) {
      if (element && event && handler) {
        element.attachEvent('on' + event, handler);
      }
    };
  }
})();

/**
 * @description 解绑事件 off(element, event, handler)
 */
export const off = (() => {
  if (document.removeEventListener) {
    return function(element, event, handler) {
      if (element && event) {
        element.removeEventListener(event, handler, false);
      }
    };
  } else {
    return function(element, event, handler) {
      if (element && event) {
        element.detachEvent('on' + event, handler);
      }
    };
  }
})();

/**
 * 判断一个对象是否存在key，如果传入第二个参数key，则是判断这个obj对象是否存在key这个属性
 * 如果没有传入key这个参数，则判断obj对象是否有键值对
 */
export const hasKey = (obj, key) => {
  if (key) return key in obj;
  else {
    let keysArr = Object.keys(obj);
    return keysArr.length;
  }
};

export function hasProperty(obj, prop) {
  if (Object.prototype.hasOwnProperty.call(obj, prop) && obj[prop] != null && obj[prop] != undefined && obj[prop] != '') {
    return true;
  }
  return false;
}

/**
 * @param {*} obj1 对象
 * @param {*} obj2 对象
 * @description 判断两个对象是否相等，这两个对象的值只能是数字或字符串
 */
export const objEqual = (obj1, obj2) => {
  const keysArr1 = Object.keys(obj1);
  const keysArr2 = Object.keys(obj2);
  if (keysArr1.length !== keysArr2.length) return false;
  else if (keysArr1.length === 0 && keysArr2.length === 0) return true;
  /* eslint-disable-next-line */ else return !keysArr1.some(key => obj1[key] != obj2[key]);
};
