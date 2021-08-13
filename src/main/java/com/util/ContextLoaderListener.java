package com.util;

import com.datasourse.repos.RegistrationCatalog;
import com.datasourse.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.services.UserService;
import com.servlets.UserServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public void contextInitialized(ServletContextEvent sce) {

            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            UserRepository userRepository = new UserRepository();
            UserService userService = new UserService(userRepository);

            UserServlet userServlet = new UserServlet(userService , mapper);

            ServletContext context =  sce.getServletContext();
            context.addServlet("UserServlet" , userServlet).addMapping("/users/*");
        }

}
