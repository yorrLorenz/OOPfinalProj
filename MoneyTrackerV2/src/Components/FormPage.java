package Components;

import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormPage extends JPanel {

	private static final long serialVersionUID = 1L;
	private LoginForm loginForm= null;
	private RegisterForm registerForm = null;

	/**
	 * Create the panel.
	 */
	public FormPage() {
		setLayout(null);
		registerForm = new RegisterForm();
		loginForm = new LoginForm();
		
		registerForm.getReturnLabel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				remove(registerForm);
				add(loginForm);
				repaint();
				revalidate();
			}
		});
		loginForm.getRegisterLabel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				remove(loginForm);
				add(registerForm);
				repaint();
				revalidate();
			}
		});
		registerForm.setBounds(0,0,450,getToolkit().getScreenSize().height);
		loginForm.setBounds(0, 0, 450, getToolkit().getScreenSize().height);
		add(loginForm);
	}

	public LoginForm getLoginForm() {
		return loginForm;
	}

	public void setLoginForm(LoginForm loginForm) {
		this.loginForm = loginForm;
	}

	public RegisterForm getRegisterForm() {
		return registerForm;
	}

	public void setRegisterForm(RegisterForm registerForm) {
		this.registerForm = registerForm;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
