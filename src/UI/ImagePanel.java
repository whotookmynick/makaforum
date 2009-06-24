package UI;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

class ImagePanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected  Image ci=null;
 
    public ImagePanel() {

      validate();
      repaint();
    }
    
    public void setImage(Image img){
    	ci = img;
    	validate();
        repaint();
    }
    
 
  public Image getDisplayedImage() {
    return this.ci;
  }
 
  public void update(Graphics g) {
    if (ci!=null) {
      g.drawImage(ci, 0,0,this.getWidth(),this.getHeight(), this);
 
    } else {
      super.update(g);
    }
  }
 
  public void paint(Graphics g) {
    update(g);
  }
}//end of class
