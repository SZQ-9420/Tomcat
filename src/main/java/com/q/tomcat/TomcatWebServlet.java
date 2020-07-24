package com.q.tomcat;

import com.q.annotation.WebServlet;
import com.q.exception.UriUniqueException;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class TomcatWebServlet {

    public HashMap<String,HttpServlet> tomcatservlet() throws Exception {
        HashMap<String, HttpServlet> servletHashMap = new HashMap<String, HttpServlet>();
        String basePackage ="com.q.servlet";
        String path = basePackage.replaceAll("\\.", "/");
        URL url = TomcatWebServlet.class.getResource("/");
        File file = new File(url.getPath()+ path);
        File[] files = file.listFiles();
        for (int i=0,size=files.length;i<size;i++){
               if (files[i].isFile()){
                   String name = files[i].getName();
                   if (name.endsWith(".class")){
                       String[] strings = name.split("\\.");
                       String string = strings[0];
                       String allClassName=basePackage+"."+string;
                       Class<?> aClass = Class.forName(allClassName);
                       WebServlet annotation = aClass.getAnnotation(WebServlet.class);
                       if (annotation!=null){
                           String value = annotation.value();
                           if (servletHashMap.get(value)!=null){
                               throw new UriUniqueException();
                           }
                           HttpServlet servlet = (HttpServlet) aClass.newInstance();
                           servletHashMap.put(value,servlet);
                       }
                   }
               }
        }
        return servletHashMap;
    }
}
