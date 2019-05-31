package rest.files.to;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseTo {
    private ResponseAnswer status;
    private String message;
    private String query;
    private String token;
    private LocalDateTime expiresAt;
    private FileTo object;
    private List<FileTo> objects;

    public ResponseTo() {
    }

    public ResponseTo(ResponseAnswer status) {
        this.status = status;
    }

    public ResponseTo(ResponseAnswer status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ResponseAnswer getStatus() {
        return status;
    }

    public void setStatus(ResponseAnswer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public FileTo getObject() {
        return object;
    }

    public void setObject(FileTo object) {
        this.object = object;
    }

    public List<FileTo> getObjects() {
        return objects;
    }

    public void setObjects(List<FileTo> objects) {
        this.objects = objects;
    }
}
