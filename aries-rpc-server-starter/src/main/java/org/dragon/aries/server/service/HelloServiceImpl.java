package org.dragon.aries.server.service;

import org.dragon.aries.api.entity.Person;
import org.dragon.aries.api.service.HelloService;
import org.dragon.aries.common.annotation.AriesService;

@AriesService(version = "v0.1")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        int i = 10 / 0;
        return "hello:" + name;
    }

    @Override
    public Person getPerson(String name) {
        return new Person(name, 18);
    }
}
