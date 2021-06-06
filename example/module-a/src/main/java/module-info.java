import io.whileaway.code.summer.example.module.a.ModuleA;
import io.whileaway.code.summer.modular.ModuleDefine;

module module.a {
    requires summer.modular;

    exports io.whileaway.code.summer.example.module.a.services;
    exports io.whileaway.code.summer.example.module.a;
    provides ModuleDefine with ModuleA;

}