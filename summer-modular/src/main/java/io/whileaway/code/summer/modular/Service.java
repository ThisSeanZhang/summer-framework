package io.whileaway.code.summer.modular;

public interface Service {


    static Service provider() {

        return new Service() {};
    }
}
