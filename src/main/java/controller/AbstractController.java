package controller;

import webserver.HttpMethod;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public abstract class AbstractController implements Controller{

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        if (request.matchMethod(HttpMethod.GET)) {
            doGet(request, response);
        }

        if (request.matchMethod(HttpMethod.POST)) {
            doPost(request, response);
        }
    }

    public void doPost(HttpRequest request ,HttpResponse response) throws IOException {

    }

    public void doGet(HttpRequest request ,HttpResponse response) throws IOException {

    }
}
