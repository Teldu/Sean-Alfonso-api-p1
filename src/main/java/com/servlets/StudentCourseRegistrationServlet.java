package com.servlets;

import com.documents.AppUser;
import com.dto.Principal;
import com.dto.Request;
import com.dto.SheildedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentCourseRegistrationServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;


    public StudentCourseRegistrationServlet(UserService userService , ObjectMapper mapper) {
        this.mapper = mapper;
        this.userService = userService;

    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json"); // TODO fix mapping issue
        HttpSession session = req.getSession(false);
        AppUser appUser = (session == null) ? null : (AppUser) session.getAttribute("auth-user");

        Request request = mapper.readValue(req.getInputStream() , Request.class);
        if(request == null)
        {
            resp.setStatus(401);
            return;
        }


        PrintWriter respWriter = resp.getWriter();
        try{
            if(request.getRequest().equals("Drop"))
            userService.DropClass( request.getName() , appUser.getFirstName() , appUser.getPassword());
            else
                userService.AddClass( request.getName() , appUser.getFirstName() , appUser.getPassword() );

        }catch(Exception e)
        {

        }


    }
}
