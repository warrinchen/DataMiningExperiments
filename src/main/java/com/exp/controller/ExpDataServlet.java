package com.exp.controller;

import com.alibaba.fastjson.JSON;
import com.exp.entity.Exp1Data;
import com.exp.entity.Exp2Data;
import com.exp.entity.Exp3Data;
import com.exp.service.DataMiningExp1Service;
import com.exp.service.DataMiningExp2Service;
import com.exp.service.DataMiningExp3Service;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ExpServlet", value = "/ExpServlet")
public class ExpDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String expid = request.getParameter("expid");
        if (expid.equals("exp1")) {
            exp1Data(request, response);
        } else if (expid.equals("exp2")) {
            exp2Data(request, response);
        } else if (expid.equals("exp3")) {
            exp3Data(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void exp1Data(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathPrefix = (String) request.getSession().getAttribute("expNo");
        String path = (String) request.getSession().getAttribute(pathPrefix + "Path");
        System.out.println("ExpDataServlet.exp1Data: path " + path);
        ArrayList<String> testData = new ArrayList<>();
        testData.add(request.getParameter("color"));
        testData.add(request.getParameter("burgundy"));
        testData.add(request.getParameter("voice"));
        testData.add(request.getParameter("figure"));
        testData.add(request.getParameter("belly"));
        testData.add(request.getParameter("touch"));
        System.out.println("testData: "+testData+"\nfile path: "+path);

        DataMiningExp1Service dme1Service = new DataMiningExp1Service();
        Exp1Data exp1Data = dme1Service.getExp1Data(path, testData);
        Map res = new HashMap();
        res.put("code", 0);
        res.put("msg", "");
        res.put("data", exp1Data);
        res.put("message", "ok");
        String json = JSON.toJSONString(res);
        System.out.println(json);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(json);
    }
    protected void exp2Data(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathPrefix = (String) request.getSession().getAttribute("expNo");
        String path = (String) request.getSession().getAttribute(pathPrefix + "Path");
        System.out.println("ExpDataServlet.exp2Data: path " + path);
        Map<String, Double> info = new HashMap();
        info.put("min_sup", Double.valueOf(request.getParameter("min_sup")));
        info.put("min_conf", Double.valueOf(request.getParameter("min_conf")));
        System.out.println("info: "+info+"\nfile path: "+path);

        DataMiningExp2Service dme2Service = new DataMiningExp2Service();
        Exp2Data exp2Data = dme2Service.getExp2Data(path, info);
        Map res = new HashMap();
        res.put("code", 0);
        res.put("msg", "");
        res.put("data", exp2Data);
        res.put("message", "ok");
        String json = JSON.toJSONString(res);
        System.out.println(json);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(json);
    }
    protected void exp3Data(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathPrefix = (String) request.getSession().getAttribute("expNo");
        String path = (String) request.getSession().getAttribute(pathPrefix + "Path");
        double delta = Double.parseDouble(request.getParameter("delta"));
        Map<Integer, ArrayList<Double>> centers = new HashMap();
        // 从参数中获取中心点字符串 并根据中心点数量参数 转化为Java对象
        String ctStr = request.getParameter("centers");
        System.out.println("ExpDataServlet.exp3Data: centers String:\n"+ctStr);
        String[] strs = ctStr.split("\n");
        int clusterCnt = strs.length;
        int dimCnt = 0;
        for (int i = 0; i < clusterCnt; i++) {
            ArrayList<Double> center = new ArrayList<>();
            for (String s : Arrays.asList(strs[i].split(","))) {
                center.add(Double.parseDouble(s));
            }
            centers.put(i, center);
        }
        System.out.println("ExpDataServlet.exp3Data: centers "+centers+"\nfile path: "+path);

        DataMiningExp3Service dme3Service = new DataMiningExp3Service();
        Exp3Data exp3Data = dme3Service.getExp3Data(path, centers, delta);
//        Exp3Data exp3Data = new Exp3Data();
        Map res = new HashMap();
        res.put("code", 0);
        res.put("msg", "");
        res.put("data", exp3Data);
        res.put("message", "ok");
        String json = JSON.toJSONString(res);
        System.out.println(json);

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(json);
    }
}
