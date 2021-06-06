package io.whileaway.code.summer.modular;

import io.whileaway.code.summer.modular.exception.ModuleException;
import io.whileaway.code.summer.modular.exception.ProvideException;
import io.whileaway.code.summer.modular.exception.ServiceException;
import io.whileaway.code.summer.util.CollectionUtils;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModuleManager {

    private final Map<String, ModuleDefine> loadedModules = new ConcurrentHashMap<>();

    @Setter
    @Getter
    private Path runtimePath;

    public void init(AppConfig config) throws ModuleException, ProvideException, IllegalAccessException, ServiceException {
        List<String> withoutConfigModule = config.moduleList();
        ServiceLoader<ModuleDefine> moduleDefines = ServiceLoader.load(ModuleDefine.class);
        ServiceLoader<ModuleProvider> providersLoader = ServiceLoader.load(ModuleProvider.class);
        ServiceLoader<Service> serviceLoader = ServiceLoader.load(Service.class);
        List<ModuleDefine> allModule = moduleDefines.stream().map(ServiceLoader.Provider::get)
                .filter(config::hasModuleConfig)
                .peek(module -> withoutConfigModule.remove(module.name()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(withoutConfigModule)) throw new ModuleException.ModuleNotFoundException(String.join(",", withoutConfigModule));

        for (ModuleDefine module: allModule) {
            module.prepare(this, config, providersLoader, serviceLoader);
            loadedModules.put(module.name(), module);
        }

        startAllModule();
    }

    public Optional<ModuleDefine> findModule(String moduleName) {
        return loadedModules.containsKey(moduleName) ? Optional.of(loadedModules.get(moduleName)) : Optional.empty();
    }

    public Function<Constructor<?>[], Constructor<?>> mostArgsConstruct() {
        return cons -> {
            Constructor<?> max = cons[0];
            for (Constructor<?> constructor: cons) {
                if (constructor.getParameterCount() >max.getParameterCount()) {
                    max = constructor;
                }
            }
            return max;
        };
    }

    private void startAllModule() throws ServiceException {
        Set<String> startedModuleNames = new HashSet<>();
        Set<String> unStartedModuleNames = new HashSet<>(loadedModules.keySet());

        while(unStartedModuleNames.size() > 0) {
            Iterator<String> unStartModule = unStartedModuleNames.iterator();
            while(unStartModule.hasNext()) {
                ModuleDefine unStartedModule = loadedModules.get(unStartModule.next());
                if (unStartedModule.requireModules().isEmpty() || startedModuleNames.containsAll(unStartedModule.requireModules())) {
                    unStartedModule.start();
                    unStartModule.remove();
//                    unStartedModuleNames.remove(unStartedModule.name());
                    startedModuleNames.add(unStartedModule.name());
                }
            }
        }

    }
}
