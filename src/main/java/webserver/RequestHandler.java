package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.HttpRequestUtils.parseQueryString;
import static util.IOUtils.CONTENT_LENGTH;
import static util.IOUtils.readData;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, UTF_8));

            HttpRequest request = new HttpRequest(in);
            String url = request.getPath();

            if (url.startsWith("/user/create")) {
                String requestBody = readData(br, Integer.parseInt(request.getHeader(CONTENT_LENGTH)));

                Map<String, String> map = parseQueryString(requestBody);
                User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
                DataBase.addUser(user);
                log.debug("User : {}", user);

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);

            } else if (url.equals("/user/login")) {
                String requestBody = readData(br, Integer.parseInt(request.getHeader(CONTENT_LENGTH)));

                Map<String, String> map = parseQueryString(requestBody);
                log.debug("userId : {}, password : {}", map.get("userId"), map.get("password"));
                User findUser = DataBase.findUserById(map.get("userId"));
                if (findUser == null) {
                    DataOutputStream dos = new DataOutputStream(out);
                    response302Header(dos);
                    log.debug("User Not Found!");
                } else if (findUser.getPassword().equals(map.get("password"))) {
                    DataOutputStream dos = new DataOutputStream(out);
                    response302HeaderWithCookie(dos, "logined=true");
                    log.debug("Login Success!");
                } else {
                    DataOutputStream dos = new DataOutputStream(out);
                    response302HeaderWithCookie(dos, "logined=false");
                    log.debug("Password Wrong!");
                }

            } else if (url.startsWith("/user/list")) {
                String cookie = request.getHeader("Cookie");

                DataOutputStream dos = new DataOutputStream(out);

                if (cookie != null && cookie.contains("logined=true")) {
                    byte[] body = ViewFactory.generateUsers().getBytes();
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                } else {
                    response302HeaderLogin(dos);
                }

            } else if (url.endsWith(".css")) {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200HeaderWithCss(dos, body.length);
                responseBody(dos, body);

            } else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderLogin(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /user/login.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200HeaderWithCss(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
