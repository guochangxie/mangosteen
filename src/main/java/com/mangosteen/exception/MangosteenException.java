package com.mangosteen.exception;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/2/1810:53 AM
 */
public class MangosteenException extends RuntimeException {



    public MangosteenException(String message) {
        super(message);
    }

    public MangosteenException(String message, Throwable cause) {
        super(message, cause);

    }
}
