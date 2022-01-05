package com.zhy.lib_library.proxy;

/**
 * @author ； ZY
 * @date : 2020/9/24
 * @describe :
 */
public class SimpleStaticProxyDemo {
    public interface IService {
        void sayHello();
    }

    static class RealService implements IService {

        @Override
        public void sayHello() {
            System.out.println("say hello !!");
        }
    }

    static class ProxyService implements IService {
        IService realService;

        public ProxyService(IService realService) {
            this.realService = realService;
        }

        @Override
        public void sayHello() {
            System.out.println("proxy say hello !");
            this.realService.sayHello();
        }
    }

    public static void main(String[] args) {
        IService service = new RealService();
        ProxyService proxyService = new ProxyService(service);
        proxyService.sayHello();
    }

}
