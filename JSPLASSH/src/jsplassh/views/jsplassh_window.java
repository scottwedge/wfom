package jsplassh.views;

import com.fazecast.jSerialComm.SerialPort;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.json.JSONObject;
import org.json.JSONTokener;

public class jsplassh_window extends JFrame {
	
	
	private JPanel contentPane;
	List<String> orderList = new ArrayList<String>();
	String orderString = new String();
	int arduinoState = 0;
	int ledState = 0;
	int solisState = 0;
	int mode = 1;
	boolean ledOn[] = {false, false, false, false};
	String colors[] = {"Red","Blue","Green","Lime"};
	String mouse = new String();
	String uni = new String();
	boolean readyToDeploy = false;
	private JTextField framerate;
	private JTextField setHeight;
	private JTextField exposureTime;
	private JTextField setWidth;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					jsplassh_window frame = new jsplassh_window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public jsplassh_window() {
		initComponents();
	}
	private void initComponents() {
		OutputStream out = initializeArduino();
		setTitle("SPLASSH");
		setIconImage(Toolkit.getDefaultToolkit().getImage(jsplassh_window.class.getResource("/jsplassh/resources/1027308.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 326, 525);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblSolisParameters = new JLabel("SOLIS Parameters");
		lblSolisParameters.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblSolisParameters.setBounds(5, 5, 120, 14);
		
		JSlider binning = new JSlider();
		binning.setSnapToTicks(true);
		binning.setPaintTicks(true);
		binning.setBounds(15, 45, 43, 111);
		binning.setToolTipText("");
		binning.setOrientation(SwingConstants.VERTICAL);
		binning.setMajorTickSpacing(1);
		binning.setMaximum(4);
		binning.setMinimum(1);
		
		JLabel lblBinning = new JLabel("Binning");
		lblBinning.setBounds(42, 30, 34, 14);
		
		JLabel lblSetFramerate = new JLabel("Set Framerate (fps)");
		lblSetFramerate.setBounds(110, 30, 95, 14);
		lblSetFramerate.setEnabled(false);
		
		framerate = new JTextField();
		framerate.setBounds(110, 55, 86, 20);
		framerate.setEnabled(false);
		framerate.setText("50.70");
		framerate.setColumns(10);
		
		JLabel lblSetHeight = new JLabel("Set Height");
		lblSetHeight.setBounds(214, 30, 50, 14);
		
		setHeight = new JTextField();
		setHeight.setBounds(214, 55, 86, 20);
		setHeight.setText("2048");
		setHeight.setColumns(10);
		
		JLabel lblExposureTimes = new JLabel("Exposure Time (s)");
		lblExposureTimes.setBounds(110, 82, 86, 14);
		
		exposureTime = new JTextField();
		exposureTime.setBounds(110, 102, 86, 20);
		exposureTime.setText("0.0068");
		exposureTime.setColumns(10);
		
		JLabel lblSetWidth = new JLabel("Set Width");
		lblSetWidth.setBounds(214, 82, 47, 14);
		
		setWidth = new JTextField();
		setWidth.setBounds(214, 102, 86, 20);
		setWidth.setText("2048");
		setWidth.setColumns(10);
		
		JLabel lblx = new JLabel("1x1");
		lblx.setBounds(68, 45, 18, 14);
		
		JLabel lblx_1 = new JLabel("2x2");
		lblx_1.setBounds(68, 77, 18, 14);
		
		JLabel lblx_2 = new JLabel("4x4");
		lblx_2.setBounds(68, 110, 18, 14);
		
		JLabel lblx_3 = new JLabel("8x8");
		lblx_3.setBounds(68, 142, 18, 14);
		
		JLabel lblLedControl = new JLabel("LED Control");
		lblLedControl.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblLedControl.setBounds(15, 190, 84, 14);
		
		JLabel lblOrder_1 = new JLabel("Strobe Order");
		lblOrder_1.setBounds(135, 240, 63, 14);
		
		JLabel lblLeds = new JLabel("LEDs");
		lblLeds.setBounds(46, 215, 23, 14);
		
		JLabel lblArduinoState = new JLabel("Arduino State");
		lblArduinoState.setBounds(15, 377, 66, 14);
		
		JLabel lblNewLabel = new JLabel("LED Set State");
		lblNewLabel.setBounds(121, 377, 66, 14);
		
		JLabel lblSolisState = new JLabel("SOLIS State");
		lblSolisState.setBounds(214, 377, 58, 14);
		
		JLabel arduinoStatusLbl = new JLabel("");
		arduinoStatusLbl.setBounds(35, 397, 20, 20);
		
		JLabel ledStatusLbl = new JLabel("");
		ledStatusLbl.setBounds(142, 397, 20, 20);
		
		JLabel solidStatusLbl = new JLabel("");
		solidStatusLbl.setBounds(236, 397, 20, 20);
		
		JButton btnDeploySettingsTo_1 = new JButton("Deploy Settings to SOLIS");
		btnDeploySettingsTo_1.setBounds(80, 452, 153, 23);
		btnDeploySettingsTo_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int b = binning.getValue();
				String h = setHeight.getText();
				String w = setWidth.getText();
				String e = exposureTime.getText();
				String s = orderString;
				String f = framerate.getText();
				String u = uni;
				String m = mouse;
				writeJsonSettings(b, f, h, e, w, s, u, m);
				System.exit(0);
			}
		});
		btnDeploySettingsTo_1.setEnabled(false);
		JButton checkStatusBtn = new JButton("Check States");
		checkStatusBtn.setBounds(107, 423, 95, 23);
		checkStatusBtn.addActionListener(new ActionListener() {
			int ledState = 1;
			public void actionPerformed(ActionEvent arg0) {
				
				if (checkArduino(out) == 1) {
					arduinoStatusLbl.setIcon(new ImageIcon(jsplassh_window.class.getResource("/jsplassh/resources/check.png")));
				}
				else {
					arduinoStatusLbl.setIcon(new ImageIcon(jsplassh_window.class.getResource("/jsplassh/resources/x.png")));
					System.out.println("Arduino is Not Connected!");
				}
				
				if (orderList.size()>0) {
					ledStatusLbl.setIcon(new ImageIcon(jsplassh_window.class.getResource("/jsplassh/resources/check.png")));
				}
				else {
					ledStatusLbl.setIcon(new ImageIcon(jsplassh_window.class.getResource("/jsplassh/resources/x.png")));
					System.out.println("LED Strobe Order Not Set!");
				}
				
				if (checkSolis() == 1) {
					solidStatusLbl.setIcon(new ImageIcon(jsplassh_window.class.getResource("/jsplassh/resources/check.png")));
				}
				else {
					solidStatusLbl.setIcon(new ImageIcon(jsplassh_window.class.getResource("/jsplassh/resources/x.png")));
					System.out.println("SOLIS is Not Running!");
				}
				
				if (checkSolis()== 1 && ledState == 1 && checkArduino(out) == 1){
					btnDeploySettingsTo_1.setEnabled(true);
				}
				else {
					btnDeploySettingsTo_1.setEnabled(false);
				}
				
				
			}
		});
		JList list = new JList();
		JLabel lblStrobeOrder = new JLabel();
		lblStrobeOrder.setBounds(264, 235, 0, 0);
		
		JButton btnRed = new JButton("Red");
		btnRed.setBounds(15, 235, 84, 23);
		btnRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (mode == 0) {
					controlLeds(arg0, out);
				}
				else {
					updateStrobeOrder(arg0, list, out);
					btnRed.setEnabled(false);
					lblStrobeOrder.setText(orderString);	
				}
			}
		});
		btnRed.setForeground(Color.RED);
		
		JButton btnGreen = new JButton("Green");
		btnGreen.setBounds(15, 264, 84, 23);
		btnGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (mode == 0) {
					controlLeds(arg0, out);
				}
				else {
					updateStrobeOrder(arg0, list, out);
					btnGreen.setEnabled(false);
					lblStrobeOrder.setText(orderString);	
				}
			}
		});
		btnGreen.setForeground(Color.GREEN);
		
		JButton btnBlue = new JButton("Blue");
		btnBlue.setBounds(15, 293, 84, 23);
		btnBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (mode == 0) {
					controlLeds(arg0, out);
				}
				else {
					updateStrobeOrder(arg0, list, out);
					btnBlue.setEnabled(false);
					lblStrobeOrder.setText(orderString);	
				}
			}
		});
		btnBlue.setForeground(Color.BLUE);
		
		JButton btnSpeckle = new JButton("Lime");
		btnSpeckle.setBounds(15, 324, 84, 23);
		btnSpeckle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (mode == 0) {
					controlLeds(arg0, out);
				}
				else {
					updateStrobeOrder(arg0, list, out);
					btnSpeckle.setEnabled(false);
					lblStrobeOrder.setText(orderString);	
				}
			}
		});
		btnSpeckle.setForeground(new Color(0, 255, 0));
		contentPane.setLayout(null);
		contentPane.add(btnRed);
		contentPane.add(btnGreen);
		contentPane.add(btnBlue);
		contentPane.add(btnSpeckle);
		contentPane.add(checkStatusBtn);
		contentPane.add(btnDeploySettingsTo_1);
		contentPane.add(arduinoStatusLbl);
		contentPane.add(ledStatusLbl);
		contentPane.add(solidStatusLbl);
		contentPane.add(lblArduinoState);
		contentPane.add(lblNewLabel);
		contentPane.add(lblSolisState);
		contentPane.add(lblSolisParameters);
		contentPane.add(lblLedControl);
		contentPane.add(lblStrobeOrder);
		contentPane.add(lblLeds);
		contentPane.add(lblOrder_1);
		contentPane.add(binning);
		contentPane.add(lblx_2);
		contentPane.add(lblx_1);
		contentPane.add(lblx_3);
		contentPane.add(lblx);
		contentPane.add(lblBinning);
		contentPane.add(exposureTime);
		contentPane.add(lblExposureTimes);
		contentPane.add(framerate);
		contentPane.add(lblSetFramerate);
		contentPane.add(setHeight);
		contentPane.add(setWidth);
		contentPane.add(lblSetWidth);
		contentPane.add(lblSetHeight);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(5, 172, 295, 7);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 361, 290, 5);
		contentPane.add(separator_1);;
		
		JLabel lblMode = new JLabel("Mode");
		lblMode.setHorizontalAlignment(SwingConstants.CENTER);
		lblMode.setBounds(198, 190, 46, 14);
		contentPane.add(lblMode);
		
		JLabel lblTestLights = new JLabel("Test LEDs");
		lblTestLights.setBounds(155, 215, 47, 14);
		contentPane.add(lblTestLights);
		
		JLabel lblSetOrder = new JLabel("Set Order");
		lblSetOrder.setBounds(242, 215, 58, 14);
		contentPane.add(lblSetOrder);
		list.setVisibleRowCount(4);
		list.setBounds(121, 260, 84, 72);
		contentPane.add(list);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateStrobeOrder(arg0,list, out);
				btnBlue.setEnabled(true);
				btnGreen.setEnabled(true);
				btnRed.setEnabled(true);
				btnSpeckle.setEnabled(true);
			}
		});
		btnClear.setBounds(214, 279, 66, 23);
		contentPane.add(btnClear);
		
		JSlider slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			int c = 0;
			public void stateChanged(ChangeEvent e) {
				c++;
				if (c >= 3) {
					mode = slider.getValue();
					c = 0;
					String message = "";
					if (mode == 1) {
						message = "Set Order";
					}
					else {
						message = "LED Test";
						orderList.clear();
						String[] orderArray = new String[orderList.size()];
						orderList.toArray(orderArray);
						list.setModel(new AbstractListModel() {
							public int getSize() {
								return orderArray.length;
							}
							public Object getElementAt(int index) {
								return((index+1)+". "+orderArray[index]);
							}
						});
					}
					System.out.println("Mode set to: "+message);
				}
				if (mode == 0) {
					list.setEnabled(false);
					btnClear.setEnabled(false);
					btnBlue.setEnabled(true);
					btnGreen.setEnabled(true);
					btnRed.setEnabled(true);
					btnSpeckle.setEnabled(true);
				}
				else {
					list.setEnabled(true);
					btnClear.setEnabled(true);
				}

			}
		});
		slider.setMaximum(1);
		slider.setBounds(204, 215, 34, 14);
		contentPane.add(slider);
	}
	
	private OutputStream initializeArduino() {
		SerialPort sp = SerialPort.getCommPort("COM4");
		sp.setComPortParameters(9600, 8, 1, 0);
		sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
		if (sp.openPort()) {
			System.out.println("Port is opened");
		}
		else {
			System.out.println("Port is not opened");
		}
		return sp.getOutputStream();
	}

	private void updateStrobeOrder(java.awt.event.ActionEvent e, JList l, OutputStream out) {
		
		if(e.getActionCommand().toString() == "Clear"){
			orderList.clear();
		}
		else if ((mode == 0)) {
			controlLeds(e, out);
		}
		else {
			orderList.add(e.getActionCommand().toString());
		}
		String[] orderArray = new String[orderList.size()];
		orderList.toArray(orderArray);
		l.setModel(new AbstractListModel() {
			public int getSize() {
				return orderArray.length;
			}
			public Object getElementAt(int index) {
				return((index+1)+". "+orderArray[index]);
			}
		});

	}
	
	private void controlLeds(ActionEvent e, OutputStream out) {
		String button = e.getActionCommand().toString();
		int i = Arrays.asList(colors).indexOf(button);
		ledOn[i] = !ledOn[i];
		String message = new String();
		for(boolean b:ledOn) {
			if (!b) {
				message += "0";
			}
			else {
				message += "1";
			}
		}
		try {
			System.out.println("Sending "+ message +" to arduino");
			out.write(message.getBytes());
			out.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private Integer checkArduino(OutputStream out) {
		if (out != null) {
			return 1;
		}
		else {
			return 0;
		}
	}	
	
	private Integer checkSolis() {
		String line;
		String pidInfo ="";
		try {
			Process p =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
			BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
			    pidInfo+=line; 
			}
			input.close();
			if(pidInfo.contains("AndorSolis.exe"))
			{
			    return 1;
			}
			else {
				return 0;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}

	
	private void writeJsonSettings(int b, String f, String h, String e, String w, String s, String u, String m) {
		FileReader reader;
		try {
			reader = new FileReader("JSPLASSH/settings.json");
			JSONTokener tokener = new JSONTokener(reader);
			JSONObject settings = new JSONObject(tokener);
			reader.close();
			JSONObject camera = new JSONObject();
			camera.put("strobe_order", s);
			camera.put("binning", b);
			camera.put("framerate", f);
			camera.put("height", h);
			camera.put("width", w);
			camera.put("exposure", e);
			settings.put("camera", camera);
			PrintWriter out = new PrintWriter("JSPLASSH/settings.json");
			out.println(settings.toString());
			out.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			try {
				reader = new FileReader("settings.json");
				JSONTokener tokener = new JSONTokener(reader);
				JSONObject settings = new JSONObject(tokener);
				reader.close();
				JSONObject camera = new JSONObject();
				camera.put("strobe_order", s);
				camera.put("binning", b);
				camera.put("framerate", f);
				camera.put("height", h);
				camera.put("width", w);
				camera.put("exposure", e);
				settings.put("camera", camera);
				PrintWriter out = new PrintWriter("settings.json");
				out.println(settings.toString());
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
