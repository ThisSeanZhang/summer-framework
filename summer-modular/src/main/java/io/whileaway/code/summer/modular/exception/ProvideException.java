package io.whileaway.code.summer.modular.exception;

import io.whileaway.code.summer.modular.ModuleProvider;

import java.util.List;
import java.util.stream.Collectors;

public class ProvideException extends Exception {

    public ProvideException(String message) {
        super(message);
    }

    public static class DuplicateProviderException extends ProvideException {
        public DuplicateProviderException(String moduleName, List<ModuleProvider> providers) {
            super("module [ " + moduleName +
                    " ] more than one provider [ " +
                    providers.stream().map(ModuleProvider::name).collect(Collectors.joining(",")) +
                    " ]"
            );
        }
    }


    public static class ProviderNotFoundException extends ProvideException {
        public ProviderNotFoundException(String name) {
            super( "[ " + name + " ] module no provider exists.");
        }
    }
}
