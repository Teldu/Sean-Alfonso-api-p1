package com.servlets;

import com.documents.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;
    private final Logger logger = LogManager.getLogger(this.getClass());

    public UserServlet(UserService userService , ObjectMapper mapper) {
        this.mapper = mapper;
        this.userService = userService;

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("<H1>User Works</H1>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           resp.setContentType("application/json");

             PrintWriter respWriter = resp.getWriter();
           try{
                AppUser appUser = mapper.readValue(req.getInputStream() , AppUser.class);
                userService.register(appUser);
                String userInfo = mapper.writeValueAsString(appUser);
                respWriter.write(userInfo);
                resp.setStatus(201);
           }
           catch(MismatchedInputException mie)
           {
               logger.error(mie.getMessage());
               resp.sendError(400 , "UnExpected Input");
           }
           catch(IOException ioe)
           {
               logger.error(ioe.getMessage());
               resp.sendError(500 , "We are sorry...");
           }
           catch(Exception e)
           {
               logger.error(e.getMessage());
               resp.sendError(500 , "We are sorry");
           }


    }

}
