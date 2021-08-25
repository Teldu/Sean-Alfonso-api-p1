package com.servlets;

import com.documents.AppUser;
import com.dto.Credentials;
import com.dto.Principal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.services.UserService;
import com.util.exceptions.AuthenticationException;
import com.web.security.TokenGenerator;
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

//TODO give this class a logger
public class AuthServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;
    private final TokenGenerator tokenGenerator;
    private final Logger logger = LogManager.getLogger(this.getClass());

    public AuthServlet(UserService userService , ObjectMapper mapper , TokenGenerator tokenGenerator) {
        this.mapper = mapper;
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            Credentials credentials = mapper.readValue(req.getInputStream() , Credentials.class);
            AppUser user = userService.login(credentials.getUserName() , credentials.getPassword());

            Principal p = new Principal(user);
            String payload = mapper.writeValueAsString(p);
            respWriter.write(payload);


            String token = tokenGenerator.createToken(p);
            resp.setHeader(tokenGenerator.getJwtConfig().getHeader(), token);


        } catch (AuthenticationException ae) {
            resp.setStatus(401); // server's fault
            //respWriter.write(mapper.writeValueAsString(errResp));
        }  catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500); // server's fault
            //ErrorResponse errResp = new ErrorResponse(500, "The server experienced an issue, please try again later.");
            // respWriter.write(mapper.writeValueAsString(errResp));
        }


        // RequestDispatcher requestDispatcher = req.getRequestDispatcher("auth-user");
        // requestDispatcher.forward(req , resp);
    }

}
