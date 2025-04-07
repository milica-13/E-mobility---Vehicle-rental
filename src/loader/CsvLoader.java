package loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvLoader extends Loader{

	//private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy.");
	
	public CsvLoader(Path path) {
		super(path);
	}
	
	@Override
	public List<String> loadFile() throws IOException{
		List<String> lines = Files.readAllLines(path);
        
        // preskacemo prvi red zaglavalja
        lines.remove(0);
        
        return lines;
	}
}
