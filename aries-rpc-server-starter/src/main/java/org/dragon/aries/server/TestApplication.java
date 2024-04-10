package org.dragon.aries.server;

public class TestApplication {
    public static void main(String[] args) {
        try {
            doFun();
        } catch (RuntimeException e) {
            System.out.println("e => " + e.getMessage());
        }
    }

    private static void doFun() throws RuntimeException {
        try {
            doFun1();
        } catch (Exception e) {
            System.out.println("dddd");
            throw new RuntimeException(e);
        }
    }

    private static void doFun1() throws RuntimeException {
        throw new RuntimeException();
    }
}
