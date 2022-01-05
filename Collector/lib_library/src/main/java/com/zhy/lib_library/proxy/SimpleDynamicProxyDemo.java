package com.zhy.lib_library.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author ； ZY
 * @date : 2020/9/24
 * @describe :
 */
public class SimpleDynamicProxyDemo {
    static interface IService {
        void sayHello();
    }

    static class RealService implements IService {

        @Override
        public void sayHello() {
            System.out.println("hello !");
        }
    }

    static class SimpleInvocationHandler implements InvocationHandler {
        private Object realObj;

        public SimpleInvocationHandler(Object realObj) {
            this.realObj = realObj;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            System.out.println("entering :" + method.getName());
            Object result = method.invoke(realObj, objects);
            System.out.println("leaving :" + method.getName());
            return result;
        }
    }

    public static void main(String[] args) {
        IService realService = new RealService();
       /* IService proxyService = (IService) Proxy.newProxyInstance(IService.class.getClassLoader(), new Class[]{IService.class},
                new SimpleInvocationHandler(realService));*/
        try {
        Class<?> proxyCls = Proxy.getProxyClass(IService.class.getClassLoader(), IService.class);
        Constructor<?> ctor = proxyCls.getConstructor(InvocationHandler.class);
        InvocationHandler handler = new SimpleInvocationHandler(realService);
        IService proxyService = (IService) ctor.newInstance(handler);
        proxyService.sayHello();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
