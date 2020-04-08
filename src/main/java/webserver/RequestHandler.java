package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            if (request.getPath().startsWith("/user/create")) {
                User user = new User(request.getBody("userId"), request.getBody("password"),
                        request.getBody("name"), request.getBody("email"));
                DataBase.addUser(user);
                log.debug("User : {}", user);

                response.sendRedirect("/index.html");

            } else if (request.getPath().equals("/user/login")) {
                User findUser = DataBase.findUserById(request.getBody("userId"));

                if (findUser == null) {
                    response.sendRedirect("/index.html");
                    log.debug("User Not Found!");
                } else if (findUser.getPassword().equals(request.getBody("password"))) {
                    response.addHeader("Set-Cookie", "logined=true");
                    response.sendRedirect("/index.html");
                    log.debug("Login Success!");
                } else {
                    response.sendRedirect("/user/login.html");
                    log.debug("Password Wrong!");
                }

            } else if (request.getPath().startsWith("/user/list")) {
                String cookie = request.getHeader("Cookie");

                if (cookie != null && cookie.contains("logined=true")) {
                    String body = ViewFactory.generateUsers();
                    response.forwardBody(body);
                } else {
                    response.sendRedirect("/user/login.html");
                }

            } else if (request.getPath().endsWith(".css")) {
                response.addHeader("Content-Type", "text/css;charset=utf-8");
                response.forward(request.getPath());
            } else {
                response.forward(request.getPath());
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
