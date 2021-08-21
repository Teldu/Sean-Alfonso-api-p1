package com.servlets;

import com.dto.Principal;
import com.dto.SheildedUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentCourseRegistrationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Principal principal = (session == null) ? null : (Principal) session.getAttribute("auth-user");
        if(principal == null)
        {
            resp.setStatus(401);
            return;
        }

        String userParam = req.getParameter("id");
        PrintWriter respWriter = resp.getWriter();
        try{
            if(userParam == null)
            {
                //  List<AppUser> users = userService.findAll();TODO implement find all method in user service
                //respWriter.write(mapper.writeValueAsString(users));
            }else   {
                //SheildedUser appUser = userService.FindUserById(userParam); // TODO implement find user by id method in user service
                //respWriter.write(mapper.writeValueAsString(appUser));
            }

        }catch(Exception e)
        {

        }

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }
}
