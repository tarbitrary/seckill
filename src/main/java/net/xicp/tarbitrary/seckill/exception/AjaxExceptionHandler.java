package net.xicp.tarbitrary.seckill.exception;

import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.result.CodeMsg;
import net.xicp.tarbitrary.seckill.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author tarbitrary
 */
@RestControllerAdvice(annotations = {RestController.class, ResponseBody.class})
@Slf4j
public class AjaxExceptionHandler {
    @ExceptionHandler
    public Result ajaxExceptionHanler(
            HttpServletRequest request, HttpServletResponse response, Exception exception) {
        log.info("ajax handler");

        Result result = Result.error(CodeMsg.SERVER_ERROR);

        if (exception instanceof GlobalException) {
            GlobalException ex = (GlobalException) exception;
            result = Result.error(ex.getCm());
        } else if (exception instanceof BindException) {
            BindException ex = (BindException) exception;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            result = Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }

        return result;
    }
}
