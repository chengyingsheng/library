package com.ziroom.bsrd.basic;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author linmin
 * @date 2009-12-4
 */
public class BeanHelper {
    protected static final Object[] NULL_ARGUMENTS = {};
    private static Map<String, ReflectionInfo> cache = new HashMap<String, ReflectionInfo>();
    private static BeanHelper bhelp = new BeanHelper();
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    private BeanHelper() {
    }

    public static BeanHelper getInstance() {
        return bhelp;
    }

    public static List<String> getPropertys(Object bean) {
        return Arrays.asList(getInstance().getPropertiesAry(bean));

    }

    public static Object getProperty(Object bean, String propertyName) {

        try {
            Method method = getInstance().getMethod(bean, propertyName, false);
            if (propertyName != null && method == null) {
                return null;
            } else if (method == null) {
                return null;
            }
            return method.invoke(bean, NULL_ARGUMENTS);
        } catch (Exception e) {
            String errStr = "Failed to get property: " + propertyName;
            // Logger.warn(errStr, e);
            throw new RuntimeException(errStr, e);
        }
    }

    @Deprecated
    public static Method getMethod(Object bean, String propertyName) {
        return getInstance().getMethod(bean, propertyName, true);
    }

    public static Method getWriteMethod(Object bean, String propertyName) {
        return getInstance().getMethod(bean, propertyName, true);
    }

    public static Method getReadMethod(Object bean, String propertyName) {
        return getInstance().getMethod(bean, propertyName, false);
    }

    public static void invokeMethod(Object bean, Method method, Object value) {
        try {
            if (method == null)
                return;
            Object[] arguments = {value};
            method.invoke(bean, arguments);
        } catch (Exception e) {
            String errStr = "Failed to set property: " + method.getName();
            throw new RuntimeException(errStr, e);
        }
    }

    public static void setProperty(Object bean, String propertyName,
                                   Object value) {
        try {
            Method method = getInstance().getMethod(bean, propertyName, true);
            if (propertyName != null && method == null) {
                return;
            } else if (method == null) {
                return;
            }
//			Object obj = getProperty(bean, propertyName);
//			if(obj!=null){
//				if(StringUtils.isNotEmpty(obj.toString())){
//					return ;
//				}
//			}
//			String name = method.getParameterTypes()[0].getName();
//			System.out.println(propertyName+"----- :"+name);
            Object[] arguments = {coventer(method, value)};
            method.invoke(bean, arguments);
        } catch (Exception e) {
            String errStr = "Failed to set property: " + propertyName
                    + " on bean: " + bean.getClass().getName() + " with value:"
                    + value;
            throw new RuntimeException(errStr, e);
        }
    }

    private static Object coventer(Method method, Object value1) {
        Object value = null;
//        if (method.getParameterTypes()[0].equals(UFBoolean.class)) {
//            value = getValue("N", value1);
//            return UFBoolean.valueOf(value.toString());
//        }
//        if (method.getParameterTypes()[0].equals(UFDate.class)) {
//            value = getValue(TimeToolkit.getCurrentDate(), value1);
//            return UFDate.getDate(value.toString());
//        }
//        if (method.getParameterTypes()[0].equals(UFDateTime.class)) {
//            value = getValue(TimeToolkit.getCurrentDate(), value1);
//            return new UFDateTime(value.toString());
//        }
//        if (method.getParameterTypes()[0].equals(UFDouble.class)) {
//            value = getValue(UFDouble.ZERO_DBL, value1);
//            return new UFDouble(value.toString());
//        }
        if (method.getParameterTypes()[0].equals(Integer.class)) {
            value = getValue(Integer.valueOf(0), value1);
            return Integer.valueOf(value.toString());
        }
        if (method.getParameterTypes()[0].equals(String.class)) {
            value = getValue("", value1);
            return value.toString();
        }
        if (method.getParameterTypes()[0].getName().equals("int")) {
            value = getValue("0", value1);
            return Integer.valueOf(value1.toString()).intValue();
        }
        if (method.getParameterTypes()[0].getName().equals("long")) {
            value = getValue("0", value1);
            return Long.valueOf(value1.toString()).longValue();
        }
        return null;
    }

    private static Object getValue(Object defalutvalue, Object value) {
        if (value == null) {
            value = defalutvalue;
        }

        return value;
    }

    public String[] getPropertiesAry(Object bean) {
        ReflectionInfo reflectionInfo = null;
        rwl.readLock().lock();
        try {
            reflectionInfo = cachedReflectionInfo(bean.getClass());
            Set<String> propertys = new HashSet<String>();
            for (String key : reflectionInfo.writeMap.keySet()) {
                if (reflectionInfo.writeMap.get(key) != null) {
//					propertys.add(key.toLowerCase());
                    propertys.add(key);
                }
            }
            return propertys.toArray(new String[0]);
        } finally {
            rwl.readLock().unlock();
        }
    }

    private Method getMethod(Object bean, String propertyName,
                             boolean isSetMethod) {
        Method method = null;
        rwl.readLock().lock();
        ReflectionInfo reflectionInfo = null;
        try {
            reflectionInfo = cachedReflectionInfo(bean.getClass());
            if (isSetMethod) {
                method = reflectionInfo.getWriteMethod(propertyName);
            } else {
                method = reflectionInfo.getReadMethod(propertyName);
            }
            return method;
        } finally {
            rwl.readLock().unlock();
        }
    }

    private ReflectionInfo cachedReflectionInfo(Class<?> beanCls) {
        return cacheReflectionInfo(beanCls, null);
    }

    private ReflectionInfo cacheReflectionInfo(Class<?> beanCls,
                                               List<PropDescriptor> pdescriptor) {
        String key = beanCls.getName();
        ReflectionInfo reflectionInfo = cache.get(key);
        if (reflectionInfo == null) {
            rwl.readLock().unlock();
            rwl.writeLock().lock();
            rwl.readLock().lock();
            try {
                reflectionInfo = cache.get(key);
                if (reflectionInfo == null) {
                    reflectionInfo = new ReflectionInfo();
                    List<PropDescriptor> propDesc = new ArrayList<PropDescriptor>();
                    if (pdescriptor != null) {
                        propDesc.addAll(pdescriptor);
                    } else {
                        propDesc = getPropertyDescriptors(beanCls);
                    }
                    for (PropDescriptor pd : propDesc) {
                        Method readMethod = pd.getReadMethod(beanCls);
                        Method writeMethod = pd.getWriteMethod(beanCls);
                        if (readMethod != null)
                            reflectionInfo.readMap.put(pd.getName(), readMethod); // store as
                        // lower
                        // case
                        if (writeMethod != null)
                            reflectionInfo.writeMap.put(pd.getName(), writeMethod);
                    }
                    cache.put(key, reflectionInfo);
                }
            } finally {
                rwl.writeLock().unlock();
            }
        }
        return reflectionInfo;

    }

    /*
     * 返回所有get的方法
     */
    public Method[] getAllGetMethod(Class<?> beanCls, String[] fieldNames) {

        Method[] methods = null;
        ReflectionInfo reflectionInfo = null;
        List<Method> al = new ArrayList<Method>();
        rwl.readLock().lock();
        try {
            reflectionInfo = cachedReflectionInfo(beanCls);
        } finally {
            rwl.readLock().unlock();
        }
        for (String str : fieldNames) {
            al.add(reflectionInfo.getReadMethod(str));
        }
        methods = al.toArray(new Method[al.size()]);
        return methods;
    }

    private List<PropDescriptor> getPropertyDescriptors(Class<?> clazz) {
        List<PropDescriptor> descList = new ArrayList<PropDescriptor>();
        List<PropDescriptor> superDescList = new ArrayList<PropDescriptor>();
        List<String> propsList = new ArrayList<String>();
        Class<?> propType = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().length() < 4) {
                continue;
            }
            if (method.getName().charAt(3) < 'A'
                    || method.getName().charAt(3) > 'Z') {
                continue;
            }
            if (method.getName().startsWith("set")) {
                if (method.getParameterTypes().length != 1) {
                    continue;
                }
                if (method.getReturnType() != void.class) {
                    continue;
                }
                propType = method.getParameterTypes()[0];
            } else if (method.getName().startsWith("get")) {
                if (method.getParameterTypes().length != 0) {
                    continue;
                }
                propType = method.getReturnType();
            } else {
                continue;
            }
            String propname = method.getName().substring(3, 4).toLowerCase();
            if (method.getName().length() > 4) {
                propname = propname + method.getName().substring(4);
            }
            if (propname.equals("class")) {
                continue;
            }
            if (propsList.contains(propname)) {
                continue;
            } else {
                propsList.add(propname);
            }
            descList.add(new PropDescriptor(clazz, propType, propname));
        }

        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            superDescList = getPropertyDescriptors(superClazz);
            descList.addAll(superDescList);
            if (!isBeanCached(superClazz)) {
                cacheReflectionInfo(superClazz, superDescList);
            }
        }
        return descList;
    }

    private boolean isBeanCached(Class<?> bean) {
        String key = bean.getName();
        ReflectionInfo cMethod = cache.get(key);
        if (cMethod == null) {
            rwl.readLock().lock();
            try {
                cMethod = cache.get(key);
                if (cMethod == null) {
                    return false;
                }
            } finally {
                rwl.readLock().unlock();
            }
        }
        return true;
    }

    static class ReflectionInfo {
        /**
         * all stored as lowercase
         */
        Map<String, Method> readMap = new HashMap<String, Method>();

        /**
         * all stored as lowercase
         */
        Map<String, Method> writeMap = new HashMap<String, Method>();

        Method getReadMethod(String prop) {
            return prop == null ? null : readMap.get(prop);
        }

        Method getWriteMethod(String prop) {
            return prop == null ? null : writeMap.get(prop);
        }
    }
}

class PropDescriptor {
    private Class<?> beanType;

    private Class<?> propType;

    private String name;

    private String baseName;

    public PropDescriptor(Class<?> beanType, Class<?> propType, String propName) {
        if (beanType == null) {
            throw new IllegalArgumentException("Bean Class can not be null!");
        }
        if (propName == null) {
            throw new IllegalArgumentException(
                    "Bean Property name can not be null!");
        }
        this.propType = propType;
        this.beanType = beanType; // in which this property is declared.
        this.name = propName;
        this.baseName = changeFirstCharacterCase(true, propName);
    }

    private static String changeFirstCharacterCase(boolean capitalize, String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        StringBuffer buf = new StringBuffer(strLen);
        if (capitalize) {
            buf.append(Character.toUpperCase(str.charAt(0)));
        } else {
            buf.append(Character.toLowerCase(str.charAt(0)));
        }
        buf.append(str.substring(1));
        return buf.toString();
    }

    /**
     * currBean my override get and set.
     */
    public synchronized Method getReadMethod(Class<?> currBean) {
        Method readMethod;
        String readMethodName = null;
        if (propType == boolean.class || propType == null) {
            readMethodName = "is" + baseName;
        } else {
            readMethodName = "get" + baseName;
        }
        Class<?> classStart = currBean;
        if (classStart == null) {
            classStart = beanType;
        }
        readMethod = findMemberMethod(classStart, readMethodName, 0, null);
        if (readMethod == null && readMethodName.startsWith("is")) {
            readMethodName = "get" + baseName;
            readMethod = findMemberMethod(classStart, readMethodName, 0, null);
        }
        if (readMethod != null) {
            int mf = readMethod.getModifiers();
            if (!Modifier.isPublic(mf)) {
                return null;
            }
            Class<?> retType = readMethod.getReturnType();
            if (!propType.isAssignableFrom(retType)) {

            }
        }
        return readMethod;
    }

    public synchronized Method getWriteMethod(Class<?> currBean) {
        Method writeMethod;
        String writeMethodName = null;
        if (propType == null) {
            propType = findPropertyType(getReadMethod(currBean), null);
        }
        if (writeMethodName == null) {
            writeMethodName = "set" + baseName;
        }
        Class<?> classStart = currBean;
        if (classStart == null) {
            classStart = beanType;
        }
        writeMethod = findMemberMethod(classStart, writeMethodName, 1,
                (propType == null) ? null : new Class[]{propType});
        if (writeMethod != null) {
            int mf = writeMethod.getModifiers();
            if (!Modifier.isPublic(mf) || Modifier.isStatic(mf)) {
                writeMethod = null;
            }
        }
        return writeMethod;
    }

    private Class<?> findPropertyType(Method readMethod, Method writeMethod) {
        Class<?> propertyType = null;
        if (readMethod != null) {
            propertyType = readMethod.getReturnType();
        }
        if (propertyType == null && writeMethod != null) {
            Class<?> params[] = writeMethod.getParameterTypes();
            propertyType = params[0];
        }
        return propertyType;
    }

    private Method findMemberMethod(Class<?> beanClass, String mName, int num,
                                    Class<?>[] args) {
        Method foundM = null;
        Method[] methods = beanClass.getDeclaredMethods();
        if (methods.length < 0) {
            return null;
        }
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(mName)) {
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == num) {
                    boolean match = true;
                    for (int i = 0; i < num; i++) {
                        // parameter should be compatible with prop type
                        if (!args[i].isAssignableFrom(paramTypes[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        foundM = method;
                        break;
                    }
                }
            }
        }
        // recursively find super
        if (foundM == null) {
            if (beanClass.getSuperclass() != null) {
                foundM = findMemberMethod(beanClass.getSuperclass(), mName,
                        num, args);
            }
        }
        return foundM;
    }

    public String getName() {
        return name;
    }
}
