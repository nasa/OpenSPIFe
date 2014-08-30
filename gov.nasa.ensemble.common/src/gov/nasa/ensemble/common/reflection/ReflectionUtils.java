/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.common.reflection;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ReflectionUtils {

	private static Map<List, Field> fieldCache = new HashMap<List, Field>();
	private static Map<List, Method> methodCache = new HashMap<List, Method>();

	private static Map<Class, Class> primitiveToComplexMap = new HashMap<Class, Class>();
	static {
		primitiveToComplexMap.put(int.class, Integer.class);
		primitiveToComplexMap.put(long.class, Long.class);
		primitiveToComplexMap.put(short.class, Short.class);
		primitiveToComplexMap.put(char.class, Character.class);
		primitiveToComplexMap.put(float.class, Float.class);
		primitiveToComplexMap.put(double.class, Double.class);
		primitiveToComplexMap.put(boolean.class, Boolean.class);
	}

	/**
	 * Returns the field with the specified name. This method will climb up the class hierarchy until it finds a field with the given name.
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return the field with the specified name
	 */
	public static Field getField(Class<?> clazz, String fieldName) {
		List key = Arrays.asList(new Object[] { clazz, fieldName });
		Field field = fieldCache.get(key);
		if (field != null)
			return field;

		do {
			try {
				field = clazz.getDeclaredField(fieldName);
				break;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		} while (!clazz.equals(Object.class));

		fieldCache.put(key, field);
		return field;
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Object target, String fieldName) {
		Class clazz = target instanceof Class ? (Class) target : target.getClass();
		Field field = getField(clazz, fieldName);
		if (field == null)
			throw new IllegalArgumentException("Unable to find field " + fieldName + " in " + target + "'s class hierarchy.");
		return get(target, field);
	}

	public static <T> T get(Object target, Field field) {
		boolean oldAccessibility = field.isAccessible();
		field.setAccessible(true);
		try {
			return (T) field.get(target);
		} catch (IllegalAccessException e) {
			Logger.getLogger(ReflectionUtils.class).error("That's un-possible!", e);
			return null;
		} finally {
			field.setAccessible(oldAccessibility);
		}
	}

	public static void set(Object target, String fieldName, Object value) {
		Class clazz = target instanceof Class ? (Class) target : target.getClass();
		Field field = getField(clazz, fieldName);
		if (field == null)
			throw new IllegalArgumentException("Unable to find field " + fieldName + " in " + target + "'s class hierarchy.");
		boolean oldAccessibility = field.isAccessible();
		synchronized (field) {
			try {
				field.setAccessible(true);
				field.set(target, value);
			} catch (IllegalAccessException e) {
				LogUtil.error("That's un-possible!", e);
			} finally {
				field.setAccessible(oldAccessibility);
			}
		}
	}

	/**
	 * Returns a list of all the fields for a class and all of its relevant superclasses.
	 * 
	 * @param clazz
	 * @return a list of all the fields for a class and all of its relevant superclasses
	 */
	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		while (!clazz.equals(Object.class)) {
			Collections.addAll(fields, clazz.getDeclaredFields());
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	/**
	 * Returns a list of all the fields for a class and all of its relevant superclasses.
	 * 
	 * @param clazz
	 * @param includeStaticFields
	 * @return a list of all the fields for a class and all of its relevant superclasses
	 */
	public static List<Field> getAllFields(Class<?> clazz, boolean includeStaticFields) {
		List<Field> allFields = getAllFields(clazz);
		List<Field> fields = new ArrayList<Field>();
		for (Field f : allFields) {
			if (includeStaticFields || !Modifier.isStatic(f.getModifiers()))
				fields.add(f);
		}
		return fields;
	}

	public static boolean isCollection(Field f) {
		return Collection.class.isAssignableFrom(f.getType());
	}

	public static boolean isMap(Field f) {
		return Map.class.isAssignableFrom(f.getType());
	}

	/**
	 * Returns the method with the specified name and matching arguments. This method will climb up the class hierarchy until it finds something.
	 * 
	 * @param clazz
	 * @param methodName
	 * @return the method with the specified name and matching arguments
	 */
	public static Method getMethod(Class<?> clazz, String methodName, Object... arguments) {
		List key = Arrays.asList(new Object[] { clazz, methodName, getClassNames(arguments) });
		Method method = methodCache.get(key);
		if (method != null)
			return method;

		do {
			method = getDeclaredMethod(clazz, methodName, arguments);
			if (method != null)
				return method;
			clazz = clazz.getSuperclass();
		} while (!clazz.equals(Object.class));

		methodCache.put(key, method);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T invoke(Object target, String methodName, Object... arguments) {
		return (T) invoke(target.getClass(), target, methodName, arguments);
	}

	@SuppressWarnings("unchecked")
	public static <T> T invokeStatic(Class clazz, String methodName, Object... arguments) {
		return (T) invoke(clazz, null, methodName, arguments);
	}

	private static Object invoke(Class clazz, Object target, String methodName, Object... arguments) {
		Method method = getMethod(clazz, methodName, arguments);
		if (method == null)
			throw new IllegalArgumentException("Unable to find method " + methodName + "(" + getClassNames(arguments) + ")" + " in class hierarchy of " + clazz);
		boolean oldAccessibility = method.isAccessible();
		method.setAccessible(true);
		try {
			return method.invoke(target, arguments);
		} catch (IllegalAccessException e) {
			Logger.getLogger(ReflectionUtils.class).error("That's un-possible!", e);
			return null;
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException("Error reflectively invoking " + method, e);
		} finally {
			method.setAccessible(oldAccessibility);
		}
	}

	public static Method getDeclaredMethod(Class clazz, String name, Object... args) {
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(name)) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length != args.length)
					continue;
				boolean parametersMatch = true;
				for (int i = 0; i < args.length; i++) {
					Class<?> argType = parameterTypes[i];
					Object arg = args[i];
					if (arg == null || argType.isAssignableFrom(arg.getClass()))
						continue;
					if (isAutoboxable(argType, arg.getClass()))
						continue;

					parametersMatch = false;
					break;
				}
				if (parametersMatch)
					return method;
			}
		}
		return null;
	}

	public static <T> T construct(Class<T> clazz, Object... arguments) throws InstantiationException {
		Constructor<T> constructor = getDeclaredConstructor(clazz, arguments);
		if (constructor == null)
			throw new IllegalArgumentException("Unable to find constructor " + clazz.getSimpleName() + "(" + getClassNames(arguments) + ")");
		boolean oldAccessibility = constructor.isAccessible();
		constructor.setAccessible(true);
		try {
			return constructor.newInstance(arguments);
		} catch (IllegalAccessException e) {
			Logger.getLogger(ReflectionUtils.class).error("That's un-possible!", e);
			return null;
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException("Error reflectively invoking " + constructor, e);
		} finally {
			constructor.setAccessible(oldAccessibility);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Object... args) {
		for (Constructor constructor : clazz.getDeclaredConstructors()) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			if (parameterTypes.length != args.length)
				continue;
			boolean parametersMatch = true;
			for (int i = 0; i < args.length; i++) {
				Class<?> argType = parameterTypes[i];
				Object arg = args[i];
				if (arg == null || argType.isAssignableFrom(arg.getClass()))
					continue;
				if (isAutoboxable(argType, arg.getClass()))
					continue;

				parametersMatch = false;
				break;
			}
			if (parametersMatch)
				return constructor;
		}
		return null;
	}

	private static boolean isAutoboxable(Class<? extends Object> class1, Class<?> class2) {
		return primitiveToComplexMap.get(class1) == class2 || primitiveToComplexMap.get(class2) == class1;
	}

	public static String getClassNames(Object... list) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < list.length; i++) {
			Object object = list[i];
			sb.append(object == null ? "null" : object.getClass().getSimpleName());
			if (i < list.length - 1)
				sb.append(", ");
		}
		return sb.toString();
	}
}
