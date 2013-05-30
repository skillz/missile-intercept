package com.skillzgames.mintercept.common;


public interface Setting {
    public abstract void set(boolean newValue);
    public abstract boolean toggle();
    public abstract boolean get();
}
