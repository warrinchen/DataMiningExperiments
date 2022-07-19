package com.exp.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "IndexServlet", value = "/index")
public class IndexServlet extends HttpServlet {
    //service
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.getSession().setAttribute("exp1Path", "");
        System.out.println("IndexServlet.doGet: session path1: "+request.getSession().getAttribute("exp1Path"));
        request.getSession().setAttribute("exp2Path", "");
        System.out.println("IndexServlet.doGet: session path2: "+request.getSession().getAttribute("exp2Path"));
        request.getSession().setAttribute("exp3Path", "");
        System.out.println("IndexServlet.doGet: session path3: "+request.getSession().getAttribute("exp3Path"));
//        request.getSession().setAttribute("expNo", "exp1");
        request.getRequestDispatcher("/index.html").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
