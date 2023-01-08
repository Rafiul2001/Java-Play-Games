package BrickGame;

import JavaPlayGames.JPG;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public final class BrickGameFrame extends JFrame {

    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;

    public BrickGameFrame() {
        setVisible(true);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("! ! BRICK GAME ! !");
        Image imageIcon = Toolkit.getDefaultToolkit().getImage("src//BrickGame//BrickLogo.png");
        setIconImage(imageIcon);
        setResizable(false);
        BrickGamePanel panel = new BrickGamePanel();
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
