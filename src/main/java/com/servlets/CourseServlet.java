package com.servlets;

import com.documents.Authorization;
import com.documents.ClassDetails;
import com.dto.Classdto;
import com.dto.RequestObjects.DeleteRequest;
import com.dto.Principal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.datasourse.repos.RegistrationCatalog;
import com.services.UserService;
import com.util.exceptions.InvalidRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                ClassDetails registerCourseRequest = registrationCatalog.GetClassDetailsOf(courseName);
                System.out.println(registerCourseRequest);
                respWriter.write(mapper.writeValueAsString(registerCourseRequest));
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
            ClassDetails registerCourseRequest = mapper.readValue(req.getInputStream() , ClassDetails.class);


            if(registerCourseRequest != null) {
             Classdto classD =  registrationCatalog.save(registerCourseRequest);
             ///System.out.println(classD);
             String classInfo = mapper.writeValueAsString(classD);

             respWriter.write(classInfo);
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
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Principal principal = (Principal) req.getAttribute("principal");
        PrintWriter respWriter = resp.getWriter();

        if(principal == null)
        {
            resp.setStatus(401);
            return;
        }

        String status = principal.getType();
        try{
            if(status == Authorization.NONE.toString() || status == null)
            {
                resp.setStatus(404);
                return;
            }else if (status == Authorization.STUDENT.toString()){

                resp.sendError(404 , "Unauthorized command");
            }else
            {
                DeleteRequest course = mapper.readValue(req.getInputStream() , DeleteRequest.class);
                System.out.println(course.toString());
                userService.RemoveClassFromCatalog(course.getClassName());
                String classInfo3 = mapper.writeValueAsString(course);
                respWriter.write(classInfo3 + " Remove!");
            }

        }catch(Exception e)
        {

        }
    }
}
