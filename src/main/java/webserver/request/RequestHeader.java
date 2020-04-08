package webserver.request;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private Map<String, String> headers = new HashMap<>();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void add(String next) {
        String[] headerTokens = HttpRequestUtils.parseHeaderToken(next);
        headers.put(headerTokens[0], headerTokens[1]);
    }

    public boolean isExistHeader(String key) {
        return headers.containsKey(key);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}
