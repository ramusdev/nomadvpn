package com.rg.nomadvpn.locator;

import java.lang.reflect.InvocationTargetException;

public class ServiceLocatorContext {
    public ServiceLocatorContext() {
    }

    public <T> ServiceBase createService(Class<T> className) {
        ServiceBase serviceBase = null;
        try {
            serviceBase = (ServiceBase) className.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return serviceBase;
    }
}
