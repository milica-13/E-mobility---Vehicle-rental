package loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class ConfigLoader implements Serializable{
	private static final String CONFIG_FAJL_PUTANJA = "./resources" +  File.separator;
	private Properties properties;
	/*Otvaranje properti fajla*/
	public ConfigLoader(String propertiesFileName) throws IOException{
		this.properties = new Properties();
		 String configFilePath = CONFIG_FAJL_PUTANJA + propertiesFileName;
		try {
			FileInputStream input = new FileInputStream(configFilePath);
			properties.load(input);
			input.close();
		}catch(IOException e) {
			System.out.println("Greska prilikom ucitavanja protperti fajla!");
		}
	}

	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	public void setProperty(String key, String defaultValue) {
		this.properties.setProperty(key, defaultValue);
	}
	
	//stil not sur treba li mi ovo ali od viska glava ne boli
	public void storeProperties(String outputFileName, String comments) throws IOException{
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String outputPath = rootPath + outputFileName;
		try {
			FileOutputStream output = new FileOutputStream(outputPath);
			properties.store(output, comments);
			output.close();
		}catch(IOException e) {
			System.out.println("Greska kod skladistenja podataka");
		}
	}
	
}


