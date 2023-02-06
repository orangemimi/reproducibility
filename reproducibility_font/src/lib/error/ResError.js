import { errorNotification } from '@/utils/notification';
function ExtendableBuiltin(cls) {
  function ExtendableBuiltin() {
    cls.apply(this, arguments);
  }

  ExtendableBuiltin.prototype = Object.create(cls.prototype);
  Object.setPrototypeOf(ExtendableBuiltin, cls);

  return ExtendableBuiltin;
}

//自定义请求异常错误，注意这里是后台异常
export class ResError extends ExtendableBuiltin(Error) {
  constructor(message) {
    super(message);
    this.message = message;
    this.name = 'ResError';
    errorNotification(message);
  }
}
