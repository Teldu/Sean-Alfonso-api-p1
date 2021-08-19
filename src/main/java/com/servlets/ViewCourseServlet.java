package com.servlets;

import com.documents.AppUser;
import com.documents.Authorization;
import com.dto.Principal;
import com.dto.SheildedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.services.RegistrationCatalog;
import com.services.UserService;
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
        HttpSession session = req.getSession(false);
        SheildedUser sheildedUser = (session == null) ? null : (SheildedUser) session.getAttribute("auth-user");
        if(sheildedUser == null)
        {
            resp.setStatus(401);
            return;
        }


        String courseName = req.getParameter("Course-Name");
        PrintWriter respWriter = resp.getWriter();
        try{
            if(courseName == null)
            {
                registrationCatalog.;


            }else   {
                SheildedUser appUser = userService.FindUserById(userParam); // TODO implement find user by id method in user service
                respWriter.write(mapper.writeValueAsString(appUser));
            }

        }catch(Exception e)
        {

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        PrintWriter respWriter = resp.getWriter();
        try{

        }
        catch(MismatchedInputException mie)
        {
            logger.error(mie.getMessage());
            resp.sendError(400 , "UnExpected Input");
        }
        catch(IOException ioe)
        {
            logger.error(ioe.getMessage());
            resp.sendError(404 , "File not found");
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
            resp.sendError(500 , "We are sorry");
        }


    }

}
