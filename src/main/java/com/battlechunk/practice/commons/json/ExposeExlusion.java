package com.battlechunk.practice.commons.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;
import jdk.nashorn.internal.ir.annotations.Ignore;

public class ExposeExlusion implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        final Ignore ignore = fieldAttributes.getAnnotation(Ignore.class);
        if(ignore != null)
            return true;
        final Expose expose = fieldAttributes.getAnnotation(Expose.class);
        return expose != null && (!expose.serialize() || !expose.deserialize());
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}