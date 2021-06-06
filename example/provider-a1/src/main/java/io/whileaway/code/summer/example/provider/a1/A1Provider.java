package io.whileaway.code.summer.example.provider.a1;

import io.whileaway.code.summer.example.module.a.ModuleA;
import io.whileaway.code.summer.example.provider.a1.config.ProviderConfigA;
import io.whileaway.code.summer.modular.ModuleConfig;
import io.whileaway.code.summer.modular.ModuleProvider;
import io.whileaway.code.summer.modular.annontion.Provide;

import java.lang.invoke.MethodHandles;

@Provide(ModuleA.class)
public class A1Provider extends ModuleProvider {

    ModuleConfig config = new ProviderConfigA();

    public A1Provider() {
        super(MethodHandles.lookup());
    }

    @Override
    public ModuleConfig providerConfig() {
        return config;
    }

}
