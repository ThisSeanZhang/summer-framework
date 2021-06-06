package io.whileaway.code.summer.example.provider.b;

import io.whileaway.code.summer.example.module.a.ModuleA;
import io.whileaway.code.summer.example.module.b.ModuleB;
import io.whileaway.code.summer.example.module.b.services.ModuleBService;
import io.whileaway.code.summer.example.provider.b.config.ProviderConfigB;
import io.whileaway.code.summer.modular.ModuleConfig;
import io.whileaway.code.summer.modular.ModuleProvider;
import io.whileaway.code.summer.modular.annontion.Provide;
import io.whileaway.code.summer.modular.annontion.Require;
import io.whileaway.code.summer.modular.exception.ServiceException;

import java.lang.invoke.MethodHandles;

@Provide(ModuleB.class)
@Require(moduleName = {ModuleA.class})
public class BProvider extends ModuleProvider {

    ModuleConfig config = new ProviderConfigB();

    public BProvider() {
        super(MethodHandles.lookup());
    }

    @Override
    public ModuleConfig providerConfig() {
        return config;
    }

    @Override
    public void start() throws ServiceException {
        getService(ModuleBService.class).startBService();
    }
}
