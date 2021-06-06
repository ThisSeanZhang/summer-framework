package io.whileaway.code.summer.example.provider.a2;

import io.whileaway.code.summer.example.module.a.services.ModuleAService;
import io.whileaway.code.summer.example.provider.a2.config.ProviderConfigA2;

public class ModuleA2ServiceImpl implements ModuleAService {

    private ProviderConfigA2 config;

    @Override
    public void startAService() {
        System.out.println("module A2 config name: " + config.getName());
    }

    @Override
    public Integer doSomeWork() {
        return 66;
    }
}
