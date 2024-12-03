package Components;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPasswordField;
import javax.swing.JFormattedTextField;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginForm extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel loginLabel;
	private JPasswordField passwordField;
	private JLabel passwordLabel;
	private JFormattedTextField usernameText;
	private JLabel usernameLabel;
	private JLabel registerLabel;
	private JButton loginButton;

	
	private String color1 = "#FFFFFF"; 
	private String color2 = "#FFFFFF";
	/**
	 * Create the panel.
	 */
	public LoginForm() {
		setBackground(Color.WHITE);
		setLayout(null);
		
		loginLabel = new JLabel("Login");
		loginLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 54));
		loginLabel.setBounds(69, 153, 133, 79);
		add(loginLabel);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(69, 388, 290, 35);
		add(passwordField);
		
		passwordLabel = new JLabel("Password");
		passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		passwordLabel.setBounds(69, 362, 106, 15);
		add(passwordLabel);
		
		usernameText = new JFormattedTextField();
		usernameText.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		usernameText.setBounds(69, 305, 290, 35);
		add(usernameText);
		
		usernameLabel = new JLabel("Username");
		usernameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		usernameLabel.setBounds(69, 278, 106, 15);
		add(usernameLabel);
		
		registerLabel = new JLabel("Register account");
		registerLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				registerLabel.setForeground(Color.BLACK);
				registerLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		registerLabel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				registerLabel.setForeground(Color.BLUE);
				registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		registerLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		registerLabel.setBounds(253, 434, 106, 20);
		add(registerLabel);
		
 
		loginButton = new JButton("Login") {
			@Override
			protected void paintComponent(Graphics g) {
				int w = loginButton.getWidth();
				int h = loginButton.getHeight();
				Graphics2D g2 = (Graphics2D)g;
				GradientPaint gra = new GradientPaint(0,0, Color.decode(color1), w/4,0, Color.decode(color2));
				g2.setPaint(gra);
				g2.fillRoundRect(0, 0, w, h, h, h);
				super.paintComponent(g);
			}
		};
		loginButton.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				loginButton.setForeground(Color.WHITE);
				color2 = "#b2f3c1"; 
				color1 = "#49664f";
				loginButton.repaint();
				loginButton.revalidate();
			}
		});
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				loginButton.setForeground(Color.BLACK);
				color1 = "#FFFFFF";
				color2 = "#FFFFFF"; 
				loginButton.repaint();
				loginButton.revalidate();
			}
		});
		loginButton.setContentAreaFilled(false);
		loginButton.setBorder(null);
		loginButton.setFocusPainted(false);
		loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 24));
		loginButton.setBounds(253, 509, 106, 40);
		add(loginButton);
	}

	public JButton getLoginButton() {
		return loginButton;
	}

	public void setLoginButton(JButton btnNewButton) {
		this.loginButton = btnNewButton;
	}

	public JLabel getLoginLabel() {
		return loginLabel;
	}

	public void setLoginLabel(JLabel loginLabel) {
		this.loginLabel = loginLabel;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(JPasswordField passwordField) {
		this.passwordField = passwordField;
	}

	public JLabel getPasswordLabel() {
		return passwordLabel;
	}

	public void setPasswordLabel(JLabel passwordLabel) {
		this.passwordLabel = passwordLabel;
	}

	public JFormattedTextField getUsernameText() {
		return usernameText;
	}

	public void setUsernameText(JFormattedTextField usernameText) {
		this.usernameText = usernameText;
	}

	public JLabel getUsernameLabel() {
		return usernameLabel;
	}

	public void setUsernameLabel(JLabel usernameLabel) {
		this.usernameLabel = usernameLabel;
	}

	public JLabel getRegisterLabel() {
		return registerLabel;
	}

	public void setRegisterLabel(JLabel registerLabel) {
		this.registerLabel = registerLabel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
