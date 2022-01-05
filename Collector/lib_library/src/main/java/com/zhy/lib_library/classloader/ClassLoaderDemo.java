package com.zhy.lib_library.classloader;

/**
 * @author ； ZY
 * @date : 2020/9/27
 * @describe :
 */
public class ClassLoaderDemo {

    public static void main(String[] args) {
        ClassLoader cls = ClassLoaderDemo.class.getClassLoader();
        while (null != cls) {
            System.out.println(cls.getClass().getName());
            cls = cls.getParent();
        }

        System.out.println(String.class.getClassLoader());
    }
}
