package org.dragon.aries.client.service;

import org.dragon.aries.api.entity.Person;
import org.dragon.aries.api.service.HelloService;
import org.dragon.aries.common.annotation.AriesComponent;
import org.dragon.aries.common.annotation.AriesReference;

@AriesComponent
public class RpcClientService {
    @AriesReference(version = "v0.1", retry = 1, retryInterval = 2000, timeout = 2000, callback = "对不起，调用失败")
    private HelloService helloService;

    public void remoteProxy() {
        System.out.println(helloService.sayHello("wuxiangyi"));
        Person person = helloService.getPerson("wuxiangyi");
        System.out.println(person);
    }
}
