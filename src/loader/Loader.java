package loader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public abstract class Loader {
	protected Path path;
	
	public Loader(Path path) {
		this.path = path;
	}
	
	public abstract List<String> loadFile()throws IOException;
}
