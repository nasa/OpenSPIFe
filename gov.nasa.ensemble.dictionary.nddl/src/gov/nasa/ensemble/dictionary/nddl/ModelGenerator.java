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
package gov.nasa.ensemble.dictionary.nddl;


import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.util.DictionaryUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;

public class ModelGenerator {

	public static final String INITIAL_STATE_NDDL = "initial-state.nddl";
	public static final String MODEL_NDDL = "model.nddl";
	public static final String OBJECTS_NDDL = "objects.nddl";
	
	private static final IPath MODEL_PATH = Platform.getStateLocation(Activator.getDefault().getBundle());
	private static final IPath TMP_PATH;
	private static final Logger LOGGER = Logger.getLogger(ModelGenerator.class.getName());
	//private static URL url = FileLocator.find(Activator.getDefault().getBundle(), new Path("nddl-files" + File.separator), null);

	public static interface Preferences 
	{
		public boolean useResourceSolvingForStateConstraints();
		public boolean translateNumericResources();
	}
	
	static {
		String foo = "";
		try {
			foo = FileUtilities.getTempDirectory();
		}
		catch(IOException ioe) {
			LOGGER.log(Level.SEVERE, "Failed to find a temporary directory to write files to.");
		}
		TMP_PATH = new Path(foo);
		
		prefs_ = new Preferences() {
			@Override
			public boolean useResourceSolvingForStateConstraints() { return false; }			
			@Override
			public boolean translateNumericResources() { return false; }			
		};
	}
	
	protected static Preferences prefs_;
	
	public static Preferences getPreferences() { return prefs_; }
	public static void setPreferences(Preferences p) { prefs_ = p; } 
	
	public static boolean generateModel(URI uri) throws Exception {
		try {
			// Demand load resource for this file.
			//
			EActivityDictionary ad = DictionaryUtil.load(uri);
			return generateModel(ad);
		} catch (RuntimeException rte) {
			System.out.println("Problem loading " + uri);
			rte.printStackTrace();
		} catch (Exception e) {
			System.out.println("Problem generating NDDL files ");
			e.printStackTrace();
			throw e;
		}
		return false;
	}

	public static ParseInterpreter parseInterpret(EActivityDictionary ad) {
		ParseInterpreter parseInterpreter = new ParseInterpreter(ad, "CPU", "On", 360, 540, 420);
		parseInterpreter.interpretAD(true);
		return parseInterpreter;
	}

	public static boolean generateModel(EActivityDictionary ad) 
		throws IOException 
	{
		FileOutputStream streams[] = makeModelOutputStreams();
		
		ParseInterpreter parseInterpreter = parseInterpret(ad);
		parseInterpreter.writeObjects(streams[0]);		
		parseInterpreter.writeCompats(streams[1], OBJECTS_NDDL);
		parseInterpreter.writeInitialState(streams[2], MODEL_NDDL);
		
		for (FileOutputStream stream : streams)
			stream.close();

		// Put all model-related files in the required location
		relocateModelFiles();

		return true;
	}
	
	protected static FileOutputStream[] makeModelOutputStreams()
		throws IOException 
	{
		FileOutputStream streams[] = {
			new FileOutputStream(TMP_PATH.append(OBJECTS_NDDL).toFile()),
			new FileOutputStream(TMP_PATH.append(MODEL_NDDL).toFile()),
			new FileOutputStream(TMP_PATH.append(INITIAL_STATE_NDDL).toFile())
		};

		for (FileOutputStream stream : streams)
			stream.flush();

		return streams;
	}
	
	protected static void relocateModelFiles()
		throws IOException, FileNotFoundException 
	{
		// Copy Generated files from tmp dir to model dir
		String files[] = { 
				OBJECTS_NDDL,
				MODEL_NDDL,
				INITIAL_STATE_NDDL
		};
		for (String file : files) {
			IOUtils.copy(
					new FileInputStream(TMP_PATH.append(file).toFile()),
					new FileOutputStream(MODEL_PATH.append(file).toFile()));			
		}

		// Copy required nddl files to tmp dir
		String nddlFiles[] = {
				"SaturatedResource.nddl",
				"TLM-Format.nddl",
				"Boolean_Object.nddl",
				"SolverConfig.xml"
		};
		for (String nddlFile : nddlFiles) {
			NDDLUtil.copyFile("nddl-files/"+nddlFile, TMP_PATH.append(nddlFile).toString());			
		}		
	}

	public final static StringBuilder getFileContent(String fileName)
			throws IOException {
		File file = TMP_PATH.append(fileName).toFile();
		return readModelFile(file);
	}

	private static StringBuilder readModelFile(File file) throws IOException {
		StringBuilder result = new StringBuilder();
		FileInputStream fis = null;
		InputStreamReader reader = null;
		int size;
		try {
			char[] buffer = new char[1024];
			fis = new FileInputStream(file);

			FileWriter filewriter = new FileWriter("out");
			String encname = filewriter.getEncoding();
			filewriter.close();

			Charset cs = Charset.forName(encname);
			CharsetDecoder csd = cs.newDecoder();
			csd.onMalformedInput(CodingErrorAction.REPLACE);

			reader = new InputStreamReader(fis, csd);
			while ((size = reader.read(buffer, 0, 1024)) > 0) {
				result.append(buffer, 0, size);
			}
		} catch (Exception e) {
			// System.out.println(encoding);
			e.printStackTrace();
		} finally {
			if (fis != null) {
				fis.close();
			}

			if (reader != null) {
				reader.close();
			}
		}

		return result;
	}
}
