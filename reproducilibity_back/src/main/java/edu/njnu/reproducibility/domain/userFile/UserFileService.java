package edu.njnu.reproducibility.domain.userFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.njnu.reproducibility.domain.userFile.dto.AddUserFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/22/13:29
 * @Description:
 */

@Service
public class UserFileService {

    @Autowired
    UserFileRepository userFileRepository;

    public UserFile addUserFile(AddUserFileDTO addUserFileDTO) {
        UserFile userFile = new UserFile();
        addUserFileDTO.convertTo(userFile);
        return userFileRepository.save(userFile);
    }

    public JSONArray getUserFileByFidAndStorey(String fid, String storey, String userId) {
        JSONArray jsonArray = new JSONArray();
        List<UserFile> userFiles;
        if(fid.equals("-1") && storey.equals("-1")) {
            userFiles = userFileRepository.findAllByStoreyAndUserId("-1", userId);
        } else {
            userFiles = userFileRepository.findAllByStoreyAndFidAndUserId(storey, fid, userId);
        }
        for(UserFile userFile : userFiles) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", userFile.getName());
            jsonObject.put("date", userFile.getUpdateTime());
            jsonObject.put("type", userFile.getType());
            jsonObject.put("size", userFile.getSize());
            jsonObject.put("id", userFile.getId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
