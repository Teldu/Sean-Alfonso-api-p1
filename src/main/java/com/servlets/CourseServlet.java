package com.servlets;

import com.documents.Authorization;
import com.documents.ClassDetails;
import com.dto.Classdto;
import com.dto.ErrorResponse;
import com.dto.RequestObjects.DeleteRequest;
import com.dto.Principal;
import com.dto.RequestObjects.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.datasourse.repos.RegistrationCatalog;
import com.services.UserService;
import com.util.DateParser;
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
import java.util.List;

public class CourseServlet extends HttpServlet {
    private final RegistrationCatalog registrationCatalog;
    private final UserService userService;
    private final ObjectMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(CourseServlet.class);

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

        DeleteRequest courseName =  mapper.readValue(req.getInputStream() , DeleteRequest.class);

        try{
            if(courseName == null || courseName.getClassName().isEmpty())//TODO Students shoudn't see all registered students : Admin Can see the all Registered Students
            {
                List<ClassDetails> allClassDetails = registrationCatalog.showClasses();
                System.out.println(allClassDetails);
                respWriter.write(mapper.writeValueAsString(allClassDetails));

            }else   {
                ClassDetails registerCourseRequest = registrationCatalog.GetClassDetailsOf(courseName.getClassName());
                System.out.println(registerCourseRequest);
                respWriter.write(mapper.writeValueAsString(registerCourseRequest));
            }

        }   catch(InvalidRequestException e)
        {
            logger.error(e.getMessage());
            ErrorResponse errResp = new ErrorResponse(500 , "Invalid Request");
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
            ErrorResponse errResp = new ErrorResponse(500 , "action cannot be completed");
            respWriter.write(mapper.writeValueAsString(errResp));

            return;
        }
    }

    //add class
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try{
        Principal principal = (Principal) req.getAttribute("principal");
        PrintWriter respWriter = resp.getWriter();
        if(principal == null)
        {

            ErrorResponse errResp = new ErrorResponse(401, "null session");
            respWriter.write(mapper.writeValueAsString(errResp));
            return ;

        }


        if(!principal.getType().equals("ADMIN"))
        {
            //resp.sendRedirect("/auth");
            resp.setStatus(403);
            ErrorResponse errResp = new ErrorResponse(500, "Cannot Authenticate");
            respWriter.write(mapper.writeValueAsString(errResp));
            System.out.println("Unauthorized Command");
            return;
        }




            ClassDetails registerCourseRequest = mapper.readValue(req.getInputStream() , ClassDetails.class);
            registerCourseRequest.setOpen(new DateParser().htmlWindow(registerCourseRequest.getRegistrationTime() , registerCourseRequest.getRegistrationClosedTime()));

            if(registerCourseRequest != null) {
             Classdto classD =  registrationCatalog.save(registerCourseRequest);
             ///System.out.println(classD);
             String classInfo = mapper.writeValueAsString(classD);

             respWriter.write(classInfo);
            }
        }catch (JsonProcessingException jpe)
        {
            logger.error(jpe.getMessage());
            resp.sendError(500 , "Failure Mapping classinfo to String");
            return;
        }
        catch(IOException ioe)
        {
            logger.error(ioe.getMessage());
            resp.sendError(500 , "Failure Reading  File");
            return;
        }
        catch(Exception e)
        {
                e.printStackTrace();
            logger.error(e.getMessage());
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
                ErrorResponse errResp = new ErrorResponse(404, "Unauthorized command");
                respWriter.write(mapper.writeValueAsString(errResp));

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
            logger.error(e.getMessage());
        }
    }
}
