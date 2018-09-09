package com.testers;

public class CallGraphs
{
    public static void main(String[] args) {
        doStuff();
    }

    public static void doStuff() {
        new A().foo();
        new B().fooB();
    }
}

class A
{
    public void foo() {
        bar();
    }

    public void bar() {
    }
}
class B{
    public void fooB(){
        barB();
    }
    public void barB(){

    }
}
