import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/** A randomizer that displays words from a file. To be used for Science Word practices.
 * 
 * @author Jim Ren
 *
 */

public class SciWordRandomizer {
	static int numCorrect;
	static int numSkipped;
	static ArrayList<String> words;
	static GregorianCalendar calender;
	static HashSet<Integer> usedIndex;
	static Timer timer;
	
	static final int WORD_CAP = 70;
	
	public static void main (String args[]) throws IOException {
		//Initialization
		resetVar();
		if (args.length != 1) {
			String errStr = "Usage:\tjava SciWordRandomizer <filename>";
			System.err.println(errStr);
			System.exit(1);
		}
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException e) {
			System.err.printf("File %s not found.", args[0]);
			System.exit(2);
		}
		
		//Read in data
		words = new ArrayList<String>();
		while (true) {
			String strIn = input.readLine();
			if (strIn != null) {
				words.add(strIn);
			} else {
				break;
			}
		}
		
		//GUI
		final JFrame frame = new JFrame("Science Word Randomizer");
		JPanel panel = new JPanel();
		JPanel displayPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel timerPanel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		displayPanel.setLayout(new GridBagLayout());
		c.weighty = 2;
		c.gridy = 0;
		panel.add(displayPanel, c);
		c.weighty = 1;
		c.gridy = 1;
		panel.add(timerPanel, c);
		c.weighty = 0;
		c.gridy = 2;
		panel.add(buttonPanel, c);
			
		final JLabel label = new JLabel(nextRandomWord(), SwingConstants.CENTER);
		label.setFont(new Font("Times New Roman", Font.PLAIN, 40));
		final JLabel count = new JLabel("0/0");
		final JLabel dummy = new JLabel("\n\nCorrect/Skipped:");
		
		//Timer
		final JLabel timerLabel = new JLabel();
		timerPanel.add(timerLabel);
		timerLabel.setText("" + calender.get(GregorianCalendar.SECOND));
		
		//Buttons
		final JButton next = new JButton("Next");
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				numCorrect++;
				label.setText(nextRandomWord());
				count.setText(String.format("%d/%d", numCorrect, numSkipped));
			}
		});
		final JButton skip = new JButton("Skip");
		skip.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				numSkipped++;
				label.setText(nextRandomWord());
				count.setText(String.format("%d/%d", numCorrect, numSkipped));
			}
		});
		JButton restart = new JButton("Restart");
		restart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				resetVar();
				label.setForeground(Color.BLACK);
				next.setEnabled(true);
				skip.setEnabled(true);
				label.setText(nextRandomWord());
				count.setText("0/0");
			}
		});
		JButton quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		c.weighty = 1;
		c.gridy = 0;
		displayPanel.add(label, c);
		c.weighty = 1;
		c.gridy = 1;
		displayPanel.add(dummy, c);
		c.weighty = 0;
		c.gridy = 2;
		displayPanel.add(count, c);
		buttonPanel.add(next);
		buttonPanel.add(skip);
		buttonPanel.add(restart);
		buttonPanel.add(quit);

		frame.add(panel);
		frame.setSize(512, 384);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		while (true) {
			System.out.println(numCorrect + numSkipped);
			timerLabel.setText("Time: " + calender.get(GregorianCalendar.SECOND));
			if (numCorrect + numSkipped >= WORD_CAP) {
				label.setText(String.format("%d Words All Done!", WORD_CAP));
				label.setForeground(Color.RED);
				next.setEnabled(false);
				skip.setEnabled(false);
			}
		}
	}
	
	public static String nextRandomWord() {
		Random random= new Random();
		int rndInt = random.nextInt(words.size());
		while (usedIndex.contains(rndInt)) {
			rndInt = random.nextInt(words.size());
		}
		usedIndex.add(rndInt);
		return words.get(rndInt);
	}
	
	public static void resetVar() {
		usedIndex = new HashSet<Integer>();
		numCorrect = numSkipped = 0;
		timer = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				calender.add(GregorianCalendar.SECOND, 1);
			}
		});
		calender = new GregorianCalendar();
	}
}

//class nextWordAction implements ActionListener {
//	private String display;
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		display = SciWordRandomizer.nextRandomWord();
//	}
//	
//	public String getNextWord() {
//		return display;
//	}
//}
