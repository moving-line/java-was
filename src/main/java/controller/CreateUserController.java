package controller;

import model.User;
import service.UserService;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public class CreateUserController extends AbstractController {

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        User newUser = new User(
                request.getBody("userId"),
                request.getBody("password"),
                request.getBody("name"),
                request.getBody("email")
        );
        UserService.add(newUser);

        response.sendRedirect("/index");
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        response.forward(request.getPath() + ".html");
    }
}
