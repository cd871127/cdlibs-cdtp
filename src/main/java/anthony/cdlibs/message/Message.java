package anthony.cdlibs.message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Anthony on 2017/8/1.UUID.randomUUID().toString().replace("-", "");
 */
public abstract class Message<T> {
    private Map<String, String> headers = new HashMap<>();
    private T body;

    public Message() {
        headers.put("id", UUID.randomUUID().toString().replace("-", ""));
        headers.put("class-name", getClass().getName());
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public abstract byte[] getBodyBytes();

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public abstract void setBody(byte[] bodyBytes);

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
