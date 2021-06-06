import io.whileaway.code.summer.example.provider.a2.A2Provider;
import io.whileaway.code.summer.example.provider.a2.ModuleA2ServiceImpl;
import io.whileaway.code.summer.modular.ModuleProvider;
import io.whileaway.code.summer.modular.Service;

module provider.a2 {
    requires summer.modular;
    requires module.a;
    requires lombok;


    provides ModuleProvider with A2Provider;
    provides Service with ModuleA2ServiceImpl;
}