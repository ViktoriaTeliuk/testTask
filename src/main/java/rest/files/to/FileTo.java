package rest.files.to;

public class FileTo {
    private String name;
    private String path;

    public FileTo(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public FileTo(String name) {
        this.name = name;
    }

    public FileTo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
