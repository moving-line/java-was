package webserver.request;

import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class RequestBody {
    private Map<String, String> body;

    public RequestBody(String body) {
        this.body = parseQueryString(body);
    }

    public String getBody(String key) {
        return body.get(key);
    }
}
