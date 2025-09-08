package com.github.tukenuke.tuske.util;

import org.bukkit.event.Event;

import java.lang.reflect.Method;

public final class SkriptCompat {
    private SkriptCompat() {}

    public static boolean isCurrentEvent(Class<? extends Event>... events) {
        try {
            Class<?> loader = Class.forName("ch.njol.skript.ScriptLoader");
            Method m = ReflectionUtils.getMethod(loader, "isCurrentEvent", Class[].class);
            if (m != null) {
                Object r = m.invoke(null, (Object) events);
                return r instanceof Boolean && (Boolean) r;
            }
        } catch (Throwable ignored) {}
        return false;
    }

    public static void setCurrentEvent(String name, Class<? extends Event>... events) {
        try {
            Class<?> loader = Class.forName("ch.njol.skript.ScriptLoader");
            Method m = ReflectionUtils.getMethod(loader, "setCurrentEvent", String.class, Class[].class);
            if (m != null) m.invoke(null, name, events);
        } catch (Throwable ignored) {}
    }

    public static void deleteCurrentEvent() {
        try {
            Class<?> loader = Class.forName("ch.njol.skript.ScriptLoader");
            Method m = ReflectionUtils.getMethod(loader, "deleteCurrentEvent");
            if (m != null) m.invoke(null);
        } catch (Throwable ignored) {}
    }

    public static String getCurrentEventName() {
        try {
            Class<?> loader = Class.forName("ch.njol.skript.ScriptLoader");
            Method m = ReflectionUtils.getMethod(loader, "getCurrentEventName");
            Object r = m != null ? m.invoke(null) : null;
            return r != null ? r.toString() : null;
        } catch (Throwable ignored) {}
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends Event>[] getCurrentEvents() {
        try {
            Class<?> loader = Class.forName("ch.njol.skript.ScriptLoader");
            Method m = ReflectionUtils.getMethod(loader, "getCurrentEvents");
            Object r = m != null ? m.invoke(null) : null;
            if (r instanceof Class[]) return (Class<? extends Event>[]) r;
        } catch (Throwable ignored) {}
        return null;
    }
}


