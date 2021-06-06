package io.whileaway.code.summer.modular;

import io.whileaway.code.summer.modular.annontion.Provide;
import io.whileaway.code.summer.modular.exception.ProvideException;
import io.whileaway.code.summer.modular.exception.ServiceException;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ModuleDefine {

    private final String name;

    @Getter
    private ModuleProvider loadedProvider = null;


    public ModuleDefine() {
        this.name = this.getClass().getModule().getName();
    }
    public String name() {
        return name;
    }

    public void prepare(ModuleManager manager, AppConfig appConfig, ServiceLoader<ModuleProvider> providersLoader, ServiceLoader<Service> serviceLoader) throws ProvideException, IllegalAccessException {
        AppConfig.ModuleConfig moduleConfig = appConfig.getModuleConfig(name);
        List<ModuleProvider> providers = providersLoader.stream()
                .filter(this::isMyProvider)
                .map(ServiceLoader.Provider::get)
                .filter(provider -> moduleConfig.hasProviderConfig(provider.name()))
                .collect(Collectors.toList());
        if (providers.size() > 1) throw new ProvideException.DuplicateProviderException(name(), providers);
        if (providers.size() == 0) throw new ProvideException.ProviderNotFoundException(name());

        loadedProvider = providers.get(0);
        loadedProvider.setManager(manager);
        loadedProvider.setModuleDefine(this);

        loadedProvider.initProviderConfig(moduleConfig.getProviderConfig(loadedProvider.name()));

//        List<Service> allService = serviceLoader.stream()
//                .map(ServiceLoader.Provider::get)
//                .collect(Collectors.toList());
//        services().stream()
//                .collect(Collectors.toMap(Function.identity(), s -> s.isAssignableFrom())
//        Map<Class<? extends Service>, Service> provideServiceMap = serviceLoader.stream()
        List<Service> allMyService = serviceLoader.stream()
                // service的包名是否包含provider的包名 如果包含则表示是这个provider的服务
                .filter(s -> s.type().getPackageName().startsWith(loadedProvider.getClass().getPackageName()))
                .map(ServiceLoader.Provider::get).collect(Collectors.toList());
        autoRegisterServiceToProvider(allMyService);
//                .collect(HashMap::new, (map, service) -> {
////                    System.out.println(service.getClass().getInterfaces());
//                    Stream.of(service.getClass().getInterfaces())
//                            .filter(Service.class::isAssignableFrom)
//                            .forEach(i -> map.put((Class<Service>)i, service));
//                } , HashMap::putAll);
//        Collectors.toMap(Service::getClass, Function.identity());
    }

    public void start() throws ServiceException {
        loadedProvider.prepare();
        loadedProvider.start();
    }



    @SuppressWarnings("unchecked")
    private void autoRegisterServiceToProvider(List<Service> allMyService) {
        allMyService.stream()
                .filter(s -> s.getClass().getInterfaces().length > 0)
                .forEach(
                    service -> Stream.of(service.getClass().getInterfaces())
                        .filter(Service.class::isAssignableFrom)
                        .forEach(i -> loadedProvider.registerService((Class<Service>)i, service))
                );
    }

    private boolean isMyProvider(ServiceLoader.Provider<ModuleProvider> provider) {
        return provider.type().isAnnotationPresent(Provide.class) && Objects.equals(provider.type().getAnnotation(Provide.class).value(), this.getClass());
    }
//
//    private boolean isMyService(ServiceLoader.Provider<Service> provider) {
//        return !services().isEmpty() && services().contains(provider.type());
//    }

    public Set<String> requireModules() {
        return loadedProvider.getRequireModuleName();
    }
}
