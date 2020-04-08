package controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public class MainController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        response.forward(request.getPath() + ".html");
    }
}
