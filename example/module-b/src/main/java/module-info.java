import io.whileaway.code.summer.example.module.b.ModuleB;
import io.whileaway.code.summer.modular.ModuleDefine;

module module.b {
    requires summer.modular;

    exports io.whileaway.code.summer.example.module.b.services;
    exports io.whileaway.code.summer.example.module.b;
    provides ModuleDefine with ModuleB;
}