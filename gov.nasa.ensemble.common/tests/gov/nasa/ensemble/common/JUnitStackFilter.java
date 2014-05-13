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

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class JUnitStackFilter {

    private static final String[] DEFAULT_TRACE_FILTERS = new String[] {
        "junit.framework.TestCase",
        "junit.framework.TestResult",
        "junit.framework.TestSuite",
        "junit.framework.Assert.", // don't filter AssertionFailure
        "junit.swingui.TestRunner",
        "junit.awtui.TestRunner",
        "junit.textui.TestRunner",
        "java.lang.reflect.Method.invoke(",
        "sun.reflect.",
        "org.apache.tools.ant.",
        // JUnit 4 support:
        "org.junit.",
        "junit.framework.JUnit4TestAdapter",
        // See wrapListener for reason:
        "Caused by: java.lang.AssertionError",
        " more",
    };

    /**
     * Returns a filtered stack trace.
     * This is ripped out of junit.runner.BaseTestRunner.
     * @param t the exception to filter.
     * @return the filtered stack trace.
     */
    public static String getFilteredTrace(Throwable t) {
        String trace = getStackTrace(t);
        return JUnitStackFilter.filterStack(trace);
    }

    /**
     * Convenient method to retrieve the full stacktrace from a given exception.
     * @param t the exception to get the stacktrace from.
     * @return the stacktrace from the given exception.
     */
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        pw.close();
        return sw.toString();
    }

    /**
     * Filters stack frames from internal JUnit and Ant classes
     * @param stack the stack trace to filter.
     * @return the filtered stack.
     */
    public static String filterStack(String stack) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        StringReader sr = new StringReader(stack);
        BufferedReader br = new BufferedReader(sr);

        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (!filterLine(line)) {
                    pw.println(line);
                }
            }
        } catch (Exception e) {
            return stack; // return the stack unfiltered
        }
        return sw.toString();
    }

    private static boolean filterLine(String line) {
        for (int i = 0; i < DEFAULT_TRACE_FILTERS.length; i++) {
            if (line.indexOf(DEFAULT_TRACE_FILTERS[i]) != -1) {
                return true;
            }
        }
        return false;
    }
    
}
