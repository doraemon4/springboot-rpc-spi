package com.stephen.learning.blade.core;

import com.stephen.learning.blade.core.decoder.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 13:55
 * @Version: 1.0
 */
public class ExtensionLoader<T> {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
    private static final String BLADE_DIRECTORY = "META-INF/blade/";
    private static final String BLADE_INTERNAL_DIRECTORY = BLADE_DIRECTORY + "internal/";
    // =============全局缓存=============
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    // =============实例缓存=============
    private final Class<?> type;
    private volatile ConcurrentHashMap<String, Class<?>> cachedExtensionClasses;
    private volatile ConcurrentHashMap<String, T> cachedExtensionInstace;
    private Set<Class<?>> cachedWrapperClasses;

    public ExtensionLoader(Class<T> type) {
        this.type = type;
        loadExtensionClasses();
    }

    private void loadExtensionClasses() {
        if (cachedExtensionClasses == null) {
            synchronized (type) {
                if (cachedExtensionClasses == null) {
                    cachedExtensionClasses = new ConcurrentHashMap<>();
                    loadFile(BLADE_DIRECTORY);
                    loadFile(BLADE_INTERNAL_DIRECTORY);
                }
            }
        }
    }

    /**
     * 将扩展实现存放到 cachedExtensionClasses<name, class>中
     * @param dir
     */
    private void loadFile(String dir) {
        String fileName = dir + type.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }

            if (urls == null) {
                return; // 此目录下没有扩展文件
            }

            while (urls.hasMoreElements()) {
                java.net.URL url = urls.nextElement();

                try(BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"))){
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        final int ci = line.indexOf('#');
                        if (ci >= 0) {
                            line = line.substring(0, ci);
                        }
                        line = line.trim();
                        if (line.length() <= 0) {
                            continue; // 本行是纯注释
                        }

                        String name = null;
                        int i = line.indexOf('=');
                        if (i > 0) {
                            name = line.substring(0, i).trim();
                            line = line.substring(i + 1).trim();
                        }
                        if (line.length() <= 0) {
                            logger.warn("extention class is blank after '=', description file:" + fileName);
                            continue;
                        }

                        try {
                            Class<?> clazz = Class.forName(line, true, classLoader);
                            if (!type.isAssignableFrom(clazz)) {
                                throw new IllegalStateException("Error when load extension class(interface: " + type + ", class line: " + clazz.getName()
                                        + "), class " + clazz.getName() + "is not subtype of interface.");
                            }

                            try {
                                clazz.getConstructor(type);
                                if (cachedWrapperClasses == null) {
                                    cachedWrapperClasses = new HashSet<>();
                                }
                                cachedWrapperClasses.add(clazz);
                            } catch (NoSuchMethodException e) {
                                clazz.getConstructor();
                                Class<?> c = cachedExtensionClasses.get(name);
                                if (c == null) {
                                    cachedExtensionClasses.put(name, clazz);
                                } else if (c != clazz) {
                                    throw new IllegalStateException("Duplicate extension " + type.getName() + " name " + name + " on " + c.getName() + " and " + clazz.getName());
                                }
                            }


                        } catch (Throwable t) {
                            logger.error("load extension occures error!", t);
                        }
                    }
                } catch (Throwable t) {
                    logger.error("load extension occures error!", t);
                }

            }

        } catch (Throwable t) {
            logger.error("Exception when load extension class(interface: " + type + ", description file: " + fileName + ").", t);
        }
    }

    /**
     * 获取 type 的 ExtensionLoader
     *
     * @param type
     * @return
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null)
            throw new IllegalArgumentException("Extension type == null");
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type(" + type + ") is not interface!");
        }
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

    /**
     * 获取适配的扩展
     *
     * @param name
     * @return
     */
    public T getExtension(String name) {
        if (name == null || name.length() == 0){
            throw new IllegalArgumentException("Extension name == null");
        }

        if(cachedExtensionClasses.get(name) == null){
            throw new IllegalArgumentException("Extension name:" + name + " not exist");
        }

        if(cachedExtensionInstace == null || cachedExtensionInstace.get(name) == null){
            synchronized(type){
                if(cachedExtensionInstace == null || cachedExtensionInstace.get(name) == null){
                    cachedExtensionInstace = cachedExtensionInstace != null ? cachedExtensionInstace : new ConcurrentHashMap<String, T>();
                    try {
                        T instance = (T) cachedExtensionClasses.get(name).newInstance();
                        if(cachedWrapperClasses != null){
                            for(Class<?> clazz : cachedWrapperClasses){
                                instance = (T) clazz.getConstructor(type).newInstance(instance);
                            }
                        }
                        cachedExtensionInstace.put(name, instance);
                    } catch (Throwable t) {
                        throw new IllegalStateException("Extension instance(name: " + name + ", class: " + type + ")  could not be instantiated: " + t.getMessage(), t);
                    }
                }
            }
        }

        return cachedExtensionInstace.get(name);
    }

    public static void main(String[] args) {
        Decoder xml = ExtensionLoader.getExtensionLoader(Decoder.class).getExtension("xml");
        Decoder json = ExtensionLoader.getExtensionLoader(Decoder.class).getExtension("json");
        System.out.println(xml);
        System.out.println(json);
    }

}