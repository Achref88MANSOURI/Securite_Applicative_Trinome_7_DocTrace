
package com.doctrace.api;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.HttpMethod;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RestSecurityArchitectureTest {

    private static final String PACKAGE_NAME = "com.doctrace.api";

    @Test
    void everyRestEndpointMustHaveASecurityPolicy() throws Exception {

        Set<Class<?>> resourceClasses = findClasses();

        assertFalse(resourceClasses.isEmpty(),
                "No REST resource classes were discovered.");

        int endpointCount = 0;

        for (Class<?> resource : resourceClasses) {

            if (!resource.isAnnotationPresent(
                    jakarta.ws.rs.Path.class)) {
                continue;
            }

            boolean classHasPolicy = hasSecurityPolicy(resource);

            for (Method method : resource.getMethods()) {

                if (!isHttpEndpoint(method)) {
                    continue;
                }

                endpointCount++;

                boolean methodHasPolicy =
                        hasSecurityPolicy(method);

                assertTrue(
                        classHasPolicy || methodHasPolicy,
                        "Missing security policy on endpoint: "
                                + resource.getName()
                                + "#"
                                + method.getName()
                );
            }
        }

        assertTrue(endpointCount > 0,
                "No REST endpoints were discovered.");
    }

    private Set<Class<?>> findClasses() throws Exception {

        Set<Class<?>> classes = new HashSet<>();

        String packagePath = PACKAGE_NAME.replace('.', '/');

        ClassLoader loader =
                Thread.currentThread().getContextClassLoader();

        var resources = loader.getResources(packagePath);

        while (resources.hasMoreElements()) {

            URL resource = resources.nextElement();

            if (!resource.getProtocol().equals("file")) {
                continue;
            }

            Path directory = Path.of(resource.toURI());

            try (Stream<Path> paths = Files.walk(directory)) {

                paths.filter(path ->
                                path.toString().endsWith(".class"))
                        .filter(path ->
                                !path.getFileName().toString()
                                        .equals("package-info.class"))
                        .filter(path ->
                                !path.getFileName().toString()
                                        .equals("module-info.class"))
                        .forEach(path -> {

                            String relative = directory
                                    .relativize(path)
                                    .toString();

                            String className = PACKAGE_NAME + "."
                                    + relative
                                    .substring(0, relative.length() - 6)
                                    .replace(File.separatorChar, '.');

                            try {
                                classes.add(Class.forName(
                                        className,
                                        false,
                                        loader
                                ));
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        }

        return classes;
    }

    private boolean isHttpEndpoint(Method method) {

        for (Annotation annotation : method.getAnnotations()) {

            if (annotation.annotationType()
                    .isAnnotationPresent(HttpMethod.class)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasSecurityPolicy(
            java.lang.reflect.AnnotatedElement element) {

        return element.isAnnotationPresent(RolesAllowed.class)
                || element.isAnnotationPresent(DenyAll.class)
                || element.isAnnotationPresent(PermitAll.class);
    }
}
