package net.xicp.tarbitrary.seckill.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class BeanUtil {
    private static final Set<Class> PRIMITIVE_TYPE_WRAPPED_SET = new HashSet<Class>() {
        {
            add(Byte.class);
            add(Short.class);
            add(Integer.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);
            add(Boolean.class);
            add(Character.class);
            add(Void.class);
        }
    };

    public static <T> String bean2String(T value) {
        final Class<?> aClass = value.getClass();
        if (String.class == aClass) {
            return (String) value;
        }

        if (isPrimitiveWrapped(value)) {
            return "" + value;
        }

        return JSON.toJSONString(value);
    }


    private static <T> boolean isPrimitiveWrapped(T t) {
        return isPrimitiveWrapped(t.getClass());
    }

    private static <T> boolean isPrimitiveWrapped(Class<T> t) {

        return PRIMITIVE_TYPE_WRAPPED_SET.contains(t);
    }

    public static <T> T string2Bean(String sResult, Class<T> t) {
        if (sResult == null || sResult.length() <= 0 || t == null) {
            return null;
        }

        if (t == String.class) {
            return (T) sResult;
        }

        if (isPrimitiveWrapped(t)) {
            try {
                final Constructor<T> constructor = t.getConstructor(String.class);
                final Method valueOf = t.getMethod("valueOf", String.class);
                final Object invoke = valueOf.invoke(null, sResult);


                return (T) invoke;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        final JSONObject jsonObject = JSON.parseObject(sResult);

        final T result = JSONObject.toJavaObject(jsonObject, t);

        return result;
    }


}
