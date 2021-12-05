package edu.njnu.reproducibility.domain.content;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import edu.njnu.reproducibility.domain.content.support.ContextCollection.Context;
import edu.njnu.reproducibility.domain.content.support.ContextCollection.EssentialInformation;
import edu.njnu.reproducibility.domain.content.support.ResourceCollection.*;
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
import java.util.UUID;

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

    public Content getContent(String projectId) {
        Content content = contentRepository.findByProjectId(projectId);
        if(content == null) {
            throw MyException.noObject();
        } else {
            return content;
        }
    }

    /**
    * @Description:以下部分是Context的相关代码
    * @Author: Yiming
    * @Date: 2021/11/28
    */

    public Context getContextByProject(String projectId) {
        Content content = contentRepository.findByProjectId(projectId);
        if(content == null) {
            throw MyException.noObject();
        }
        return content.getContext();
    }

    //修改EssentialInformation
    public void updateEssentialInformation(String projectId, EssentialInformation essentialInformation) {
        Content content = contentRepository.findByProjectId(projectId);
        if(content == null) {
            throw MyException.noObject();
        }
        content.getContext().setEssentialInformation(essentialInformation);
        contentRepository.save(content);
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
            input.setUid(UUID.randomUUID().toString());
            content.getResource().getInputs().add(input);
        }else if(jsonObject.getStr("resourceType").equals("parameter")) {
            Parameter parameter = jsonObject.getJSONObject("info").toBean(Parameter.class);
            if(content.getResource().getParameters() == null) {
                List<Parameter> parameterList = new ArrayList<>();
                content.getResource().setParameters(parameterList);
            }
            parameter.setUid(UUID.randomUUID().toString());
            content.getResource().getParameters().add(parameter);
        } else if(jsonObject.getStr("resourceType").equals("output")) {
            Output output = jsonObject.getJSONObject("info").toBean(Output.class);
            if(content.getResource().getOutputs() == null) {
                List<Output> outputList = new ArrayList<>();
                content.getResource().setOutputs(outputList);
            }
            output.setUid(UUID.randomUUID().toString());
            content.getResource().getOutputs().add(output);
        } else if(jsonObject.getStr("resourceType").equals("model")) {
            Model model = jsonObject.getJSONObject("info").toBean(Model.class);
            if(content.getResource().getModels() == null) {
                List<Model> models = new ArrayList<>();
                content.getResource().setModels(models);
            }
            model.setUid(UUID.randomUUID().toString());
            content.getResource().getModels().add(model);
        } else if(jsonObject.getStr("resourceType").equals("dataservice")) {
            DataService dataService = jsonObject.getJSONObject("info").toBean(DataService.class);
            if(content.getResource().getDataServices() == null) {
                List<DataService> dataServices = new ArrayList<>();
                content.getResource().setDataServices(dataServices);
            }
            dataService.setUid(UUID.randomUUID().toString());
            content.getResource().getDataServices().add(dataService);
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

    public void updateResource(JSONObject jsonObject) {
        String projectId = jsonObject.getStr("projectId");
        String format = jsonObject.getStr("format");
        Content content = contentRepository.findByProjectId(projectId);
        if(content == null) {
            throw MyException.noObject();
        }
        if(format.equals("input") ) {
            Input input = jsonObject.getJSONObject("bean").toBean(Input.class);
            int index = 0;
            for(Input temp : content.getResource().getInputs()) {
                if(temp.getUid().equals(input.getUid())) {
                    content.getResource().getInputs().set(index, input);
                    break;
                }
                index++;
            }
        } else if(format.equals("parameter")) {
            Parameter parameter = jsonObject.getJSONObject("bean").toBean(Parameter.class);
            int index = 0;
            for(Parameter temp : content.getResource().getParameters()) {
                if(temp.getUid().equals(parameter.getUid())) {
                    content.getResource().getParameters().set(index, parameter);
                    break;
                }
                index++;
            }
        } else if(format.equals("output")) {
            Output output = jsonObject.getJSONObject("bean").toBean(Output.class);
            int index = 0;
            for(Output temp : content.getResource().getOutputs()) {
                if(temp.getUid().equals(output.getUid())) {
                    content.getResource().getOutputs().set(index, output);
                    break;
                }
                index++;
            }
        } else if(format.equals("model")) {
            Model model = jsonObject.getJSONObject("bean").toBean(Model.class);
            int index = 0;
            for(Model temp : content.getResource().getModels()) {
                if(temp.getUid().equals(model.getUid())) {
                    content.getResource().getModels().set(index, model);
                    break;
                }
                index++;
            }
        } else if(format.equals("dataservice")) {
            DataService dataService = jsonObject.getJSONObject("bean").toBean(DataService.class);
            int index = 0;
            for(DataService temp : content.getResource().getDataServices()) {
                if(temp.getUid().equals(dataService.getUid())) {
                    content.getResource().getDataServices().set(index, dataService);
                    break;
                }
                index++;
            }
        }
        contentRepository.save(content);
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
