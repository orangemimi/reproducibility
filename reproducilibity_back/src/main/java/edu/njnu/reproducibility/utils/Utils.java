package edu.njnu.reproducibility.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.domain.project.Project;
import edu.njnu.reproducibility.domain.user.User;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

public class Utils {
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


}
