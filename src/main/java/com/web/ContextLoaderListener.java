package com.web;

import com.datasourse.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.services.RegistrationCatalog;
import com.services.UserService;
import com.servlets.*;
import com.util.MongoClientFactory;
import com.web.security.JwtConfig;
import com.web.security.TokenGenerator;


import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.EnumSet;

public class ContextLoaderListener implements ServletContextListener {

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            UserRepository userRepository = new UserRepository(mongoClient);
            RegistrationCatalog registrationCatalog = new RegistrationCatalog();
            UserService userService = new UserService(userRepository, registrationCatalog);
            JwtConfig jwtConfig = new JwtConfig();
            TokenGenerator tokenGenerator = new TokenGenerator(jwtConfig);
            AuthFilter authFilter = new AuthFilter(jwtConfig);


            UserServlet userServlet = new UserServlet(userService, mapper);
            AuthServlet authServlet = new AuthServlet(userService, mapper, tokenGenerator);
            CourseServlet courseServlet = new CourseServlet(registrationCatalog, userService, mapper);
            StudentCourseRegistrationServlet studentCourseRegistrationServlet = new StudentCourseRegistrationServlet(userService, mapper);
            ViewStudentCourseServlet viewStudentCourseServlet = new ViewStudentCourseServlet(userService, mapper);

            ServletContext context = sce.getServletContext();
            context.addFilter("AuthFilter" , authFilter).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST) , true , "/*");
            context.addServlet("UserServlet", userServlet).addMapping("/users/*");
            context.addServlet("CourseServlet", courseServlet).addMapping("/users/courses");
            context.addServlet("StudentCourseRegistrationServlet", studentCourseRegistrationServlet).addMapping("/users/registration");
            context.addServlet("StudentCourseViewServlet", viewStudentCourseServlet).addMapping("/viewStudentCourse");
            context.addServlet("AuthServlet", authServlet).addMapping("/auth");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void contextDestroyed(ServletContextEvent sce) {
        MongoClientFactory.getInstance().cleanUp();
    }

}
