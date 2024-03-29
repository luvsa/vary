package org.luvsa.vary.other;

import org.luvsa.lang.Strings;
import org.luvsa.vary.TypeSupplier.Types;
import org.luvsa.vary.Vary;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * {@link Object} 转换成 {@link Map<>}
 *
 * @author Aglet
 * @create 2022/6/28 11:16
 */
@Types(Map.class)
public class ToMap implements Provider, Function<Object, Map<String, Object>> {

    private final Map<Class<? extends Annotation>, BiFunction<Class<?>, Object, Map<String, Object>>> map = Map.of(SupportIob.class, (clazz, o) -> {
        var map = new HashMap<String, Object>();
        var methods = clazz.getDeclaredMethods();
        for (var method : methods) {
            var iob = method.getAnnotation(Iob.class);
            if (iob == null || !iob.show()) {
                continue;
            }
            var alias = iob.alias();
            var value = iob.value();
            var name = alias.isBlank() ? Util.refer(method.getName()) : alias;
            try {
                var invoke = method.invoke(o);
                map.put(name, Map.of("name", value, "value", realize(invoke)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                // zhi
            }
        }
        return map;
    }, SupportField.class, (clazz, o) -> {
        var map = new HashMap<String, Object>();
        var fields = clazz.getDeclaredFields();
        for (var field : fields) {
            var name = field.getName();
            var methodName = "get" + Strings.capitalize(name);
            try {
                var method = clazz.getDeclaredMethod(methodName);
                var invoke = method.invoke(o);
                map.put(name, realize(invoke));
            } catch (Exception e) {
                //
            }
        }
        return map;
    });

    private Object realize(Object invoke) {
        if (invoke instanceof List<?> list) {
            var maps = new ArrayList<>();
            for (var o : list) {
                maps.add(Vary.change(o, Map.class));
            }
            return maps;
        }
        return Vary.realize(invoke, Map.class);
    }

    @Override
    public Function<Object, ?> get(Type type) {
        return this;
    }

    @Override
    public Map<String, Object> apply(Object o) {
        // 推断
        var aClass = infer(o);
        var apply = apply(aClass, o);
        if (apply == null) {
            var interfaces = aClass.getInterfaces();
            for (Class<?> item : interfaces) {
                var result = apply(item, o);
                if (result != null) {
                    return result;
                }
            }
            throw new UnsupportedOperationException("不支持将数据【" + aClass + " ： " + o + "】 转换成 Map！提示： 您似乎漏了注解");
        }
        return apply;
    }

    private Map<String, Object> apply(Class<?> aClass, Object o) {
        if (aClass == null) {
            return null;
        }
        var entries = map.entrySet();
        for (var entry : entries) {
            var key = entry.getKey();
            if (aClass.isAnnotationPresent(key)) {
                return entry.getValue().apply(aClass, o);
            }
        }
        return apply(aClass.getSuperclass(), o);
    }

    private Class<?> infer(Object o) {
        var aClass = o.getClass();
        if (Proxy.class.isAssignableFrom(aClass)) {
            var interfaces = aClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                return anInterface;
            }
            return aClass.getSuperclass();
        }
        return aClass;
    }


}
