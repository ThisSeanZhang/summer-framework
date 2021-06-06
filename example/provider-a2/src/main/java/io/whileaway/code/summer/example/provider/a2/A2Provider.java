package io.whileaway.code.summer.example.provider.a2;

import io.whileaway.code.summer.example.module.a.ModuleA;
import io.whileaway.code.summer.example.provider.a2.config.ProviderConfigA2;
import io.whileaway.code.summer.modular.ModuleConfig;
import io.whileaway.code.summer.modular.ModuleProvider;
import io.whileaway.code.summer.modular.annontion.Provide;

import java.lang.invoke.MethodHandles;

@Provide(ModuleA.class)
public class A2Provider extends ModuleProvider {

    ModuleConfig config = new ProviderConfigA2();

    public A2Provider() {
        super(MethodHandles.lookup());
    }

    @Override
    public ModuleConfig providerConfig() {
        return config;
    }

}
