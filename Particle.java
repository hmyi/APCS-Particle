/* Tim Yi
 * AP Computer Science
 * 02/28/2018
 * Project Particles Simulator - Particle
 */

package apcsjava;

import java.awt.geom.Point2D; //for Point2D.Double
//import java.awt.geom.Point2D.Double;

public class Particle {

	private Point2D.Double location;
	private Point2D.Double velocity;
	private double magnitude;
	private boolean active;
	private boolean charge;

	// your code goes here

	public Particle(Point2D.Double loc, Point2D.Double vel, double mag, boolean act, boolean cha) { // customized new
		location = loc;
		velocity = vel;
		magnitude = mag;
		active = act;
		charge = cha;
	}

	public Particle() { // standard new
		location = new Point2D.Double(0, 0);
		velocity = new Point2D.Double(0, 0);
		magnitude = 0;
		active = false;
		charge = true;
	}

	public Point2D.Double getLoc() {
		return location;
	}

	public Point2D.Double getVel() {
		return velocity;
	}

	public double getMag() {
		return magnitude;
	}

	public void setLoc(double locX, double locY) {
		location = new Point2D.Double(locX, locY);
	}

	public void setVel(double velX, double velY) {
		velocity = new Point2D.Double(velX, velY);
	}

	public void setMag(double mag) {
		magnitude = mag;
	}

	public void activate() {
		active = true;
	}

	public void deactivate() {
		active = false;
	}

	public boolean isActive() {
		return active;
	}

	public void addVel() {
		location = new Point2D.Double(location.getX() + velocity.getX(), location.getY() + velocity.getY());
	}

	public boolean getCharge() {
		return charge;
	}

	public void setCharge(boolean cha) {
		charge = cha;
	}
}
