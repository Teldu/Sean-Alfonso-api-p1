package com.servlets;

import com.documents.AppUser;
import com.documents.Authorization;
import com.documents.ClassDetails;
import com.dto.SheildedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.RegistrationCatalog;
import com.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
        HttpSession session = req.getSession(false);
        AppUser adminUser = (session == null) ? null : (AppUser) session.getAttribute("auth-user");


        if(adminUser == null)
        {
            resp.setStatus(401);
            return;
        }

        Authorization status = adminUser.getAuthorization();
        try{
            if(status == Authorization.NONE || status == null)
            {
                resp.setStatus(404);
                return;
            }else if (status == Authorization.STUDENT){

                resp.sendError(404 , "Unauthorized command");
            }else
            {
                ClassDetails classDetails = mapper.readValue(req.getInputStream() , ClassDetails.class);
                ClassDetails courseDetails = registrationCatalog.GetClassDetailsOf(classDetails.getClassName());
            }

        }catch(Exception e)
        {

        }
    }

}
