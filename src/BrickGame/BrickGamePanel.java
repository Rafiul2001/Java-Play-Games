package BrickGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public final class BrickGamePanel extends JPanel implements ActionListener {

    private AudioInputStream sound;
    private Clip clip;
    private boolean running;
    private boolean start;
    private Timer timer;
    private final int delay = 10;
    Graphics2D g2d;

    private final int PANEL_WIDTH = 772;
    private final int PANEL_HEIGHT = 500;
    private final int PANEL_X = 5;
    private final int PANEL_Y = 5;

    private float ballX = 0f;
    private float ballY = 473f;
    private final float BALL_WIDTH = 20f;
    private final float BALL_HEIGHT = 20f;
    private float ballXDir = 5f;
    private float ballYDir = -5f;

    private float playerX = 300f;
    private final float playerY = 493f;
    private final float PLAYER_WIDTH = 150f;
    private final float PLAYER_HEIGHT = 5f;

    private final int row = 6;
    private final int column = 11;
    private final int map[][] = new int[row][column];

    private float boxX = 0f;
    private float boxY = 0f;
    private final float BOX_WIDTH = 50;
    private final float BOX_HEIGHT = 30;

    Random random;
    private int level = 1;
    public int score = 0;
    private int target = 0;

    private JLabel scoreBoard;
    private JLabel levelBoard;
    private JLabel selectLevellbl;

    private JButton startBtn;
    private JButton backToMainMenuBtn;
    private JButton levelBtn;
    private JButton levelBtn1;
    private JButton levelBtn2;
    private JButton levelBtn3;
    private JButton levelBtn4;
    private JButton levelBtn5;
    private JButton levelBtn6;

    Ellipse2D.Double ball;
    Rectangle2D.Double player;
    Rectangle2D.Double lineUp;

    public BrickGamePanel() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                playerX = (float) e.getX() - PLAYER_WIDTH / 2;
            }
        });
        start();
    }

    public void start() {
        running = true;
        start = false;
        random = new Random();
        timer = new Timer(delay, this);
        timer.start();

        scoreBoard = new JLabel();
        scoreBoard.setBounds(5, 520, 150, 30);
        add(scoreBoard);

        levelBoard = new JLabel();
        levelBoard.setBounds(617, 520, 150, 30);
        add(levelBoard);

        backToMainMenuBtn = new JButton();
        backToMainMenuBtn.setBounds((PANEL_X + (PANEL_WIDTH - 150) / 2), 520, 150, 30);
        backToMainMenuBtn.setText("Back");

        backToMainMenuBtn.addActionListener((ActionEvent e) -> {
            start = false;
            running = false;
            level = level - 1;
            score = 0;
            target = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    map[i][j] = 0;
                }
            }
            levelBoard.setText("");
            scoreBoard.setText("");
            remove(backToMainMenuBtn);
            add(startBtn);
            add(selectLevellbl);
            add(levelBtn);
        });

        startBtn = new JButton();
        startBtn.setBounds((PANEL_X + (PANEL_WIDTH - 150) / 2), 80, 150, 30);
        startBtn.setText("START GAME");
        add(startBtn);

        selectLevellbl = new JLabel();
        selectLevellbl.setText("Selected Level : " + level);
        selectLevellbl.setBounds((PANEL_X + (PANEL_WIDTH - 150) / 2), 115, 250, 30);
        add(selectLevellbl);

        levelBtn = new JButton();
        levelBtn.setBounds((PANEL_X + (PANEL_WIDTH - 200) / 2), 150, 200, 30);
        levelBtn.setText("SELECT STARTING LEVEL");
        add(levelBtn);

        levelBtn.addActionListener((ActionEvent e) -> {
            addAllLevelBtn();
        });

        levelBtn1 = new JButton();
        levelBtn1.setBounds((PANEL_X + (PANEL_WIDTH - 150) / 2), 185, 150, 30);
        levelBtn1.setText("LEVEL 1");

        levelBtn1.addActionListener((ActionEvent e) -> {
            level = 1;
            selectLevellbl.setText("Selected Level : " + level);
            removeAllLevelBtn();
        });

        levelBtn2 = new JButton();
        levelBtn2.setBounds((PANEL_X + (PANEL_WIDTH - 150) / 2), 220, 150, 30);
        levelBtn2.setText("LEVEL 2");

        levelBtn2.addActionListener((ActionEvent e) -> {
            level = 2;
            selectLevellbl.setText("Selected Level : " + level);
            removeAllLevelBtn();
        });

        levelBtn3 = new JButton();
        levelBtn3.setBounds((PANEL_X + (PANEL_WIDTH - 150) / 2), 255, 150, 30);
        levelBtn3.setText("LEVEL 3");

        levelBtn3.addActionListener((ActionEvent e) -> {
            level = 3;
            selectLevellbl.setText("Selected Level : " + level);
            removeAllLevelBtn();
        });

        levelBtn4 = new JButton();
        levelBtn4.setBounds((PANEL_X + (PANEL_WIDTH - 150) / 2), 290, 150, 30);
        levelBtn4.setText("LEVEL 4");

        levelBtn4.addActionListener((ActionEvent e) -> {
            level = 4;
            selectLevellbl.setText("Selected Level : " + level);
            removeAllLevelBtn();
        });

        levelBtn5 = new JButton();
        levelBtn5.setBounds((PANEL_X + (PANEL_WIDTH - 150) / 2), 325, 150, 30);
        levelBtn5.setText("LEVEL 5");

        levelBtn5.addActionListener((ActionEvent e) -> {
            level = 5;
            selectLevellbl.setText("Selected Level : " + level);
            removeAllLevelBtn();
        });

        levelBtn6 = new JButton();
        levelBtn6.setBounds((PANEL_X + (PANEL_WIDTH - 150) / 2), 360, 150, 30);
        levelBtn6.setText("LEVEL 6");

        levelBtn6.addActionListener((ActionEvent e) -> {
            level = 6;
            selectLevellbl.setText("Selected Level : " + level);
            removeAllLevelBtn();
        });

        startBtn.addActionListener((ActionEvent e) -> {
            start = true;
            running = true;
            remove(startBtn);
            remove(levelBtn);
            remove(selectLevellbl);
            removeAllLevelBtn();
        });
    }

    public void boxMap() {

        if (level == 1 && score == target) {
            setTimerDelayBeforeStartingLevel();
            ballX = (float) (PANEL_X + random.nextInt(PANEL_WIDTH - (int) BALL_WIDTH));
            ballY = 446f;
            int a;
            a = 5;
            for (int i = 0; i < row; i++) {
                for (int j = a; j < column - a; j++) {
                    map[i][j] = 1;
                    target++;
                }
                a--;
            }
            level++;
        } else if (level == 2 && score == target) {
            setTimerDelayBeforeStartingLevel();
            ballX = (float) (PANEL_X + random.nextInt(PANEL_WIDTH - (int) BALL_WIDTH));
            ballY = 446f;
            int a;
            a = 0;
            for (int i = 0; i < row; i++) {
                for (int j = a; j < column - a; j++) {
                    map[i][j] = 1;
                    target++;
                }
                a++;
            }
            level++;
        } else if (level == 3 && score == target) {
            setTimerDelayBeforeStartingLevel();
            ballX = (float) (PANEL_X + random.nextInt(PANEL_WIDTH - (int) BALL_WIDTH));
            ballY = 446f;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (j % 2 != 0) {
                        map[i][j] = 1;
                        target++;
                    }
                }
            }
            level++;
        } else if (level == 4 && score == target) {
            setTimerDelayBeforeStartingLevel();
            ballX = (float) (PANEL_X + random.nextInt(PANEL_WIDTH - (int) BALL_WIDTH));
            ballY = 446f;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (j % 2 == 0) {
                        map[i][j] = 1;
                        target++;
                    }
                }
            }
            level++;
        } else if (level == 5 && score == target) {
            setTimerDelayBeforeStartingLevel();
            ballX = (float) (PANEL_X + random.nextInt(PANEL_WIDTH - (int) BALL_WIDTH));
            ballY = 446f;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (i % 2 == 0 && j % 2 != 0) {
                        map[i][j] = 1;
                        target++;
                    }
                    if (i % 2 != 0 && j % 2 == 0) {
                        map[i][j] = 1;
                        target++;
                    }
                }
            }
            level++;
        } else if (level == 6 && score == target) {
            setTimerDelayBeforeStartingLevel();
            ballX = (float) (PANEL_X + random.nextInt(PANEL_WIDTH - (int) BALL_WIDTH));
            ballY = 446f;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    map[i][j] = 1;
                    target++;
                }
            }
            level++;
        } else if (level == 7 && score == target) {
            running = false;
        } else {
            timer.setDelay(delay);
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g2d = (Graphics2D) g;

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (start) {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT);
            if (running) {
                ball = new Ellipse2D.Double(
                        ballX,
                        ballY,
                        BALL_WIDTH,
                        BALL_HEIGHT);

                g2d.setColor(Color.red);
                g2d.fill(ball);

                timer.setInitialDelay(1000);
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < column; j++) {
                        if (map[i][j] == 1) {
                            boxX = j * BOX_WIDTH + 5;
                            boxY = i * BOX_HEIGHT + 5;
                            Rectangle2D.Double boxRect = new Rectangle2D.Double(
                                    boxX + (PANEL_WIDTH - column * BOX_WIDTH) / 2,
                                    boxY + 75,
                                    BOX_WIDTH,
                                    BOX_HEIGHT);
                            g2d.setColor(Color.WHITE);
                            g2d.fill(boxRect);
                            g2d.setColor(Color.BLACK);
                            g2d.draw(boxRect);
                        }
                    }
                }

                player = new Rectangle2D.Double(
                        playerX,
                        playerY,
                        PLAYER_WIDTH,
                        PLAYER_HEIGHT);
                g2d.setColor(Color.GRAY);
                g2d.fill(player);
            } else {
                gameOver(g2d);
            }
        }
    }

    public void gameOver(Graphics g2d) {
        g2d.setColor(Color.red);
        g2d.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g2d.getFont());
        g2d.drawString("Game Over",
                (PANEL_WIDTH - metrics1.stringWidth("Game Over")) / 2,
                PANEL_HEIGHT / 2);
    }

    public void ballMove() {
        ballX += ballXDir;
        ballY += ballYDir;
    }

    public void checkCollisions() {
        Rectangle rectBall = new Rectangle(
                (int) ballX,
                (int) ballY,
                (int) BALL_WIDTH,
                (int) BALL_HEIGHT);
        Rectangle rectPlayer = new Rectangle(
                (int) playerX,
                (int) playerY,
                (int) PLAYER_WIDTH,
                (int) PLAYER_HEIGHT);
        // Check Collisions Between Player And SideBars
        if (playerX <= PANEL_X) {
            playerX = PANEL_X;
        }
        if (playerX + PLAYER_WIDTH >= PANEL_X + PANEL_WIDTH) {
            playerX = PANEL_X + PANEL_WIDTH - PLAYER_WIDTH;
        }
        // Check Collisions Between Ball And SideBar
        if (ballX + BALL_WIDTH >= PANEL_WIDTH + PANEL_X) { // right side collision
            ballXDir = -ballXDir;
        }
        if (ballX <= PANEL_X) { // left side collision
            ballXDir = -ballXDir;
        }
        if (ballY + BALL_HEIGHT >= PANEL_HEIGHT + PANEL_Y) { // bottom side collision
            ballYDir = -ballYDir;
            running = false;
        }
        if (ballY <= PANEL_Y) { // top side collision
            ballYDir = -ballYDir;
        }
        // Check Collisions Between Ball And Player
        if (rectBall.intersects(rectPlayer)) {
//            try {
////                sound = AudioSystem.getAudioInputStream(new File("src/BrickGame/ball_ground.wav"));
//                clip = AudioSystem.getClip();
//                clip.open(AudioSystem.getAudioInputStream(new File("src/BrickGame/ball_ground.wav")));
//                clip.start();
//            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
//                Logger.getLogger(BrickGamePanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
            if (ballX + BALL_WIDTH - 5f <= playerX) {
                ballX = playerX - BALL_WIDTH;
                ballXDir = -ballXDir;
            } else if (ballX + 5f >= playerX + PLAYER_WIDTH) {
                ballX = playerX + PLAYER_WIDTH;
                ballXDir = -ballXDir;
            }
            if (ballY + BALL_HEIGHT - 5f <= playerY) {
                ballY = playerY - BALL_HEIGHT;
                ballYDir = -ballYDir;
            } else if (ballY + 5f >= playerY + PLAYER_HEIGHT) {
                ballY = playerY + PLAYER_HEIGHT;
                ballYDir = -ballYDir;
            }
        }
        //Check Collisions Between Ball And Boxes;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (map[i][j] == 1) {
                    boxX = j * BOX_WIDTH + 5 + (PANEL_WIDTH - column * BOX_WIDTH) / 2;
                    boxY = i * BOX_HEIGHT + 5 + 75;
                    Rectangle rectBox = new Rectangle(
                            (int) boxX,
                            (int) boxY,
                            (int) BOX_WIDTH,
                            (int) BOX_HEIGHT);
                    if (rectBall.intersects(rectBox)) {
                        if (ballX + BALL_WIDTH - 5f <= boxX && ballXDir > 0) {
                            //ballX = playerX - BALL_WIDTH;
                            ballXDir = -ballXDir;
                        } else if (ballX + 5f >= boxX + BOX_WIDTH && ballXDir < 0) {
                            //ballX = playerX + PLAYER_WIDTH;
                            ballXDir = -ballXDir;
                        }
                        if (ballY + BALL_HEIGHT - 5f <= boxY && ballYDir > 0) {
                            //ballY = playerY - BALL_HEIGHT;
                            ballYDir = -ballYDir;
                        } else if (ballY + 5f >= boxY + BOX_HEIGHT && ballYDir < 0) {
                            //ballY = playerY + PLAYER_HEIGHT;
                            ballYDir = -ballYDir;
                        }
                        map[i][j] = 0;
                        score++;
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (start) {
            if (running) {
                boxMap();
                ballMove();
                checkCollisions();
                scoreBoard.setText("Score : " + score);
                levelBoard.setText("Selected level : " + (level - 1));
            } else {
                add(backToMainMenuBtn);
            }

        }
        repaint();

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addAllLevelBtn() {
        add(levelBtn1);
        add(levelBtn2);
        add(levelBtn3);
        add(levelBtn4);
        add(levelBtn5);
        add(levelBtn6);
    }

    public void removeAllLevelBtn() {
        remove(levelBtn1);
        remove(levelBtn2);
        remove(levelBtn3);
        remove(levelBtn4);
        remove(levelBtn5);
        remove(levelBtn6);
    }

    public void setTimerDelayBeforeStartingLevel() {
        //timer.setDelay(100);
        if (ballYDir > 0) {
            ballYDir = -ballYDir;
        }
    }

    public void setTimerStop() {
        timer.stop();
    }
}
