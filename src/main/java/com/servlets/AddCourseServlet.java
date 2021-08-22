package com.servlets;

import com.documents.Authorization;
import com.documents.ClassDetails;
import com.dto.Classdto;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        HttpSession session = req.getSession(false);
//        SheildedUser sheildedUser = (session == null) ? null : (SheildedUser) session.getAttribute("auth-user");
//        if(sheildedUser == null)
//        {
//            //resp.sendRedirect("/auth");
//            resp.setStatus(401);
//            return;
//        }
//        if(sheildedUser.getAuthorization() !=  Authorization.ADMIN)
//        {
//            //resp.sendRedirect("/auth");
//            resp.setStatus(403);
//            System.out.println("Unauthorized Command");
//            return;
//        }


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
