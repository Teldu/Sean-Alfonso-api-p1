package com.servlets;

import com.dto.ErrorResponse;
import com.dto.Principal;
import com.dto.RequestObjects.DeleteRequest;
import com.dto.SheildedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.UserService;
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

public class ViewStudentCourseServlet extends HttpServlet {
    private final UserService userService;
    private final ObjectMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(ViewStudentCourseServlet.class);

    public ViewStudentCourseServlet(UserService userService , ObjectMapper mapper) {
        this.mapper = mapper;
        this.userService = userService;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Principal principal = (Principal) req.getAttribute("principal");
        // finding the current user from the principal in the database
        SheildedUser appUser = (principal == null) ? null : userService.FindUserName(principal.getUsername());
        DeleteRequest courseName =  mapper.readValue(req.getInputStream() , DeleteRequest.class);
        if(appUser == null)
        {
            resp.setStatus(401);
            return;
        }



        PrintWriter respWriter = resp.getWriter();
        try{
            if(courseName == null || courseName.getClassName().isEmpty() || courseName.getClassName().equals("All"))
            {
               respWriter.write(mapper.writeValueAsString(appUser.getRegisteredClasses()));
            }else   {
               // ClassDetails classDetails = registrationCatalog.GetClassDetailsOf(courseName);
              //  respWriter.write(mapper.writeValueAsString(classDetails));
            }

        }   catch(InvalidRequestException e)
        {

            ErrorResponse errResp = new ErrorResponse(500, "Server error");
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
            ErrorResponse errResp = new ErrorResponse(500, "Server error");
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }
    }
}
