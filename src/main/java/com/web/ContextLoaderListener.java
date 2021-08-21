package com.web;

import com.CorsFilter;
import com.datasourse.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.services.RegistrationCatalog;
import com.services.UserService;
import com.servlets.AddCourseServlet;
import com.servlets.AuthServlet;
import com.servlets.UserServlet;
import com.servlets.ViewCourseServlet;
import com.util.MongoClientFactory;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

           MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            UserRepository userRepository = new UserRepository(mongoClient);
            RegistrationCatalog registrationCatalog = new RegistrationCatalog();
            UserService userService = new UserService(userRepository);
            CorsFilter Filter = new CorsFilter();

            UserServlet userServlet = new UserServlet(userService , mapper);
            AuthServlet authServlet = new AuthServlet(userService , mapper);
            AddCourseServlet addCourseServlet = new AddCourseServlet( registrationCatalog,userService , mapper);
            ViewCourseServlet viewCourseServlet = new ViewCourseServlet(registrationCatalog,userService , mapper);

            ServletContext context =  sce.getServletContext();
            context.addFilter("CorsFilter" , Filter);
            context.addServlet("UserServlet" , userServlet).addMapping("/users/*");
            context.addServlet("CourseAddServlet" , addCourseServlet).addMapping("/users/addCourse");
            context.addServlet("CourseViewServlet" , viewCourseServlet).addMapping("/viewCourse");
            context.addServlet("AuthServlet" , authServlet).addMapping("/auth");
        }


    public void contextDestroyed(ServletContextEvent sce) {
        MongoClientFactory.getInstance().cleanUp();
    }

}
