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
package gov.nasa.ensemble.common;

import static gov.nasa.ensemble.common.functional.Lists.bind;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ClassLoaderObjectInputStream;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import fj.F;
import fj.data.Option;
import gov.nasa.ensemble.common.functional.Lists;
import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DateUtils;

public class CommonUtils {

	private static Logger trace = Logger.getLogger(CommonUtils.class);

	public static Comparator<String> ALPHABETICAL_COMPARATOR = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	};

	public static final F<IProduct, String> productName = new F<IProduct, String>() {
		@Override
		public String f(final IProduct product) {
			return product.getName();
		}
	};

	public static final F<IProduct, String> productId = new F<IProduct, String>() {
		@Override
		public String f(final IProduct product) {
			return product.getId();
		}
	};

	public static Option<String> getProductName() {
		return getProduct().map(productName);
	}

	public static Option<String> getProductId() {
		return getProduct().map(productId);
	}

	public static Option<IProduct> getProduct() {
		return Option.fromNull(Platform.getProduct());
	}

	/**
	 * Returns true if both objects are null, or if they are equals to each other.
	 * 
	 * @param object1
	 * @param object2
	 * @return true if both objects are null, or if they are equals to each other.
	 */
	public static final boolean equals(Object object1, Object object2) {
		if (object1 == object2) {
			return true;
		}
		if (object1 == null) {
			return false;
		}
		return object1.equals(object2);
	}

	public static String getHostname() throws IOException {
		return InetAddress.getLocalHost().getHostName();
	}

	/**
	 * A compiled pattern for use instead of <string>.split(",")
	 */
	public static final Pattern COMMA_PATTERN = Pattern.compile(",");

	/**
	 * Creates a temporary directory - TODO: ensure all contents are erased
	 * 
	 * @return
	 */
	public static File createTempDir() {
		final String baseTempPath = System.getProperty("java.io.tmpdir");
		Random rand = new Random();
		int randomInt = 1 + rand.nextInt();
		File tempDir = new File(baseTempPath + File.separator + "tempDir" + randomInt);
		if (tempDir.exists() == false) {
			tempDir.mkdir();
		}
		tempDir.deleteOnExit();
		return tempDir;
	}

	/**
	 * Creates a temporary file and adds a shutdown hook for it to be deleted on VM exit. This file is *NOT* guaranteed to be deleted, but this method is still better than calling File.createTempFile.
	 * 
	 * @param prefix
	 * @param suffix
	 * @return File
	 * @throws IOException
	 */
	public static File createTemporaryFile(String prefix, String suffix) throws IOException {
		File file = File.createTempFile(prefix, suffix);
		file.deleteOnExit();
		return file;
	}

	public static byte[] compress(byte[] input) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DeflaterOutputStream dos = new DeflaterOutputStream(baos);
			dos.write(input);
			dos.close();
			return baos.toByteArray();
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	public static byte[] decompress(byte[] input) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InflaterInputStream ios = new InflaterInputStream(new ByteArrayInputStream(input));
			IOUtils.copy(ios, baos);
			return baos.toByteArray();
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	public static String formatNumber(int num, int places) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < places; ++i)
			b.append("0");
		return new DecimalFormat(b.toString()).format(num);
	}

	public static String encodeUTF8(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// nothing
			return null;
		}
	}

	public static String decodeUTF8(String str) {
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// nothing
			return null;
		}
	}

	/**
	 * Returns true if both strings are null, or if they are equalsIgnoreCase to each other.
	 * 
	 * @param string1
	 * @param string2
	 * @return true if both strings are null, or if they are equals to each other.
	 */
	public static final boolean equalsIgnoreCase(String string1, String string2) {
		if (string1 == string2) {
			return true;
		}
		if (string1 == null) {
			return false;
		}
		return string1.equalsIgnoreCase(string2);
	}

	/**
	 * Check whether two floats are equal concerning a delta. If either value is infinity then the delta value is ignored.
	 * 
	 * @param f1
	 * @param f2
	 * @param delta
	 * @return boolean
	 */
	public static final boolean equals(float f1, float f2, float delta) {
		// handle infinity specially since subtracting two infinite values gives NaN
		if (Float.isInfinite(f1) || Float.isInfinite(f2))
			return f1 == f2;
		return Math.abs(f1 - f2) <= delta;
	}

	/**
	 * Check whether two doubles are equal concerning a delta. If either value is infinity then the delta value is ignored.
	 * 
	 * @param d1
	 * @param d2
	 * @param delta
	 * @return boolean
	 */
	public static final boolean equals(double d1, double d2, double delta) {
		// handle infinity specially since subtracting two infinite values gives NaN
		if (Double.isInfinite(d1) || Double.isInfinite(d2))
			return d1 == d2;
		return Math.abs(d1 - d2) <= delta;
	}

	/**
	 * Compares two <code>int</code> primitives numerically.
	 * 
	 * @return the value <code>0</code> if the argument <code>int1</code> is equal to the argument <code>int2</code>; a value less than <code>0</code> if the argument <code>int1</code> is numerically
	 *         less than the argument <code>int2</code>; and a value greater than <code>0</code> if the argument <code>int1</code> is numerically greater than the argument <code>int2</code> (signed
	 *         comparison).
	 * @since 1.2
	 */
	public static final int compare(int int1, int int2) {
		return (int1 < int2 ? -1 : (int1 == int2 ? 0 : 1));
	}

	/**
	 * Compares two <code>long</code> primitives numerically.
	 * 
	 * @return the value <code>0</code> if the argument <code>long1</code> is equal to the argument <code>long2</code>; a value less than <code>0</code> if the argument <code>long1</code> is
	 *         numerically less than the argument <code>long2</code>; and a value greater than <code>0</code> if the argument <code>long1</code> is numerically greater than the argument
	 *         <code>long2</code> (signed comparison).
	 * @since 1.2
	 */
	public static final int compare(long long1, long long2) {
		return (long1 < long2 ? -1 : (long1 == long2 ? 0 : 1));
	}

	/**
	 * Compare two <code>Date</code> objects temporally
	 * 
	 * @param start1
	 * @param start2
	 * @return the value <code>-1</code> if the argument <code>start1</code> is earlier than argument <code>start2</code> the value <code>1</code> if the argument <code>start1</code> is later than
	 *         argument <code>start2</code> the value <code>0</code> if the argument <code>start1</code> is the same time as argument <code>start2</code>
	 */
	public static int compare(Date start1, Date start2) {
		long difference = DateUtils.subtract(start1, start2);
		return (difference < 0 ? -1 : (difference > 0 ? 1 : 0));
	}

	/**
	 * Copy an object via Externalizable. Copying a set of interconnected objects this way will maintain all the connections. This method will also properly copy and connect loops.
	 * 
	 * @param object
	 * @return not supplied
	 */
	public static <T extends Externalizable> T copy(T object) {
		ClassLoaderObjectInputStream objectInputStream = null;
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(object);
			byte[] byteArray = byteArrayOutputStream.toByteArray();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			objectInputStream = new ClassLoaderObjectInputStream(object.getClass().getClassLoader(), byteArrayInputStream);
			// ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			@SuppressWarnings("unchecked")
			T copy = (T) objectInputStream.readObject();
			return copy;
		} catch (IOException e) {
			Logger.getLogger(CommonUtils.class).error("copy failed", e);
		} catch (ClassNotFoundException e) {
			Logger.getLogger(CommonUtils.class).error("copy failed", e);
		} finally {
			if (objectInputStream != null) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					LogUtil.error(e);
				}
			}
		}
		return null;
	}

	public static final <T> List<T> castList(Class<T> klass, List list) {
		return castList(klass, list, false);
	}

	/**
	 * Returns a new list of the items in list, casting all of them to the provided class. So, it will throw a ClassCastException if any of the items can not be cast to the provided class.
	 * 
	 * @param klass
	 * @param list
	 * @return a new list of the items in list
	 */
	public static final <T> List<T> castList(Class<T> klass, List list, boolean unmodifiable) {
		if (list == null)
			return null;
		List<T> result = new ArrayList<T>(list.size());
		for (Object object : list) {
			result.add(klass.cast(object));
		}
		return unmodifiable ? Collections.unmodifiableList(result) : result;
	}

	/**
	 * Takes a list of objects and a filtering predicate and returns a new list containing only the objects from the original list which passed the filter.
	 * 
	 * @param <T>
	 *            the type of the inputs
	 * @param list
	 *            the list of inputs
	 * @param filter
	 *            the filter to apply to the list
	 * @return a new, filtered list
	 * @deprecated use {@link Lists#filter(List, fj.F)}
	 */
	@Deprecated
	@SuppressWarnings("deprecation")
	public static <T> List<T> filter(List<T> list, final IAccepter<T> filter) {
		return Lists.filter(list, new Predicate<T>() {
			@Override
			public boolean apply(T a) {
				return filter.accepts(a);
			}
		});
	}

	/**
	 * Maps a unary function onto a list of inputs and returns a new list outputs, with null values filtered out.
	 * 
	 * @param <Input>
	 *            the type of the inputs
	 * @param <Output>
	 *            the type of the outputs
	 * @param inputList
	 *            the list of inputs
	 * @param function
	 *            the function to apply
	 * @return the output list
	 * @deprecated use {@link Lists#map(List, fj.F)}
	 */
	@Deprecated
	@SuppressWarnings("deprecation")
	public static <Input, Output> List<Output> map(List<Input> inputList, Unary<Input, Output> function) {
		return map(inputList, function, true);
	}

	/**
	 * Maps a unary function onto a list of inputs and returns a new list outputs, optionally filtering out null values.
	 * 
	 * @param <Input>
	 *            the type of the inputs
	 * @param <Output>
	 *            the type of the outputs
	 * @param inputList
	 *            the list of inputs
	 * @param function
	 *            the function to apply
	 * @param filterNulls
	 *            flag indicating whether to filter out null values from the output list. If true, the size of the output list is guaranteed to be the same size as the input list.
	 * @return the output list
	 * @deprecated use {@link Lists#map(List, fj.F)}
	 */
	@Deprecated
	@SuppressWarnings("deprecation")
	public static <Input, Output> List<Output> map(List<Input> inputList, Unary<Input, Output> function, boolean filterNulls) {
		final F<Input, Output> f = toF(function);
		if (filterNulls)
			return Lists.mapNonNulls(inputList, f);
		else
			return Lists.map(inputList, f);
	}

	/**
	 * A monadic bind function for lists. Maps a list-producing function over a List, and then flattens the resulting List of Lists.
	 * 
	 * @param <Input>
	 *            the type of the input list
	 * @param <Output>
	 *            the type of the output list
	 * @param inputList
	 *            the input list
	 * @param function
	 *            a transformation from Inputs to Lists of Outputs
	 * @deprecated use {@link Lists#bind(List, fj.F)}
	 */
	@Deprecated
	@SuppressWarnings("deprecation")
	public static <Input, Output> List<Output> flatMap(List<Input> inputList, Unary<Input, List<Output>> function) {
		return Lists.bind(inputList, toF(function));
	}

	@SuppressWarnings("deprecation")
	private static <A, B> F<A, B> toF(final Unary<A, B> unary) {
		return new F<A, B>() {
			@Override
			public B f(A a) {
				return unary.apply(a);
			}
		};
	}

	/**
	 * Adapts a list of objects to a list of adapters of a given class.
	 */
	public static <T> List<T> adaptList(List adaptables, final Class<T> adapterClass) {
		return adaptList(adaptables, adapterClass, true);
	}

	/**
	 * Adapts a list of objects to a list of adapters of a given class.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> adaptList(List adaptables, final Class<T> adapterClass, final boolean filterNulls) {
		return bind(adaptables, new F<Object, List<T>>() {
			@Override
			public List<T> f(Object adaptable) {
				final T result = getAdapter(adaptable, adapterClass);
				if (filterNulls && result == null)
					return emptyList();
				return singletonList(result);
			}
		});
	}

	/**
	 * Construct a list of strings in the English convention. Examples: { } -> "" { "Wacko" } -> "Wacko" { "Wacko", "Yacko" } -> "Wacko and Yacko" { "Wacko", "Yacko", "Dot" } ->
	 * "Wacko, Yacko, and Dot"
	 * 
	 * @param strings
	 * @return String
	 */
	public static String getListText(List<String> strings) {
		return getListText(strings, "and");
	}

	/**
	 * as {@link #getListText(List)} but with a possibly different conjunction; e.g., "or"
	 * 
	 * @param strings
	 * @param conjunction
	 * @return String
	 */
	public static String getListText(List<String> strings, String conjunction) {
		switch (strings.size()) {
		case 0:
			return "";
		case 1:
			return strings.get(0);
		case 2:
			return strings.get(0) + " " + conjunction + " " + strings.get(1);
		default:
			StringBuilder result = new StringBuilder();
			Iterator<String> stringItr = strings.iterator();
			while (stringItr.hasNext()) {
				String string = stringItr.next();
				if (!stringItr.hasNext())
					result.append(conjunction).append(" ").append(string);
				else
					result.append(string).append(", ");
			}
			return result.toString();
		}
	}

	public static <T> T getAdapter(Object adaptable, Class<T> adapterClass) {
		if (adaptable == null) {
			return null;
		}
		if (adaptable instanceof IAdaptable) {
			IAdaptable iAdaptable = (IAdaptable) adaptable;
			T adapter = adapterClass.cast(iAdaptable.getAdapter(adapterClass));
			if (adapter != null) {
				return adapter;
			}
		}
		IAdapterManager manager = Platform.getAdapterManager();
		T adapter = adapterClass.cast(manager.loadAdapter(adaptable, adapterClass.getName()));
		return adapter;
	}

	/**
	 * Tests string for null, or empty (post trim()) value
	 * 
	 * @param string
	 *            to test
	 * @return true if null OR empty after a trim() call
	 */
	public static final boolean isNullOrEmpty(String string) {
		return string == null || string.trim().isEmpty();
	}

	/**
	 * Like Object.wait(long) except without the necessity of surrounding with try/catch
	 */
	public static void wait(Object object, long milliseconds) {
		try {
			object.wait(milliseconds);
		} catch (InterruptedException e) {
			// do nothing
		}
	}

	public static boolean isOSMac() {
		return Platform.OS_MACOSX.equals(Platform.getOS());
	}

	public static boolean isOSLinux() {
		return Platform.OS_LINUX.equals(Platform.getOS());
	}

	public static boolean isOSWindows() {
		return Platform.OS_WIN32.equals(Platform.getOS());
	}

	public static boolean isWSCocoa() {
		return Platform.WS_COCOA.equals(Platform.getWS());
	}

	public static boolean isOSArch64() {
		String osArch = Platform.getOSArch();
		return Platform.ARCH_IA64.equals(osArch) || Platform.ARCH_X86_64.equals(osArch);
	}

	public static String toHex(int value, int minNumOfDigits) {
		String hexString = Integer.toHexString(value);
		while (hexString.length() < minNumOfDigits)
			hexString = "0" + hexString;
		return "0x" + hexString.toUpperCase();
	}

	/**
	 * Serializes an object and return the byte array.
	 * 
	 * @param o
	 * @return a byte[] containing the object or null if exceptions are encountered
	 */
	public static byte[] serializeObject(Object o) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
		} catch (IOException e) {
			trace.error(e, e);
			return null;
		}
		return baos.toByteArray();
	}

	public static String pad(int t, String s) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < t; i++) {
			buffer.append(s);
		}
		return buffer.toString();
	}

	public static List<String> parseAsList(String string) {
		ArrayList<String> result = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token != null) {
				result.add(token.trim());
			}
		}
		return result;
	}

	public static String[] parseAsArray(String string) {
		List<String> list = parseAsList(string);
		return list.toArray(new String[list.size()]);
	}

	public static String formatAsCsv(List<String> stringList) {
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> i = stringList.iterator(); i.hasNext();) {
			String string = i.next();
			buffer.append(string);
			if (i.hasNext()) {
				buffer.append(",");
			}
		}
		return buffer.toString();
	}

	public static RuntimeException runtime(final Exception e) {
		throw new RuntimeException(e);
	}

	public static <A> List<A> collect(final A... as) {
		return collect(Arrays.asList(as));
	}

	public static <A> List<A> collect(final Iterable<A> as) {
		final List<A> list = new ArrayList<A>();
		for (final A a : as)
			list.add(a);
		return list;
	}

	public static boolean boolProp(final String propName) {
		return "true".equalsIgnoreCase(System.getProperty(propName, "false"));
	}

	public static Throwable getRootCause(Throwable throwable) {
		final Throwable cause = throwable.getCause();
		if (cause == null)
			return throwable;
		return getRootCause(cause);
	}

	public static <T> Iterable<T> iterable(final Iterator<T> iterator) {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return iterator;
			}
		};
	}

	public static int readUpToOrEOF(byte b[], int off, int len, InputStream in) throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = in.read(b, off + n, len - n);
			if (count < 0)
				return n;
			n += count;
		}
		return n;
	}

	public static boolean isJunitRunning() {
		return CommonPlugin.isJunitRunning();
	}

	/**
	 * Find a file stored in a bundle and copy the contents to a temporary file using {@link CommonUtils#createTemporaryFile(String, String)}.
	 * 
	 * The temporary file is not guaranteed to have the same name as the file stored in the bundle.
	 * 
	 * @param bundle
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File getFileFromBundle(Bundle bundle, String path) throws FileNotFoundException, IOException {
		File file = createTemporaryFile(bundle.getSymbolicName(), null);
		IOUtils.copy(bundle.getEntry(path).openStream(), new FileOutputStream(file));
		return file;
	}

	/**
	 * Execute the operation in the undo context, in a job.
	 * 
	 * If the operation can not be executed, it will be disposed.
	 * 
	 * @param operation
	 * @param undoContext
	 */
	public static void execute(final IUndoableOperation operation, IUndoContext undoContext) {
		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		if (undoContext != null) {
			operation.addContext(undoContext);
		}
		try {
			history.execute(operation, null, null);
		} catch (ExecutionException e) {
			// should never occur
			LogUtil.error(e);
		}
	}

}
