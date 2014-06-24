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
package gov.nasa.ensemble.common.io;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.collections.Pair;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class XMLUtilities {

	private static final Logger logger = Logger.getLogger(XMLUtilities.class);
	private static final boolean validateAgainstSchema = EnsembleProperties.getBooleanPropertyValue("xml.validateOnImport", false);
	private static final EntityResolver resolver = new FileLocatorEntityResolver();
	
	private static final int UTC_HOUR_OFFSET = 0;
	private static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("GMT");
	
	private static final DatatypeFactory DATATYPE_FACTORY;
	static {
		DatatypeFactory factory = null;
		try {
			factory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			LogUtil.error("failed to instantiate DatatypeFactory");
		}
		DATATYPE_FACTORY = factory;
	}
	
	public static boolean isValidXMLAgainstSchema(File xmlFile, File xsdFile) 
		throws SAXException, IOException 
	{
		logger.debug("Validating "+xmlFile+" against "+xsdFile);
		return isValidXMLAgainstSchema(new FileReader(xmlFile), xsdFile);
	}
	
	public static boolean isValidXMLAgainstSchema(Reader reader, File xsdFile) 
		throws SAXException, IOException
	{
		InputSource src = new InputSource(reader);
		return isValidXMLAgainstSchema(src, xsdFile);
	}
	
	public static boolean isValidXMLAgainstSchema(InputSource xmlInputSource, File xsdFile) 
		throws SAXException, IOException 
	{
		return parseXML(xmlInputSource, xsdFile.toURI().toURL(), null, true, false, false, false, null, 1);
	}
	
	public static boolean parseXML(InputSource xmlInputSource, String schemaURI, ContentHandler handler, boolean stopOnWarning, boolean stopOnError, boolean stopOnFatalError, Set<XMLValidationMessage> validationMessages) throws SAXException, IOException, URISyntaxException {
		return parseXML(xmlInputSource, new URI(schemaURI), handler, stopOnWarning, stopOnError, stopOnFatalError, validationMessages);
	}
	
	public static boolean parseXML(InputSource xmlInputSource, URI schemaURI, ContentHandler handler, boolean stopOnWarning, boolean stopOnError, boolean stopOnFatalError, Set<XMLValidationMessage> validationMessages) throws SAXException, IOException {
		return parseXML(xmlInputSource, schemaURI.toURL(), handler, stopOnWarning, stopOnError, stopOnFatalError, validationMessages, 10);
	}
	
	public static boolean parseXML(InputSource xmlInputSource, URL xsdURL, ContentHandler handler,
			boolean stopOnWarning, boolean stopOnError, boolean stopOnFatalError, Set<XMLValidationMessage> validationMessages, int maximumMessagesToKeep) 
			throws SAXException, IOException {
		return parseXML(xmlInputSource, xsdURL, handler, validateAgainstSchema, stopOnWarning, stopOnError, stopOnFatalError, validationMessages, maximumMessagesToKeep);
	}

	
	private static boolean parseXML(InputSource xmlInputSource, URL xsdURL, ContentHandler handler,
			boolean validate,
			boolean stopOnWarning, boolean stopOnError, boolean stopOnFatalError, Set<XMLValidationMessage> validationMessages, int maximumMessagesToKeep) 
		throws SAXException, IOException
	{
		XMLReader parser = XMLReaderFactory.createXMLReader();
		FullValidator errorHandler = null;
		if (validate) {
			logger.debug("Validating xml against " + xsdURL);
			parser.setFeature("http://xml.org/sax/features/validation", true);
			parser.setFeature("http://apache.org/xml/features/validation/schema", true);
			parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
			parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", xsdURL.toString());
			errorHandler = new FullValidator(logger);
			errorHandler.setValidationMessages(validationMessages, maximumMessagesToKeep);
			errorHandler.setStopOnWarning(stopOnWarning);
			errorHandler.setStopOnError(stopOnError);
			errorHandler.setStopOnFatalError(stopOnFatalError);

			parser.setErrorHandler(errorHandler);  
		}
		if (handler != null) {
			parser.setContentHandler(handler);
		}
		parser.setEntityResolver(resolver);
		parser.parse(xmlInputSource);
		if (errorHandler != null) {
			if (errorHandler.validationError == true) {
				logger.debug("\tXML document has " +  errorHandler.counter + " Error(s)");
			} else {
				logger.debug("\tXML document is valid");
			}
		}
		
		return errorHandler == null ? true : !errorHandler.validationError; 
	}

	public static boolean isValidXMLAgainstSchema(URL xmlUrl, URL xsdUrl, Logger logger) throws SAXException, IOException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory.newSchema(xsdUrl);
		Validator validator = schema.newValidator();

		FullValidator handler = new FullValidator(logger);
		validator.setErrorHandler(handler);
		
		InputStream stream = xmlUrl.openStream();
		Source source = new StreamSource(stream);
		validator.validate(source);
		if (handler.validationError == true) {
			logger.debug("\tXML document has " +  handler.counter + " Error(s)");
		} else {
			logger.debug("\tXML document is valid");
		}        
    	stream.close();
        
        return !handler.validationError;
	}
			
	// Full validator checks for all errors and fatalErrors.
	private static class FullValidator extends DefaultHandler {

		int counter = 0;
		boolean validationError = false;
		final Logger logger;
		boolean stopOnFatalError = false;
		boolean stopOnError = false;
		boolean stopOnWarning = false;
		Set<XMLValidationMessage> validationMessages;
		private int maximumMessagesToKeep;
		
		public FullValidator(Logger logger) {
			this.logger = logger;
		}
				
		public void setStopOnFatalError(boolean stopOnFatalError) {
			this.stopOnFatalError = stopOnFatalError;
		}

		public void setStopOnError(boolean stopOnError) {
			this.stopOnError = stopOnError;
		}

		public void setStopOnWarning(boolean stopOnWarning) {
			this.stopOnWarning = stopOnWarning;
		}

		@Override
		public void error(SAXParseException exception) throws SAXParseException {
			XMLValidationMessage message = addMessage("ERROR", exception);
			counter++;
			validationError = true;	   
			logger.error(message);
			if (stopOnError) {
				throw getNiceException(message, exception);
			}
		} 
		
		@Override
		public void fatalError(SAXParseException exception) throws SAXParseException {
			XMLValidationMessage message = addMessage("FATAL", exception);
			validationError = true;
			logger.error(message);
			if (stopOnFatalError) {
				throw getNiceException(message, exception);
			}
		}
		
		@Override
		public void warning(SAXParseException exception) throws SAXParseException {
			XMLValidationMessage message = addMessage("WARNING", exception);
			validationError = true;	    
			logger.warn(message);
			if (stopOnWarning) {
				throw getNiceException(message, exception);
			}
		}	
		
		private XMLValidationMessage addMessage(String type, SAXParseException exception) {
			XMLValidationMessage message = new XMLValidationMessage(type, exception);
			if (validationMessages != null && validationMessages.size() < maximumMessagesToKeep) {
				validationMessages.add(message);
			}
			return message;
		}
		
		private SAXParseException getNiceException(XMLValidationMessage message, SAXParseException exception) {
			return new SAXParseException(message.toString(),exception.getPublicId(), exception.getSystemId(), exception.getLineNumber(), exception.getColumnNumber(),exception);
		}

		public void setValidationMessages(Set<XMLValidationMessage> validationMessages2, int maximumMessagesToKeep) {
			this.validationMessages = validationMessages2;
			this.maximumMessagesToKeep = maximumMessagesToKeep;
		}
	}
	
	public static class XMLValidationMessage {
		private String type;
		private String message;
		private Set<Pair<Integer, Integer>> location;
		
		public XMLValidationMessage(String type, SAXParseException exception) {
			super();
			this.type = type;
			this.message = exception.getMessage();
			location = new HashSet<Pair<Integer,Integer>>();
			location.add(new Pair(exception.getLineNumber(), exception.getColumnNumber()));
		}
		
		@Override
		public String toString() {
			Object[] locationArr = location.toArray();
			return type + ": " +
			"Line: "+ ((Pair) locationArr[0]).getLeft() + "," + 
			"Column: "+ ((Pair) locationArr[0]).getRight() + ":" + 
			"Message: "+ message;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof XMLValidationMessage)) {
				return false;
			}
			XMLValidationMessage validationMessage = (XMLValidationMessage)obj;
			return validationMessage.type.equals(type) && validationMessage.message.equals(validationMessage);
		}
		
	}
		
	/**
	 * Walks the tree finding the first path through the dom that satisfies the path
	 */
	public static Iterable<Node> selectSingletonNodeIterable(Element element, String xpath) {
		StringTokenizer tokenizer = new StringTokenizer(xpath, "/");
		String path[] = new String[tokenizer.countTokens()];
		for (int i=0; i<path.length; i++) {
			path[i] = tokenizer.nextToken();
		}
		
		if (path.length == 0) {
			return null;
		}
		
		Element result = getSingletonElement(element, path[0]);
		for (int i=1; i<path.length - 1 && result != null; i++) {
			String part = path[i];
			result = getSingletonElement(result, part);
		}
		if (result != null) {
			String part = path[path.length-1];
			return getChildElementsByTagName(result, part);
		} // else...
		return new ArrayList<Node>();
	}
	
	public static Element getSingletonElement(Element element, String tagName) {
		Element result = null;
		NodeList elements = element.getChildNodes();//XPathAPI.selectNodeList(element, tagName);
		
		// filter children by tag name
		for (int i= 0; i< elements.getLength(); i++) {
			Node e =  elements.item(i);
			if (e.getNodeName().equals(tagName)) {
				if (result==null) {
					result = (Element)e;
				}
				else {
					logger.warn("expected one child element but found more: " + tagName);
				}
			}
		}
		
		return result;
	}
	
	public static List<Node> getChildElementsByTagName(NodeList nodes, String tagName) {
		List<Node> list = new ArrayList<Node>();
		
		for (int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element)
				list.addAll(getChildElementsByTagName(node, tagName));
		}
		return list;
	}

	public static List<Node> getChildElementsByTagName(Node element, String tagName) {
		List<Node> list = new ArrayList<Node>();
		NodeList children = element.getChildNodes();
		for (int i=0; i<children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals(tagName)) {
				list.add(child);
			}
		}
		return list;
	}
	
	public static Iterable<Node> iterateOverChildren(final Node element) {
		return new Iterable<Node>() {

			NodeList childNodes = element.getChildNodes();
			int itemIndex = 0;
			int nItems = childNodes.getLength();

			@Override
			public Iterator<Node> iterator() {
				return new Iterator<Node>() {

					@Override
					public void remove() {
						throw new UnsupportedOperationException();				
					}

					@Override
					public Node next() {
						return childNodes.item(itemIndex++);
					}

					@Override
					public boolean hasNext() {
						return itemIndex < nItems;
					}
				};
			}
		};
	}
	
	public static int numberOfDescendantsTagged(Node root, String tag) {
		int self = root.getNodeName().equals(tag)? 1 : 0;
		int children = 0;
		for (Node child : XMLUtilities.iterateOverChildren(root)) {
			children += numberOfDescendantsTagged(child, tag);
		}
		return self + children;
	}

	public static int numberOfDescendantsWithTagAndValue(Node root, String tag, String value) {
		int self = root.getNodeName().equals(tag) && root.getTextContent().equals(value)? 1 : 0;
		int children = 0;
		for (Node child : XMLUtilities.iterateOverChildren(root)) {
			children += numberOfDescendantsWithTagAndValue(child, tag, value);
		}
		return self + children;
	}
	
	public static Set<String> valuesOfDescendantsTagged(Node root, String tag) {
		Set<String> result = new HashSet<String>();
		if (root.getNodeName().equals(tag)) {
			result.add(root.getTextContent());
		}
		for (Node child : XMLUtilities.iterateOverChildren(root)) {
			result.addAll(valuesOfDescendantsTagged(child, tag));
		}
		return result;
	}

	public static Element getFirstChildElement(Node parent) {
	  NodeList children = parent.getChildNodes();
	  for (int i = 0; i < children.getLength(); ++i)
	    if (children.item(i) instanceof Element)
	      return (Element) children.item(i);
	  return null;
	}
	
	public static void print(Node documentOrNode, OutputStream out) {
		TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        try {
            serializer = tfactory.newTransformer();
            //Setup indenting to "pretty print"
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf8");
            if (!(documentOrNode instanceof Document)) {
            	// terser debugging
            	serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            }
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            serializer.transform(new DOMSource(documentOrNode), new StreamResult(out));
        } catch (TransformerException e) {
            // this is fatal, just dump the stack and throw a runtime exception
            logger.error(e, e);
            
            throw new RuntimeException(e);
        }
	}
	
	public static void print(Document doc, Writer out) {
		TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        try {
            serializer = tfactory.newTransformer();
            //Setup indenting to "pretty print"
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf8");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            serializer.transform(new DOMSource(doc), new StreamResult(out));
        } catch (TransformerException e) {
            // this is fatal, just dump the stack and throw a runtime exception
            logger.error(e, e);
            
            throw new RuntimeException(e);
        }
	}

	private static class FileLocatorEntityResolver implements EntityResolver {
		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws IOException {
			InputSource toRet = null;
			try {
				URL entityURL = FileLocator.resolve(new URL(systemId));
				toRet = new InputSource(entityURL.openStream());
				toRet.setSystemId(systemId);
				toRet.setPublicId(publicId);
			} catch (NullPointerException ex) {
				return null;
			}
			return toRet;
		}
	}

	/**
	 * Extract the day component of the date. (year/month/day)
	 * 
	 * @param time
	 * @return
	 */
	public static XMLGregorianCalendar getXMLGregorianCalendar(Date time) {
		if (time == null) {
			return null;
		}
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(time);
		gCalendar.setTimeZone(UTC_TIMEZONE);
		XMLGregorianCalendar xCalendar = DATATYPE_FACTORY.newXMLGregorianCalendar(gCalendar);
		xCalendar.setHour(DatatypeConstants.FIELD_UNDEFINED);
		xCalendar.setMinute(DatatypeConstants.FIELD_UNDEFINED);
		xCalendar.setSecond(DatatypeConstants.FIELD_UNDEFINED);
		xCalendar.setFractionalSecond(null);
		xCalendar.setTimezone(UTC_HOUR_OFFSET);
		xCalendar = xCalendar.normalize();
		return xCalendar;
	}

	public static XMLGregorianCalendar getXMLGregorianCalendarWithTime(Date time) {
		if (time == null) {
			return null;
		}
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(time);
		gCalendar.setTimeZone(UTC_TIMEZONE);
		XMLGregorianCalendar xCalendar = DATATYPE_FACTORY.newXMLGregorianCalendar(gCalendar);
		xCalendar.setTimezone(UTC_HOUR_OFFSET);
		xCalendar = xCalendar.normalize();
		return xCalendar;
	}

	/**
	 * Given an xmlGregorianCalendar instance, convert it to an equivalent Date class representation
	 * @param xmlGregorianCalendar
	 * @return an equivalent Date class object.
	 */
	public static Date getDate(XMLGregorianCalendar c) {
		if (c == null) {
			return null;
		}
		return c.toGregorianCalendar().getTime();
	}
	
	/**
	 * Convert CR or CR+LF line breaks to LF line breaks.
	 * @see http://en.wikipedia.org/wiki/Newline
	 * @param input may contain any mix of newline sequences
	 * @returns null if passed null, identical string in nominal case, or string with only \n for newline.
	 */
	public static String convertNewlinesToLinefeeds(String input) {
		if (input==null) return null;
		if (input.indexOf('\r') < 0) return input; // optimization
		return input.replace("\r\n", "\n").replace("\r", "\n");
	}
	
	
}
