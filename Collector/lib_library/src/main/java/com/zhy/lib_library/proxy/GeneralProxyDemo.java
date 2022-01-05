package com.zhy.lib_library.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author ； ZY
 * @date : 2020/9/25
 * @describe :
 */
public class GeneralProxyDemo {
    interface IServiceA {
        void run();
    }

    interface IServiceB {
        void fly();
    }

    static class ServiceAImpl implements IServiceA {

        @Override
        public void run() {
            System.out.println("run !");
        }
    }

    static class ServiceBImpl implements IServiceB {

        @Override
        public void fly() {
            System.out.println("fly !");
        }
    }

    static class SimpleInvocationHandle implements InvocationHandler {
        Object realService;

        public SimpleInvocationHandle(Object realService) {
            this.realService = realService;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            System.out.println("invocation start !");
            Object result = method.invoke(realService, objects);
            System.out.println("invocation end !");
            return result;
        }
    }

    private static <T> T getProxy(Class<T> intf, T obj) {
        return (T) Proxy.newProxyInstance(intf.getClassLoader(), new Class<?>[]{intf}, new SimpleInvocationHandle(obj));
    }

    public static void main(String[] args) {
        IServiceA serviceAImpl = new ServiceAImpl();
        IServiceA serviceAProxy = getProxy(IServiceA.class, serviceAImpl);
        serviceAProxy.run();

        IServiceB serviceBImpl = new ServiceBImpl();
        IServiceB serviceBProxy = getProxy(IServiceB.class, serviceBImpl);
        serviceBProxy.fly();
    }

}
