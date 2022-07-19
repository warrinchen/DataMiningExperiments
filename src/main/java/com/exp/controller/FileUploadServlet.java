package com.exp.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "FileUploadServlet", value = "/fileUpload")
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = "";
        Map res = new HashMap();
        res.put("code", 0);
        res.put("msg", "");
        res.put("message", "ok");
        if (request.getParameter("type").equals("q")) {
            String pathPrefix = request.getParameter("expid");

            request.getSession().setAttribute("expNo", pathPrefix);

            System.out.println("FileUploadServlet.doGet: "+pathPrefix+"Path");
            System.out.println("FileUploadServlet.doGet: file path:"+request.getSession().getAttribute(pathPrefix + "Path"));
            if (!request.getSession().getAttribute(pathPrefix + "Path").equals("")) {
                res.put("data", "y");  // session中有路径, 文件已上传
            } else {
                res.put("data", "n");
            }
//            System.out.println("FileUploadServlet.doGet file path:"+request.getSession().getAttribute(pathPrefix + "Path"));
            json = JSON.toJSONString(res);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathPrefix = request.getParameter("expid");
        System.out.println("FileUploadServlet.doPost: pathPrefix " + pathPrefix);

        String res = uploadFile(request, response);
        response.getWriter().println(res);
    }

    public String uploadFile(final HttpServletRequest request, final HttpServletResponse response){
//        String pathPrefix = request.getParameter("expid");
        String pathPrefix = (String) request.getSession().getAttribute("expNo");

        System.out.println("FileUploadServlet.uploadFile: pathPrefix " + pathPrefix);
        String path = "";
        //创建一个“硬盘文件条目工厂”对象
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置阈值，设置JVM一次能够处理的文件大小（默认吞吐量是10KB）
        factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        //设置临时文件的存储位置（文件大小大于吞吐量的话就必须设置这个值，比如文件大小：1GB ，一次吞吐量：1MB）
        factory.setRepository(new File("d:\\data"));  // TODO 修改文件路径存储前缀为 data/exp1/dataset.txt
        //创建核心对象
        ServletFileUpload fileUpload = new ServletFileUpload(factory);
        //设置最大可支持的文件大小（10MB）
        fileUpload.setFileSizeMax(1024*1024*10);
        //设置转换时使用的字符集
        fileUpload.setHeaderEncoding("UTF-8");
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> fileItems = fileUpload.parseRequest(request);
                for ( FileItem fileItem : fileItems) {
                    if(fileItem.isFormField()){//判断该FileItem为一个普通的form元素
                        //获取字段名
                        String fieldName = fileItem.getFieldName();
                        //获取字段值，并解决乱码
                        String fieldValue = fileItem.getString("UTF-8");
                        System.out.println(fieldName +" : " + fieldValue);
                    }else{//判断该FileItem为一个文件
                        System.out.println("Start to uplaod file!");
                        //获取文件名
                        String fileName = fileItem.getName();
                        System.out.println("fileName : " + fileName);
                        //获取文件大小
                        long fileSize = fileItem.getSize();
                        System.out.println("fileSize : " + fileSize);

                        File file=new File("D:\\data\\upload\\" + pathPrefix);
                        if(!file.exists()){//如果文件夹不存在
                            file.mkdir();//创建文件夹
                        }

                        fileItem.write(new File("D:\\data\\upload\\" + pathPrefix + File.separator + fileName));
                        path = "D:\\data\\upload\\" + pathPrefix + "\\" + fileName;
                        System.out.println("FileUploadServlet.uploadFile: file path " + path);
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String json = "";
        Map pathmap = new HashMap();
        Map jsonmap = new HashMap<>();
        jsonmap.put("code", 0);
        jsonmap.put("msg", "");
        pathmap.put("src", path);
        jsonmap.put("data", pathmap);
        json = JSON.toJSONString(jsonmap);
        request.getSession().setAttribute(pathPrefix + "Path", path);
        return json;
    }
}
