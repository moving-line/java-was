package webserver.request;

import util.HttpRequestUtils;
import webserver.HttpMethod;

import java.util.Map;

public class RequestLine {
    private HttpMethod method;
    private String path;
    private Map<String, String> params;

    public RequestLine(String requestLine) {
        String[] lines = HttpRequestUtils.parseRequestLine(requestLine);
        String[] pathAndParams = HttpRequestUtils.parsePathAndParams(lines[1]);

        method = HttpMethod.valueOf(lines[0]);
        path = pathAndParams[0];
        if(pathAndParams.length == 2) {
            params = HttpRequestUtils.parseQueryString(pathAndParams[1]);
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getParam(String key) {
        return params.get(key);
    }
}
