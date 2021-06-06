package io.whileaway.code.summer.modular;

import io.whileaway.code.summer.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class AppConfig {
    private Map<String, ModuleConfig> modules = new HashMap<>();

    public List<String> moduleList() {
        return new LinkedList<>(modules.keySet());
    }

    public ModuleConfig addModule(String moduleName) {
        ModuleConfig newModule = new ModuleConfig();
        modules.put(moduleName, newModule);
        return newModule;
    }

    public void addModule(String key, Map<String,Object> providerConfig) {
        if (CollectionUtils.isEmpty(providerConfig)) return;
        modules.put(key, new ModuleConfig(providerConfig));
    }

    public boolean hasModuleConfig(String moduleName) {
        return modules.containsKey(moduleName);
    }

    public boolean hasModuleConfig(ModuleDefine module) {
        return modules.containsKey(module.name());
    }

    public ModuleConfig getModuleConfig(String moduleName) {
        return modules.get(moduleName);
    }

    /**
     * Module Config
     */
    public static class ModuleConfig {
        private final Map<String, ProviderConfig> providers = new HashMap<>();

        private ModuleConfig() {
        }

        private ModuleConfig(Map<String, Object> moduleConfig) {
            if (CollectionUtils.isEmpty(moduleConfig)) return;
            moduleConfig.forEach((key, value) -> {
                log.info("Get a provider define, provider name: {}", key);
                providers.put(key, new ProviderConfig(value));
            });

        }

        public Properties getProviderConfig(String providerName) {
            return providers.get(providerName).getProperties();
        }

        public boolean hasProviderConfig(String providerName) {
            return providers.containsKey(providerName);
        }

        public ModuleConfig addProviderConfig(String name, Properties properties) {
            ProviderConfig newProvider = new ProviderConfig(properties);
            providers.put(name, newProvider);
            return this;
        }
    }


    /**
     * Provider Config
     */
    public static class ProviderConfig {
        private final Properties properties;

        ProviderConfig(Object providerConfig) {
            this.properties = new Properties();
            if (providerConfig instanceof Map) {
                Map config = (Map) providerConfig;
                if (CollectionUtils.isEmpty(config)) return;
                config.forEach((key, value) -> {
                    if (value instanceof Map) {
                        Map prop = (Map) value;
                        var subProperties = new Properties();
                        prop.forEach(subProperties::put);
                        properties.put(key, subProperties);
                    } else
                        properties.put(key, value);
                });
            }
        }

        ProviderConfig(Properties properties) {
            this.properties = properties;
        }

        private Properties getProperties() {
            return properties;
        }
    }
}
