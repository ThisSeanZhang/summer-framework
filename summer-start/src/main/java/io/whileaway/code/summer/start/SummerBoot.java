package io.whileaway.code.summer.start;

import io.whileaway.code.summer.modular.AppConfig;
import io.whileaway.code.summer.modular.ModuleManager;
import io.whileaway.code.summer.start.config.AppConfigLoader;
import io.whileaway.code.summer.util.BootPathUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class SummerBoot {


    public static void begin(String[] args) {
        AppConfigLoader applicationConfigLoader = new AppConfigLoader();
        ModuleManager manager = new ModuleManager();
        try {
            File path = BootPathUtil.getPath(SummerBoot.class);
            manager.setRuntimePath(path.toPath());
            AppConfig config = applicationConfigLoader.load();
            manager.init(config);

        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            System.exit(1);
        }
    }
}
