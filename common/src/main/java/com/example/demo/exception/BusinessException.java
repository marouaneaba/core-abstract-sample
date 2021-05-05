package com.example.demo.exception;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(BusinessException.class);
    private static final String DEFAULT_ERROR_CODE = "BUSINESS_EXCEPTION";

    private String code = DEFAULT_ERROR_CODE;

    public BusinessException(){
        super();
    }

    public BusinessException(Throwable t) {
        super(t);
    }

    public BusinessException(String message, Throwable t) {
        super(message, t);
    }

    public BusinessException(String code, String message, Throwable t) {
        super(message, t);
        this.setCode(code);
    }

    public BusinessException(String code, String message) {
        super(message);
        this.setCode(code);
    }


    public BusinessException(String message) {
        super(message);
    }
}
