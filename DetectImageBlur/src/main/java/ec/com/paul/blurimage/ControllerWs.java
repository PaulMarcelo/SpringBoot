package ec.com.paul.blurimage;

import java.io.IOException;
import org.opencv.core.Core;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ControllerWs {

	@PostMapping("/isBlurImage")
	public ResponseEntity<String> isBlurImage(@RequestParam("file") MultipartFile file) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String resp = "";
		try {
			byte[] content = Utils.transformImage(file.getBytes());
			if (Utils.isBlurImage(content, file.getOriginalFilename())) {
				resp = "TRUE";
			} else {
				resp = "FALSE";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(resp);
	}

}
