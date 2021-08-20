package com.servlets;

import com.dto.ClassDetails;
import com.dto.SheildedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.RegistrationCatalog;
import com.services.UserService;
import com.util.exceptions.InvalidRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AddCourseServlet extends HttpServlet {
    private final RegistrationCatalog registrationCatalog;
    private final UserService userService;
    private final ObjectMapper mapper;
    private final Logger logger = LogManager.getLogger(this.getClass());

    public AddCourseServlet(RegistrationCatalog registrationCatalog , UserService userService , ObjectMapper objectMapper)
    {
        this.mapper = objectMapper;
        this.userService = userService;
        this.registrationCatalog = registrationCatalog;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        SheildedUser sheildedUser = (session == null) ? null : (SheildedUser) session.getAttribute("auth-user");
        ClassDetails classDetails = mapper.readValue(req.getInputStream() , ClassDetails.class);
        if(sheildedUser == null)
        {
            resp.sendRedirect("/auth");
            resp.setStatus(401);
            return;
        }


        PrintWriter respWriter = resp.getWriter();
        try{
            if(classDetails != null) {
                registrationCatalog.save(classDetails);
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
