
package CarGame;

import JavaPlayGames.JPG;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public final class CarGameFrame extends JFrame{
    private final int SCREEN_WIDTH = 400;
    private final int SCREEN_HEIGHT = 600;
    
    public CarGameFrame(){
        setVisible(true);
        setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("! ! CAR GAME ! !");
        Image imageIcon = Toolkit.getDefaultToolkit().getImage("src//CarGame//CarLogo.png");
        setIconImage(imageIcon);
        setResizable(false);
        CarGamePanel panel = new CarGamePanel(SCREEN_WIDTH,SCREEN_HEIGHT);
        panel.setLayout(null);
        add(panel);    
        addWindowListener(new WindowAdapter() { 
            @Override
            public void windowClosed(WindowEvent e) {
                new JPG().setVisible(true);
                panel.setTimerStop();
            }
        });
    }
}