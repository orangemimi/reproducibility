package edu.njnu.reproducibility.domain.content;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.content.support.ResourceCollection.Input;
import edu.njnu.reproducibility.domain.content.support.ResourceCollection.Output;
import edu.njnu.reproducibility.domain.content.support.ResourceCollection.Parameter;
import edu.njnu.reproducibility.domain.content.support.ResourceCollection.Resource;
import edu.njnu.reproducibility.domain.content.support.ScenarioCollection.FormGroup;
import edu.njnu.reproducibility.domain.content.support.ScenarioCollection.PictureGroup;
import edu.njnu.reproducibility.domain.content.support.ScenarioCollection.TextGroup;
import edu.njnu.reproducibility.domain.content.support.dto.AddContentDTO;
import edu.njnu.reproducibility.remote.DataContainerService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/16/11:24
 * @Description:
 */
@Service
public class ContentService {

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    DataContainerService dataContainerService;

    public void addContent(AddContentDTO addContentDTO) {
        try {
            Content content = new Content();
            addContentDTO.convertTo(content);
            contentRepository.save(content);
        }catch (Exception e) {
            System.out.println(e);
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }
    }

    /**
    * @Description:以下部分是Resource的相关方法
    * @Author: Yiming
    * @Date: 2021/11/17
    */


    public Content addResourceCard(JSONObject jsonObject) {
        Content content = contentRepository.findByProjectId(jsonObject.getStr("projectId"));
        if(jsonObject.getStr("resourceType").equals("input")) {
            Input input = jsonObject.getJSONObject("info").toBean(Input.class);
            if(content.getResource().getInputs() == null) {
                List<Input> inputList = new ArrayList<>();
                content.getResource().setInputs(inputList);
            }
            content.getResource().getInputs().add(input);
        }else if(jsonObject.getStr("resourceType").equals("parameter")) {
            Parameter parameter = jsonObject.getJSONObject("info").toBean(Parameter.class);
            if(content.getResource().getParameters() == null) {
                List<Parameter> parameterList = new ArrayList<>();
                content.getResource().setParameters(parameterList);
            }
            content.getResource().getParameters().add(parameter);
        } else {
            Output output = jsonObject.getJSONObject("info").toBean(Output.class);
            if(content.getResource().getOutputs() == null) {
                List<Output> outputList = new ArrayList<>();
                content.getResource().setOutputs(outputList);
            }
            content.getResource().getOutputs().add(output);
        }
        return contentRepository.save(content);
    }

    public Resource getAllResource(String projectId) {
        Content content = contentRepository.findByProjectId(projectId);
        if(content == null) {
            throw MyException.noObject();
        }
        return content.getResource();
    }


    /**
    * @Description:以下部分是Scenario相关的方法
    * @Author: Yiming
    * @Date: 2021/11/17
    */

    public PictureGroup addPictureGroup(MultipartFile multipartFile, String groupName, String origin, String remarks, String projectId) throws IOException {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        String suffix="."+ FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        File temp = File.createTempFile("temp", suffix);
        multipartFile.transferTo(temp);
        FileSystemResource resource = new FileSystemResource(temp);
        multiValueMap.add("datafile", resource);
        multiValueMap.add("name", groupName);
        JSONObject result = dataContainerService.upload(multiValueMap);
        String id = result.getStr("id");
        Content content = contentRepository.findByProjectId(projectId);
        if(content.getScenario().getPictureGroups() == null) {
            List<PictureGroup> pictureGroups = new ArrayList<>();
            content.getScenario().setPictureGroups(pictureGroups);
        }
        PictureGroup pictureGroup = new PictureGroup();
        pictureGroup.setGroupName(groupName);
        pictureGroup.setOrigin(origin);
        pictureGroup.setRemarks(remarks);
        pictureGroup.setUrl(id);
        content.getScenario().getPictureGroups().add(pictureGroup);
        contentRepository.save(content);
        return pictureGroup;
    }

    public TextGroup addTextGroup(JSONObject jsonObject) {
        String projectId = jsonObject.getStr("projectId");
        Content content = contentRepository.findByProjectId(projectId);
        if(content == null) {
            throw MyException.noObject();
        }
        if(content.getScenario().getTextGroups() == null) {
            List<TextGroup> textGroups = new ArrayList<>();
            content.getScenario().setTextGroups(textGroups);
        }
        TextGroup textGroup = new TextGroup();
        textGroup.setTitle(jsonObject.getStr("title"));
        textGroup.setContent(jsonObject.getStr("content"));
        content.getScenario().getTextGroups().add(textGroup);
        contentRepository.save(content);
        return textGroup;
    }

    public FormGroup addFormGroup(MultipartFile multipartFile, String title, String origin, String description, String projectId) throws IOException {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        String suffix="."+ FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        File temp = File.createTempFile("temp", suffix);
        multipartFile.transferTo(temp);
        FileSystemResource resource = new FileSystemResource(temp);
        multiValueMap.add("datafile", resource);
        multiValueMap.add("name", title);
        JSONObject result = dataContainerService.upload(multiValueMap);
        String id = result.getStr("id");
        Content content = contentRepository.findByProjectId(projectId);
        if(content.getScenario().getFormGroups() == null) {
            List<FormGroup> formGroups = new ArrayList<>();
            content.getScenario().setFormGroups(formGroups);
        }
        FormGroup formGroup = new FormGroup();
        formGroup.setTitle(title);
        formGroup.setDescription(description);
        formGroup.setOrigin(origin);
        formGroup.setUrl(id);
        content.getScenario().getFormGroups().add(formGroup);
        contentRepository.save(content);
        return formGroup;
    }
}
