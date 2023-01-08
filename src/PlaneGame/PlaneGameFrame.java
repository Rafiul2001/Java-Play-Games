package PlaneGame;

import JavaPlayGames.JPG;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public final class PlaneGameFrame extends JFrame {

    private final int SCREEN_WIDTH = 500;
    private final int SCREEN_HEIGHT = 600;

    public PlaneGameFrame() {
        setVisible(true);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("! ! Space Shooter ! !");
        Image imageIcon = Toolkit.getDefaultToolkit().getImage("src//PlaneGame//PlaneLogo.png");
        setIconImage(imageIcon);
        setResizable(false);
        PlaneGamePanel panel = new PlaneGamePanel(SCREEN_WIDTH, SCREEN_HEIGHT);
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
