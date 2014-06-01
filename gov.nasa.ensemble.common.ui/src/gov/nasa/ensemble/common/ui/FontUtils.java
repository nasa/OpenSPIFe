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
package gov.nasa.ensemble.common.ui;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class FontUtils {

	private static Font SYSTEM = null;
	private static Font SYSTEM_BOLD = null;
	
	/**
	 * Use if you want to cache your font globally.
	 */
	public static FontRegistry FONT_REGISTRY_INSTANCE = new FontRegistry(Display.getDefault(), false);
	
	public static Font getStyledFont(int height, int style) {
		return getStyledFont(getSystemFont(), height, style);
	}

	public static Font getStyledFont(int style) {
		Font systemFont = getSystemFont();
		return getStyledFont(systemFont.getFontData()[0].getHeight(), style);
	}
	
	public static Font getSystemFont() {
		if (SYSTEM != null) {
			return SYSTEM;
		}
		final Display display = WidgetUtils.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				SYSTEM = display.getSystemFont();
			}
		});
		return SYSTEM;
	}
	
	public static Font getSystemBoldFont() {
		if (SYSTEM_BOLD != null) {
			return SYSTEM_BOLD;
		}
		SYSTEM_BOLD = getStyledFont(SWT.BOLD);
		return SYSTEM_BOLD;
	}
	
	/**
	 * Returns a font with all attributes of the passed font except with the
	 * passed height and style. The caller is responsible for disposing of this font, if
	 * necessary.
	 */
	public static Font getStyledFont(Font f, int height, int style) {
		FontData fd = f.getFontData()[0];
		FontKey fontKey = new FontKey(f.getDevice(), fd.getName(), height, style);
		Font font = getFont(fontKey);
		return font;
	}
	
	public static Font getStyledFont(Device device, FontData[] fontDatas) {
		FontKey fontKey = new FontKey(device, fontDatas);
		return getFont(fontKey);
	}
	
	public static Font getStyledFont(Device device, FontData fontData) {
		FontKey fontKey = new FontKey(device, fontData.getName(), fontData.getHeight(), fontData.getStyle());
		return getFont(fontKey);
	}
	
	public static Font getStyledFont(Device device, String name, int height, int style) {
		FontKey fontKey = new FontKey(device, name, height, style);
		return getFont(fontKey);
	}
	
	protected static Font getFont(FontKey fontKey) {
		final Font font;
		if(fontKey.getFontDatas() == null || fontKey.getFontDatas().length == 0) {
			font = new Font(fontKey.getDevice(), fontKey.getName(), fontKey.getHeight(), fontKey.getStyle());
		} else {
			font = new Font(fontKey.getDevice(), fontKey.getFontDatas());
		}

		return font;		
	}
	
	public static Font getStyledFont(Font f, int style) {
		return getStyledFont(f, f.getFontData()[0].getHeight(), style);
	}
	
	public static Font resize(Font f, int height) {
		FontData fd = f.getFontData()[0];
		FontKey fontKey = new FontKey(f.getDevice(), fd.getName(), height, fd.getStyle());
		Font font = getFont(fontKey);
		return font;
	}
	
	static class FontKey {
		private Device device;
		private String name;
		private int height;
		private int style;
		private FontData[] fontDatas;
		private int hashCode;
		
		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public Device getDevice() {
			return device;
		}

		public String getName() {
			return name;
		}

		public int getStyle() {
			return style;
		}
		
		public FontData[] getFontDatas() {
			return fontDatas;
		}

		public FontKey(Device device, FontData[] fontDatas) {
			this(device, null, 0, 0, fontDatas);
		}
		
		public FontKey(Device device, String name, int height, int style) {
			this(device, name, height, style, null);
		}
		
		public FontKey(Device device, String name, int height, int style, FontData[] fontDatas) {
			this.device = device;
			this.name = name;
			this.height = height;
			this.style = style;
			this.fontDatas = fontDatas;
			
			int fontDataHash = 0;
			if(fontDatas != null) {
				for(FontData fontData : fontDatas) {
					fontDataHash += fontData.hashCode();
				}
			}
			
			int deviceHashCode = 0;
			if(device != null) {
				deviceHashCode = 0;
			}
			
			int nameHashCode = 0;
			if(name != null) {
				nameHashCode = name.hashCode();
			}
			
			this.hashCode = deviceHashCode + nameHashCode + height + style + fontDataHash;
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		private boolean getFontDatasEqual(FontData[] fontDatas) {
			boolean fontDatasEqual = false;
			if(fontDatas != null) {
				if(this.fontDatas != null) {
					if(this.fontDatas.length == fontDatas.length) {
						int matchedCount = 0;
						for(FontData foreignFontData : fontDatas) {
							boolean foundMatch = false;
							for(FontData localFontData : fontDatas) {
								if(localFontData.equals(foreignFontData)) {
									foundMatch = true;
									matchedCount++;
									break;
								}									
							}

							if(!foundMatch) {
								break;
							}

							if(matchedCount == this.fontDatas.length) {
								fontDatasEqual = true;
							}
						}													
					}
				}

				else {
					//fontDatasEqual = false;
				}
			}

			else if(fontDatas == null && this.fontDatas == null) {
				fontDatasEqual = true;
			}

			return fontDatasEqual;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof FontKey) {
				FontKey fontKey = (FontKey)obj;
				Device device = fontKey.getDevice();
				boolean deviceEqual = false;
				if(device != null) {
					deviceEqual = device.equals(this.device);
				}
				
				else {
					deviceEqual = this.device == null;
				}
				
				boolean fontDatasEqual = getFontDatasEqual(fontKey.getFontDatas());
				
				int height = fontKey.getHeight();
				boolean heightEqual = false;
				if(this.height == height) {
					heightEqual = true;
				}
				
				boolean styleEqual = false;
				if(this.style == fontKey.getStyle()) {
					styleEqual = true;
				}
				
				boolean nameEqual = false;
				String name = fontKey.getName();
				if(name != null) {
					if(this.name != null) {
						nameEqual = this.name.equals(name);
					}
				}
				
				else if(name == null && this.name == null) {
					nameEqual = true;
				}
			
				if(deviceEqual && fontDatasEqual && heightEqual && styleEqual && nameEqual ) {
					return true;
				}
				
				else {
					return false;
				}
			}
			
			return false;
		}					
	}
}
