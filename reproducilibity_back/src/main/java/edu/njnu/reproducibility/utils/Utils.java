package edu.njnu.reproducibility.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.domain.integratetaskInstance.IntegrateTaskInstance;
import edu.njnu.reproducibility.domain.integratetaskInstance.IntegrateTaskInstanceRepository;
import edu.njnu.reproducibility.domain.integratetaskInstance.support.Process;
import edu.njnu.reproducibility.domain.project.Project;
import edu.njnu.reproducibility.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Utils {
    @Autowired
    public static JSONObject convertMdl(JSONObject data) {
        JSONObject jsonObject = data.getJSONObject("mdlJson");
        String md5 = data.getStr("md5");
        String doi = data.getStr("oid");
        JSONArray jsonStates = jsonObject.getJSONArray("ModelClass").getJSONObject(0).getJSONArray("Behavior").getJSONObject(0).getJSONArray("StateGroup").getJSONObject(0).getJSONArray("States").getJSONObject(0).getJSONArray("State");
        JSONArray datasetItem = jsonObject.getJSONArray("ModelClass").getJSONObject(0).getJSONArray("Behavior").getJSONObject(0).getJSONArray("RelatedDatasets").getJSONObject(0).getJSONArray("DatasetItem");

        for (int j = 0; j < jsonStates.size(); j++) {
            String stateId = jsonStates.getJSONObject(j).getStr("id");
            String stateName = jsonStates.getJSONObject(j).getStr("name");
            String stateDescription = jsonStates.getJSONObject(j).getStr("description");
            for (int i = 0; i < jsonStates.getJSONObject(j).getJSONArray("Event").size(); i++) {
                JSONObject event = (JSONObject) jsonStates.getJSONObject(j).getJSONArray("Event").get(i);
                event.put("eventId", UUID.randomUUID().toString());
//                event.put("stateId", stateId);
                event.put("stateName", stateName);
                event.put("stateDescription", stateDescription);
                event.put("stateId", stateId);

                event.put("md5", md5);
                event.put("doi", doi);

                if (event.getStr("optional").equals("False") || event.getStr("optional").equals(false)) {
                    event.put("optional", "false");
                }
                if (event.getStr("optional").equals("True") || event.getStr("optional").equals(true)) {
                    event.put("optional", "true");
                }

                if (event.getStr("type").equals("response")) {
                    event.put("type", "input");
                }

                if (event.getStr("type").equals("noresponse")) {
                    event.put("type", "output");
                }

                if (event.containsKey("ResponseParameter")) {
                    String datasetReference = ((JSONObject) event.getJSONArray("ResponseParameter").get(0)).getStr("datasetReference");
                    for (int a = 0; a < datasetItem.size(); a++) {
                        JSONObject item = (JSONObject) datasetItem.get(a);

                        //新增是否为参数or文件
                        item = Utils.judgeIsParam(item);

                        if (item.getStr("name").equals(datasetReference)) {
                            event.put("datasetItem", item);
                        }
                    }
                }

                if (event.containsKey("DispatchParameter")) {
                    String datasetReference = ((JSONObject) event.getJSONArray("DispatchParameter").get(0)).getStr("datasetReference");
                    for (int a = 0; a < datasetItem.size(); a++) {
                        JSONObject item = (JSONObject) datasetItem.get(a);
                        if (item.getStr("name").equals(datasetReference)) {
                            event.put("datasetItem", item);
                        }
                    }
                }

                if (event.containsKey("ControlParameter")) {
                    String datasetReference = ((JSONObject) event.getJSONArray("ControlParameter").get(0)).getStr("datasetReference");
                    for (int a = 0; a < datasetItem.size(); a++) {
                        JSONObject item = (JSONObject) datasetItem.get(a);
                        if (item.getStr("name").equals(datasetReference)) {
                            event.put("datasetItem", item);
                        }
                    }
                }

            }
        }

        data.put("convertMdlJson", jsonStates);
        return data;
    }

    public static JSONObject judgeIsParam(JSONObject datasetItem){
        boolean flag = datasetItem.containsKey("UdxDeclaration");
        if(flag) {
            datasetItem.put("isParams", "true");
        } else {
            datasetItem.put("isParams", "false");
        }
        return datasetItem;
    }

    public static JSONArray filterUserInfo(List<User> users) {
        JSONArray userList = new JSONArray();
        for (User user : users) {
            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("value", user.getName());
//            jsonObject.put("userId", user.getUserId());
            jsonObject.put("email", user.getEmail());
            userList.add(jsonObject);
        }
        return userList;
    }

    public static JSONObject filterUserInfoProjects(User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("createdProjects", user.getCreatedProjects());
        jsonObject.put("joinedProjects", user.getJoinedProjects());
        return jsonObject;
    }

    public static JSONArray addProjectRole(Page<Project> projectList, User user) {
        JSONArray projectList1 = new JSONArray();

        for (Project project : projectList.getContent()) {
            JSONObject newProject = new JSONObject();
            newProject.put("creator", project.getCreator());
            newProject.put("members",project.getMembers());
            newProject.put("id", project.getId());
            newProject.put("name", project.getName());
            newProject.put("description", project.getDescription());
            newProject.put("picture", project.getPicture());

            newProject.put("userRole", "visitor");

            for (String create : user.getCreatedProjects()) {
                if (create.equals(project.getId())) {
                    newProject.put("userRole", "builder");
                }
            }
            for (String join : user.getJoinedProjects()) {
                if (join.equals(project.getId())) {
                    newProject.put("userRole", "rebuilder");
                }
            }

            projectList1.add(newProject);
        }
        return projectList1;
    }

    //获取请求 ip 地址
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();
        return ipAddress;
    }

    /**
    * @Description: 修改数据库IntegrateTaskInstance中的taskContent
    * @Author: Yiming
    * @Date: 2021/11/3
    */

    public static void changeStatusOfCell(IntegrateTaskInstance integrateTaskInstance, List<Map<String, String>> completedValue, List<Map<String, String>> failedValue) {
        List<Process> modelFailed = integrateTaskInstance.getTaskInfo().getModelActionList().getFailed();
        List<Process> modelCompleted = integrateTaskInstance.getTaskInfo().getModelActionList().getCompleted();
        List<Process> modelRunning = integrateTaskInstance.getTaskInfo().getModelActionList().getRunning();
        List<Process> dataServiceFailed = integrateTaskInstance.getTaskInfo().getDataProcessingList().getFailed();
        List<Process> dataServiceCompleted = integrateTaskInstance.getTaskInfo().getDataProcessingList().getCompleted();
        List<Process> dataServiceRunning = integrateTaskInstance.getTaskInfo().getModelActionList().getRunning();
        for(Map<String, String> temp : completedValue) {
            String id = temp.keySet().toArray(new String[1])[0];
            String value = temp.get(id);
            String oldStr = "<mxCell id=\"" + id + "\" value=\"\"";
            String newStr = "<mxCell id=\"" + id + "\" value=\"" + value + "\"";
            integrateTaskInstance.setTaskContent(integrateTaskInstance.getTaskContent().replaceAll(oldStr, newStr));
        }
        for(Map<String, String> temp : failedValue) {
            String id = temp.keySet().toArray(new String[1])[0];
            String oldStr = "<mxCell id=\"" + id + "\" value=\"\"";
            String newStr = "<mxCell id=\"" + id + "\" value=\"" + "failed" + "\"";
            integrateTaskInstance.setTaskContent(integrateTaskInstance.getTaskContent().replaceAll(oldStr, newStr));
        }
        for(Process p : modelFailed) {
            String id = p.getId();
            String oldStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#......;fillColor=#......";
            String newStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#f6f6f6;fillColor=#ce1212";
            integrateTaskInstance.setTaskContent(integrateTaskInstance.getTaskContent().replaceAll(oldStr, newStr));
        }
        for(Process p : dataServiceFailed) {
            String id = p.getId();
            String oldStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#......;fillColor=#......";
            String newStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#f6f6f6;fillColor=#ce1212";
            integrateTaskInstance.setTaskContent(integrateTaskInstance.getTaskContent().replaceAll(oldStr, newStr));
        }
        for(Process p : modelCompleted) {
            String id = p.getId();
            String oldStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#......;fillColor=#......";
            String newStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#24292E;fillColor=#67C23A";
            integrateTaskInstance.setTaskContent(integrateTaskInstance.getTaskContent().replaceAll(oldStr, newStr));
        }
        for(Process p : dataServiceCompleted) {
            String id = p.getId();
            String oldStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#......;fillColor=#......";
            String newStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#24292E;fillColor=#67C23A";
            integrateTaskInstance.setTaskContent(integrateTaskInstance.getTaskContent().replaceAll(oldStr, newStr));
        }
        for(Process p : modelRunning) {
            String id = p.getId();
            String oldStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#......;fillColor=#......";
            String newStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#f6f6f6;fillColor=#E6A23C";
            integrateTaskInstance.setTaskContent(integrateTaskInstance.getTaskContent().replaceAll(oldStr, newStr));
        }
        for(Process p : dataServiceRunning) {
            String id = p.getId();
            String oldStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#......;fillColor=#......";
            String newStr = "<mxCell id=\"" + id + "\" style=\"fontColor=#f6f6f6;fillColor=#E6A23C";
            integrateTaskInstance.setTaskContent(integrateTaskInstance.getTaskContent().replaceAll(oldStr, newStr));
        }
    }

    /**
    * @Description:对checkTask接口返回数据进行处理，获取output的（id，value）键值对
    * @Author: Yiming
    * @Date: 2021/11/3
    */

    public static List<Map<String, String>> getValueList(JSONObject data, String type) {
        List<Map<String, String>> list = new ArrayList<>();
        if(type.equals("completed")) {
            JSONArray modelCompleted = data.getJSONObject("taskInfo").getJSONObject("modelActionList").getJSONArray("completed");
            for(int i = 0;i < modelCompleted.size();i++) {
                JSONArray outputs = modelCompleted.get(i, JSONObject.class).getJSONObject("outputData").getJSONArray("outputs");
                for(int j = 0;j < outputs.size();j++) {
                    Map<String, String> temp = new HashMap<>();
                    temp.put(outputs.get(j, JSONObject.class).getStr("dataId"), outputs.get(j, JSONObject.class).getJSONObject("dataContent").getStr("value"));
                    list.add(temp);
                }
            }
            JSONArray dataServiceCompleted = data.getJSONObject("taskInfo").getJSONObject("dataProcessingList").getJSONArray("completed");
            for(int i = 0;i < dataServiceCompleted.size();i++) {
                JSONArray outputs = dataServiceCompleted.get(i, JSONObject.class).getJSONObject("outputData").getJSONArray("outputs");
                for(int j = 0;j < outputs.size();j++) {
                    Map<String, String> temp = new HashMap<>();
                    temp.put(outputs.get(j, JSONObject.class).getStr("dataId"), outputs.get(j, JSONObject.class).getJSONObject("dataContent").getStr("value"));
                    list.add(temp);
                }
            }
        } else if(type.equals("failed")) {
            JSONArray modelFailed = data.getJSONObject("taskInfo").getJSONObject("modelActionList").getJSONArray("failed");
            for(int i = 0;i < modelFailed.size();i++) {
                JSONArray outputs = modelFailed.get(i, JSONObject.class).getJSONObject("outputData").getJSONArray("outputs");
                for(int j = 0;j < outputs.size();j++) {
                    Map<String, String> temp = new HashMap<>();
                    temp.put(outputs.get(j, JSONObject.class).getStr("dataId"), outputs.get(j, JSONObject.class).getJSONObject("dataContent").getStr("value"));
                    list.add(temp);
                }
            }
            JSONArray dataServiceFailed = data.getJSONObject("taskInfo").getJSONObject("dataProcessingList").getJSONArray("failed");
            for(int i = 0;i < dataServiceFailed.size();i++) {
                JSONArray outputs = dataServiceFailed.get(i, JSONObject.class).getJSONObject("outputData").getJSONArray("outputs");
                for(int j = 0;j < outputs.size();j++) {
                    Map<String, String> temp = new HashMap<>();
                    temp.put(outputs.get(j, JSONObject.class).getStr("dataId"), outputs.get(j, JSONObject.class).getJSONObject("dataContent").getStr("value"));
                    list.add(temp);
                }
            }
        }
        return list;
    }

}
