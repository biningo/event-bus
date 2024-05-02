package com.hiwuyue.eventbus.spring.starter;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassLoaderHelper {
    public static List<Class<?>> scanClasses(String[] scanPackages, Class<?> interfaceClass) throws ClassNotFoundException {
        List<Class<?>> callbackClasses = new ArrayList<>();
        for (String scanPackage : scanPackages) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resource = classLoader.getResource(scanPackage.replace('.', File.separatorChar));
            String filePath = resource.getFile();
            File[] files = new File(filePath).listFiles();
            callbackClasses.addAll(loadClassFromFiles(scanPackage, files, interfaceClass));
        }
        return callbackClasses;
    }

    public static List<Class<?>> loadClassFromFiles(String basePackage, File[] files, Class<?> interfaceClass) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (files == null) {
            return classes;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(loadClassFromFiles(basePackage + "." + file.getName(), file.listFiles(), interfaceClass));
                continue;
            }
            if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().lastIndexOf("."));
                Class<?> clazz = Class.forName(basePackage + "." + className);
                if (interfaceClass.isAssignableFrom(clazz)) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
}
