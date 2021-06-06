package io.whileaway.code.summer.example.provider.a1;

import io.whileaway.code.summer.example.module.a.services.ModuleAService;
import io.whileaway.code.summer.example.provider.a1.config.ProviderConfigA;

public class ModuleAServiceImpl implements ModuleAService {

    private ProviderConfigA config;

    @Override
    public void startAService() {
        System.out.println("module A config name: " + config.getName());
    }

    @Override
    public Integer doSomeWork() {
        return 42;
    }
}
