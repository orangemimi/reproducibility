package edu.njnu.reproducibility.common.exception;



import edu.njnu.reproducibility.common.enums.ResultEnum;
import lombok.Getter;

/**
 * @ClassName MyException
 * @Description 自定义的异常，结合ResultEnum中定义的错误信息，对外暴露异常。
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
@Getter
public class MyException extends RuntimeException{
    private Integer code;

    /**
     * 共有的resultEnum
     *
     * @param resultEnum
     */
    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    /**
     * 各个系统自定义
     *
     * @param message
     */
    public MyException(String message) {
        super(message);
        this.code = -9999;
    }

    public static MyException noObject() {
        return new MyException(ResultEnum.NO_OBJECT);
    }

}