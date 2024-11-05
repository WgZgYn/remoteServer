package org.scu301.remoteserver.event;

import java.lang.reflect.InvocationTargetException;

public class MqttEventFactory {
    private static final String packagePrefix = Event.class.getPackageName() + ".events";

    public static Event createEvent(String className, Object object, String source, String event) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println(packagePrefix);
        System.out.println(packagePrefix + "." + className);
        Class<?> eventClass = Class.forName(packagePrefix + "." + className);
        return (Event) eventClass.getDeclaredConstructor(Object.class, String.class, String.class).newInstance(object, source, event);
    }
}
