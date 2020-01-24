import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Filters {

	void greyscale(BufferedImage actualImage, JPanel imagePane) {
		int[] pixel = new int[4];
		for (int i = 0; i < actualImage.getWidth(); i++) {
			for (int j = 0; j < actualImage.getHeight(); j++) {
				pixel = actualImage.getRaster().getPixel(i, j, new int[4]);

				int gs = (pixel[0] + pixel[1] + pixel[2]) / 3;
				actualImage.getRaster().setPixels(i, j, 1, 1, new int[] { (int) gs, (int) gs, (int) gs});
			}
		}
		imagePane.repaint();
	}

	void greyscaleTwo(BufferedImage actualImage, JPanel imagePane) {
		int[] pixel = new int[4];
		for (int i = 0; i < actualImage.getWidth(); i++) {
			for (int j = 0; j < actualImage.getHeight(); j++) {
				pixel = actualImage.getRaster().getPixel(i, j, new int[4]);

				double gs = (pixel[0] * 0.3 + pixel[1] * 0.59 + pixel[2] * 0.11);
				actualImage.getRaster().setPixels(i, j, 1, 1, new int[] { (int) gs, (int) gs, (int) gs });
			}
		}
		imagePane.repaint();
	}

	void mathMethod(Img img, BufferedImage actualImage, JPanel imagePane, Double number, char method) {
		int[] LUT = new int[256];

		for (int i = 0; i < 256; i++) {
			double newValue = 0;
			switch (method) {
			case '+':
				newValue = i + number;
				break;
			case '-':
				newValue = i - number;
				break;
			case '*':
				newValue = i * number;
				break;
			case '/':
				newValue = i / number;
				break;
			case 'b':
				newValue = i + number * 10;
				break;
			case 'c':
				newValue = (number) * (i - (255 / 2) + 255 / 2);
				break;
			}

			if (newValue > 255)
				LUT[i] = 255;
			else if (newValue < 0)
				LUT[i] = 0;
			else
				LUT[i] = (int) newValue;
		}
		img.updateImage(LUT, actualImage, imagePane);
	}

	BufferedImage runFilter(BufferedImage actualImage, JPanel imagePane, JTextField filterMask[][]) {
		int maskSum = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				maskSum += Integer.parseInt(filterMask[i][j].getText());
			}
		}
		BufferedImage newImage = new BufferedImage(actualImage.getWidth(), actualImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = newImage.createGraphics();
		g.drawImage(actualImage, 0, 0, null);

		for (int i = 1; i < actualImage.getWidth() - 1; i++) {
			for (int j = 1; j < actualImage.getHeight() - 1; j++) {
				int redSum = 0, greenSum = 0, blueSum = 0;

				for (int k = i - 1; k <= i + 1; k++) {
					for (int l = j - 1; l <= j + 1; l++) {
						redSum += actualImage.getRaster().getPixel(k, l, new int[4])[0]
								* Integer.parseInt(filterMask[i + 1 - k][j + 1 - l].getText());
						greenSum += actualImage.getRaster().getPixel(k, l, new int[4])[1]
								* Integer.parseInt(filterMask[i + 1 - k][j + 1 - l].getText());
						blueSum += actualImage.getRaster().getPixel(k, l, new int[4])[2]
								* Integer.parseInt(filterMask[i + 1 - k][j + 1 - l].getText());
					}
				}
				if (maskSum != 0) {
					redSum /= maskSum;
					greenSum /= maskSum;
					blueSum /= maskSum;
				}
				if (redSum > 255)
					redSum = 255;

				else if (redSum < 0)
					redSum = 0;

				if (greenSum > 255)
					greenSum = 255;

				else if (greenSum < 0)
					greenSum = 0;
				
				if (blueSum > 255)
					blueSum = 255;
				
				else if (blueSum < 0)
					blueSum = 0;

				newImage.getRaster().setPixels(i, j, 1, 1, new int[] { redSum, greenSum, blueSum });
			}
		}
		imagePane.repaint();
		return newImage;
	}
}
