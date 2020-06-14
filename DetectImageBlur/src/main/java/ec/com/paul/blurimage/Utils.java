package ec.com.paul.blurimage;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDouble;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Utils {
	static final int THRESHOLD = 200;

	public static boolean isBlurImage(byte[] arrayImage, String path) {
		Mat image = Imgcodecs.imdecode(new MatOfByte(arrayImage), Imgcodecs.IMREAD_UNCHANGED);
		// Mat image = Imgcodecs.imread(location); // location path local imagen
		Mat destination = new Mat();
		Mat matGray = new Mat();
		Imgproc.cvtColor(image, matGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Laplacian(matGray, destination, 3);
		MatOfDouble median = new MatOfDouble();
		MatOfDouble std = new MatOfDouble();
		Core.meanStdDev(destination, median, std);
		double laplacianVar = redondearDecimales(Math.pow(std.get(0, 0)[0], 2), 2);
		boolean resp = laplacianVar < THRESHOLD ? true : false;
		System.out.println(
				"Imagen: " + path + " => Varianza: " + laplacianVar + " => " + (resp ? "Blurry" : "Not Blurry"));
		return resp;
	}

	private static double redondearDecimales(double valorInicial, int numeroDecimales) {
		double parteEntera, resultado;
		resultado = valorInicial;
		parteEntera = Math.floor(resultado);
		resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
		resultado = Math.round(resultado);
		resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
		return resultado;
	}

	public static byte[] transformImage(byte[] content) throws IOException {
		InputStream in = new ByteArrayInputStream(content);
		BufferedImage bImageFromConvert = ImageIO.read(in);
		BufferedImage imageTransform = getScaledImage(bImageFromConvert, 500, 500);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(imageTransform, "jpg", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}

	private static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		double scaleX = (double) width / imageWidth;
		double scaleY = (double) height / imageHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
		return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
	}

}
