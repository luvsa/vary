package org.luvsa.vary.date;

import org.luvsa.vary.TypeSupplier.Types;

import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

/**
 * {@link java.util.Date} 转 {@link java.time.Instant}
 *
 * @author Aglet
 * @create 2022/6/27 17:03
 */
@Types(Instant.class)
public class ToInstant implements DProvider {

    @Override
    public Function<Date, ?> get(Class<?> clazz) {
        return Date::toInstant;
    }
}