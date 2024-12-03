package Components;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.border.MatteBorder;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class NavButton extends JButton {

	private static final long serialVersionUID = 1L;
	private String color1 = "#99ffce";
	private String color2 = "#cbb8ff";
	/**
	 * Create the panel.
	 */
	public NavButton(String name) {
		super(name);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				color1 = "#99ffce";
				color2 = "#cbb8ff";
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				color1 = "#ffadd2";
				color2 = "#9ea0ff";
			}
		});
		setContentAreaFilled(false);
		System.out.println(Color.decode(color1).toString());

		setFocusPainted(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setBorderPainted(false);
		setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
	}

	
	@Override
	protected void paintComponent(Graphics g) {
	int w = getWidth(), h = getHeight();
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	GradientPaint gra = new GradientPaint(0, 0, Color.decode(color1), 0, (h/2)+20, Color.decode(color2));
	g2.setPaint(gra);
	g2.fillRect(0, 0, w, h);
	super.paintComponent(g);
	}


	public String getColor1() {
		return color1;
	}


	public void setColor1(String color1) {
		this.color1 = color1;
	}


	public String getColor2() {
		return color2;
	}


	public void setColor2(String color2) {
		this.color2 = color2;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
