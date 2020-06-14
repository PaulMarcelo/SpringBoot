package ec.com.paul.blurimage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/console")
public class ControllerConsole {

	final File dir = new File("path folder image");
	final String[] EXTENSIONS = new String[] { "jpg" };
	final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
		@Override
		public boolean accept(final File dir, final String name) {
			for (final String ext : EXTENSIONS) {
				if (name.endsWith("." + ext)) {
					return (true);
				}
			}
			return (false);
		}
	};

	List<String> listPathImage = new ArrayList<String>();

	@GetMapping("/blurImage")
	public void listar() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try {
			fillListFile();
			System.out.println("*********************CHECK IMAGE*************************************");
			for (String location : listPathImage) {
				// String location = "C:\\0.jpg";
				byte[] array = Files.readAllBytes(Paths.get(location));
				Utils.isBlurImage(array, location);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("**********************************************************");
	}

	private void fillListFile() throws IOException {
		if (dir.isDirectory()) {
			for (final File f : dir.listFiles(IMAGE_FILTER)) {
				listPathImage.add(f.getAbsolutePath());
			}
		}
	}

}
