package tion;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

class MouseEventListener implements MouseInputListener {

	Point origin;
	JFrame frame;

	public MouseEventListener(JFrame frame) {
		this.frame = frame;
		origin = new Point();
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		origin.x = e.getX();
		origin.y = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		Point p = this.frame.getLocation();
		this.frame.setLocation(p.x + (e.getX() - origin.x), p.y
				+ (e.getY() - origin.y));
	}
}