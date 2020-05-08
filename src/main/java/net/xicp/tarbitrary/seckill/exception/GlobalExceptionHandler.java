package net.xicp.tarbitrary.seckill.exception;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.result.CodeMsg;
import net.xicp.tarbitrary.seckill.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author tarbitrary
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = Exception.class)
    public String doExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        try {
            log.info("simple handler");
            if (isAjaxRequest(request)) {
                ajaxHandle(request, response, e);
                return null;
            }

            webHandle(request, response, e);
        } catch (Exception ex) {

        } finally {

        }


        return "/error";
    }

    private void webHandle(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.info("error msg {}", e.getMessage());
        request.setAttribute("errorMsg", e.getMessage());
    }

    private void ajaxHandle(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        Result result = Result.error(CodeMsg.SERVER_ERROR);

        if (exception instanceof GlobalException) {
            GlobalException ex = (GlobalException) exception;
            result = Result.error(ex.getCm());
        } else if (exception instanceof BindException) {
            BindException ex = (BindException) exception;
            List<ObjectError> errors = ex.getAllErrors();

            ObjectError error = errors.get(0);
            //String msg = error.getDefaultMessage();
            //  final String message = messageSource.getMessage(error.getCode(), error.getArguments(), Locale.CHINESE);
            //request.getLocale();
            String msg = messageSource.getMessage(error.getDefaultMessage(), error.getArguments(), error.getDefaultMessage(), request.getLocale());
            result = Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }

        writer.write(JSON.toJSONString(result));
        writer.flush();
        writer.close();
    }

    public boolean isAjaxRequest(HttpServletRequest request) {
        final boolean xmlHttpRequest = request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
        return xmlHttpRequest;
    }
}
