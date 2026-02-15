package ru.antush;

import org.junit.jupiter.api.Test;
import ru.antush.entity.Company;

import java.lang.reflect.Proxy;

public class ProxyTest {

    @Test
    void testDynamic() {
        Company company = new Company();
        Proxy.newProxyInstance(company.getClass().getClassLoader(), company.getClass().getInterfaces(),
                (proxy, method, args) -> method.invoke(company, args));
    }
}
