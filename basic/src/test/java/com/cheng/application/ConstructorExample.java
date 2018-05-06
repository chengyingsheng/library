package com.cheng.application;

class Foo {
    int i;

    Foo() {
        i = 1;
        int x = getValue();
        System.out.println(x);
    }

    protected int getValue() {
        return i;
    }
}

class Bar extends Foo {
    int j;

    Bar() {
        j = 2;
    }

    @Override
    protected int getValue() {
        return j;
    }
}

public class ConstructorExample {
    public static void main(String... args) {
        Bar bar = new Bar();
    }
}