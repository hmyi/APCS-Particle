/* Tim Yi
 * AP Computer Science
 * 02/28/2018
 * Project Particles Simulator - Particle Wrapper
 */

package apcsjava;

import java.awt.EventQueue;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ParticleWrapper extends JFrame {

	public final int PANEL_SIZE = 1000;    //change these numbers as appropriate
	public final int BORDER_SPACE = 10;   
	public final int NUM_PARTICLES = 300; //for testing, try 3 instead...
	
	public ParticleWrapper() {
        setSize(PANEL_SIZE + BORDER_SPACE, PANEL_SIZE + 3*BORDER_SPACE); //probably want to change this too
		add(new MainParticle(PANEL_SIZE, PANEL_SIZE, NUM_PARTICLES));
        setResizable(false);
        setTitle("Particles Simulator");
//        setTitle("Drawing particles");	//so creative!!
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ParticleWrapper go = new ParticleWrapper();
                go.setVisible(true);
            }
        });
	}
}
