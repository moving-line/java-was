package controller;

import view.ViewFactory;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public class ListUserController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        String cookie = request.getHeader("Cookie");

        if(!isLogin(cookie)) {
            response.sendRedirect("/user/login");
        } else {
            String body = ViewFactory.generateUsers();
            response.forwardBody(body);
        }
    }

    private boolean isLogin(String cookie) {
        return cookie != null && cookie.contains("logined=true");
    }
}
