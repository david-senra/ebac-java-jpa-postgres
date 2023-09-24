
package exception;

import java.io.Serial;

public class DaoException extends Exception {

    @Serial
    private static final long serialVersionUID = 7054379063290825137L;

    public DaoException(String msg, Exception ex) {
        super(msg, ex);
    }
}
