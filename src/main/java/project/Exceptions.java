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

    /**
     * Exception for when a yield value is invalid.
     */
    protected static class YieldInvalidException extends Exception {
        /**
         * Constructor.
         * @param msg The message to contain in the exception.
         */
        protected YieldInvalidException(String msg) {
            super(msg);
        }
    }

    /**
     * Exception for when comparison params are invalid.
     */
    protected static class InvalidComparatorParamsException extends Exception {
        /**
         * Constructor.
         * @param msg the message to contain in the exception.
         */
        protected InvalidComparatorParamsException(String msg) {
            super(msg);
        }
    }
}
