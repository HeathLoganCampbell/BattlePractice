package com.battlechunk.practice.commons;

@FunctionalInterface
public interface Callback<T>
{
	public abstract void done(T paramT);
}