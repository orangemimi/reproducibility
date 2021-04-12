import { get, post, patch, del } from '@/axios';

//-------------------------------------------------users--------------------------------------
export async function getUserInfoLike(email) {
  return await get(`/users/like/${email}`);
}

export async function getUserInfoByJwtUserId() {
  return await get(`/users`);
}

export async function updateUsersByJwtUserId(form) {
  return await patch(`/users`, form);
}

export async function saveUsers(form) {
  await post(`/users/register`, form);
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

export async function saveProject(form) {
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
}

export async function deleteDataItemsById(id) {
  await del(`/dataItems/${id}`);
}

export async function getDataItemsByJwtUserId() {
  await del(`/dataItems`);
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

//--------------------------------------integrateTaskInstances--------------------------------------------
export async function getAllInstancesByTaskId(taskId, currentPage, pagesize) {
  return await get(`/integrateTaskInstances/all/${taskId}/${currentPage}/${pagesize}`); //获得该项目的所有tasks
}

export async function saveIntegrateTaskInstances(postJson) {
  return await post(`/integrateTaskInstances`, postJson);
}

export async function updateIntegrateTaskInstances(taskId, postJson) {
  return await patch(`/integrateTasks/${taskId}`, postJson);
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

//------------------------------------------noticeS------------------------------------
export async function saveNotices(form) {
  return await post(`/notices`, form);
}

//------------------------------------------emails------------------------------------
export async function saveEmails(form) {
  return await post(`/emails/invite`, form);
}
