package com.howtodoinjava.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesCacheExample {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// Get individual properties
		System.out.println(PropertiesCache.getInstance().getProperty("firstName"));
		System.out.println(PropertiesCache.getInstance().getProperty("lastName"));

		// All property names
		System.out.println(PropertiesCache.getInstance().getAllPropertyNames());

		if (PropertiesCache.getInstance().containsKey("country") == false) {
			PropertiesCache.getInstance().setProperty("country", "INDIA");
		}
		// Verify property
		System.out.println(PropertiesCache.getInstance().getProperty("country"));
		
		PropertiesCache.getInstance().flush();
	}
}

class PropertiesCache {
	private final Properties configProp = new Properties();

	private PropertiesCache() {
		try {
			// Private constructor to restrict new instances
			InputStream in = new FileInputStream("application.properties");
			System.out.println("Read all properties from file");
			configProp.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Bill Pugh Solution for singleton pattern
	private static class LazyHolder {
		private static final PropertiesCache INSTANCE = new PropertiesCache();
	}

	public static PropertiesCache getInstance() {
		return LazyHolder.INSTANCE;
	}

	public String getProperty(String key) {
		return configProp.getProperty(key);
	}

	public Set<String> getAllPropertyNames() {
		return configProp.stringPropertyNames();
	}

	public boolean containsKey(String key) {
		return configProp.containsKey(key);
	}

	public void setProperty(String key, String value) {
		configProp.setProperty(key, value);
	}
	
	public void flush() throws FileNotFoundException, IOException {
		try (final OutputStream outputstream 
					= new FileOutputStream("application.properties");) {
			configProp.store(outputstream,"File Updated");
			outputstream.close();
		}
	}
}