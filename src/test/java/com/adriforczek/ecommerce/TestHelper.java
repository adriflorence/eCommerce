package com.adriforczek.ecommerce;

import java.lang.reflect.Field;

public class TestHelper {

    public static void injectObject(Object target, String fieldName, Object toInject) {

        boolean isPrivate = false;
        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);
            if(!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
                isPrivate = true;
            }
            declaredField.set(target, toInject);

            if(isPrivate) {
                declaredField.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
