package dk.sdu.mmmi.cbse.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

public class SPILocator {
    @SuppressWarnings("rawtypes")
    private static final Map<Class, ServiceLoader> loadermap = new HashMap<Class, ServiceLoader>();

    private SPILocator() {
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> locateAll(Class<T> service) {
        ServiceLoader<T> loader = loadermap.get(service);

        boolean printStatement = false;

        // Create new loader if not already created
        if (loader == null) {
            loader = ServiceLoader.load(service);
            loadermap.put(service, loader);
            printStatement = true;
        }

        List<T> list = new ArrayList<T>();

        // Add all instances to list
        if (loader != null) {
            try {
                for (T instance : loader) {
                    list.add(instance);
                }
            } catch (ServiceConfigurationError serviceError) {
                serviceError.printStackTrace();
            }
        }

        // Print statement when loader is not null
        if (printStatement) {
            System.out.println("Found " + list.size() + " implementations for interface: " + service.getName());
        }

        return list;
    }
}