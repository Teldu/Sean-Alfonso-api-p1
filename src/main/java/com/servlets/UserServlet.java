package com.servlets;

import com.documents.AppUser;
import com.documents.Authorization;
import com.dto.Principal;
import com.dto.SheildedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(UserServlet.class);

    public UserServlet(UserService userService , ObjectMapper mapper) {
        this.mapper = mapper;
        this.userService = userService;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();
        Principal principal = (Principal) req.getAttribute("principal");
        if(principal == null)
        {
            respWriter.write("<h1></h1>");
            resp.setStatus(401);
            return;
        }

        String userParam = req.getParameter("id");

        try{
            if(userParam == null)
            {
                //  List<AppUser> users = userService.findAll();TODO implement find all method in user service
                //respWriter.write(mapper.writeValueAsString(users));
            }else   {
                SheildedUser appUser = userService.FindUserById(userParam); // TODO implement find user by id method in user service
                respWriter.write(mapper.writeValueAsString(appUser));
            }

        }catch(Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        PrintWriter respWriter = resp.getWriter();
        try{

            AppUser appUser = mapper.readValue(req.getInputStream() , AppUser.class);
            appUser.setAuthorization(Authorization.STUDENT);
            Principal principal = new Principal(userService.register(appUser));


            String userInfo = mapper.writeValueAsString(principal);

            respWriter.write(userInfo);
            resp.setStatus(201);
        }
        catch(MismatchedInputException mie)
        {
            logger.error(mie.getMessage());
            resp.setStatus(400);
            resp.sendError(400 , "UnExpected Input");
        }
        catch(IOException ioe)
        {
            logger.error(ioe.getMessage());
            resp.setStatus(404);
            resp.sendError(404 , "File not found");
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
            resp.setStatus(500);
            resp.sendError(500 , "We are sorry");
        }


    }

}
