import java.awt.Image;
import javax.swing.ImageIcon;

public class Card {
	String color;  // clubs, hearts, diamonds, spades
	String type;   // 2,3,4,5,6,7,8,9,10,J,Q,K,A
	int value;	   // 1,2,3,4,5,6,7,8,9,10,11
	Card(String c, String t, int v){
		color = c;
		type = t;
		value = v;
	}
	public int getValue() { return value; }
	
	// https://stackoverflow.com/questions/2856480/resizing-a-imageicon-in-a-jbutton
	public ImageIcon getIcon() { 
		ImageIcon firstIcon = new ImageIcon("src/icons/"+ color.toLowerCase() + "_" + type + ".png"); 
		Image img = firstIcon.getImage();
		Image imgNew = img.getScaledInstance(130, 200, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(imgNew);
	}
}
