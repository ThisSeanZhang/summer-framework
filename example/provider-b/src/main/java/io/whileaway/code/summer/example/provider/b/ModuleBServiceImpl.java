package io.whileaway.code.summer.example.provider.b;

import io.whileaway.code.summer.example.module.a.services.ModuleAService;
import io.whileaway.code.summer.example.module.b.services.ModuleBService;
import io.whileaway.code.summer.example.provider.b.config.ProviderConfigB;
import io.whileaway.code.summer.modular.annontion.Inject;

public class ModuleBServiceImpl implements ModuleBService {

    private ProviderConfigB config;

    @Inject
    private ModuleAService aService;


    @Override
    public void startBService() {
        aService.startAService();
        System.out.println("B config: " + config.getName());
        System.out.println("in B Service get A service number: " + aService.doSomeWork());
    }
}
