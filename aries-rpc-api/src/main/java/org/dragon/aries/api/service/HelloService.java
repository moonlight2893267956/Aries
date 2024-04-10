package org.dragon.aries.api.service;

import org.dragon.aries.api.entity.Person;

public interface HelloService {
    String sayHello(String name);
    Person getPerson(String name);
}
