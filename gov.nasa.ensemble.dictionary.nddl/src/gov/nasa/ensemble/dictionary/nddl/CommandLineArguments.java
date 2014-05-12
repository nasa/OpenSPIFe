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
/**
 * 
 */
package gov.nasa.ensemble.dictionary.nddl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class CommandLineArguments {
	
	private static final Logger trace = Logger.getLogger(CommandLineArguments.class);
	
	public enum Option {
		ACTIVITY_DICTIONARY_FILE(null, "activity_dictionary_file", null),
		OUTPUT_DIRECTORY_PATH(null, "output_directory", null),
		CPU_STATE("cpu_state", "cpu_state_name", null),
		CPU_VALUE("cpu_value", "cpu_on_value_name", null),
		BOOT("boot", "cpu_boot_duration_in_seconds", "360"),
		SHUTDOWN("shutdown", "cpu_shutdown_duration_in_seconds", "540"),
		GAP("gap", "min_duration_between_shutdown_and_boot_in_seconds", "420");
		public String shortName;
		public String longName;
		public String defaultValue;
		private Option(String shortName, String longName, String defaultValue) {
			this.shortName = shortName;
			this.longName = longName;
			this.defaultValue = defaultValue;
		}
		public boolean equalsName(String name) {
			if (name == null) return false;
			else return (name.equals("-"+shortName) || name.equals(longName));
		}
		@Override
		public String toString() {
			return (defaultValue==null?"":"[") 
			+ (shortName==null?longName:"-"+shortName+" "+longName) 
			+ (defaultValue==null?"":"]");
		}
	}
	
	private Map<Option, String> values = new HashMap<Option, String>();
	
	public CommandLineArguments(String[] args) throws Exception {
		for (Option option: Option.values()) {
			values.put(option, option.defaultValue);
		}
		Option savedOption = null;
		for (String arg: args) {
			Option currentOption = getOption(arg);
			if (currentOption == null) {
				if (savedOption == null) {
					if (values.get(Option.ACTIVITY_DICTIONARY_FILE) == null) {
						File file = new File(arg);
						if (file.exists() && file.isFile() && file.canRead()) {
							values.put(Option.ACTIVITY_DICTIONARY_FILE, arg);
						} else {
							throw new Exception("Activity Dictionary file not found: " + arg);
						}
					} else if (values.get(Option.OUTPUT_DIRECTORY_PATH) == null) {
						File file = new File(arg);
						if (file.exists() && file.isDirectory() && file.canRead()) {
							values.put(Option.OUTPUT_DIRECTORY_PATH, arg);
						} else {
							throw new Exception("Output directory not found: " + arg);
						}							
					} else {
						throw new Exception("Unknown command line argument: " + arg);
					}
				} else {
					switch (savedOption) {
					case CPU_STATE:
						values.put(Option.CPU_STATE, arg);
						break;
					case CPU_VALUE:
						values.put(Option.CPU_VALUE, arg);
						break;
					case BOOT:
						values.put(Option.BOOT, String.valueOf(Integer.parseInt(arg)));
						break;
					case SHUTDOWN:
						values.put(Option.SHUTDOWN, String.valueOf(Integer.parseInt(arg)));
						break;
					case GAP:
						values.put(Option.GAP, String.valueOf(Integer.parseInt(arg)));
						break;
					case OUTPUT_DIRECTORY_PATH:
					case ACTIVITY_DICTIONARY_FILE:
					default:
						trace.warn("Unhandled Option enum");
					}
						
					savedOption = null;
				}
			} else {
				savedOption = currentOption;
			}
		}
		if (getValue(Option.ACTIVITY_DICTIONARY_FILE) == null) {
			throw new Exception("No activity dictionary path specified");
		}
	}
	
	private Option getOption(String name) throws Exception {
		for (Option option: Option.values()) {
			if (option.equalsName(name)) {
				return option;
			}
		}
		return null;
	}
	
	public String getValue(Option option) {
		return values.get(option);
	}
	
	public int getIntValue(Option option) {
		return Integer.parseInt(getValue(option));
	}
}
