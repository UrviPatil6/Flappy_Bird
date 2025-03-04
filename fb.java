package javaflappybird;
import javax.swing.*;
public class fb {

	public static void main(String[] args) {
		JFrame frame=new JFrame("Flappy Bird");
		frame.setSize(360,640);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		flappybird flappy=new flappybird();
		frame.add(flappy);
		frame.pack();//dimensions not include titlebar of frame
		flappy.requestFocus();
		frame.setVisible(true);
	}

}
