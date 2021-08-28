package com.servlets;

import com.documents.Authorization;
import com.documents.ClassDetails;
import com.dto.Principal;
import com.dto.RequestObjects.RegisterCourseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.datasourse.repos.RegistrationCatalog;
import com.services.UserService;

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



    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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



                    // get course from database , students should not be changed
                ClassDetails courseDetails = registrationCatalog.GetClassDetailsOf(course.getTargetCourse());
                if(courseDetails == null)
                {
                    resp.sendError(500 , "null course");
                    return;
                }

                //respWriter.write(courseDetails.toString());
                switch(course.getIntent())
                {
                        case "All":
                        case "all":
                            courseDetails.setClassName(course.getClassName());
                            courseDetails.setClassSize(course.getClassSize());
                            courseDetails.setOpen(course.isOpen());
                            courseDetails.setRegistrationTime(course.getRegistrationTime());
                            courseDetails.setRegistrationClosedTime(course.getRegistrationClosedTime());
                            userService.updateCourse( course.getTargetCourse(), courseDetails);
                            String classInfo = mapper.writeValueAsString(course);
                            respWriter.write(classInfo + " Updated!");
                            break;
                    case "Time":
                    case "time":
                        courseDetails.setRegistrationTime(course.getRegistrationTime());
                        courseDetails.setRegistrationClosedTime(course.getRegistrationClosedTime());
                        registrationCatalog.UpdateFull(course.getTargetCourse(), courseDetails);
                        String classInfo1 = mapper.writeValueAsString(course);
                        respWriter.write(classInfo1 + " Updated!");
                        break;
                    case "status":
                        courseDetails.setOpen(course.isOpen());
                        registrationCatalog.UpdateFull(course.getTargetCourse(), courseDetails);
                        String classInfo2 = mapper.writeValueAsString(course);
                        respWriter.write(classInfo2 + " Updated!");
                        break;
                    case "Delete":
                        userService.RemoveClassFromCatalog(course.getTargetCourse());
                        String classInfo3 = mapper.writeValueAsString(course);
                        respWriter.write(classInfo3 + " Updated!");
                        break;

                }

            }

        }catch(Exception e)
        {

        }
    }

}
