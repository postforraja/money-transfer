package com.revolut.moneytransfer.util;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	private static Properties properties = new Properties();
	private static Logger logger = Logger.getLogger(PropertyUtil.class);

	static {
		String configFileName = System.getProperty("application.properties");

		if (configFileName == null) {
			configFileName = "application.properties";
		}

		loadConfig(configFileName);
	}

	private static void loadConfig(String fileName) {
		if (null == fileName) {
			logger.warn("Load Config: config file name cannot be null");
		} else {
			try {
				logger.info("loadConfig(): Loading config file: " + fileName);
				InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
				properties.load(fis);
			} catch (FileNotFoundException fnfe) {
				logger.error("loadConfig(): file name not found " + fileName, fnfe);
			} catch (IOException ioe) {
				logger.error("loadConfig(): error when reading the config " + fileName, ioe);
			}
		}
	}

	public static String getStringProperty(String key) {
		String value = properties.getProperty(key);

		if (null == value) {
			value = System.getProperty(key);
		}

		return value;
	}

}
