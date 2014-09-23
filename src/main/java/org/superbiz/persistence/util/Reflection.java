package org.superbiz.persistence.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class Reflection {

	public static <T> T annotation(Set<Annotation> annotations, Class<T> clazz) {
    	
    	for (Annotation annotation : annotations) {
			if(clazz.isInstance(annotation)) {
				return clazz.cast(annotation);
			}
		}
    	
    	return null;
    	
    }
	
    public static Iterable<Parameter> params(final Method method, final Object[] values) {
        return new Iterable<Parameter>() {
            @Override
            public Iterator<Parameter> iterator() {
                return new Iterator<Parameter>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < method.getParameterTypes().length;
                    }

                    @Override
                    public Parameter next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return new Parameter(method.getParameterAnnotations()[index], method.getParameterTypes()[index], values[index++]);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static Iterable<Parameter> params(final Constructor constructor, final Object[] values) {
        return new Iterable<Parameter>() {
            @Override
            public Iterator<Parameter> iterator() {
                return new Iterator<Parameter>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < constructor.getParameterTypes().length;
                    }

                    @Override
                    public Parameter next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return new Parameter(constructor.getParameterAnnotations()[index], constructor.getParameterTypes()[index], values[index++]);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
