package com.q.servlet;

import com.q.annotation.WebServlet;
import com.q.tomcat.HttpServlet;
@WebServlet("/user")
public class UserServlet implements HttpServlet {

    public void doGet() {
        System.out.println("Get方法");
    }

    public void doPost() {
        System.out.println("Post方法");
    }
}
