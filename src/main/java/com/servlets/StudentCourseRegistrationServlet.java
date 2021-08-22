package com.servlets;

import com.documents.AppUser;
import com.dto.Credentials;
import com.dto.Principal;
import com.dto.Request;
import com.dto.SheildedUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.UserService;
import com.util.exceptions.DataSourceException;
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
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        if(session == null)
        {
            resp.sendError(400 , "session expired");
            return;
        }
        AppUser appUser  = (session == null) ? null : (AppUser) session.getAttribute("auth-user");




        try {
            PrintWriter respWriter = resp.getWriter();
            Request request = mapper.readValue(req.getInputStream() , Request.class);
            respWriter.write(request.getRequest());
            System.out.println( request.getRequest() + " : Regestration Servlet");

            if(request == null)
            {
                resp.setStatus(400);
                return;
            }

            if( appUser == null)
            {
                resp.setStatus(500);
                return;
            }



            if (request.getRequest().equals("Drop"))
            {
                userService.DropClass(request.getName(), appUser.getFirstName(), appUser.getUsername());
            respWriter.write("Succesfully Droped " + request.getRequest());
             }
            else
            {
                userService.AddClass( request.getName() , appUser.getFirstName(), appUser.getUsername());
                respWriter.write("Succesfully Added " + request.getRequest());
            }


        }catch(DataSourceException e)
        {
            resp.sendError(400 , "file data ");
        } catch(JsonProcessingException e)
        {
            resp.sendError(500 , "Error on Json");
        }catch(IOException e)
        {
            resp.sendError(500 , "Error on File reader");
        }catch(Exception e)
        {
            e.printStackTrace();
            resp.sendError(500 , "Error on Programmer");
        }


    }
}
