package edu.njnu.reproducibility.common.untils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName PropsUtils
 * @Description todo
 * @Author sun_liber
 * @Date 2019/10/15
 * @Version 1.0.0
 */
public class PropsUtils {

    private static final String PATH="project.properties";

    public static String getProp(String type){
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(PATH);
        Properties prop = new Properties();
        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(type);
    }
}
