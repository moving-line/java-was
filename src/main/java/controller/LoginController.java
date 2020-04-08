package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.RequestHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public class LoginController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        User findUser = UserService.findUserById(request.getBody("userId"));

        if (findUser == null) {
            response.sendRedirect("/index");
            log.debug("User Not Found!");
        } else if (findUser.matchPassword(request.getBody("password"))) {
            response.addHeader("Set-Cookie", "logined=true");
            response.sendRedirect("/index");
            log.debug("Login Success!");
        } else {
            response.sendRedirect("/user/login");
            log.debug("Password Wrong!");
        }
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        response.forward(request.getPath() + ".html");
    }
}
