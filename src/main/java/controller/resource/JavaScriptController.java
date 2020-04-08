package controller.resource;

import controller.AbstractController;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public class JavaScriptController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.addHeader("Content-Type", "Application/javascript;charset=utf-8");
        response.forward(request.getPath());
    }
}
