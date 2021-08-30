package com.servlets;

import com.documents.Authorization;
import com.documents.ClassDetails;
import com.dto.ErrorResponse;
import com.dto.Principal;
import com.dto.RequestObjects.RegisterCourseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.datasourse.repos.RegistrationCatalog;
import com.services.UserService;
import com.util.DateParser;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CourseEditorServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(CourseEditorServlet.class);
    private final RegistrationCatalog registrationCatalog;
    private final UserService userService;
    private final ObjectMapper mapper;

    public CourseEditorServlet(RegistrationCatalog registrationCatalog , UserService userService , ObjectMapper objectMapper)
    {
        this.mapper = objectMapper;
        this.userService = userService;
        this.registrationCatalog = registrationCatalog;
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Principal principal = (Principal) req.getAttribute("principal");
        PrintWriter respWriter = resp.getWriter();

        if(principal == null)
        {

            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, "session invalid");
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }

        String status = principal.getType();
        try{
            if(status == Authorization.NONE.toString() || status == null)
            {
                resp.setStatus(403);
                ErrorResponse errResp = new ErrorResponse(500, "Unautorized command");
                respWriter.write(mapper.writeValueAsString(errResp));
                return;
            }else if (status == Authorization.STUDENT.toString()){
                resp.setStatus(404);
                ErrorResponse errResp = new ErrorResponse(404 , "Unauthorized command");
                respWriter.write(mapper.writeValueAsString(errResp));
            }else
            {
                RegisterCourseRequest course = mapper.readValue(req.getInputStream() , RegisterCourseRequest.class);



                    // get course from database , students should not be changed
                ClassDetails courseDetails = registrationCatalog.GetClassDetailsOf(course.getTargetCourse());
                if(courseDetails == null)
                {
                    resp.setStatus(500);
                    ErrorResponse errResp = new ErrorResponse(500 , "null course");
                    respWriter.write(mapper.writeValueAsString(errResp));
                    return;
                }

                //respWriter.write(courseDetails.toString());
                switch(course.getIntent())
                {
                        case "All":
                        case "all":
                            courseDetails.setClassName(course.getClassName());
                            courseDetails.setClassSize(course.getClassSize());
                            courseDetails.setRegistrationTime(course.getRegistrationTime());
                            courseDetails.setRegistrationClosedTime(course.getRegistrationClosedTime());
                            // check if class is still in registration window
                            courseDetails.setOpen(new DateParser().htmlWindow (courseDetails.getRegistrationTime(),courseDetails.getRegistrationTime()));
                            courseDetails.setMeetingPeriod(course.getMeetingPeriod());
                            userService.updateCourse( course.getTargetCourse(), courseDetails);
                            String classInfo = mapper.writeValueAsString(course);
                            respWriter.write(classInfo + " Updated!");
                            break;
                    case "Time":
                    case "time":
                        courseDetails.setRegistrationTime(course.getRegistrationTime());
                        courseDetails.setRegistrationClosedTime(course.getRegistrationClosedTime());
                        // check if class is still in registration window
                        courseDetails.setOpen(new DateParser().htmlWindow (courseDetails.getRegistrationTime(),courseDetails.getRegistrationTime()));
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

                }

            }

        }catch(Exception e)
        {
            resp.setStatus(500);
            logger.error(e.getMessage());
        }
    }

}
