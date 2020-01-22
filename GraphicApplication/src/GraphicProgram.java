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
	private static final long serialVersionUID = 1L;

	private BufferedImage actualImage, originalImage;
	private JButton[] buttons = new JButton[7];
	private JButton grayTwo, grayOne, filterOK;
	private JComboBox<Object> filterType;
	private JFileChooser chooser, saveChooser;
	private JLabel colorRGB, colorPreview, actualImageScale, brightness, contrast;
	private JMenu mPlik;
	private JMenuBar menuBar;
	private JMenuItem miOpenImage, miSaveImage, miResetImage, miExit;
	private JPanel contentPane, configurationPane, imagePane;
	private JScrollPane scrollImagePane;
	private JSlider zoomSlider, brightnessSlider, contrastSlider;
	private JTextField[] numberOperation = new JTextField[4];
	private JTextField filterMask[][] = new JTextField[3][3];
	private Robot robot;

	int id = 0, positionX, positionY, oHeightCheck = 0, oWidthCheck = 0, circleRCheck, lineXCheck = 0, lineYCheck = 0,
			doAddidngCheck = 0, doSubtractionCheck = 0, multipledCheck = 1, dividedCheck = 1, iWidth, iHeight;

	Img img = new Img();
	Filters filters = new Filters();

	public GraphicProgram() {
		super("Program graficzny");
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

		contrast = new JLabel("contrast: 100%");
		contrast.setFont(new Font("Arial", Font.PLAIN, 12));
		contrast.setBounds(25, 220, 250, 40);
		configurationPane.add(contrast);

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

		contrastSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 1);
		contrastSlider.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contrastSlider.setBackground(Color.WHITE);
		contrastSlider.setValue(100);
		contrastSlider.setBounds(25, 255, 250, 40);
		contrastSlider.setMinorTickSpacing(25);
		contrastSlider.setMajorTickSpacing(100);
		contrastSlider.setPaintTicks(true);
		contrastSlider.setPaintLabels(true);
		contrastSlider.addMouseMotionListener(mouseMotionListener);
		contrastSlider.addMouseListener(mouseListener);
		configurationPane.add(contrastSlider);

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
			numberOperation[i] = new JTextField("1");
			numberOperation[i].setBackground(Color.WHITE);
			numberOperation[i].setFont(new Font("Tahoma", Font.PLAIN, 11));
			numberOperation[i].addMouseListener(mouseListener);
			numberOperation[i].addKeyListener(keyListener);
			configurationPane.add(numberOperation[i]);
		}

		numberOperation[0].setBounds(90, 360, 40, 30);
		numberOperation[1].setBounds(230, 360, 40, 30);
		numberOperation[2].setBounds(90, 400, 40, 30);
		numberOperation[3].setBounds(230, 400, 40, 30);

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

	public BufferedImage getActualImage() {
		return actualImage;
	}

	public void setActualImage(BufferedImage actualImage) {
		this.actualImage = actualImage;
	}

	private Double getAddedNumber() {
		return Double.parseDouble(numberOperation[0].getText());
	}

	private Double getReducedNumber() {
		return Double.parseDouble(numberOperation[1].getText());
	}

	private Double getMultipliedNumber() {
		return Double.parseDouble(numberOperation[2].getText());
	}

	private Double getDividedNumber() {
		return Double.parseDouble(numberOperation[3].getText());
	}

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
			} else if (e.getSource() == miResetImage) {
				setActualImage(originalImage);
				zoomSlider.setValue(100);
				brightnessSlider.setValue(100);
				contrastSlider.setValue(100);
				imagePane.repaint();

			} else if (e.getSource() == filterOK) {
				actualImage = filters.runFilter(actualImage, imagePane, filterMask);

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

			} else if (e.getSource() == contrastSlider) {
				contrast.setText("Kontrast: " + contrastSlider.getValue() + "%");
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
			setActualImage(originalImage);
				zoomSlider.setValue(100);
				actualImageScale.setText("Rozmiar obrazu: 100% ");
				brightnessSlider.setValue(100);
				brightness.setText("Jasność: 100% ");
				contrastSlider.setValue(100);
				contrast.setText("Kontrast: 100% ");
				imagePane.repaint();
			}
			if (e.getSource() == grayOne) {
				filters.greyscale(actualImage, imagePane);
			}
			if (e.getSource() == grayTwo) {
				filters.greyscaleTwo(actualImage, imagePane);
			}
			if (e.getSource() == buttons[0]) {
				filters.mathMethod(img, actualImage, imagePane, getAddedNumber(), '+');
			}
			if (e.getSource() == buttons[1]) {
				filters.mathMethod(img, actualImage, imagePane, getReducedNumber(), '-');
			}
			if (e.getSource() == buttons[2]) {
				filters.mathMethod(img, actualImage, imagePane, getMultipliedNumber(), '*');
			}
			if (e.getSource() == buttons[3]) {
				filters.mathMethod(img, actualImage, imagePane, getDividedNumber(), '/');
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getSource() == zoomSlider) {
				double zoomValue = (double) zoomSlider.getValue() / 100;
				setActualImage(img.resize(zoomValue, actualImage));

				imagePane.setPreferredSize(new Dimension((int) ((double) iWidth * zoomSlider.getValue() / 100),
						(int) ((double) iHeight * zoomSlider.getValue() / 100)));
				imagePane.repaint();

			} else if (e.getSource() == brightnessSlider && actualImage != null) {
				double brightnessValue = (double) brightnessSlider.getValue() / 100;
				filters.mathMethod(img, actualImage, imagePane, brightnessValue, 'b');
				imagePane.repaint();

			} else if (e.getSource() == contrastSlider && actualImage != null) {
				double contrastValue = (double) contrastSlider.getValue() / 100;
				filters.mathMethod(img, actualImage, imagePane, contrastValue, 'c');
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