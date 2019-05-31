package rest.files.to;

public enum ResponseAnswer {
    SUCCESS("success"),
    ERROR("error");

    private final String result;
    ResponseAnswer(String result) {
        this.result = result;
    }

    public String getErrorCode() {
        return result;
    }
}
