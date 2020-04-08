package webserver.response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private DataOutputStream dos;
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void addHeader(String key, String value) {
        if (headers.containsKey(key)) {
            headers.remove(key);
        }
        headers.put(key, value);
    }

    public void forward(String path) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        response200Header(body.length);
        processHeaders();
        responseBody(body);
        dos.flush();
    }

    public void forwardBody(String bodyValue) throws IOException {
        byte[] body = bodyValue.getBytes();
        response200Header(body.length);
        processHeaders();
        responseBody(body);
        dos.flush();
    }

    public void response200Header(int contentLength) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Length: " + contentLength + "\r\n");
    }

    public void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
    }

    public void sendRedirect(String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + location + "\r\n");
        processHeaders();
        dos.flush();
    }

    private void processHeaders() throws IOException {
        for (String headerKey : headers.keySet()) {
            dos.writeBytes(headerKey + ": " + headers.get(headerKey) + "\r\n");
        }
        dos.writeBytes("\r\n");
    }
}
