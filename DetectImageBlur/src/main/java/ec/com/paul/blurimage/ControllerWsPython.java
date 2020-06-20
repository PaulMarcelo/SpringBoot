package ec.com.paul.blurimage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.opencv.core.Core;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

@RestController
public class ControllerWsPython {

	@PostMapping("/isBlurImage")
	public ResponseEntity<String> isBlurImage(@RequestParam("file") MultipartFile file) {

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

	private int THRESHOLD = 20;
	private int MAX_PERCENTAGE_VALUE_TO_SHARP = 100;
	private int MIN_PERCENTAGE_VALUE_TO_SHARP = 80;

	@PostMapping("/isBlurFromImageFile")
	public ResponseEntity<String> isBlurFromImageFile(@RequestParam("file") MultipartFile file) {
		String resp = "";
		try {
//			byte[] content = Utils.transformImage(file.getBytes());
			byte[] content = file.getBytes();

			String response = callWSBlur(file.getOriginalFilename(), content);

			Map<String, Object> map = getMapResponse(response);
			Double value = (Double) map.get("value");
			Boolean isblur = (Boolean) map.get("isblur");
			if (isblur) {
				if (getPercent(value) < MIN_PERCENTAGE_VALUE_TO_SHARP) {
					resp = "Imagen Borrosa, repetir foto";
				} else if (getPercent(value) < MAX_PERCENTAGE_VALUE_TO_SHARP) {
					String responseSharp = callWSSharp(file.getOriginalFilename(), content);
					Map<String, Object> mapSharp = getMapResponse(responseSharp);
					String filename = (String) mapSharp.get("filename");
					String data = (String) mapSharp.get("data");
					resp = "Aplicando filtro......... : " + filename;
				}
			} else {
				resp = "Imagen no borrosa";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(resp);

	}

	public String callWSSharp(String filename, byte[] content) throws HttpClientErrorException {
		String url = "http://127.0.0.1:8000/openVC/sharpFromImageFile/";
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", getFileEntity(filename, content));
		String response = callWSAndGetResponse(url, body);
		return response;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMapResponse(String response) throws Exception {
		Gson gson = new Gson();
		Map<String, Object> map = new HashMap<String, Object>();
		map = (Map<String, Object>) gson.fromJson(response, map.getClass());
		return map;
	}

	public String callWSBlur(String filename, byte[] content) throws HttpClientErrorException {
		String url = "http://127.0.0.1:8000/openVC/isBlurFromImageFile/";
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", getFileEntity(filename, content));
//		body.add("thresh", THRESHOLD);
//		body.add("radio", 60);
		String response = callWSAndGetResponse(url, body);
		return response;
	}

	public String callWSAndGetResponse(String url, MultiValueMap<String, Object> body) throws HttpClientErrorException {
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, getHeaders());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
		return response.getBody();
	}

	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		return headers;
	}

	public HttpEntity<byte[]> getFileEntity(String filename, byte[] content) {
		MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
		ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file").filename(filename)
				.build();
		fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
		HttpEntity<byte[]> fileEntity = new HttpEntity<>(content, fileMap);
		return fileEntity;
	}

	public double getPercent(double valor) {
		return (valor * 100) / THRESHOLD;
	}

	// @SuppressWarnings("unchecked")
	// @PostMapping("/isBlurFromImageFile")
	// public ResponseEntity<String> isBlurFromImageFile(@RequestParam("file")
	// MultipartFile file) {
	//
	// String resp = "";
	// try {
	// byte[] content = Utils.transformImage(file.getBytes());
	// byte[] encoded = Base64.getEncoder().encode(file.getBytes());
	// // String eee = new String(encoded);
	// HttpHeaders headers = new HttpHeaders();
	// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	// MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
	// ContentDisposition contentDisposition =
	// ContentDisposition.builder("form-data").name("file")
	// .filename(file.getOriginalFilename()).build();
	// fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
	// HttpEntity<byte[]> fileEntity = new HttpEntity<>(content, fileMap);
	// MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	// body.add("file", fileEntity);
	// body.add("thresh", 20);
	// body.add("radio", 60);
	// HttpEntity<MultiValueMap<String, Object>> requestEntity = new
	// HttpEntity<>(body, headers);
	// String serverUrl = "http://127.0.0.1:8000/openVC/isBlurFromImageFile/";
	// RestTemplate restTemplate = new RestTemplate();
	// ResponseEntity<String> response = restTemplate.postForEntity(serverUrl,
	// requestEntity, String.class);
	// System.out.println("Response: " + response.getBody());
	// Gson gson = new Gson();
	// Map<String, Object> map = new HashMap<String, Object>();
	// map = (Map<String, Object>) gson.fromJson(response.getBody(),
	// map.getClass());
	// System.out.println("Param1: " + map.get("filename"));
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (HttpClientErrorException e) {
	// e.printStackTrace();
	// }
	// return ResponseEntity.ok(resp);
	// }

}
