import ElementUI from 'element-ui';
/**
 * @param {string} behavior
 * @description create /upload
 * @param {string} message
 * @description 成功信息
 */
export function successNotification(behavior, message) {
  ElementUI.Notification({
    title: 'Success',
    message: `You have ${behavior} the ${message} successfully`,
    type: 'success'
  });
}

export function errorNotification(message) {
  ElementUI.Notification({
    title: 'Error',
    message: message,
    type: 'error'
  });
}

export function successNot(message) {
  ElementUI.Notification({
    title: 'Success',
    message: message,
    type: 'success'
  });
}