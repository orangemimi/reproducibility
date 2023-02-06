package edu.njnu.reproducibility.domain.integratetask.support;

import edu.njnu.reproducibility.domain.dataItemCollection.DataItemCollection;
import edu.njnu.reproducibility.domain.file.FileItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：Zhiyi
 * @Date ：2021/4/7 15:02
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class ResultValidation {
    String name = "";
    String description = "";
    String stepType = "";
    List<String> pictures = new ArrayList<>();
    List<DataItemCollection>  dataResource = new ArrayList<DataItemCollection>();
    ResultOutput resultOutput = new ResultOutput();
    String softName = "";
    String softVersion = "";
    String softStorage = "";
    String softDepend = "";
    String codeContent = "";
    String codeLanguage = "";
    String codeDepend  = "";
    String isCodeUpload = "false";
}

