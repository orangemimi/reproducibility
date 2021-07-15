import { get, post, patch, del } from '@/axios';
import { successNotification } from '@/utils/notification';
//-------------------------------------------------users--------------------------------------

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

//----------------------------------------------------projects-----------------------------------------

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
  await del(`/dataItems/${id}`);
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

export async function changeNoticeState(id, state) {
  return await patch(`/notices/${id}/${state}`);
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
