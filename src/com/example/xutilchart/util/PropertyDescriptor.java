package com.example.xutilchart.util;

import java.lang.reflect.Method;

/**
 * 自定义的属性描述器，用以解决Android中没有属性描述其的问题
 *
 * @author lishichao
 */
public class PropertyDescriptor {
    private String name;
    private String displayName;
    private Method readMethod;
    private Method writeMethod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

}
