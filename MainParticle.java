/* Tim Yi
 * AP Computer Science
 * 02/28/2018
 * Project Particles Simulator - Main Particle
 */

package apcsjava;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
//import java.awt.Insets;
//import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JButton;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class MainParticle extends JPanel implements ActionListener {

	private Particle[] particles; // the array of particles
	private Timer timer; // the timer for animation

	private final double WALL_FRICTION = -0.3; // if you're using walls, the % velocity change from bouncing off a wall
	private final double FORCE_STRENGTH = 0.2; // the force proportionality constant
	private final double CLOSE_TOL = 3.0; // the "close enough" tolerance, as a square of distance (in pixels)

	private int xD, yD;
	private double speedIndex = 0;
	private double sizeIndex = 100;

	private JButton startBtn;
	private JButton stopBtn;
	private JButton resetBtn;
	private JButton switchModeBtn;
	private JButton switchChargeBtn;

	private JTextField addSizeTxt;

	private boolean isRunning;

	private int modeIndex;
	private int chargeIndex;

	private double addSize = 10;

	public MainParticle(int xDim, int yDim, int numPart) {
		setBackground(Color.BLACK);
		addMouseListener(new MAdapter());
		addMouseMotionListener(new MAdapter());
		setFocusable(true);
		setDoubleBuffered(true);
		timer = new Timer(1, this);
		timer.start();

		// addMouseListener(new MAdapter());
		// addMouseMotionListener(new MAdapter());

		xD = xDim;
		yD = yDim;

		particles = new Particle[numPart];
		for (int i = 0; i < numPart; i++) { // initialize particles
			// your code goes here

			// initialize particles, give them random size and location within a range
			Point2D.Double loc = new Point2D.Double(Math.random() * xDim, Math.random() * yDim);
			Point2D.Double vel = new Point2D.Double(Math.random() * speedIndex - speedIndex / 2,
					Math.random() * speedIndex - speedIndex / 2);
			double mag = Math.random() * sizeIndex + 1;
			// if in electrical mode, randomly give it positive or negative charge
			particles[i] = new Particle(loc, vel, mag, true, modeIndex == 0 ? true : Math.random() < 0.5);
		}
		initBtns();
		initTxts();
		addThingsToPanel();
	}

	public void addThingsToPanel() {
		GridBagConstraints c = new GridBagConstraints();
		add(startBtn, c);
		add(stopBtn, c);
		add(resetBtn, c);
		add(addSizeTxt, c);
		add(switchChargeBtn, c);
		add(switchModeBtn, c);
	}

	public void paintComponent(Graphics g) { // draw graphics in the panel
		super.paintComponent(g); // call superclass to make panel display correctly
		drawParticles(g); // then draw the particles
	}

	public void initBtns() {
		startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = true;
			}
		});
		stopBtn = new JButton("Stop");
		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = false;
			}
		});
		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = false;
				for (int i = 0; i < particles.length; i++) { // reinitialize particles

					// initialize particles, give them random size and location within a range
					Point2D.Double loc = new Point2D.Double(Math.random() * xD, Math.random() * yD);
					Point2D.Double vel = new Point2D.Double(Math.random() * speedIndex - speedIndex / 2,
							Math.random() * speedIndex - speedIndex / 2);
					double mag = Math.random() * sizeIndex + 1;
					// if in electrical mode, randomly give it positive or negative charge
					particles[i] = new Particle(loc, vel, mag, true, modeIndex == 0 ? true : Math.random() < 0.5);
				}
				updateParticles();
				repaint();
			}
		});
		switchModeBtn = new JButton("Gravity Mode");
		modeIndex = 0;
		switchModeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = false;
				modeIndex = (modeIndex + 1) % 2;
				switch (modeIndex) {
				case 0: {
					switchModeBtn.setText("Gravity Mode");
					break;
				}
				case 1: {
					switchModeBtn.setText("Electrical Mode");
					break;
				}
				}
				for (int i = 0; i < particles.length; i++) { // reinitialize particles

					// initialize particles, give them random size and location within a range
					Point2D.Double loc = new Point2D.Double(Math.random() * xD, Math.random() * yD);
					Point2D.Double vel = new Point2D.Double(Math.random() * speedIndex - speedIndex / 2,
							Math.random() * speedIndex - speedIndex / 2);
					double mag = Math.random() * sizeIndex + 1;
					// if in electrical mode, randomly give it positive or negative charge
					particles[i] = new Particle(loc, vel, mag, true, modeIndex == 0 ? true : Math.random() < 0.5);
				}
				updateParticles();
				repaint();
			}
		});
		switchChargeBtn = new JButton("Add Pos+");
		chargeIndex = 0;
		switchChargeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chargeIndex = (chargeIndex + 1) % 2;
				switch (chargeIndex) {
				case 0: {
					switchChargeBtn.setText("Add Pos+");
					break;
				}
				case 1: {
					switchChargeBtn.setText("Add Neg-");
					break;
				}
				}
			}
		});
	}

	public void initTxts() {
		addSizeTxt = new JTextField("10", 4);
		addSizeTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					addSize = Double.parseDouble(addSizeTxt.getText());
				} catch (NumberFormatException e) {
				}
			}
		});
	}

	// draws only active particles
	// draws a circle using g.fillOval whose CENTER is the particle's location and
	// whose RADIUS is half the particle's magnitude
	public void drawParticles(Graphics g) {
		for (Particle p : particles) {
			// your code goes here
			if (p.isActive()) { // give every active particle the corresponding color
				if (modeIndex == 0) {
					g.setColor(Color.white);
				} else {
					if (p.getCharge()) {
						g.setColor(Color.RED);
					} else {
						g.setColor(Color.BLUE);
					}
				}
				g.fillOval((int) p.getLoc().getX(), (int) p.getLoc().getY(), (int) p.getMag() / 2,
						(int) p.getMag() / 2);
			}
		}
	}

	// updates ACTIVE particles by checking if their velocity would send them past
	// the edge of the viewing window
	// and then adjusting their velocity or position appropriately if necessary
	// and finally using addVel to actually relocate the particle according to its
	// new location and velocity
	public void updateParticles() {
		// your code goes here

		for (Particle p : particles) {
			if (p.isActive()) {
				if (p.getLoc().getX() + p.getVel().getX() > xD - p.getMag() / 2
						|| p.getLoc().getX() + p.getVel().getX() < 0) {
					// if something goes out the left wall or the right wall, bounce it back and
					// reduce its speed
					p.setVel(p.getVel().getX() * WALL_FRICTION, p.getVel().getY());
					if (p.getLoc().getX() + p.getVel().getX() * 2 > xD - p.getMag() / 2) {
						// if it is still out after adding velocity factor, forcefully move it back in
						p.setLoc(xD - p.getMag() / 2, p.getLoc().getY());
					}
				}
				if (p.getLoc().getY() + p.getVel().getY() > yD - 32 - p.getMag() / 2
						|| p.getLoc().getY() + p.getVel().getY() < 0) {
					// if something goes out the ceiling or the floor, bounce it back and reduce its
					// speed
					p.setVel(p.getVel().getX(), p.getVel().getY() * WALL_FRICTION);
					if (p.getLoc().getY() + p.getVel().getY() * 2 > yD - 32 - p.getMag() / 2) {
						// if it is still out after adding velocity factor, forcefully move it back in
						p.setLoc(p.getLoc().getX(), yD - 32 - p.getMag() / 2);
					}
				}
				p.addVel();
			}
		}
	}

	// given two particles and the square of their distance, return the appropriate
	// force calculation
	public double forceMag(Particle p1, Particle p2, double distSq) {
		// your code goes here

		if (modeIndex == 0) { // (m1*m2)/r^2
			return FORCE_STRENGTH * (p1.getMag() * p2.getMag()) / distSq;
		} else { // (q1*q2)/r^2
			if (p1.getCharge() == p2.getCharge()) { // same charge repeals
				return -FORCE_STRENGTH * (p1.getMag() * p2.getMag()) / distSq;
			} else { // opposite charge attracts
				return FORCE_STRENGTH * (p1.getMag() * p2.getMag()) / distSq;
			}
		}
		// return FORCE_STRENGTH * (p1.getMag() * p2.getMag())/distSq; //rewrite this
		// line too
	}

	// for gravitational simulation, pick a particle to make bigger, deactivate the
	// other one
	// for electrical simulation, pick the stronger particle, weaken it by the
	// other's magnitude, deactivate the other one
	// for electrical simulation, if the two particles are equal in magnitude,
	// deactivate both of them
	// for both, the remaining (if there is one) particle's velocity needs to be the
	// sum of the two particle's velocities
	private void mergeParticles(Particle p1, Particle p2) {
		// your code goes here

		// swap p1 and p2 if p2 is larger
		// makes p1 always larger or equal to p2
		Particle temp;
		if (p1.getMag() < p2.getMag()) {
			temp = p1;
			p1 = p2;
			p2 = temp;
		}
		double tempMag = p1.getMag();

		if (modeIndex == 0) { // adding the magnitudes
			p1.setMag(Math.sqrt(p1.getMag() * p1.getMag() + p2.getMag() * p2.getMag()));
		} else {
			if (p1.getCharge() == p2.getCharge()) { // adding the magnitudes
				p1.setMag(Math.sqrt(p1.getMag() * p1.getMag() + p2.getMag() * p2.getMag()));
			} else { // subtracting p2 from p1
				p1.setMag(Math.sqrt(p1.getMag() * p1.getMag() - p2.getMag() * p2.getMag()));
			}
		}
		// conservation of momentum: m1 * v1 + m2 * v2 = (m1 + m2) * v3
		p1.setVel((tempMag * p1.getVel().getX() + p2.getMag() * p2.getVel().getX()) / p1.getMag(),
				(tempMag * p1.getVel().getY() + p2.getMag() * p2.getVel().getY()) / p1.getMag());
		if (p1.getMag() < 1) { // if p1 is too small, deactivate it
			p1.deactivate();
		}
		p2.deactivate();
	}

	// the heart of the simulation - using the force calculation to inform every
	// particle's new velocity
	// uses a vector sum of the effect of EVERY OTHER ACTIVE particle to calculate
	// the new change in velocity (acceleration)
	// and then actually adjusts every particle's velocity based on that calculation
	public void updateVelocities() {
		double x = 0, y = 0; // the vector sum force calculation variables
		double fm = 0; // the force magnitude
		double angle = 0; // the bearing from one particle to another particle, in radians
		double distSq = 0; // the square of the distance between particles p1 and p2 (for loop variable
							// names)
		// lots of nested for and if statements ahead!
		// hint 1: you should calculate distSq before calculating forceMag since you may
		// want/need to merge particles instead
		// hint 2: the following three lines of code need to be in your code, in one
		// contiguous block
		// angle = Math.atan2(p2.getLoc().y - p1.getLoc().y, p2.getLoc().x -
		// p1.getLoc().x); //sweet, sweet trigonometry
		// x += fm * Math.cos(angle); //the horizontal component of the force vector
		// y += fm * Math.sin(angle); //the vertical component of the force vector

		// your code goes here
		for (Particle p1 : particles) {
			for (Particle p2 : particles) {
				if (p1.isActive() && p2.isActive() && !p1.equals(p2)) { // if p1 and p2 are not the same
					if ((p1.getLoc().getX() - p2.getLoc().getX()) * (p1.getLoc().getX() - p2.getLoc().getX())
							+ (p1.getLoc().getY() - p2.getLoc().getY())
									* (p1.getLoc().getY() - p2.getLoc().getY()) <= CLOSE_TOL * CLOSE_TOL) {
						// merge them if them get close enough
						mergeParticles(p1, p2);
					}

					distSq = (p1.getLoc().getX() + p1.getMag() / 4 - (p2.getLoc().getX() + p2.getMag() / 4))
							* (p1.getLoc().getX() + p1.getMag() / 4 - (p2.getLoc().getX() + p2.getMag() / 4))
							+ (p1.getLoc().getY() + p1.getMag() / 4 - (p2.getLoc().getY() + p2.getMag() / 4))
									* (p1.getLoc().getY() + p1.getMag() / 4 - (p2.getLoc().getY() + p2.getMag() / 4));
					// so that the calculation is done from the center of the circles
					fm = forceMag(p1, p2, distSq);
					angle = Math.atan2(p2.getLoc().y + p2.getMag() / 4 - (p1.getLoc().y + p1.getMag() / 4),
							p2.getLoc().x + p2.getMag() / 4 - (p1.getLoc().x + p1.getMag() / 4));
					// sweet, sweet trigonometry
					x += fm * Math.cos(angle); // the horizontal component of the force vector
					y += fm * Math.sin(angle); // the vertical component of the force vector
				}
			}
			// update velocity
			p1.setVel(p1.getVel().getX() + x / p1.getMag(), p1.getVel().getY() + y / p1.getMag());
			x = 0;
			y = 0;
		}
	}

	@Override
	// updates velocities, then locations, then draws everything active
	public void actionPerformed(ActionEvent arg0) {
		if (isRunning) {
			updateVelocities();
			updateParticles();
			repaint();
		}
	}

	private class MAdapter extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			for (Particle p : particles) {
				if (p.isActive() == false) {
					// find a deactivated particle, give it new identity, activate it
					p.setVel(0, 0);
					p.setCharge(chargeIndex == 0 ? true : false);
					p.setMag((int) addSize);
					p.setLoc(e.getX() - 2 * p.getMag(), e.getY() - 2 * p.getMag());
					p.activate();
				}
			}
		}
	}
}
