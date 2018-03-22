package com.es;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by FangYan on 2017/10/5 0005.
 */
public class CustomExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {

        ex.printStackTrace();

        CustomerException CustomerException = null;

        //如果抛出的是系统自定义异常则直接转换
        if(ex instanceof CustomerException){
            CustomerException = (CustomerException)ex;
        }else{
            //如果抛出的不是系统自定义异常则重新构造一个系统错误异常。
            CustomerException = new CustomerException("系统错误，请与系统管理 员联系！");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", CustomerException.getMessage());
        modelAndView.setViewName("404");

        return modelAndView;
    }

}