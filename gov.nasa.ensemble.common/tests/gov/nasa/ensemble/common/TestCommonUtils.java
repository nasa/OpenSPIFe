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

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.ArrayList;

import org.junit.Assert;

import org.eclipse.core.runtime.Platform;
import org.junit.Test;

public class TestCommonUtils extends Assert {

	private static final String URL_AS_UTF8 = "http%3A%2F%2Fgoogle.com%2Fsearch%26param%3Dmore+than+one+word";
	private static final String URL_IN_PLAIN_TEXT = "http://google.com/search&param=more than one word";

	@Test
	public void equals() throws Exception {
		Object o = new Object();
		assertTrue(CommonUtils.equals(o, o));
		assertTrue(CommonUtils.equals(null, null));
		assertTrue(CommonUtils.equals(true, true));
		assertTrue(CommonUtils.equals(1, 1));
		assertTrue(CommonUtils.equals("alpha", "alpha"));

		Object o2 = new Object();
		assertFalse(CommonUtils.equals(o, o2));
		assertFalse(CommonUtils.equals(null, true));
		assertFalse(CommonUtils.equals(1, 2));
		assertFalse(CommonUtils.equals("alpha", "ALPHA"));
		assertFalse(CommonUtils.equals("alpha", "beta"));
	}
	
	@Test
	public void equalsIgnoreCase() throws Exception {
		assertTrue(CommonUtils.equalsIgnoreCase("alpha", "alpha"));
		assertTrue(CommonUtils.equalsIgnoreCase("alpha", "ALPHA"));
		assertFalse(CommonUtils.equalsIgnoreCase("alpha", "bravo"));
		assertFalse(CommonUtils.equalsIgnoreCase(null, "echo"));
	}
	
	@Test
	public void floatDoubleEquals() throws Exception {
		float f1 = 1.00001f;
		float f2 = 1.000001f;
		
		assertTrue(CommonUtils.equals(f1, f2, 0.001f)); //float
		assertTrue(CommonUtils.equals(f1, f2, 0.001));  //double

		assertTrue(CommonUtils.equals(f1, f2, 0.00001f)); //float
		assertTrue(CommonUtils.equals(f1, f2, 0.00001));  //double

		assertFalse(CommonUtils.equals(f1, f2, 0.000001f)); //float
		assertFalse(CommonUtils.equals(f1, f2, 0.000001));  //double
		
		float f3 = Float.POSITIVE_INFINITY;
		float f4 = Float.POSITIVE_INFINITY;
		float f5 = Float.NEGATIVE_INFINITY;
		assertTrue(CommonUtils.equals(f3, f4, 0.00001f));
		assertFalse(CommonUtils.equals(f4, f5, 0.000001f));
		
		double d3 = Double.POSITIVE_INFINITY;
		double d4 = Double.POSITIVE_INFINITY;
		double d5 = Double.NEGATIVE_INFINITY;
		assertTrue(CommonUtils.equals(d3, d4, 0.000001));
		assertFalse(CommonUtils.equals(d5, d4, 0.000001));
	}
	
	@Test
	public void encodeUTF8() throws Exception {
		String utf8 = CommonUtils.encodeUTF8(URL_IN_PLAIN_TEXT);
		assertEquals(URL_AS_UTF8, utf8);
	}
	
	@Test
	public void decodeHTF8() throws Exception {
		String plainText = CommonUtils.decodeUTF8(URL_AS_UTF8);
		assertEquals(URL_IN_PLAIN_TEXT, plainText);
	}
	
	@Test
	public void compareIntLong() throws Exception {
		assertEquals(0, CommonUtils.compare(0, 0));
		assertTrue(CommonUtils.compare(2, 1) > 0);
		assertTrue(CommonUtils.compare(1, 2) < 0);
		
		assertEquals(0, CommonUtils.compare(0l, 0l));
		assertTrue(CommonUtils.compare(2l, 1l) > 0);
		assertTrue(CommonUtils.compare(1l, 2l) < 0);
	}
	
	@Test
	public void isMacOrWinOrLinux() throws Exception {
		LogUtil.debug(Platform.getOS() +" is our OS");
		LogUtil.debug("Mac "+CommonUtils.isOSMac());
		LogUtil.debug("Win "+CommonUtils.isOSWindows());
		LogUtil.debug("Linux "+CommonUtils.isOSLinux());
		assertTrue(CommonUtils.isOSLinux() || CommonUtils.isOSMac() || CommonUtils.isOSWindows());
	}
	
	@Test
	public void pad() throws Exception {
		assertEquals("foo", CommonUtils.pad(1, "foo"));
		assertEquals("", CommonUtils.pad(0, "foo"));
		assertEquals("", CommonUtils.pad(-1, "foo"));
		assertEquals("      ", CommonUtils.pad(6, " "));
	}
	
	@Test
	public void toHex() throws Exception {
		assertEquals("0xFFFFFFFF", CommonUtils.toHex(-1, 0));
		assertEquals("0x00000000000C", CommonUtils.toHex(12, 12));
		assertEquals("0xFACE0FF", CommonUtils.toHex(262988031, 7));
	}
	
	@Test
	public void getListText() throws Exception {
		ArrayList<String> strings = new ArrayList<String>();
		assertEquals("", CommonUtils.getListText(strings));
	
		strings.add("wine");
		assertEquals("wine", CommonUtils.getListText(strings));
	
		strings.add("women");
		assertEquals("wine and women", CommonUtils.getListText(strings));

		strings.add("song");
		assertEquals("wine, women, and song", CommonUtils.getListText(strings));
	}
	
    @Test
    public void waitSwallowingException() throws Exception {
    	Thread runner = new Thread() {
    		Object tool = new Object();
    		@Override
    		public void run() {
    			synchronized(tool) {
    				CommonUtils.wait(tool, 2000);
    			}
    		}
    	};
    	runner.start();
    	try {
    		runner.interrupt();
    	} catch (Throwable e) {
    		if (e instanceof InterruptedException)
    			fail();
    	}
    	
    }

}
