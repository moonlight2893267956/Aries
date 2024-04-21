package org.dragon.aries.server.service;

import org.dragon.aries.api.entity.Person;
import org.dragon.aries.api.service.HelloService;
import org.dragon.aries.common.annotation.AriesService;

@AriesService(version = "v0.1")
public class HelloServiceImpl implements HelloService {
    private static int count = 0;
    @Override
    public String sayHello(String name) {
        try {
            if (count % 2 == 0) {
                int i = 10 / 0;
            } else {
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            count ++;
        }
        return "hello:" + name;
    }

    @Override
    public Person getPerson(String name) {
        return new Person(name, 18);
    }
}
