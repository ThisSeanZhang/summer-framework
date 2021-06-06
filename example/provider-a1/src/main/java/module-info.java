import io.whileaway.code.summer.example.provider.a1.A1Provider;
import io.whileaway.code.summer.example.provider.a1.ModuleAServiceImpl;
import io.whileaway.code.summer.modular.ModuleProvider;
import io.whileaway.code.summer.modular.Service;

module provider.a1 {
    requires summer.modular;
    requires module.a;
    requires lombok;

    provides Service with ModuleAServiceImpl;
    provides ModuleProvider with A1Provider;
}