package exception;

import java.io.Serial;

public class SemEstoqueException extends Exception {

    @Serial
    private static final long serialVersionUID = -7509649433607067138L;

    public SemEstoqueException(String msg) {
        super(msg);
    }
}
