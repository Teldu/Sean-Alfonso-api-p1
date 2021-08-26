package com.servlets;

import com.documents.AppUser;
import com.documents.Authorization;
import com.documents.ClassDetails;
import com.dto.Principal;
import com.dto.RegisterCourseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.RegistrationCatalog;
import com.services.UserService;
import com.documents.Date3;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CourseEditorServlet extends HttpServlet {
    private final RegistrationCatalog registrationCatalog;
    private final UserService userService;
    private final ObjectMapper mapper;

    public CourseEditorServlet(RegistrationCatalog registrationCatalog , UserService userService , ObjectMapper objectMapper)
    {
        this.mapper = objectMapper;
        this.userService = userService;
        this.registrationCatalog = registrationCatalog;
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                RegisterCourseRequest course = mapper.readValue(req.getInputStream() , RegisterCourseRequest.class);
                System.out.println(course);

//                String targetCourse = (String)req.getAttribute("targetCourse");
//                String intent = (String)req.getAttribute("intent");
//                String className = (String)req.getAttribute("className");
//                int classSize = (Integer) req.getAttribute("classSize");
//                boolean open = (boolean)req.getAttribute("open");
//                Date3 registrationTime = (Date3) req.getAttribute("registrationDate");
//                Date3 registrationClosedTime = (Date3)req.getAttribute("registrationCloseDate");

                ClassDetails courseDetails = registrationCatalog.GetClassDetailsOf(course.getClassName());
                if(courseDetails == null)
                {
                    resp.sendError(500 , "null course");
                    return;
                }

                respWriter.write(courseDetails.toString());
                switch(course.getIntent())
                {
                        case "All":
                        case "all":
//                            courseDetails.setClassName(className);
//                            courseDetails.setClassSize(classSize);
//                            courseDetails.setOpen(open);
//                            courseDetails.setRegistrationTime(registrationTime);
//                            courseDetails.setRegistrationClosedTime(registrationClosedTime);
//                            registrationCatalog.UpdateFull(courseDetails);
//                            respWriter.write(targetCourse + " Updated!");
                            break;
                    case "Time":
                    case "time":
//                        courseDetails.setRegistrationTime(registrationTime);
//                        courseDetails.setRegistrationClosedTime(registrationClosedTime);
//                        registrationCatalog.UpdateFull(courseDetails);
//                        respWriter.write(targetCourse + " Updated!");
                        break;
                    case "status":
//                        courseDetails.setOpen(open);
//                        registrationCatalog.UpdateFull(courseDetails);
//                        String classStatus = (courseDetails.isOpen() == true) ? "open" : "closed";
//                        respWriter.write(targetCourse + " is " + classStatus +" !");
                        break;
                    case "Delete":
                        userService.RemoveClassFromCatalog(course.getClassName());
                        respWriter.write(course.getClassName() + " Removed!");
                        break;

                }

            }

        }catch(Exception e)
        {

        }
    }

}
