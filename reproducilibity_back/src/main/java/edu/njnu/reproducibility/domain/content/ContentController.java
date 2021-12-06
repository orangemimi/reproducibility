package edu.njnu.reproducibility.domain.content;

import cn.hutool.json.JSONObject;
import edu.njnu.reproducibility.common.untils.JsonResult;
import edu.njnu.reproducibility.common.untils.ResultUtils;
import edu.njnu.reproducibility.domain.content.support.ContextCollection.EssentialInformation;
import edu.njnu.reproducibility.domain.content.support.dto.AddContentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2021/11/16/11:21
 * @Description:
 */
@RestController
@RequestMapping("/Content")
public class ContentController {
    @Autowired
    ContentService contentService;

    @RequestMapping(value = "/addContent", method = RequestMethod.POST)
    public JsonResult addContent(@RequestBody AddContentDTO addContentDTO) {
        contentService.addContent(addContentDTO);
        return ResultUtils.success(1);
    }

    @RequestMapping(value = "/getContent/{projectId}", method = RequestMethod.GET)
    public JsonResult getContent(@PathVariable String projectId) {
        return ResultUtils.success(contentService.getContent(projectId));
    }

    @RequestMapping(value = "/addResourceCard", method = RequestMethod.PATCH)
    public JsonResult addContentCard(@RequestBody JSONObject jsonObject) {
        return ResultUtils.success(contentService.addResourceCard(jsonObject));
    }

    @RequestMapping(value = "/getAllResource/{projectId}", method = RequestMethod.GET)
    public JsonResult getAllResource(@PathVariable String projectId) {
        return ResultUtils.success(contentService.getAllResource(projectId));
    }

    @RequestMapping(value = "/addPictureGroup", method = RequestMethod.POST)
    public JsonResult addPictureGroup(@RequestParam("datafile") MultipartFile multipartFile, @RequestParam("groupName") String groupName, @RequestParam("origin") String origin, @RequestParam("remarks") String remarks, @RequestParam("projectId") String projectId) throws IOException {
        return ResultUtils.success(contentService.addPictureGroup(multipartFile, groupName, origin, remarks, projectId));
    }

    @RequestMapping(value = "/addTextGroup", method = RequestMethod.POST)
    public JsonResult addTextGroup(@RequestBody JSONObject jsonObject) {
        return ResultUtils.success(contentService.addTextGroup(jsonObject));
    }

    @RequestMapping(value = "/addFormGroup", method = RequestMethod.POST)
    public JsonResult addFormGroup(@RequestParam("datafile") MultipartFile multipartFile, @RequestParam("title") String title, @RequestParam("origin") String origin, @RequestParam("description") String description, @RequestParam("projectId") String projectId) throws IOException {
        return ResultUtils.success(contentService.addFormGroup(multipartFile, title, origin, description, projectId));
    }

    @RequestMapping(value = "/getContextByProject/{projectId}", method = RequestMethod.GET)
    public JsonResult getContextByProject(@PathVariable String projectId) {
        return ResultUtils.success(contentService.getContextByProject(projectId));
    }

    @RequestMapping(value = "/updateEssentialInformation/{projectId}", method = RequestMethod.POST)
    public JsonResult updateEssentialInformation(@RequestBody EssentialInformation essentialInformation, @PathVariable String projectId) {
        contentService.updateEssentialInformation(projectId, essentialInformation);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/updateResource", method = RequestMethod.PATCH)
    public JsonResult updateResource(@RequestBody JSONObject jsonObject) {
        contentService.updateResource(jsonObject);
        return ResultUtils.success();
    }
}