package org.luvsa.vary.date;

import org.luvsa.vary.TypeSupplier.Types;

import java.util.Date;
import java.util.function.Function;

/**
 * {@link java.util.Date} 转 {@link Long 时间戳}
 *
 * @author Aglet
 * @create 2022/6/27 17:02
 */
@Types(Long.class)
public class ToLong implements DProvider {

    @Override
    public Function<Date, ?> get(Class<?> clazz) {
        return Date::getTime;
    }

}
