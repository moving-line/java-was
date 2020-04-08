package controller.resource;

import controller.AbstractController;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public class FontController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.addHeader("Content-Type", "application/x-font-ttf");
        response.forward(request.getPath());
    }
}
