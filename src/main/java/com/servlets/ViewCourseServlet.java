package com.servlets;

import com.documents.AppUser;
import com.documents.Authorization;
import com.dto.ClassDetails;
import com.dto.Principal;
import com.dto.SheildedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
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
import javax.swing.text.View;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ViewCourseServlet  extends HttpServlet {
    private final RegistrationCatalog registrationCatalog;
    private final UserService userService;
    private final ObjectMapper mapper;
    private final Logger logger = LogManager.getLogger(this.getClass());

    public ViewCourseServlet(RegistrationCatalog registrationCatalog , UserService userService , ObjectMapper objectMapper)
    {
        this.mapper = objectMapper;
        this.userService = userService;
        this.registrationCatalog = registrationCatalog;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        Principal principal = (session == null) ? null : (Principal) session.getAttribute("auth-user");
        if(principal == null)
        {
            resp.setStatus(401);
            return;
        }


        String courseName = req.getParameter("Course-Name");
        PrintWriter respWriter = resp.getWriter();
        try{
            if(courseName == null)
            {
               List<ClassDetails> allClassDetails = registrationCatalog.showClasses();
                respWriter.write(mapper.writeValueAsString(allClassDetails));
            }else   {
                ClassDetails classDetails = registrationCatalog.GetClassDetailsOf(courseName);
                respWriter.write(mapper.writeValueAsString(classDetails));
            }

        }   catch(InvalidRequestException e)
        {

            resp.sendError(500 , "action cannot be completed");
            return;
        }
        catch(Exception e)
        {
            resp.sendError(500 , "action cannot be completed");
            return;
        }
    }



}
