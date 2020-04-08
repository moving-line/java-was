package webserver.request;

import util.IOUtils;
import webserver.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.IOUtils.CONTENT_LENGTH;

public class HttpRequest {
    private RequestLine line;
    private RequestHeader header;
    private RequestBody body;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, UTF_8));

        line = new RequestLine(br.readLine());

        header = new RequestHeader();
        while (true) {
            String next = br.readLine();
            if (next == null || next.equals("")) {
                break;
            }
            header.add(next);
        }

        if (header.isExistHeader(CONTENT_LENGTH)) {
            body = new RequestBody(IOUtils.readData(br, Integer.parseInt(header.getHeader(CONTENT_LENGTH))));
        }
    }

    public HttpMethod getMethod() {
        return line.getMethod();
    }

    public String getPath() {
        return line.getPath();
    }

    public String getHeader(String headerKey) {
        return header.getHeader(headerKey);
    }

    public String getParameter(String paramKey) {
        return line.getParam(paramKey);
    }

    public String getBody(String bodyKey) {
        return body.getBody(bodyKey);
    }
}
