package rest.files.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class UserFile extends AbstractNamedEntity{
    @Column(name = "path", nullable = false)
    @NotBlank
    private String path;

    @Column(name = "created", nullable = false)
    @NotNull
    @JsonProperty("timeCreated")
    private LocalDateTime created;

    @Column(name = "size", nullable = false)
    private long size;

    @Column(name = "content_type")
    private String contentType;

 /*   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JsonIgnore
    private User user;
*/
    @Column(name = "user_id")
    @NotNull
    @JsonIgnore
    private int userId;

    public UserFile() {
    }

    public UserFile(Integer id, String name, @NotBlank String path, @NotNull LocalDateTime created, long size, String contentType, @NotNull int userId) {
        super(id, name);
        this.path = path;
        this.created = created;
        this.size = size;
        this.contentType = contentType;
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getUser() {
        return userId;
    }

    public void setUser(int user) {
        this.userId = user;
    }
}
