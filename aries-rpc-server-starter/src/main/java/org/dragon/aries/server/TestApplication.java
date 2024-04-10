package org.dragon.aries.server;

import org.dragon.aries.api.entity.Person;

public class TestApplication {
    public static void main(String[] args) throws NoSuchMethodException {
        System.out.println(TestApplication.class.getDeclaredMethod("doFun").getReturnType().equals(String.class));
    }

    private static Object doFun() throws RuntimeException {
        try {
            doFun1();
        } catch (Exception e) {
            System.out.println("dddd");
            throw new RuntimeException(e);
        }
        return null;
    }

    private static void doFun1() throws RuntimeException {
        throw new RuntimeException();
    }
}
