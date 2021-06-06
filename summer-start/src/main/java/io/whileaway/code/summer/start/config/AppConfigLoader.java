package io.whileaway.code.summer.start.config;

import io.whileaway.code.summer.modular.AppConfig;
import io.whileaway.code.summer.start.exception.ConfigFileException;
import io.whileaway.code.summer.util.CollectionUtils;
import io.whileaway.code.summer.util.ResourceUtils;
import io.whileaway.code.summer.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.nio.file.ProviderNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AppConfigLoader {

    private static final String DISABLE_SELECTOR = "-";
    private static final String SELECTOR = "selector";

    private final Yaml yaml = new Yaml();

    private final Map<String, String> systemEnv = System.getenv();
    private final Properties properties;

    public AppConfigLoader() {
        properties = System.getProperties();
    }

    public AppConfig load() throws ConfigFileException {
        AppConfig config = new AppConfig();
        this.loadConfig(config);
        return config;
    }

    private void loadConfig(AppConfig config) throws ConfigFileException {
        try {
            Reader applicationReader = ResourceUtils.read("application.yml");
            Map<String, Map<String, Object>> rawConfig = yaml.loadAs(applicationReader, Map.class);
            if (CollectionUtils.isEmpty(rawConfig)) throw new ConfigFileException.ConfigFileEmptyException();
            rawConfig.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, this::selectModuleConfig))
                    .forEach(config::addModule);

        } catch (FileNotFoundException e) {
            throw new ConfigFileException.ConfigFileNotFoundException(e.getMessage(), e);
        }
    }

    private Optional<String> getConfigOverrideValue(String config) {
        Optional<Tuple<String, String>> fileConfig = ConfigurerPlaceholderSymbol.getGlobalKeyAndValueTuple(config);
        if (fileConfig.isEmpty()) return Optional.empty();
        Tuple<String, String>  configTuple = fileConfig.get();

        if (systemEnv.containsKey(configTuple.getKey())) {
            configTuple.setValue(System.getenv().get(configTuple.getKey()));
        }

        if (properties.containsKey(configTuple.getKey())) {
            configTuple.setValue(System.getProperty(configTuple.getKey()));
        }
        return Optional.ofNullable(configTuple.getValue());
    }

    private Map<String, Object> selectModuleConfig(Map.Entry<String, Map<String, Object>> entry) {
        if (CollectionUtils.isEmpty(entry.getValue())) {
            log.info("module [ {} ] without any config", entry.getKey());
            return new HashMap<>();
        }
        Optional<String> selector = this.getConfigOverrideValue(entry.getValue().getOrDefault(SELECTOR, "").toString());
        if (selector.isEmpty() || StringUtils.isEmptyStr(selector.get())) {
            log.info("module [ {} ] selector select empty value", entry.getKey());
            return new HashMap<>();
        }

        if (DISABLE_SELECTOR.equals(selector.get())) {
            log.info("module [ {} ] selector select: [ {} ]", entry.getKey(), DISABLE_SELECTOR);
            return new HashMap<>();
        }

        entry.getValue().computeIfAbsent(selector.get(), key -> {
            throw new ProviderNotFoundException("without provider [ " + key + " ] config found for module [ " + entry.getKey() + " ], " +
                    "if you want to remove it, set the selector to [ - ]");
        });
        return Collections.singletonMap(selector.get(), entry.getValue().get(selector.get()));
    }

}
