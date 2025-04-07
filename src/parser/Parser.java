package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class Parser<T> {
	//public static final String DELIMITER = ",";
	protected Path path;

	public Parser(Path path) {
		this.path = path;
	}
	
	//public abstract List<String> loadFile()throws IOException;
	public List<String> loadFile()throws IOException{
		return Files.readAllLines(path);
	}
	
}
