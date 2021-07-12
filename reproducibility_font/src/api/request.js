import { get, post, patch, del } from '@/axios';

//-------------------------------------------------users--------------------------------------
export async function getUserInfoLike(email) {
  return await get(`/users/like/${email}`);
}

export async function getUserInfoByJwtUserId() {
  return await get(`/users`);
}

<<<<<<< HEAD
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
=======
export async function updateUsersByJwtUserId(form) {
  return await patch(`/users`, form);
}

export async function saveUsers(form) {
  await post(`/users/register`, form);
>>>>>>> parent of f11cd19 (mxgraph)
}

//----------------------------------------------------projects-----------------------------------------
export async function getProjectAndUsers(projectId) {
  return await get(`/projects/user/${projectId}`);
}

export async function getAllProjects(currentPage, pagesize) {
  await get(`/projects/${currentPage}/${pagesize}`);
}

export async function getProjectById(projectId) {
  return await get(`/projects/one/${projectId}`);
}

export async function updateProject(projectId, form) {
  return await patch(`/projects/${projectId}`, form);
}

export async function updateProjectMembers(projectId, form) {
  let data = await patch(`/projects/members/${projectId}`, form);
  if (data != null) {
    successNotification('update', 'project');
  }
  return data;
}

export async function saveProject(form) {
<<<<<<< HEAD
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
=======
  return await post(`/projects`, form);
}

//---------------------------------------------------resources------------------------------------------
export async function getResourcesById(projectId) {
  return await get(`/resources/${projectId}`);
}

export async function saveResources(form) {
  return await post(`/resources`, form);
}

export async function updateResources(projectId, form) {
  return await patch(`/resources/data/${projectId}`, form);
}

//-----------------------------------------------dataitems---------------------------------------------
export async function saveDataItems(form) {
  return await post(`/dataItems`, form);
>>>>>>> parent of f11cd19 (mxgraph)
}
export async function updateDataItemById(id, form) {
  return await patch(`/dataItems/${id}`, form);
}

export async function deleteDataItemsById(id) {
  await del(`/dataItems/${id}`);
}

export async function getDataItemsByJwtUserId() {
  await del(`/dataItems`);
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
  return await get(`/integrateTasks/all/${projectId}`); //获得该项目的所有tasks
}

export async function saveIntegrateTasks(postJson) {
  return await post(`/integrateTasks`, postJson);
}

export async function updateIntegrateTasks(taskId, postJson) {
  return await patch(`/integrateTasks/${taskId}`, postJson);
}

export async function updateIntegrateTaskInstance(taskId, instanceId) {
  let data = await patch(`/integrateTasks/changeSelectInstance/${taskId}/${instanceId}`);

  return data;
}

//--------------------------------------integrateTaskInstances--------------------------------------------
<<<<<<< HEAD

export async function getAllIntegrateTaskInstancesByTaskId(taskId, currentPage, pagesize) {
  return await get(`/integrateTaskInstances/${taskId}/${currentPage}/${pagesize}`);
}

export async function getIntegrateTaskInstanceById(id) {
  let data = await get(`/integrateTaskInstances/one/${id}`);

  return data;
=======
export async function getAllInstancesByTaskId(taskId, currentPage, pagesize) {
  return await get(`/integrateTaskInstances/all/${taskId}/${currentPage}/${pagesize}`); //获得该项目的所有tasks
>>>>>>> parent of f11cd19 (mxgraph)
}

export async function saveIntegrateTaskInstances(postJson) {
  return await post(`/integrateTaskInstances`, postJson);
}

<<<<<<< HEAD
export async function updateIntegrateTaskInstanceById(id, postJson) {
  let data = await patch(`/integrateTaskInstances/${id}`, postJson);
  if (data != null) {
    successNotification('update', 'integrateTaskInstances');
  }
  return data;
=======
export async function updateIntegrateTaskInstances(taskId, postJson) {
  return await patch(`/integrateTasks/${taskId}`, postJson);
>>>>>>> parent of f11cd19 (mxgraph)
}

export async function updateNoteTaskInstanceById(id, postJson) {
  let data = await patch(`/integrateTaskInstances/note/${id}`, postJson);
  if (data != null) {
    successNotification('update', 'integrateTaskInstances');
  }
  return data;
}

//-------------------------------------------modelitems--------------------------------------------------
export async function getAllModels(currentPage, pagesize) {
  return await get(`/modelItems/${currentPage}/${pagesize}`);
}

export async function getModelsByPrivacy(privacy, currentPage, pagesize) {
  return await get(`/modelItems/${privacy}/${currentPage}/${pagesize}`);
}

export async function getModelsByProvider(currentPage, pagesize) {
  return await get(`/modelItems/provider/${currentPage}/${pagesize}`);
}

//------------------------------------------methods-----------------------------------
export async function saveMethods(form) {
  return await post(`/methods`, form);
}

<<<<<<< HEAD
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
=======
//------------------------------------------noticeS------------------------------------
export async function saveNotices(form) {
  return await post(`/notices`, form);
>>>>>>> parent of f11cd19 (mxgraph)
}

//------------------------------------------emails------------------------------------
export async function saveEmails(form) {
  return await post(`/emails/invite`, form);
}

export async function getAllUnitsFromPoral(currentPage, pagesize) {
  return await get(`/portal/getUnitList/${currentPage}/${pagesize}`);
}

//------------------------------------------dataService------------------------------------

export async function getDataServices(currentPage, pagesize) {
  return await get(`/dataServices/${currentPage}/${pagesize}`);
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
