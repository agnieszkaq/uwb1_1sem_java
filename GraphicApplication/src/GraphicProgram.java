import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Font;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.InputEvent;

public class GraphicProgram extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton[] buttons = new JButton[7];
	private int licznik = 1;
	private JButton grayTwo, grayOne, filterOK;
	private BufferedImage actualImage, originalImage;
	private JLabel colorRGB, colorPreview, actualImageScale, brightness, kontrast;
	private JFileChooser chooser, saveChooser;
	private JScrollPane scrollImagePane;
	private JSlider zoomSlider, brightnessSlider, kontrastSlider;
	private JTextField[] dzialania = new JTextField[4];
	private JPanel contentPane, configurationPane, imagePane;
	private JMenuBar menuBar;
	private JMenu mPlik, mObraz, mOkno;
	private JMenuItem miOpenImage, miSaveImage, miResetImage, miExit, miMedian3, miMedian5;
	private JTextField filterMask[][] = new JTextField[3][3];
	private JComboBox<Object> filterType;

	int id = 0, positionX, positionY, oHeightCheck = 0, oWidthCheck = 0, circleRCheck, lineXCheck = 0, lineYCheck = 0,
			doAddidngCheck = 0, doSubtractionCheck = 0, mnozonaCheck = 1, dzielonaCheck = 1, iWidth, iHeight;
	private Robot robot;

	public GraphicProgram() {
		super("Linia");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1350, 700);
		setResizable(false);
		MyActionListener actionListener = new MyActionListener();
		MyMouseListener mouseListener = new MyMouseListener();
		MyMouseMotionListener mouseMotionListener = new MyMouseMotionListener();
		MyKeyListener keyListener = new MyKeyListener();

		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);

		mPlik = new JMenu("Plik");
		menuBar.add(mPlik);

		miOpenImage = new JMenuItem("Otwórz obraz");
		miOpenImage.setBackground(Color.WHITE);
		miOpenImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		miOpenImage.addActionListener(actionListener);
		mPlik.add(miOpenImage);

		miSaveImage = new JMenuItem("Zapisz obraz");
		miSaveImage.setBackground(Color.WHITE);
		miSaveImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		miSaveImage.addActionListener(actionListener);
		mPlik.add(miSaveImage);
		mPlik.addSeparator();

		miResetImage = new JMenuItem("Zresetuj obraz");
		miResetImage.setBackground(Color.WHITE);
		miResetImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		miResetImage.addActionListener(actionListener);
		mPlik.add(miResetImage);
		mPlik.addSeparator();

		miExit = new JMenuItem("Zakończ");
		miExit.setBackground(Color.WHITE);
		miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		miExit.addActionListener(actionListener);
		mPlik.add(miExit);

		mObraz = new JMenu("Obraz");
		mObraz.setBackground(Color.DARK_GRAY);
		menuBar.add(mObraz);

		mOkno = new JMenu("Okno");
		mOkno.setBackground(Color.DARK_GRAY);
		menuBar.add(mOkno);
		mObraz.addSeparator();

		JMenu mnFiltry = new JMenu("Filtry");
		mnFiltry.setBackground(Color.WHITE);
		mObraz.add(mnFiltry);

		mnFiltry.addSeparator();

		miMedian3 = new JMenuItem("Filtr medianowy 3x3");
		miMedian3.setBackground(Color.WHITE);
		miMedian3.addActionListener(actionListener);
		mnFiltry.add(miMedian3);

		miMedian5 = new JMenuItem("Filtr medianowy 5x5");
		miMedian5.setBackground(Color.WHITE);
		miMedian5.addActionListener(actionListener);
		mnFiltry.add(miMedian5);

		setJMenuBar(menuBar);

		configurationPane = new JPanel();
		configurationPane.setBackground(Color.WHITE);
		configurationPane.setBounds(0, 0, 300, 700);
		configurationPane.setLayout(null);
		contentPane.add(configurationPane);

		zoomSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 1);
		zoomSlider.setFont(new Font("Tahoma", Font.PLAIN, 11));
		zoomSlider.setBackground(Color.WHITE);
		zoomSlider.setValue(100);
		zoomSlider.setBounds(25, 105, 250, 40);
		zoomSlider.setMinorTickSpacing(25);
		zoomSlider.setMajorTickSpacing(100);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(true);
		zoomSlider.addMouseMotionListener(mouseMotionListener);
		zoomSlider.addMouseListener(mouseListener);
		configurationPane.add(zoomSlider);

		imagePane = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(actualImage, 0, 0, null);
			}
		};

		imagePane.setLayout(null);
		imagePane.addMouseMotionListener(mouseMotionListener);
		imagePane.addMouseListener(mouseListener);

		scrollImagePane = new JScrollPane(imagePane);
		scrollImagePane.setPreferredSize(new Dimension(1045, 650));
		scrollImagePane.setBorder(null);
		scrollImagePane.setBounds(300, 0, 1045, 650);
		scrollImagePane.setAutoscrolls(true);
		contentPane.add(scrollImagePane);

		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF & PNG Images", "jpg", "gif", "png");
		chooser.setFileFilter(filter);
		saveChooser = new JFileChooser();

		colorRGB = new JLabel("Kolor RGB: ---");
		colorRGB.setFont(new Font("Arial", Font.PLAIN, 12));
		colorRGB.setBounds(75, 25, 250, 40);
		configurationPane.add(colorRGB);

		colorPreview = new JLabel();
		colorPreview.setBounds(25, 25, 40, 40);
		colorPreview.setOpaque(true);
		colorPreview.setBackground(Color.WHITE);
		configurationPane.add(colorPreview);

		actualImageScale = new JLabel("Rozmiar obrazu: 100%");
		actualImageScale.setFont(new Font("Arial", Font.PLAIN, 12));
		actualImageScale.setBounds(25, 85, 250, 12);
		configurationPane.add(actualImageScale);

		brightness = new JLabel("Jasność: 100%");
		brightness.setFont(new Font("Arial", Font.PLAIN, 12));
		brightness.setBounds(25, 145, 250, 40);
		configurationPane.add(brightness);

		kontrast = new JLabel("Kontrast: 100%");
		kontrast.setFont(new Font("Arial", Font.PLAIN, 12));
		kontrast.setBounds(25, 220, 250, 40);
		configurationPane.add(kontrast);

		brightnessSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 1);
		brightnessSlider.setFont(new Font("Tahoma", Font.PLAIN, 11));
		brightnessSlider.setBackground(Color.WHITE);
		brightnessSlider.setValue(100);
		brightnessSlider.setBounds(25, 180, 250, 40);
		brightnessSlider.setMinorTickSpacing(25);
		brightnessSlider.setMajorTickSpacing(100);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setPaintLabels(true);
		brightnessSlider.addMouseMotionListener(mouseMotionListener);
		brightnessSlider.addMouseListener(mouseListener);
		configurationPane.add(brightnessSlider);

		kontrastSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 1);
		kontrastSlider.setFont(new Font("Tahoma", Font.PLAIN, 11));
		kontrastSlider.setBackground(Color.WHITE);
		kontrastSlider.setValue(100);
		kontrastSlider.setBounds(25, 255, 250, 40);
		kontrastSlider.setMinorTickSpacing(25);
		kontrastSlider.setMajorTickSpacing(100);
		kontrastSlider.setPaintTicks(true);
		kontrastSlider.setPaintLabels(true);
		kontrastSlider.addMouseMotionListener(mouseMotionListener);
		kontrastSlider.addMouseListener(mouseListener);
		configurationPane.add(kontrastSlider);

		grayOne = new JButton("szarość- sposób 1");
		grayOne.setFont(new Font("Tahoma", Font.PLAIN, 10));
		grayOne.setBorderPainted(false);
		grayOne.setBackground(Color.ORANGE);
		grayOne.setBounds(15, 320, 130, 30);
		grayOne.addActionListener(actionListener);
		configurationPane.add(grayOne);
		grayOne.addMouseListener(mouseListener);

		grayTwo = new JButton("szarość- sposób 2");
		grayTwo.setFont(new Font("Tahoma", Font.PLAIN, 10));
		grayTwo.setBorderPainted(false);
		grayTwo.setBackground(Color.ORANGE);
		grayTwo.setBounds(155, 320, 130, 30);
		grayTwo.addActionListener(actionListener);
		configurationPane.add(grayTwo);
		grayTwo.addMouseListener(mouseListener);

		buttons[0] = new JButton("dodaj");
		buttons[1] = new JButton("odejmnij");
		buttons[2] = new JButton("pomnoż");
		buttons[3] = new JButton("przedziel");

		buttons[0] = new JButton("dodaj");
		buttons[0].setBounds(15, 360, 70, 30);

		buttons[1] = new JButton("odejmij");
		buttons[1].setBounds(155, 360, 70, 30);

		buttons[2] = new JButton("pomnoż");
		buttons[2].setBounds(15, 400, 70, 30);

		buttons[3] = new JButton("dziel");
		buttons[3].setBounds(155, 400, 70, 30);

		for (int i = 0; i < 4; i++) {
			buttons[i].setFont(new Font("Tahoma", Font.PLAIN, 10));
			buttons[i].setBorderPainted(false);
			buttons[i].setBackground(Color.ORANGE);
			buttons[i].addMouseListener(mouseListener);
			configurationPane.add(buttons[i]);

		}

		for (int i = 0; i < 4; i++) {
			dzialania[i] = new JTextField("1");
			dzialania[i].setBackground(Color.WHITE);
			dzialania[i].setFont(new Font("Tahoma", Font.PLAIN, 11));
			dzialania[i].addMouseListener(mouseListener);
			dzialania[i].addKeyListener(keyListener);
			configurationPane.add(dzialania[i]);
		}

		dzialania[0].setBounds(90, 360, 40, 30);
		dzialania[1].setBounds(230, 360, 40, 30);
		dzialania[2].setBounds(90, 400, 40, 30);
		dzialania[3].setBounds(230, 400, 40, 30);

		JLabel lblFiltrowanieMaska = new JLabel("filtry: ");
		lblFiltrowanieMaska.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFiltrowanieMaska.setBounds(25, 495, 250, 40);
		configurationPane.add(lblFiltrowanieMaska);

		filterOK = new JButton("OK");
		filterOK.setFont(new Font("Tahoma", Font.PLAIN, 12));
		filterOK.setBorderPainted(false);
		filterOK.setBackground(Color.RED);
		filterOK.setBounds(183, 600, 87, 25);
		filterOK.addActionListener(actionListener);
		configurationPane.add(filterOK);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(25, 536, 150, 65);
		configurationPane.add(panel);
		panel.setLayout(new GridLayout(3, 3, 0, 0));

		filterType = new JComboBox<Object>();
		filterType.setModel(new DefaultComboBoxModel<Object>(new String[] { "Wygładzający (uśredniający)", "Sobel",
				"Górnoprzepustowy", "Rozmycie Gaussowskie", "Splot maski" }));
		filterType.setBackground(Color.WHITE);
		filterType.setBounds(75, 500, 165, 25);
		filterType.addActionListener(actionListener);
		configurationPane.add(filterType);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				filterMask[i][j] = new JTextField("1");
				panel.add(filterMask[i][j]);
			}
		}
	}

	public int getThisX(MouseEvent e) {
		return e.getX();
	}

	public int getThisY(MouseEvent e) {
		return e.getY();
	}

	private void getAddedNumber() {
		doAddidngCheck = Integer.parseInt(dzialania[0].getText());
	}

	private void getReducedNumber() {
		doSubtractionCheck = Integer.parseInt(dzialania[1].getText());
	}

	private void getMultipliedNumber() {
		mnozonaCheck = Integer.parseInt(dzialania[2].getText());
	}

	private void getDividedNumber() {
		dzielonaCheck = Integer.parseInt(dzialania[3].getText());
	}

	private class ImageHelper {

		private void resize(double ratio) {
			BufferedImage newImage = new BufferedImage((int) (actualImage.getWidth() * ratio),
					(int) (actualImage.getHeight() * ratio), BufferedImage.TYPE_INT_RGB);

			int[] pixel = new int[4];
			for (int i = 0; i < newImage.getWidth(); i++) {
				for (int j = 0; j < newImage.getHeight(); j++) {
					pixel = actualImage.getRaster().getPixel((int) (i / ratio), (int) (j / ratio), new int[4]);
					newImage.getRaster().setPixels(i, j, 1, 1, new int[] { pixel[0], pixel[1], pixel[2], pixel[3] });
				}
			}
			actualImage = newImage;
		}

		private void reset() {
			actualImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics g = actualImage.createGraphics();
			g.drawImage(originalImage, 0, 0, null);

		}

		private void updateImage(int[] LUT) {
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

	ImageHelper imageHelper = new ImageHelper();

	private class Filter {
		private void greyscale() {
			int[] pixel = new int[4];
			for (int i = 0; i < actualImage.getWidth(); i++) {
				for (int j = 0; j < actualImage.getHeight(); j++) {
					pixel = actualImage.getRaster().getPixel(i, j, new int[4]);
					int gs = (pixel[0] + pixel[1] + pixel[2]) / 3;

					actualImage.getRaster().setPixels(i, j, 1, 1, new int[] { gs, gs, gs });
				}
			}
			imagePane.repaint();
		}

		private void greyscaleTwo() {
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

		private void doAddidng() {
			getAddedNumber();
			int[] LUT = new int[256];

			for (int i = 0; i < 256; i++) {
				double newValue = i + doAddidngCheck;
				if (newValue > 255)
					LUT[i] = 255;
				else if (newValue < 0)
					LUT[i] = 0;
				else
					LUT[i] = (int) newValue;
			}
			imageHelper.updateImage(LUT);
		}

		private void doSubtraction() {
			getReducedNumber();
			int[] LUT = new int[256];

			for (int i = 0; i < 256; i++) {
				double newValue = i - doSubtractionCheck;
				if (newValue > 255)
					LUT[i] = 255;
				else if (newValue < 0)
					LUT[i] = 0;
				else
					LUT[i] = (int) newValue;
			}
			imageHelper.updateImage(LUT);
		}

		private void doMultiplication() {
			getMultipliedNumber();
			int[] LUT = new int[256];

			for (int i = 0; i < 256; i++) {
				double newValue = i * mnozonaCheck;
				if (newValue > 255)
					LUT[i] = 255;
				else if (newValue < 0)
					LUT[i] = 0;
				else
					LUT[i] = (int) newValue;
			}
			imageHelper.updateImage(LUT);
		}

		private void doDivision() {
			getDividedNumber();
			int[] LUT = new int[256];

			for (int i = 0; i < 256; i++) {
				double newValue = i / dzielonaCheck;
				if (newValue > 255)
					LUT[i] = 255;
				else if (newValue < 0)
					LUT[i] = 0;
				else
					LUT[i] = (int) newValue;
			}
			imageHelper.updateImage(LUT);

		}

		public void brightness(double brightnessValue) {
			int[] LUT = new int[256];

			for (int i = 0; i < 256; i++) {
				double newValue = i + brightnessValue * 10;
				if (newValue > 255)
					LUT[i] = 255;
				else if (newValue < 0)
					LUT[i] = 0;
				else
					LUT[i] = (int) newValue;
			}
			imageHelper.updateImage(LUT);
		}

		public void kontrast(double kontrastValue) {
			int[] LUT = new int[256];

			for (int i = 0; i < 256; i++) {
				double newValue = (kontrastValue) * (i - (255 / 2) + 255 / 2);
				if (newValue > 255)
					LUT[i] = 255;
				else if (newValue < 0)
					LUT[i] = 0;
				else
					LUT[i] = (int) newValue;
			}
			imageHelper.updateImage(LUT);
		}

		private void runFilter() {
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

			actualImage = newImage;
			imagePane.repaint();
		}

		private void medianFilter(int size) {
			BufferedImage newImage = new BufferedImage(actualImage.getWidth(), actualImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics g = newImage.createGraphics();
			g.drawImage(actualImage, 0, 0, null);

			int margin = ((size - 1) / 2);
			for (int i = margin; i < actualImage.getWidth() - margin; i++) {
				for (int j = margin; j < actualImage.getHeight() - margin; j++) {
					int pxRed[] = new int[size * size];
					int pxGreen[] = new int[size * size];
					int pxBlue[] = new int[size * size];
					int a = 0;

					for (int k = i - margin; k <= i + margin; k++) {
						for (int l = j - margin; l <= j + margin; l++) {
							pxRed[a] = actualImage.getRaster().getPixel(k, l, new int[4])[0];
							pxGreen[a] = actualImage.getRaster().getPixel(k, l, new int[4])[1];
							pxBlue[a] = actualImage.getRaster().getPixel(k, l, new int[4])[2];
							a++;
						}
					}
					Arrays.sort(pxRed);
					Arrays.sort(pxGreen);
					Arrays.sort(pxBlue);
					newImage.getRaster().setPixels(i, j, 1, 1,
							new int[] { pxRed[(a + 1) / 2], pxGreen[(a + 1) / 2], pxBlue[(a + 1) / 2] });
				}
			}

			actualImage = newImage;
			imagePane.repaint();
		}
	}

	Filter filter = new Filter();

	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == miOpenImage) {
				int returnVal = chooser.showOpenDialog(configurationPane);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						actualImage = ImageIO.read(chooser.getSelectedFile());
						originalImage = ImageIO.read(chooser.getSelectedFile());
					} catch (IOException f) {
						f.printStackTrace();
					}

					iWidth = actualImage.getWidth();
					iHeight = actualImage.getHeight();
					imagePane.setPreferredSize(new Dimension(actualImage.getWidth() * zoomSlider.getValue() / 100,
							actualImage.getHeight() * zoomSlider.getValue() / 100));
					imagePane.revalidate();
					imagePane.repaint();
				}
			} else if (e.getSource() == miSaveImage) {
				int returnVal = saveChooser.showSaveDialog(configurationPane);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						ImageIO.write(actualImage, "PNG", new File(saveChooser.getSelectedFile() + ".png"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} else if (e.getSource() == miResetImage || licznik == 3) {
				imageHelper.reset();
				zoomSlider.setValue(100);
				brightnessSlider.setValue(100);
				kontrastSlider.setValue(100);
				imagePane.repaint();
			} else if (e.getSource() == miMedian3) {
				filter.medianFilter(3);
			} else if (e.getSource() == miMedian5) {
				filter.medianFilter(5);
			} else if (e.getSource() == filterOK) {
				filter.runFilter();
			} else if (e.getSource() == filterType) {
				if (filterType.getSelectedItem().equals("Wygładzający (uśredniający)")) {
					filterMask[0][0].setText("1");
					filterMask[0][1].setText("1");
					filterMask[0][2].setText("1");
					filterMask[1][0].setText("1");
					filterMask[1][1].setText("1");
					filterMask[1][2].setText("1");
					filterMask[2][0].setText("1");
					filterMask[2][1].setText("1");
					filterMask[2][2].setText("1");
				} else if (filterType.getSelectedItem().equals("Sobel")) {
					filterMask[0][0].setText("1");
					filterMask[0][1].setText("2");
					filterMask[0][2].setText("1");
					filterMask[1][0].setText("0");
					filterMask[1][1].setText("0");
					filterMask[1][2].setText("0");
					filterMask[2][0].setText("-1");
					filterMask[2][1].setText("-2");
					filterMask[2][2].setText("-1");
				} else if (filterType.getSelectedItem().equals("Górnoprzepustowy")) {
					filterMask[0][0].setText("-1");
					filterMask[0][1].setText("-1");
					filterMask[0][2].setText("-1");
					filterMask[1][0].setText("-1");
					filterMask[1][1].setText("9");
					filterMask[1][2].setText("-1");
					filterMask[2][0].setText("-1");
					filterMask[2][1].setText("-1");
					filterMask[2][2].setText("-1");
				} else if (filterType.getSelectedItem().equals("Rozmycie Gaussowskie")) {
					filterMask[0][0].setText("1");
					filterMask[0][1].setText("2");
					filterMask[0][2].setText("1");
					filterMask[1][0].setText("2");
					filterMask[1][1].setText("4");
					filterMask[1][2].setText("2");
					filterMask[2][0].setText("1");
					filterMask[2][1].setText("2");
					filterMask[2][2].setText("1");
				} else if (filterType.getSelectedItem().equals("Splot maski")) {
					filterMask[0][0].setText("0");
					filterMask[0][1].setText("0");
					filterMask[0][2].setText("0");
					filterMask[1][0].setText("0");
					filterMask[1][1].setText("0");
					filterMask[1][2].setText("0");
					filterMask[2][0].setText("0");
					filterMask[2][1].setText("0");
					filterMask[2][2].setText("0");
				}

			}
		}
	}

	private class MyMouseMotionListener implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			if (e.getSource() == zoomSlider) {
				actualImageScale.setText("Rozmiar obrazu: " + zoomSlider.getValue() + "%");
			} else if (e.getSource() == brightnessSlider) {
				brightness.setText("Jasność: " + brightnessSlider.getValue() + "%");
			} else if (e.getSource() == kontrastSlider) {
				kontrast.setText("Kontrast: " + kontrastSlider.getValue() + "%");
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			System.out.println(e.getX() + " " + e.getY());
			try {
				robot = new Robot();
			} catch (AWTException e1) {
				e1.printStackTrace();
			}

			Color pixelColor = robot.getPixelColor(e.getXOnScreen(), e.getYOnScreen());
			colorRGB.setText("Kolor RGB: [" + Integer.toString(pixelColor.getRed()) + " "
					+ Integer.toString(pixelColor.getGreen()) + " " + Integer.toString(pixelColor.getBlue()) + "]");
			colorPreview.setBackground(pixelColor);
		}
	}

	private class MyMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == imagePane) {
				imageHelper.reset();
				zoomSlider.setValue(100);
				actualImageScale.setText("Rozmiar obrazu: 100% ");
				brightnessSlider.setValue(100);
				brightness.setText("Jasność: 100% ");
				kontrastSlider.setValue(100);
				kontrast.setText("Kontrast: 100% ");
				imagePane.repaint();

			}
			if (e.getSource() == grayOne) {
				filter.greyscale();
			}
			if (e.getSource() == grayTwo) {
				filter.greyscaleTwo();
			}
			if (e.getSource() == buttons[0]) {
				filter.doAddidng();
			}
			if (e.getSource() == buttons[1]) {
				filter.doSubtraction();
			}
			if (e.getSource() == buttons[2]) {
				filter.doMultiplication();
			}
			if (e.getSource() == buttons[3]) {
				filter.doDivision();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getSource() == zoomSlider) {
				imageHelper.resize((double) zoomSlider.getValue() / 100);
				imagePane.setPreferredSize(new Dimension((int) ((double) iWidth * zoomSlider.getValue() / 100),
						(int) ((double) iHeight * zoomSlider.getValue() / 100)));

				imagePane.repaint();
			} else if (e.getSource() == brightnessSlider && actualImage != null) {
				filter.brightness((double) brightnessSlider.getValue() / 100);
				imagePane.repaint();
			} else if (e.getSource() == kontrastSlider && actualImage != null) {
				filter.kontrast((double) kontrastSlider.getValue() / 100);
				imagePane.repaint();
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}

	private class MyKeyListener implements KeyListener {
		@Override
		public void keyTyped(java.awt.event.KeyEvent evt) {
			char c = evt.getKeyChar();
			if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
				evt.consume();
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
		}
	}
}