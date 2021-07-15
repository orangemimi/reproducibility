package edu.njnu.reproducibility.utils;

import cn.hutool.core.lang.UUID;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/11 14:45
 * @modified By：
 * @version: 1.0.0
 */
public class FileUtil {
    //静态方法：三个参数：文件的二进制，文件路径，文件名
    //通过该方法将在指定目录下添加指定文件
    public static String uploadFile(MultipartFile upload, String filePath) throws Exception {
        File file = new File(filePath);

        if (!file.exists()) {
            file.mkdirs();
        }

        String originalFileName = upload.getOriginalFilename();//获取原始图片的扩展名
        String newFileName = UUID.randomUUID() + originalFileName;
        String newFilePath = filePath + newFileName; //新文件的路径

        upload.transferTo(new File(newFilePath));  //将传来的文件写入新建的文件
        return newFileName;
    }

    /**
     * 删除单个文件
     * @param   fileName    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        Boolean flag = false;
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除单个文件
     * @param   content    内容
     * @param   prefix    文件名前缀
     * @param   suffix    文件名后缀
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static File getFile(String content, String filePath,String prefix,String suffix) throws IOException{
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String newFileName = UUID.randomUUID() + prefix+"."+suffix;
        String newFilePath = filePath + newFileName; //新文件的路径
        File file2= new File(newFilePath);

        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file2), "UTF-8");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.flush();
        bw.close();
        fw.close();

        return file2;
    }



}

