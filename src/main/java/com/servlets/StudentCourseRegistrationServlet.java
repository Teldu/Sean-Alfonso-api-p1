package com.servlets;

import com.documents.AppUser;
import com.documents.Authorization;
import com.documents.ClassDetails;
import com.dto.Credentials;
import com.dto.Principal;
import com.dto.Request;
import com.dto.SheildedUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.UserService;
import com.util.exceptions.DataSourceException;
import com.util.exceptions.InvalidRequestException;
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
        Principal principal = (Principal) req.getAttribute("principal");
        if(principal == null)
        {
            resp.sendError(400 , "session expired");
            return;
        }

        if (principal.getType().equals(Authorization.ADMIN.toString()))
        {
            System.out.println(principal.getUsername());
            resp.sendError(403 , "Admin Cannot Register for Courses");
        }

        try {


            SheildedUser appUser  = userService.FindUserName(principal.getUsername());

            PrintWriter respWriter = resp.getWriter();
            Request request = mapper.readValue(req.getInputStream() , Request.class);
            String requestJsonFrom = mapper.writeValueAsString(request);
            respWriter.write(requestJsonFrom);
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
                ClassDetails tempClass = userService.getClassDetailsOf(request.getName());
                if(tempClass.getStudentsRegistered().contains(appUser.getFirstName()))
                {
                    resp.sendError(400 , "Already Registered For Class");
                    return;
                }
                userService.AddClass( request.getName() , appUser.getFirstName(), appUser.getUsername());
                respWriter.write("Succesfully Added " + request.getRequest());
            }


        }
        catch(InvalidRequestException e)
        {
            resp.getWriter().write("<h1>" + e.getMessage() + "</h1>");
            resp.setStatus(301);
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
