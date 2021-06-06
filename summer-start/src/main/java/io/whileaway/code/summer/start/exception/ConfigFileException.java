package io.whileaway.code.summer.start.exception;

public class ConfigFileException extends Exception {

    public ConfigFileException(String message) {
        super(message);
    }

    public ConfigFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class ConfigFileEmptyException extends ConfigFileException {

        public ConfigFileEmptyException() {
            super("Found config file but it is empty ☹!!!");
        }

        public ConfigFileEmptyException(String message) {
            super(message);
        }

        public ConfigFileEmptyException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class ConfigFileNotFoundException extends ConfigFileException {

        public ConfigFileNotFoundException() {
            super("");
        }

        public ConfigFileNotFoundException(String message) {
            super(message);
        }

        public ConfigFileNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
