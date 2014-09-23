package org.superbiz.persistence.util;


import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class Parameter implements AnnotatedElement {

    private final Annotation[] annotations;
    private final Class<?> type;
    private final Object value;

    public Parameter(Annotation[] annotations, Class<?> type, Object value) {
        this.annotations = annotations;
        this.type = type;
        this.value = value;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return getAnnotation(annotationClass) != null;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotationClass.equals(annotation.annotationType())) return (T) annotation;
        }
        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return getAnnotations();
    }

    public Object getValue() {
        return value;
    }
}
