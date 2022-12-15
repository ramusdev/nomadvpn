package com.rg.nomadvpn.locator;

import android.util.Log;

import com.rg.nomadvpn.MainActivity;

public class ServiceLocator {
    public static ServiceLocatorCache serviceLocatorCache = new ServiceLocatorCache();

    public static <T> ServiceBase getService(Class<T> className) {
        ServiceBase service = serviceLocatorCache.getService(className);

        if (service != null) {
            return service;
        }

        ServiceLocatorContext serviceLocatorContext = new ServiceLocatorContext();
        ServiceBase serviceBase = serviceLocatorContext.createService(className);
        serviceLocatorCache.addService(serviceBase);

        return serviceBase;
    }
}
