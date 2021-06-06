import io.whileaway.code.summer.example.module.b.services.ModuleBService;
import io.whileaway.code.summer.example.provider.b.BProvider;
import io.whileaway.code.summer.example.provider.b.ModuleBServiceImpl;
import io.whileaway.code.summer.modular.ModuleProvider;
import io.whileaway.code.summer.modular.Service;

module provider.b {

    requires lombok;
    requires module.b;
    requires module.a;
    requires summer.modular;

    provides ModuleBService with ModuleBServiceImpl;
    provides ModuleProvider with BProvider;
    provides Service with ModuleBServiceImpl;
}