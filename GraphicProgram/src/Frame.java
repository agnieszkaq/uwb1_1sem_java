import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

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
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Frame extends JFrame implements ActionListener, MouseMotionListener, MouseListener, KeyListener {
	private static final int FRAME_WIDTH = 1350;
	private static final int FRAME_HIGH = 700;
	private BufferedImage actualImage, originalImage;

	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HIGH);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);

		JMenu mPlik = new JMenu("Plik");
		menuBar.add(mPlik);

		JMenuItem miOpenImage = new JMenuItem("Otwórz obraz");
		miOpenImage.setBackground(Color.WHITE);
		miOpenImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		miOpenImage.addActionListener((ActionListener) this);
		mPlik.add(miOpenImage);

		JMenuItem miSaveImage = new JMenuItem("Zapisz obraz");
		miSaveImage.setBackground(Color.WHITE);
		miSaveImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		miSaveImage.addActionListener((ActionListener) this);
		mPlik.add(miSaveImage);
		mPlik.addSeparator();

		JMenuItem miResetImage = new JMenuItem("Zresetuj obraz");
		miResetImage.setBackground(Color.WHITE);
		miResetImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		miResetImage.addActionListener((ActionListener) this);
		mPlik.add(miResetImage);
		mPlik.addSeparator();

		JMenuItem miExit = new JMenuItem("Zakoñcz");
		miExit.setBackground(Color.WHITE);
		miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		miExit.addActionListener((ActionListener) this);
		mPlik.add(miExit);

		JMenu mObraz = new JMenu("Obraz");
		mObraz.setBackground(Color.DARK_GRAY);
		menuBar.add(mObraz);

		JMenu mnFiltry = new JMenu("Filtry");
		mnFiltry.setBackground(Color.WHITE);
		mObraz.add(mnFiltry);

		mnFiltry.addSeparator();

		JMenuItem miMedian3 = new JMenuItem("Filtr medianowy 3x3");
		miMedian3.setBackground(Color.WHITE);
		miMedian3.addActionListener(this);
		mnFiltry.add(miMedian3);

		JMenuItem miMedian5 = new JMenuItem("Filtr medianowy 5x5");
		miMedian5.setBackground(Color.WHITE);
		miMedian5.addActionListener(this);
		mnFiltry.add(miMedian5);

		setJMenuBar(menuBar);

		JPanel configurationPane = new JPanel();
		configurationPane.setBackground(Color.WHITE);
		configurationPane.setBounds(0, 0, 300, 700);
		configurationPane.setLayout(null);
		contentPane.add(configurationPane);

		JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 0, 1350, 1);
		zoomSlider.setFont(new Font("Tahoma", Font.PLAIN, 11));
		zoomSlider.setBackground(Color.WHITE);
		zoomSlider.setValue(100);
		zoomSlider.setBounds(25, 105, 250, 40);
		zoomSlider.setMinorTickSpacing(25);
		zoomSlider.setMajorTickSpacing(200);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(true);
		zoomSlider.addMouseMotionListener(this);
		zoomSlider.addMouseListener(this);
		configurationPane.add(zoomSlider);

		JPanel imagePane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(actualImage, 0, 0, null);
			}
		};

		imagePane.setLayout(null);
		imagePane.addMouseMotionListener(this);
		imagePane.addMouseListener(this);

		JScrollPane scrollImagePane = new JScrollPane(imagePane);
		scrollImagePane.setPreferredSize(new Dimension(1045, 650));
		scrollImagePane.setBorder(null);
		scrollImagePane.setBounds(300, 0, 1045, 650);
		scrollImagePane.setAutoscrolls(true);
		contentPane.add(scrollImagePane);

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF & PNG Images", "jpg", "gif", "png");
		chooser.setFileFilter(filter);
		JFileChooser saveChooser = new JFileChooser();

		JLabel colorRGB = new JLabel("Kolor RGB: ---");
		colorRGB.setFont(new Font("Arial", Font.PLAIN, 12));
		colorRGB.setBounds(75, 25, 250, 40);
		configurationPane.add(colorRGB);

		JLabel colorPreview = new JLabel();
		colorPreview.setBounds(25, 25, 40, 40);
		colorPreview.setOpaque(true);
		colorPreview.setBackground(Color.WHITE);
		configurationPane.add(colorPreview);

		JLabel actualImageScale = new JLabel("Rozmiar obrazu: 100%");
		actualImageScale.setFont(new Font("Arial", Font.PLAIN, 12));
		actualImageScale.setBounds(25, 85, 250, 12);
		configurationPane.add(actualImageScale);

		JLabel brightness = new JLabel("Jasność: 100%");
		brightness.setFont(new Font("Arial", Font.PLAIN, 12));
		brightness.setBounds(25, 145, 250, 40);
		configurationPane.add(brightness);

		JLabel kontrast = new JLabel("Kontrast: 100%");
		kontrast.setFont(new Font("Arial", Font.PLAIN, 12));
		kontrast.setBounds(25, 220, 250, 40);
		configurationPane.add(kontrast);

		JSlider brightnessSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 1);
		brightnessSlider.setFont(new Font("Tahoma", Font.PLAIN, 11));
		brightnessSlider.setBackground(Color.WHITE);
		brightnessSlider.setValue(100);
		brightnessSlider.setBounds(25, 180, 250, 40);
		brightnessSlider.setMinorTickSpacing(25);
		brightnessSlider.setMajorTickSpacing(100);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setPaintLabels(true);
		brightnessSlider.addMouseMotionListener(this);
		brightnessSlider.addMouseListener(this);
		configurationPane.add(brightnessSlider);

		JSlider kontrastSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 1);
		kontrastSlider.setFont(new Font("Tahoma", Font.PLAIN, 11));
		kontrastSlider.setBackground(Color.WHITE);
		kontrastSlider.setValue(100);
		kontrastSlider.setBounds(25, 255, 250, 40);
		kontrastSlider.setMinorTickSpacing(25);
		kontrastSlider.setMajorTickSpacing(100);
		kontrastSlider.setPaintTicks(true);
		kontrastSlider.setPaintLabels(true);
		kontrastSlider.addMouseMotionListener(this);
		kontrastSlider.addMouseListener(this);
		configurationPane.add(kontrastSlider);

		JButton grayOne = new JButton("szarość- sposób 1");
		grayOne.setFont(new Font("Tahoma", Font.PLAIN, 10));
		grayOne.setBorderPainted(false);
		grayOne.setBackground(Color.ORANGE);
		grayOne.setBounds(15, 320, 130, 30);
		grayOne.addActionListener(this);
		configurationPane.add(grayOne);
		grayOne.addMouseListener(this);

		JButton grayTwo = new JButton("szarość- sposób 2");
		grayTwo.setFont(new Font("Tahoma", Font.PLAIN, 10));
		grayTwo.setBorderPainted(false);
		grayTwo.setBackground(Color.ORANGE);
		grayTwo.setBounds(155, 320, 130, 30);
		grayTwo.addActionListener(this);
		configurationPane.add(grayTwo);
		grayTwo.addMouseListener(this);

		JButton[] buttons = new JButton[4];
		buttons[0] = new JButton("dodaj");
		buttons[1] = new JButton("odejmnij");
		buttons[2] = new JButton("pomnóż");
		buttons[3] = new JButton("przedziel");

		buttons[0] = new JButton("dodaj");
		buttons[0].setBounds(15, 360, 70, 30);

		buttons[1] = new JButton("odejmij");
		buttons[1].setBounds(155, 360, 70, 30);

		buttons[2] = new JButton("pomnóż");
		buttons[2].setBounds(15, 400, 70, 30);

		buttons[3] = new JButton("dziel");
		buttons[3].setBounds(155, 400, 70, 30);
		
		JTextField[] dzialania = new JTextField[4];
		for (int i = 0; i < 4; i++) {
			dzialania[i] = new JTextField("1");
			dzialania[i].setBackground(Color.WHITE);
			dzialania[i].setFont(new Font("Tahoma", Font.PLAIN, 11));
			dzialania[i].addMouseListener(this);
			dzialania[i].addKeyListener((KeyListener) this);
			configurationPane.add(dzialania[i]);
		}

		dzialania[0].setBounds(90, 360, 40, 30);
		dzialania[1].setBounds(230, 360, 40, 30);
		dzialania[2].setBounds(90, 400, 40, 30);
		dzialania[3].setBounds(230, 400, 40, 30);


		for (int i = 0; i < 4; i++) {
			buttons[i].setFont(new Font("Tahoma", Font.PLAIN, 10));
			buttons[i].setBorderPainted(false);
			buttons[i].setBackground(Color.ORANGE);
			buttons[i].addMouseListener(this);
			configurationPane.add(buttons[i]);
		}

		JLabel lblFiltrowanieMaska = new JLabel("filtry: ");
		lblFiltrowanieMaska.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFiltrowanieMaska.setBounds(25, 495, 250, 40);
		configurationPane.add(lblFiltrowanieMaska);

		JButton filterOK = new JButton("OK");
		filterOK.setFont(new Font("Tahoma", Font.PLAIN, 12));
		filterOK.setBorderPainted(false);
		filterOK.setBackground(Color.RED);
		filterOK.setBounds(183, 600, 87, 25);
		filterOK.addActionListener(this);
		configurationPane.add(filterOK);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(25, 536, 150, 65);
		configurationPane.add(panel);
		panel.setLayout(new GridLayout(3, 3, 0, 0));

		JComboBox<Object> filterType = new JComboBox<Object>();
		filterType.setModel(new DefaultComboBoxModel<Object>(new String[] { "Wygładzający (uśredniający)", "Sobel",
				"Górnoprzepustowy", "Rozmycie Gaussowskie", "Splot maski" }));
		filterType.setBackground(Color.WHITE);
		filterType.setBounds(75, 500, 165, 25);
		filterType.addActionListener(this);
		configurationPane.add(filterType);

		JTextField filterMask[][] = new JTextField[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				filterMask[i][j] = new JTextField("1");
				panel.add(filterMask[i][j]);
			}
		}
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
