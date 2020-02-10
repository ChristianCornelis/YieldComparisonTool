package project;

/**
 * Custom exceptions class.
 */
public class Exceptions {

    /**
     * Exception for when a crop type is not a key in the map containing conversion factors from bushels.
     */
    protected static class BushelsConversionKeyNotFoundException extends Exception {
        /**
         * Constructor.
         * @param msg The message to contain in the exception.
         */
        protected BushelsConversionKeyNotFoundException(String msg) {
            super(msg);
        }
    }

    protected static class YieldInvalidException extends Exception {
        /**
         * Constructor.
         * @param msg The message to contain in the exception.
         */
        protected YieldInvalidException(String msg) {
            super(msg);
        }
    }
}
