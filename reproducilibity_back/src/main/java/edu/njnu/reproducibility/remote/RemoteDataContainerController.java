package edu.njnu.reproducibility.remote;

import edu.njnu.reproducibility.annotation.JwtTokenParser;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/29 11:01
 * @modified By：
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/dataContainer")
public class RemoteDataContainerController {
    @Autowired
    DataContainerFeign dataContainerFeign;

    @RequestMapping (value = "/uploadFileForm", method = RequestMethod.POST)
//    @NeedAuth
    public JsonResult uploadMultipleData(HttpServletRequest request, @JwtTokenParser(key = "name") String name, @JwtTokenParser() String id) throws IOException, ServletException {
        MultipartHttpServletRequest request1 = (MultipartHttpServletRequest) request;
        Collection<Part> parts = request1.getParts();
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        for (Part part : parts) {
            String header = part.getHeader("Content-Disposition");
            String filename2 = header.substring(header.indexOf("filename=\"") + 10,header.lastIndexOf("\""));//filename=" (整个字符串长度为10，所以要加10)
            // 获取文件名
            String fileName = part.getName();
            //  获取文件后缀名
            String suffix ="." + FilenameUtils.getExtension(filename2);
            File file=File.createTempFile(part.getName(),suffix);//创建临时文件
            FileUtils.copyInputStreamToFile(part.getInputStream(),file);
            FileSystemResource fileSystemResource=new FileSystemResource(file);
            form.add("ogmsdata",fileSystemResource);
        }
        form.add("serverNode", "china");
        form.add("userId", id);
        form.add("name", name);
        form.add("origination","reproducibility");
        return ResultUtils.success(dataContainerFeign.addData(form));
    }


}
