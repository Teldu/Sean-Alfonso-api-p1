package com.servlets;

import com.documents.Authorization;
import com.documents.ClassDetails;
import com.dto.ErrorResponse;
import com.dto.Principal;
import com.dto.RequestObjects.Request;
import com.dto.SheildedUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.UserService;
import com.util.exceptions.DataSourceException;
import com.util.exceptions.InvalidRequestException;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentCourseRegistrationServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(StudentCourseRegistrationServlet.class);

    public StudentCourseRegistrationServlet(UserService userService , ObjectMapper mapper) {
        this.mapper = mapper;
        this.userService = userService;

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           resp.setContentType("application/json");
        PrintWriter respWriter = resp.getWriter();
           try {
               Principal principal = (Principal) req.getAttribute("principal");

        if(principal == null)
        {
            resp.setStatus(400);
            ErrorResponse errResp = new ErrorResponse(400, "null sesion");
            respWriter.write(mapper.writeValueAsString(errResp));

            return;
        }

        if (principal.getType().equals(Authorization.ADMIN.toString()))
        {
            System.out.println(principal.getUsername());
            resp.setStatus(400);
            ErrorResponse errResp = new ErrorResponse(400,"Admin Cannot Register for Courses");
            respWriter.write(mapper.writeValueAsString(errResp));
        }




            SheildedUser appUser  = userService.FindUserName(principal.getUsername());
            System.out.println(appUser.toString());

            Request request = mapper.readValue(req.getInputStream() , Request.class);
            String requestJsonFrom = mapper.writeValueAsString(request);
            respWriter.write(requestJsonFrom);
            System.out.println( request.getRequest() + " : Regestration Servlet");

            if(request == null)
            {
                ErrorResponse errResp = new ErrorResponse(500, "Server error");
                respWriter.write(mapper.writeValueAsString(errResp));
                return;
            }

            if( appUser == null)
            {
                ErrorResponse errResp = new ErrorResponse(500, "Server error");
                respWriter.write(mapper.writeValueAsString(errResp));
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
                    resp.setStatus(403);
                    resp.sendError(403 , "Already Registered For Class");
                    return;
                }
                userService.AddClass( request.getName() , appUser.getFirstName(), appUser.getUsername());
                respWriter.write("Succesfully Added " + request.getRequest());
            }


        }
        catch(InvalidRequestException e)
        {
            logger.error(e.getMessage());
            resp.setStatus(402);
            ErrorResponse errResp = new ErrorResponse(402, e.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        }catch(DataSourceException e)
        {
            logger.error(e.getMessage());
            resp.setStatus(500);
            ErrorResponse errResp = new ErrorResponse(500, e.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch(JsonProcessingException e)
        {
            logger.error(e.getMessage());
            resp.setStatus(500);
            ErrorResponse errResp = new ErrorResponse(500, "Server error");
            respWriter.write(mapper.writeValueAsString(errResp));

        }catch(Exception e)
        {
            logger.error(e.getMessage());
            resp.setStatus(500);
            ErrorResponse errResp = new ErrorResponse(500, "Server error");
            respWriter.write(mapper.writeValueAsString(errResp));
        }


    }
}
