package io.whileaway.code.summer.modular;

import io.whileaway.code.summer.modular.annontion.Inject;
import io.whileaway.code.summer.modular.annontion.Require;
import io.whileaway.code.summer.modular.exception.ServiceException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.module.ModuleDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class ModuleProvider {

    @Setter
    @Getter
    private ModuleManager manager;

    @Setter
    private ModuleDefine moduleDefine;

    @Setter
    private MethodHandles.Lookup lookup;

    @Getter
    private final Set<String> requireModuleName;

    public ModuleProvider(MethodHandles.Lookup lookup) {
        this.lookup = lookup;
        Require annotation = this.getClass().getAnnotation(Require.class);
        this.requireModuleName = Objects.isNull(annotation)
                ? Collections.emptySet()
                : Arrays.stream(annotation.moduleName()).map(ann -> ann.getModule().getName()).collect(Collectors.toSet());
    }

    public String name() {
        return this.getClass().getModule().getName();
    }

    private final Map<Class<? extends Service>, Service> services = new HashMap<>();

    public abstract ModuleConfig providerConfig();

    /**
     * prepare Service, register Service prepare to inject other module
     */
    public void prepare() {
        Map<Class<? extends Service>, Service> requireService = this.getClass().getModule().getDescriptor().requires().stream().map(ModuleDescriptor.Requires::name)
                .map(manager::findModule)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ModuleDefine::getLoadedProvider)
                .map(ModuleProvider::allRegisterService)
                .collect(HashMap::new, HashMap::putAll, HashMap::putAll);

        for (Service service: services.values()) {
            try {
                Class<? extends Service> destClass = service.getClass();
                if (Objects.nonNull(providerConfig())) {
                    Optional<Field> configField = Stream.of(destClass.getDeclaredFields())
                            .filter(field -> field.getType().equals(providerConfig().getClass()))
                            .findAny();
                    if (configField.isPresent()) {
                        Field field = configField.get();
                        MethodHandles.Lookup privateLookupIn = MethodHandles.privateLookupIn(destClass, this.lookup);
                        VarHandle config = privateLookupIn.findVarHandle(destClass, field.getName(), providerConfig().getClass());
                        config.set(service, providerConfig());
                    }
                }
                List<Field> injectField = Stream.of(destClass.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(Inject.class))
                        .collect(Collectors.toList());
                for (Field field: injectField) {
                    if (!requireService.containsKey(field.getType())) {
                        log.error("can not inject {}, not Found", field.getName());
                        continue;
                    }
                    MethodHandles.Lookup privateLookupIn = MethodHandles.privateLookupIn(destClass, this.lookup);
                    VarHandle config = privateLookupIn.findVarHandle(destClass, field.getName(), field.getType());
                    config.set(service, requireService.get(field.getType()));
                }
            } catch (IllegalAccessException|NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * start Provider
     */
    public void start() throws ServiceException {

    }


    protected void initProviderConfig(Properties src) throws IllegalAccessException{
        ModuleConfig config = providerConfig();
        if (Objects.isNull(config)) {
            log.info("provider config is null, while ignore!");
            return;
        }
        Class<? extends ModuleConfig> destClass = config.getClass();
        List<Field> fields = Stream.of(destClass.getDeclaredFields())
                .filter(field -> src.containsKey(field.getName()))
                .collect(Collectors.toList());
        MethodHandles.Lookup privateLookupIn = MethodHandles.privateLookupIn(destClass, this.lookup);
        for (Field field : fields) {
                try {
                    VarHandle varHandle = privateLookupIn.findVarHandle(destClass, field.getName(), field.getType());
                    varHandle.set(config, src.get(field.getName()));
                } catch (NoSuchFieldException e) {
                    log.warn(field.getName() + " setting is not supported in " + name() + " provider of " + moduleDefine.name() + " module");
                }
            }

    }

    public Map<Class<? extends Service>, Service> allRegisterService() {
        return services;
    }
    @SuppressWarnings("unchecked")
    public <T extends Service> T getService(Class<T> serviceType) throws ServiceException {
        Service service = services.get(serviceType);
        if (Objects.nonNull(service)) return (T) service;
        throw new ServiceException.ServiceNotProvidedException(serviceType.getName());
    }

    public<T extends Service> void registerService(Class<T> serviceType, T service) {
        if (serviceType.isInstance(service)) {
            services.put(serviceType, service);
        }
    }

    public void clearAllRegisterService() {
        services.clear();
    }

}
