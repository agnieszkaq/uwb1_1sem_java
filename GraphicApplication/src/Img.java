import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Img {

	BufferedImage resize(double ratio, BufferedImage actualImage) {
		BufferedImage newImage = new BufferedImage((int) (actualImage.getWidth() * ratio),
				(int) (actualImage.getHeight() * ratio), BufferedImage.TYPE_INT_RGB);

		int[] pixel = new int[4];
		for (int i = 0; i < newImage.getWidth(); i++) {
			for (int j = 0; j < newImage.getHeight(); j++) {
				pixel = actualImage.getRaster().getPixel((int) (i / ratio), (int) (j / ratio), new int[4]);
				newImage.getRaster().setPixels(i, j, 1, 1, new int[] { pixel[0], pixel[1], pixel[2], pixel[3] });
			}
		}
		return newImage;
	}

	void updateImage(int[] LUT, BufferedImage actualImage, JPanel imagePane) {
		int[] pixel = new int[4];
		for (int i = 0; i < actualImage.getWidth(); i++) {
			for (int j = 0; j < actualImage.getHeight(); j++) {
				pixel = actualImage.getRaster().getPixel(i, j, new int[4]);
				actualImage.getRaster().setPixels(i, j, 1, 1,
						new int[] { LUT[pixel[0]], LUT[pixel[1]], LUT[pixel[2]], LUT[pixel[3]] });
			}
		}
		imagePane.repaint();
	}
}
