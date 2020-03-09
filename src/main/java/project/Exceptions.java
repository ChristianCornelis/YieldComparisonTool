package project;

/**
 * Custom exceptions class.
 */
public class Exceptions {

    /**
     * Exception for when a crop type is not a key in the map containing conversion factors from bushels.
     */
    public static class BushelsConversionKeyNotFoundException extends Exception {
        /**
         * Constructor.
         * @param msg The message to contain in the exception.
         */
        public BushelsConversionKeyNotFoundException(String msg) {
            super(msg);
        }
    }

    /**
     * Exception for when a yield value is invalid.
     */
    public static class YieldInvalidException extends Exception {
        /**
         * Constructor.
         * @param msg The message to contain in the exception.
         */
        public YieldInvalidException(String msg) {
            super(msg);
        }
    }

    /**
     * Exception for when comparison params are invalid.
     */
    public static class InvalidComparatorParamsException extends Exception {
        /**
         * Constructor.
         * @param msg the message to contain in the exception.
         */
        public InvalidComparatorParamsException(String msg) {
            super(msg);
        }
    }

    /**
     * Exception for when DB writes fail.
     */
    public static class DatabaseWriteException extends Exception {
        /**
         * Constructor.
         * @param msg the message to contain in the exception.
         */
        public DatabaseWriteException(String msg) {
            super(msg);
        }
    }

    /**
     * Exception for when DB Deletions fails.
     */
    public static class DatabaseDeletionException extends Exception {
        /**
         * Constructor.
         * @param msg the message to contain in the exception.
         */
        public DatabaseDeletionException(String msg) {
            super(msg);
        }
    }

    /**
     * Exception to be thrown when no records deleted from DB.
     */
    public static class NoDatabaseRecordsRemovedException extends Exception {
        /**
         * Constructor.
         * @param msg the message to contain in the exception.
         */
        public NoDatabaseRecordsRemovedException(String msg) {
            super(msg);
        }
    }
}
