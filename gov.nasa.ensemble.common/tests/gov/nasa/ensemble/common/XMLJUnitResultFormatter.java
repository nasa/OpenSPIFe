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

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Prints XML output of the test to a specified Writer.
 *
 * @see FormatterElement
 */

public class XMLJUnitResultFormatter implements ITestSuiteListener, XMLConstants {

    /** constant for unnamed test-suites/cases */
    private static final String UNKNOWN = "unknown";

    private static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception exc) {
            throw new ExceptionInInitializerError(exc);
        }
    }

    /**
     * The XML document.
     */
    private Document doc;
    /**
     * The wrapper for the whole testsuite.
     */
    private Element rootElement;
    /**
     * Element for the current test.
     */
    private Hashtable<Test, Element> testElements = new Hashtable<Test, Element>();
    /**
     * tests that failed.
     */
    private Hashtable<Test, Test> failedTests = new Hashtable<Test, Test>();
    /**
     * Timing helper.
     */
    private Hashtable<Test, Long> testStarts = new Hashtable<Test, Long>();
    /**
     * Where to write the log to.
     */
    private OutputStream out;

    /** No arg constructor. */
    public XMLJUnitResultFormatter() {
    	// nothing
    }
    
    public XMLJUnitResultFormatter(OutputStream out) {
    	setOutput(out);
    }

    /** {@inheritDoc}. */
    public void setOutput(OutputStream out) {
        this.out = out;
    }

    /** {@inheritDoc}. */
    public void setSystemOutput(String out) {
        formatOutput(SYSTEM_OUT, out);
    }

    /** {@inheritDoc}. */
    public void setSystemError(String out) {
        formatOutput(SYSTEM_ERR, out);
    }

    /**
     * The whole testsuite started.
     * @param suite the testsuite.
     */
    @Override
	public void startTestSuite(TestSuite suite) {
        doc = getDocumentBuilder().newDocument();

        rootElement = doc.createElement(TESTSUITE);
        doc.appendChild(rootElement);
        String n = suite.getName();
        rootElement.setAttribute(ATTR_NAME, n == null ? UNKNOWN : n);

        //add the timestamp
        final String timestamp = ISO8601DateFormat.formatISO8601(new Date());
        rootElement.setAttribute(TIMESTAMP, timestamp);
        //and the hostname.
        rootElement.setAttribute(HOSTNAME, getHostname());

        // Output properties
        Element propsElement = doc.createElement(PROPERTIES);
        rootElement.appendChild(propsElement);
        Properties props = System.getProperties();
        if (props != null) {
            Enumeration e = props.propertyNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                Element propElement = doc.createElement(PROPERTY);
                propElement.setAttribute(ATTR_NAME, name);
                propElement.setAttribute(ATTR_VALUE, props.getProperty(name));
                propsElement.appendChild(propElement);
            }
        }
    }

    /**
     * get the local hostname
     * @return the name of the local host, or "localhost" if we cannot work it out
     */
    private String getHostname()  {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    /**
     * The whole testsuite ended.
     * @param result the result.
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws ClassCastException 
     */
    @Override
	public void endTestSuite(TestResult result, long runTime) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        rootElement.setAttribute(ATTR_TESTS, "" + result.runCount());
        rootElement.setAttribute(ATTR_FAILURES, "" + result.failureCount());
        rootElement.setAttribute(ATTR_ERRORS, "" + result.errorCount());
        rootElement.setAttribute(ATTR_TIME, "" + (runTime / 1000.0));
        if (out != null) {
        	Writer wri = null;
            try {
                wri = new BufferedWriter(new OutputStreamWriter(out, "UTF8"));
                wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                (new DOMElementWriter()).write(rootElement, wri, 0, "  ");
                wri.flush();
            } catch (IOException exc) {
                throw new RuntimeException("Unable to write log file", exc);
            } finally {
                if (out != System.out && out != System.err) {
                	if (wri != null) {
                		try {
                			wri.close();
                		} catch (IOException e) {
                			// ignore
                		}
                	}
                }
            }
        }
    }

    /**
     * Interface TestListener.
     *
     * <p>A new Test is started.
     * @param t the test.
     */
    @Override
	public void startTest(Test t) {
        testStarts.put(t, new Long(System.currentTimeMillis()));
    }

    /**
     * Interface TestListener.
     *
     * <p>A Test is finished.
     * @param test the test.
     */
    @Override
	public void endTest(Test test) {
        // Fix for bug #5637 - if a junit.extensions.TestSetup is
        // used and throws an exception during setUp then startTest
        // would never have been called
        if (!testStarts.containsKey(test)) {
            startTest(test);
        }

        Element currentTest = null;
        if (!failedTests.containsKey(test)) {
            currentTest = doc.createElement(TESTCASE);
            String n = JUnitVersionHelper.getTestCaseName(test);
            currentTest.setAttribute(ATTR_NAME,
                                     n == null ? UNKNOWN : n);
            // a TestSuite can contain Tests from multiple classes,
            // even tests with the same name - disambiguate them.
            currentTest.setAttribute(ATTR_CLASSNAME,
                    JUnitVersionHelper.getTestCaseClassName(test));
            rootElement.appendChild(currentTest);
            testElements.put(test, currentTest);
        } else {
            currentTest = testElements.get(test);
        }

        Long l = testStarts.get(test);
        currentTest.setAttribute(ATTR_TIME,
            "" + ((System.currentTimeMillis() - l.longValue()) / 1000.0));
    }

    /**
     * Interface TestListener for JUnit &lt;= 3.4.
     *
     * <p>A Test failed.
     * @param test the test.
     * @param t the exception.
     */
    public void addFailure(Test test, Throwable t) {
        formatError(FAILURE, test, t);
    }

    /**
     * Interface TestListener for JUnit &gt; 3.4.
     *
     * <p>A Test failed.
     * @param test the test.
     * @param t the assertion.
     */
    @Override
	public void addFailure(Test test, AssertionFailedError t) {
        addFailure(test, (Throwable) t);
    }

    /**
     * Interface TestListener.
     *
     * <p>An error occurred while running the test.
     * @param test the test.
     * @param t the error.
     */
    @Override
	public void addError(Test test, Throwable t) {
        formatError(ERROR, test, t);
    }

    private void formatError(String type, Test test, Throwable t) {
        if (test != null) {
            endTest(test);
            failedTests.put(test, test);
        }

        Element nested = doc.createElement(type);
        Element currentTest = null;
        if (test != null) {
            currentTest = testElements.get(test);
        } else {
            currentTest = rootElement;
        }

        currentTest.appendChild(nested);

        String message = t.getMessage();
        if (message != null && message.length() > 0) {
            nested.setAttribute(ATTR_MESSAGE, t.getMessage());
        }
        nested.setAttribute(ATTR_TYPE, t.getClass().getName());

        String strace = JUnitStackFilter.getFilteredTrace(t);
        Text trace = doc.createTextNode(strace);
        nested.appendChild(trace);
    }

    private void formatOutput(String type, String output) {
        Element nested = doc.createElement(type);
        rootElement.appendChild(nested);
        nested.appendChild(doc.createCDATASection(output));
    }

} // XMLJUnitResultFormatter
