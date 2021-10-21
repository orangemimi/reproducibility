import { get, post, patch, del } from '@/axios';
import { successNotification } from '@/utils/notification';
//-------------------------------------------------users--------------------------------------

//判断是否是已经Star的项目
export async function isStarProject(projectId) {
  return await get(`/users/isSart/${projectId}`)
}

export async function getUserByEmailLike(email) {
  return await get(`/users/like/${email}`);
}

export async function getUserByJwtUserId() {
  return await get(`/users`);
}

export async function getUserProjectInfo() {
  return await get(`/users/projects`);
}

export async function updateUserByJwtUserId(form) {
  let data = await patch(`/users`, form);
  if (data != null) {
    successNotification('update', 'user');
  }
  return data;
}

export async function updateUserJoinedProjects(form) {
  let data = await patch(`/users`, form);
  // if (data != null) {
  //   successNotification('update', 'user');
  // }
  return data;
}
export async function updateUserCreatedProjects(form) {
  let data = await patch(`/users`, form);
  // if (data != null) {
  //   successNotification('update', 'user');
  // }
  return data;
}

export async function saveUser(form) {
  let data = await post(`/users/register`, form);
  if (data != null) {
    successNotification('create', 'user');
  }
  return data;
}

//发送验证码邮件
export async function sendCodeEmail(email) {
  return await get(`/users/sendEmail/${email}`)
}

//通过验证码修改密码
export async function changePWDbyCode(email, code, password) {
  return await patch(`/users/${email}/${code}/${password}`)
}

//通过原密码修改密码
export async function changePassword(oldPWD, newPWD) {
  return await patch(`/users/changePWD/${oldPWD}/${newPWD}`)
}

//调用远程服务器获取用户信息
export async function getUserinfo() {
  return await get(`/users/getuserinfo`)
}

//获取本地和远程所有用户信息
export async function getAllUserInfo(userId) {
  return await get(`/users/getAllInfo/${userId}`)
}

//----------------------------------------------------projects-----------------------------------------

//获取project的star数
export async function getStarCount(projectId) {
  return await get(`/projects/getStarCount/${projectId}`)
}

export async function getProjectAndUsers(projectId) {
  return await get(`/projects/user/${projectId}`);
}

export async function getAllProjects(currentPage, pagesize) {
  let data = await get(`/projects/${currentPage}/${pagesize}`);
  console.log(data);
  return data;
}

export async function getProjectById(projectId) {
  return await get(`/projects/one/${projectId}`);
}

export async function updateProject(projectId, form) {
  let data = await patch(`/projects/${projectId}`, form);
  if (data != null) {
    successNotification('update', 'project');
  }
  return data;
}

export async function updateProjectMembers(projectId, form) {
  let data = await patch(`/projects/members/${projectId}`, form);
  if (data != null) {
    successNotification('update', 'project');
  }
  return data;
}

export async function saveProject(form) {
  let data = await post(`/projects`, form);
  if (data != null) {
    successNotification('create', 'project');
  }
  return data;
}

export async function forkProject(form) {
  let data = await post(`/projects/fork`, form);
  // if (data != null) {
  //   successNotification('create', 'project');
  // }
  return data;
}

//获取个人所有的projects
export async function getMyProjects() {
  return await get(`/projects/getmyprojects`)
}


//star项目
export async function starProject(projectId) {
  return await patch(`/projects/star/${projectId}`)
}

//unstar项目
export async function unStarProject(projectId) {
  return await patch(`/projects/unStar/${projectId}`)
}

//获取project的Privacy
export async function getPrivacy(projectId) {
  return await get(`/projects/getPrivacy/${projectId}`)
}

//---------------------------------------------------contextDefinition------------------------------------------

// export async function updateContexByProjectId(projectId) {
//   return await get(`/context/${projectId}`);
// }

export async function saveContext(form) {
  return await post(`/context`, form);
}

export async function updateContexByProjectId(projectId, form) {
  let data = await patch(`/context/${projectId}`, form);
  if (data != null) {
    successNotification('update', 'context definition');
  }
  return data;
}

export async function getContextByProjectId(projectId) {
  return await get(`/context/${projectId}`);
}

//---------------------------------------------------projectResources------------------------------------------

export async function getProjectResourcesById(projectId) {
  return await get(`/projectResources/${projectId}`);
}

export async function saveProjectResource(form) {
  return await post(`/projectResources`, form);
}

export async function uploadProjectResourcePicture(form) {
  return await post(`/projectResources/picture`, form);
}

export async function updateProjectResource(projectId, form) {
  let data = await patch(`/projectResources/data/${projectId}`, form);
  if (data != null) {
    successNotification('update', 'projectResources');
  }
  return data;
}

//---------------------------------------------------Resources------------------------------------------

export async function getResourcesById(projectId) {
  return await get(`/resources/${projectId}`);
}

export async function saveResource(form) {
  return await post(`/resources`, form);
}

export async function uploadResourcePicture(form) {
  return await post(`/resources/picture`, form);
}

export async function updateResource(projectId, form) {
  let data = await patch(`/resources/data/${projectId}`, form);
  if (data != null) {
    successNotification('update', 'resources');
  }
  return data;
}

//-----------------------------------------------dataitems---------------------------------------------

export async function saveFileItem(form) {
  let data = await post(`/fileItems`, form);
  if (data != null) {
    successNotification('save', 'file items');
  }
  return data;
}
export async function updateFileItemById(id, form) {
  return await patch(`/fileItems/${id}`, form);
}

export async function deleteFileItemById(id) {
  await del(`/fileItems/${id}`);
}

export async function getFileItemsByJwtUserId() {
  return await get(`/fileItems`);
}

export async function getFileItemByCreatorId(projectId) {
  return await get(`/fileItems/creator/${projectId}`);
}

//-----------------------------------------------dataitems---------------------------------------------

export async function saveDataItem(form) {
  console.log('form', form);
  let data = await post(`/dataItems`, form);
  if (data != null) {
    successNotification('save', 'data items');
  }
  return data;
}
export async function updateDataItemById(id, form) {
  return await patch(`/dataItems/${id}`, form);
}

export async function deleteDataItemById(id) {
  await del(`/dataItems/del/${id}`);
}

export async function getDataItemsByJwtUserId() {
  return await get(`/dataItems`);
}

export async function getDataItemsByProjectId(projectId) {
  return await get(`/dataItems/${projectId}`);
}

// export async function getDataItemsByProjectId(projectId) {
//   return await get(`/dataItems/creator/${projectId}`);
// }

//-----------------------------------------------dataContainer---------------------------------------------

export async function postDataContainer(form) {
  // debugger;
  let data = await post(`/dataContainer/uploadSingle`, form);
  return data;
}

//----------------------------------------------integrate tasks------------------------------------------

export async function getAllIntegrateTasksByProjectId(projectId) {
  return await get(`/integrateTasks/all/${projectId}`);
}

export async function getIntegrateTaskByTaskId(taskId) {
  return await get(`/integrateTasks/${taskId}`);
}

export async function saveIntegrateTask(postJson) {
  let data = await post(`/integrateTasks`, postJson);
  if (data != null) {
    successNotification('save', 'integrate tasks');
  }
  return data;
}

export async function updateIntegrateTask(taskId, postJson) {
  let data = await patch(`/integrateTasks/${taskId}`, postJson);
  if (data != null) {
    successNotification('update', 'integrate tasks');
  }
  return data;
}

export async function updateIntegrateTaskInstance(taskId, instanceId) {
  let data = await patch(`/integrateTasks/changeSelectInstance/${taskId}/${instanceId}`);

  return data;
}

//--------------------------------------integrateTaskInstances--------------------------------------------

export async function getAllIntegrateTaskInstancesByTaskId(taskId, currentPage, pagesize) {
  return await get(`/integrateTaskInstances/${taskId}/${currentPage}/${pagesize}`);
}

export async function getIntegrateTaskInstanceById(id) {
  let data = await get(`/integrateTaskInstances/one/${id}`);

  return data;
}

export async function saveIntegrateTaskInstance(postJson) {
  let data = await post(`/integrateTaskInstances`, postJson);
  if (data != null) {
    successNotification('create', 'integrateTaskInstances');
  }
  return data;
}

export async function updateIntegrateTaskInstanceById(id, postJson) {
  let data = await patch(`/integrateTaskInstances/${id}`, postJson);
  if (data != null) {
    successNotification('update', 'integrateTaskInstances');
  }
  return data;
}

export async function updateNoteTaskInstanceById(id, postJson) {
  let data = await patch(`/integrateTaskInstances/note/${id}`, postJson);
  if (data != null) {
    successNotification('update', 'integrateTaskInstances');
  }
  return data;
}

//-------------------------------------------modelitems--------------------------------------------------

export async function getAllModelItems(currentPage, pagesize) {
  return await get(`/modelItems/${currentPage}/${pagesize}`);
}

export async function getModelItemsByPrivacy(privacy, currentPage, pagesize) {
  return await get(`/modelItems/${privacy}/${currentPage}/${pagesize}`);
}

export async function getModelItemsByProvider(currentPage, pagesize) {
  return await get(`/modelItems/provider/${currentPage}/${pagesize}`);
}

//------------------------------------------methods-----------------------------------

export async function saveMethod(form) {
  let data = await post(`/methods`, form);
  if (data != null) {
    successNotification('save', 'method');
  }
  return data;
}

//------------------------------------------scenario------------------------------------

export async function getScenarioByProjectId(projectId) {
  return await get(`/scenario/${projectId}`);
}

export async function saveScenario(postJson) {
  return await post(`/scenario`, postJson);
}

export async function updateScenarioByProjectId(projectId, postJson) {
  let data = await patch(`/scenario/${projectId}`, postJson);
  if (data != null) {
    successNotification('update', 'scenario');
  }
  return data;
}

//------------------------------------------notices------------------------------------

export async function saveNotice(form) {
  console.log(form);
  let data = await post(`/notices`, form);
  console.log(data);
  return data;
}

export async function getNoticesByRecipientId() {
  return await get(`/notices/recipient`);
}
export async function getNoticesBySenderId() {
  return await get(`/notices/sender`);
}

//获取可见的消息
export async function getNoticesByRecipientIdTrue() {
  return await get(`/notices/recipientTrue`);
}
//获取可见的消息
export async function getNoticesBySenderIdTrue() {
  return await get(`/notices/senderTrue`);
}

export async function changeNoticeState(id, state) {
  return await patch(`/notices/${id}/${state}`);
}

export async function deleteNotice(noticeId) {
  return await del(`/notices/${noticeId}`)
}

//使recipient的消息不可见，即删除
export async function changeRecState(noticeId) {
  return await patch(`/notices/delrec/${noticeId}`)
}
//使sender的消息不可见,即删除
export async function changeSenState(noticeId) {
  return await patch(`/notices/delsen/${noticeId}`)
}

//改变回复消息是否已读的状态，只要用户点击到回复消息的页面，即认为回复消息已读了
export async function changeAllReplyisread() {
  return await patch(`/notices/changeallreply`)
}

//------------------------------------------emails------------------------------------
export async function saveEmails(form) {
  let data = await post(`/emails/invite`, form);
  if (data != null) {
    successNotification('save', 'emails');
  }
  return data;
}



//------------------------------------------records------------------------------------

export async function getRecordByProjectId(projectId) {
  return await get(`/records/${projectId}`);
}

export async function saveRecord(form) {
  return await post(`/records`, form);
}

//------------------------------------------portal------------------------------------

export async function getModelInfo(doi) {
  return await get(`/portal/modelBehavior/${doi}`);
}

export async function getAllUnitsFromPoral(currentPage, pagesize) {
  return await get(`/portal/getUnitList/${currentPage}/${pagesize}`);
}

//------------------------------------------dataService------------------------------------

export async function getDataServices(currentPage, pagesize) {
  return await get(`/dataServices/${currentPage}/${pagesize}`);
}

export async function saveDataService(form) {
  return await post(`/dataServices`, form);
}

export async function getDataServiceInfo(form) {
  debugger;

  return await post(`/dataContainer/dataService/getData`, form);
}

export async function getDataServiceInfo1(form) {

  return await post(`/dataContainer/dataService/findData`, form);
}

export async function getAllProcessing(list) {
  return await get(`/dataContainer/dataService/getAllProcessing`, list)
}

//获取自己的dataService
export async function getMyDataService() {
  return await get(`/dataServices/getmydataservices`)
}

//------------------------------------------dataServiceCode------------------------------------

export async function postDataServiceCode(form) {
  return await post(`/dataServiceCode`, form);
}

//------------------------------------------manager server------------------------------------
export async function runtask(formData) {
  return await post(`/managerServer/runtask`, formData);
}

export async function checkTaskStatus(tid) {
  return await get(`/managerServer/checkTaskStatus/${tid}`);
}

//------------------------------------------completion------------------------------------
export async function savePerformance(form) {
  return await post(`/performances`, form);
}

export async function updatePerformanceById(type, id, form) {
  return await patch(`/performances/${type}/${id}`, form); //type==context/projectResource/scenario/results
}

export async function getPerformanceByProjectId(projectId) {
  return await get(`/performances/project/${projectId}`);
}
