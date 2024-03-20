package model;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
                            date = "2024-02-27T16:17:37.541889551Z[GMT]")
public class ApiException extends Exception {
    public final int code;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
