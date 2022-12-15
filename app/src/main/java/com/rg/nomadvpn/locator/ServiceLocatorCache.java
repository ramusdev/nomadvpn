package com.rg.nomadvpn.locator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceLocatorCache {
    private List<ServiceBase> services;

    public ServiceLocatorCache() {
        this.services = new ArrayList<>();
    }

    public <T> ServiceBase getService(Class<T> serviceBase) {
        Optional<ServiceBase> optionalService = this.services.stream()
                .filter(e -> e.getClass().hashCode() == serviceBase.hashCode())
                .findFirst();

        System.out.println("Size: " + services.size());

        return optionalService.orElse(null);
    }

    public void addService(ServiceBase service) {
        Optional<ServiceBase> optionalService = this.services.stream()
                .filter(e -> e.getClass().hashCode() == service.getClass().hashCode())
                .findFirst();

        ServiceBase serviceBase = optionalService.orElse(null);
        if (serviceBase == null) {
            this.services.add(service);
        }
    }

    public void getAll() {
        System.out.println(this.services.size());
    }
}
