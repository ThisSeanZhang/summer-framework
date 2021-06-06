package io.whileaway.code.summer.modular.exception;

public class ServiceException extends Exception {

    public ServiceException(String message) {
        super(message);
    }

    public static class ServiceNotProvidedException extends ServiceException {
        public ServiceNotProvidedException(String serviceTypeName) {
            super("Service " + serviceTypeName + " should not be provided, based on moduleDefine define.");
        }
    }
}
