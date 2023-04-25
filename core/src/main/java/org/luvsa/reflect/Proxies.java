package org.luvsa.reflect;

import org.luvsa.vary.Vary;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * @author Aglet
 * @create 2023/2/6 16:44
 */
public final class Proxies {

    private Proxies() {
        throw new AssertionError("No " + Proxies.class + " instances for you!");
    }

    /**
     * 创建动态代理 的接口对象
     *
     * @param clazz   接口类
     * @param handler 代理 handler
     * @param <T>     接口类
     * @return 接口对象
     */
    public static <T> T of(Class<T> clazz, InvocationHandler handler) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException();
        }
        var o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            var methodName = method.getName();
            if (Objects.equals(methodName, "toString") && args.length == 0) {
                return "Proxy-" + handler.toString();
            }
//            if (Objects.equals(methodName, "hashCode") && args.length == 0) {
//                return handler.hashCode();
//            }
//            if (Objects.equals(methodName, "equals") && args.length == 1) {
//                return handler.invoke(proxy, method, args);
//            }
            return handler.invoke(proxy, method, args);
        });
        return Vary.change(o, clazz);
    }
}