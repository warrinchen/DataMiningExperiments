package com.exp.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跳转到对应的html文件
 */
@WebServlet(name = "ForwardServlet", value = "/forward/*")
public class ForwardServlet extends HttpServlet { // TODO 通过a标签的href访问, 为什么对应相同的有效的URL, 只会拦截一次? 即doGet只会执行一次
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        /*
         * /forward/form
         * /forward/a/b/c/form
         */
        String subUri=uri.substring(1);
        String page = subUri.substring(subUri.indexOf("/"));
//        request.getSession().removeAttribute("expNo");
//        request.getSession().setAttribute("expNo", page.substring(1));
//        System.out.println("ForwardServlet.doGet: expNo " + page.substring(1));

        request.getRequestDispatcher(page+".html").forward(request, response);
        System.out.println("in ForwardServlet: "+uri+"\nto "+page+".html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
        System.out.println("ForwardServlet.doPost");
    }
}
