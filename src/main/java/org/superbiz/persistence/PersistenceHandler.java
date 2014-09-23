/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.superbiz.persistence;

import static org.jongo.Oid.withOid;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;

import org.jongo.MongoCollection;
import org.superbiz.persistence.util.Parameter;
import org.superbiz.persistence.util.Reflection;

public class PersistenceHandler {

    public static Object invoke(MongoCollection mongoCollection, Method method, Object[] args) throws Throwable {

        if (method.isAnnotationPresent(Find.class)) {

            return find(mongoCollection, method, args);

        }

        if (method.isAnnotationPresent(FindOne.class)) {

            return findOne(mongoCollection, method, args);

        }

        if (method.isAnnotationPresent(FindById.class)) {
            return findById(mongoCollection, method, args);
        }

        if (method.isAnnotationPresent(Update.class)) {

            return update(mongoCollection, method, args);

        }

        if (method.isAnnotationPresent(Remove.class)) {

            return remove(mongoCollection, method, args);

        }

        if (method.isAnnotationPresent(Insert.class)) {
            return insert(mongoCollection, method, args);
        }

        throw new AbstractMethodError("No handler logic for method: " + method.toString());
    }

    private static Object findById(MongoCollection mongoCollection, Method method, Object[] args) {

        final Class<?> entityClass = method.getReturnType();
        final Iterable<Parameter> params = Reflection.params(method, args);
        final Parameter parameter = params.iterator().next();

        if (parameter.getValue() == null || !(parameter.getValue() instanceof String))
            throw new IllegalArgumentException(parameter.getType().getSimpleName()
                    + " object is null or not of type String");

        return mongoCollection.findOne(withOid((String) parameter.getValue())).as(entityClass);
    }

    public static Object insert(MongoCollection mongoCollection, Method method, Object[] args) throws Throwable {
        final Iterable<Parameter> params = Reflection.params(method, args);
        final Parameter parameter = params.iterator().next();

        Object value = parameter.getValue();
        if (value == null)
            throw new IllegalArgumentException(parameter.getType().getSimpleName() + " object is null");

        mongoCollection.insert(value);

        return value;

    }

    public static Object findOne(MongoCollection mongoCollection, Method method, Object[] args) throws Throwable {
        final Class<?> entityClass = method.getReturnType();
        final FindOne findOne = method.getAnnotation(FindOne.class);
        final String query = findOne.value();

        return mongoCollection.findOne(query, args).as(entityClass);
    }

    public static Object find(MongoCollection mongoCollection, Method method, Object[] args) throws Throwable {

        if (!isIterator(method) && !isIterable(method)) {
            throw new IllegalArgumentException("Return type in find should be an iterable or iterator");
        }

        ParameterizedType genericReturnType = (ParameterizedType) method.getGenericReturnType();
        final Class<?> entityClass = (Class<?>) genericReturnType.getActualTypeArguments()[0];
        final Find find = method.getAnnotation(Find.class);
        final String query = find.value();

        return mongoCollection.find(query, args).as(entityClass);
    }

    public static int update(MongoCollection mongoCollection, Method method, Object[] args) throws Throwable {
        final Iterable<Parameter> params = Reflection.params(method, args);
        final Parameter parameter = params.iterator().next();

        if (parameter.getValue() == null)
            throw new IllegalArgumentException(parameter.getType().getSimpleName() + " object is null");

        final Update update = method.getAnnotation(Update.class);
        final String query = update.value();

        if (parameter.getValue() == null)
            throw new IllegalArgumentException(parameter.getType().getSimpleName() + " object is null");

        return mongoCollection.update(query).with(parameter.getValue()).getN();
    }

    public static int remove(MongoCollection mongoCollection, Method method, Object[] args) throws Throwable {

        final Remove remove = method.getAnnotation(Remove.class);
        final String query = remove.value();

        return mongoCollection.remove(query, args).getN();
    }

    /**
     * Is the return value a list?
     * 
     * @param method
     * @return
     */
    private static boolean isIterable(Method method) {
        return Iterable.class.isAssignableFrom(method.getReturnType());
    }

    private static boolean isIterator(Method method) {
        return Iterator.class.isAssignableFrom(method.getReturnType());
    }

}
