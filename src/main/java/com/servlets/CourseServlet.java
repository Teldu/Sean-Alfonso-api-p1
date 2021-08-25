package com.servlets;

import com.documents.AppUser;
import com.documents.Authorization;
import com.documents.ClassDetails;
import com.dto.Classdto;
import com.dto.Principal;
import com.dto.SheildedUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.RegistrationCatalog;
import com.services.UserService;
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
import java.util.List;

public class CourseServlet extends HttpServlet {
    private final RegistrationCatalog registrationCatalog;
    private final UserService userService;
    private final ObjectMapper mapper;
    private final Logger logger = LogManager.getLogger(this.getClass());

    public CourseServlet(RegistrationCatalog registrationCatalog , UserService userService , ObjectMapper objectMapper)
    {
        this.mapper = objectMapper;
        this.userService = userService;
        this.registrationCatalog = registrationCatalog;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");
        Principal principal = (Principal) req.getAttribute("principal");
        if(principal == null){
            respWriter.write("Session has expired");
            return;
        }

        String courseName = req.getParameter("coursename");

        try{
            if(courseName == null || courseName.isEmpty())//TODO Students shoudn't see all registered students : Admin Can see the all Registered Students
            {
                List<ClassDetails> allClassDetails = registrationCatalog.showClasses();
                System.out.println(allClassDetails);
                respWriter.write(mapper.writeValueAsString(allClassDetails));
            }else   {
                ClassDetails classDetails = registrationCatalog.GetClassDetailsOf(courseName);
                System.out.println(classDetails);
                respWriter.write(mapper.writeValueAsString(classDetails));
            }

        }   catch(InvalidRequestException e)
        {

            resp.sendError(500 , "Invalid Request");
            return;
        }
        catch(Exception e)
        {
            resp.sendError(500 , "action cannot be completed");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Principal principal = (Principal) req.getAttribute("principal");
        if(principal == null)
        {
            //resp.sendRedirect("/auth");
            resp.setStatus(401);
            return ;

        }


        if(!principal.getType().equals("ADMIN"))
        {
            //resp.sendRedirect("/auth");
            resp.setStatus(403);
            System.out.println("Unauthorized Command");
            return;
        }


        try{

            PrintWriter respWriter = resp.getWriter();
            ClassDetails classDetails = mapper.readValue(req.getInputStream() , ClassDetails.class);
            respWriter.write("<h1>" + classDetails.toString() + "</h1>");

            if(classDetails != null) {
             Classdto classD =  registrationCatalog.save(classDetails);
             String classInfo = mapper.writeValueAsString(classD);
                respWriter.write("<h1>" + classInfo + "</h1>");
            }
        }catch (JsonProcessingException jpe)
        {
            resp.sendError(500 , "Failure Mapping classinfo to String");
            return;
        }
        catch(IOException ioe)
        {
            resp.sendError(500 , "Failure Reading  File");
            return;
        }
        catch(Exception e)
        {
                e.printStackTrace();
            resp.sendError(500 , "e.getMessage()");
            return;
        }



    }
}
