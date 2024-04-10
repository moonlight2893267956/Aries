package org.dragon.aries.server.service;

import org.dragon.aries.api.entity.Person;
import org.dragon.aries.api.service.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello:" + name;
    }

    @Override
    public Person getPerson(String name) {
        return new Person(name, 18);
    }
}
