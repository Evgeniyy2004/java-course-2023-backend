package io.swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
                            date = "2024-02-27T16:17:37.541889551Z[GMT]")
public class NotFoundException extends ApiException {
    private final int code;

    public NotFoundException(int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
