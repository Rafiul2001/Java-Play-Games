package CarGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public final class CarGamePanel extends JPanel implements ActionListener {

    private Graphics2D g2d;
    private Timer timer;
    private Timer timer1;
    private Timer timer2;
    private final Random random = new Random();
    private final int delay = 10;
    private boolean running = false;
    private boolean start = false;

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    private final int PANEL_X = 7;
    private final int PANEL_Y = 0;
    private final int PANEL_WIDTH = 240;
    private final int PANEL_HEIGHT = 573;

    private final int UNIT_SIZE = 40;
    private int posX;
    private int posY;

    private final int totalXPos = PANEL_WIDTH / UNIT_SIZE;
    private final int totalYPos = PANEL_HEIGHT / UNIT_SIZE;

    private BufferedImage PLAYER_IMAGE;
    private BufferedImage ENEMY_IMAGE1;
    private BufferedImage ENEMY_IMAGE2;
    private BufferedImage ENEMY_IMAGE3;
    private BufferedImage ENEMY_IMAGE4;
    private BufferedImage ENEMY_IMAGE5;

    private final int ENEMY_CAR_WIDTH = 40;
    private final int ENEMY_CAR_HEIGHT = 80;
    private int enemyCarSpeed = 3;
    private final ArrayList<Integer> enemyCarX0 = new ArrayList<>();
    private final ArrayList<Integer> enemyCarY0 = new ArrayList<>();
    private final ArrayList<Integer> enemyCarYSpeed = new ArrayList<>();
    private final List<BufferedImage> enemyImages = new ArrayList<>();

    private final ArrayList<Integer> roadDividerX = new ArrayList<>();
    private final ArrayList<Integer> roadDividerY = new ArrayList<>();

    private final int PLAYER_WIDTH = 40;
    private final int PLAYER_HEIGHT = 80;
    private int playerX = 120;
    private int playerY = PANEL_HEIGHT - PLAYER_HEIGHT;

    private int score = 0;

    private JButton startBtn;
    private JButton backBtn;
    private JLabel scoreBoard;
    private int lvl = 0;

    public CarGamePanel(int width, int height) {
        this.SCREEN_WIDTH = width;
        this.SCREEN_HEIGHT = height;
        start();
        setFocusable(true);
        myKeyAdapter();
    }

    public void start() {
        try {
            PLAYER_IMAGE = ImageIO.read(getClass().getResource("RPlayer.png"));
            ENEMY_IMAGE1 = ImageIO.read(getClass().getResource("enemy1.png"));
            ENEMY_IMAGE2 = ImageIO.read(getClass().getResource("enemy2.png"));
            ENEMY_IMAGE3 = ImageIO.read(getClass().getResource("enemy3.png"));
            ENEMY_IMAGE4 = ImageIO.read(getClass().getResource("enemy4.png"));
            ENEMY_IMAGE5 = ImageIO.read(getClass().getResource("enemy5.png"));
            System.out.println(totalXPos + " " + totalYPos);
        } catch (IOException ex) {
            Logger.getLogger(CarGamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        timer = new Timer(delay, this);
        timer.start();

        timer1 = new Timer(500, (ActionEvent e) -> {
            if (running) {
                int x1 = random.nextInt(5);
                switch (x1) {
                    case 1:
                        enemyImages.add(ENEMY_IMAGE1);
                        break;
                    case 2:
                        enemyImages.add(ENEMY_IMAGE2);
                        break;
                    case 3:
                        enemyImages.add(ENEMY_IMAGE3);
                        break;
                    case 4:
                        enemyImages.add(ENEMY_IMAGE4);
                        break;
                    default:
                        enemyImages.add(ENEMY_IMAGE5);
                        break;
                }
                enemyCarX0.add(random.nextInt(totalXPos) * ENEMY_CAR_WIDTH);
                enemyCarY0.add(PANEL_Y);
                enemyCarYSpeed.add(enemyCarSpeed);
            }
        });
        timer1.start();

        timer2 = new Timer(200, (ActionEvent e) -> {
            if (running) {
                for (int i = 1; i < totalXPos; i++) {
                    if (lvl == 0) {
                        for (int j = 0; j < totalYPos; j++) {
                            roadDividerX.add(i * UNIT_SIZE);
                            roadDividerY.add(j * UNIT_SIZE);
                        }

                    } else {
                        roadDividerX.add(i * UNIT_SIZE);
                        roadDividerY.add(0);
                    }
                }
                lvl = 1;
            }
        });
        timer2.start();

        startBtn = new JButton();
        startBtn.setBounds((SCREEN_WIDTH - 150) / 2, 80, 150, 30);
        startBtn.setText("Start");
        add(startBtn);

        backBtn = new JButton();
        backBtn.setBounds(SCREEN_WIDTH - 150, 435, 100, 30);
        backBtn.setText("Back");
        add(backBtn);

        scoreBoard = new JLabel();
        scoreBoard.setBounds(SCREEN_WIDTH - 150, 400, 100, 30);
        scoreBoard.setText("Score : " + score);
        add(scoreBoard);

        startBtn.addActionListener((ActionEvent e) -> {
            start = true;
            running = true;
        });

        backBtn.addActionListener((ActionEvent e) -> {
            running = false;
            start = false;
            enemyCarX0.removeAll(enemyCarX0);
            enemyCarY0.removeAll(enemyCarY0);
            enemyCarYSpeed.removeAll(enemyCarYSpeed);
            enemyImages.removeAll(enemyImages);
        });
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
                drawRoad(g2d);
                player(g2d);
                enemyCar(g2d);
//                g2d.setColor(Color.BLUE);
//                for (int i = 0; i < totalYPos; i++) {
//                    for (int j = 0; j < totalXPos; j++) {
//                        posX = j * UNIT_SIZE;
//                        posY = i * UNIT_SIZE;
//                        g2d.setColor(Color.BLUE);
//                        g2d.drawRect(PANEL_X + posX, PANEL_Y + posY, UNIT_SIZE, UNIT_SIZE);
//                    }
//                }
            } else {
                gameOver(g2d);
            }
        }
    }

    public void drawRoad(Graphics2D g2d) {
        g2d.setColor(new Color(107, 103, 103));
        g2d.fillRect(PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT);

        for (int x = 0; x < roadDividerX.size(); x++) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(
                    PANEL_X + roadDividerX.get(x),
                    PANEL_Y + roadDividerY.get(x),
                    4,
                    20
            );
        }
        for (int x = 0; x < roadDividerY.size(); x++) {
            roadDividerY.set(x, roadDividerY.get(x) + 3);
        }
    }

    public void player(Graphics2D g2d) {
        g2d.drawImage(PLAYER_IMAGE,
                PANEL_X + playerX,
                PANEL_Y + playerY,
                PLAYER_WIDTH,
                PLAYER_HEIGHT, this);
    }

    public void enemyCar(Graphics2D g2d) {
        for (int x = 0; x < enemyCarX0.size(); x++) {
            g2d.drawImage(enemyImages.get(x),
                    PANEL_X + enemyCarX0.get(x),
                    PANEL_Y + enemyCarY0.get(x),
                    ENEMY_CAR_WIDTH,
                    ENEMY_CAR_HEIGHT, this);
        }
    }

    public void enemyCarMove() {
        for (int x = 0; x < enemyCarX0.size(); x++) {
            enemyCarY0.set(x, enemyCarYSpeed.set(x, enemyCarYSpeed.get(x) + enemyCarSpeed));
        }
    }

    public void gameOver(Graphics g2d) {
        g2d.setColor(Color.red);
        g2d.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g2d.getFont());
        g2d.drawString("Game Over",
                (PANEL_WIDTH - metrics1.stringWidth("Game Over")) / 2,
                PANEL_HEIGHT / 2);
    }

    public void checkCollisions() {
        Rectangle playerRect = new Rectangle(
                PANEL_X + playerX,
                PANEL_Y + playerY,
                PLAYER_WIDTH,
                PLAYER_HEIGHT);
        for (int x = 0; x < enemyCarX0.size(); x++) {
            if (enemyCarY0.get(x) >= PANEL_HEIGHT - ENEMY_CAR_HEIGHT) {
                enemyCarX0.remove(x);
                enemyCarY0.remove(x);
                enemyCarYSpeed.remove(x);
                enemyImages.remove(x);
                score++;
            }
            Rectangle enemyRect = new Rectangle(
                    PANEL_X + enemyCarX0.get(x),
                    PANEL_Y + enemyCarY0.get(x),
                    ENEMY_CAR_WIDTH,
                    ENEMY_CAR_HEIGHT);
            if (enemyRect.intersects(playerRect)) {
                running = false;
            }
        }
        //RoadDividers with bottom side
        for (int i = 0; i < roadDividerY.size(); i++) {
            if (roadDividerY.get(i) >= PANEL_HEIGHT) {
                roadDividerX.remove(i);
                roadDividerY.remove(i);
            }
        }
        // Player with Panel Sides
        if (playerX <= 0) {
            playerX = 0;
        }
        if (playerX >= PANEL_WIDTH - PLAYER_WIDTH) {
            playerX = PANEL_WIDTH - PLAYER_WIDTH;
        }
        if (playerY <= 0) {
            playerY = 0;
        }
        if (playerY >= PANEL_HEIGHT - PLAYER_HEIGHT) {
            playerY = PANEL_HEIGHT - PLAYER_HEIGHT;
        }
    }

    public void scoreChecker() {
        switch (score) {
            case 0:
                enemyCarSpeed = 5;
                timer1.setDelay(500);
                break;
            case 20:
                enemyCarSpeed = 6;
                break;
            case 50:
                enemyCarSpeed = 7;
                break;
            case 100:
                enemyCarSpeed = 8;
                timer1.setDelay(300);
            case 300:
                enemyCarSpeed = 9;
                timer1.setDelay(200);
                break;
            default:
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (start) {
            if (running) {
                scoreChecker();
                checkCollisions();
                enemyCarMove();
                remove(backBtn);
            } else {
                add(backBtn);
            }
            remove(startBtn);
            add(scoreBoard);
            scoreBoard.setText("Score : " + score);
        } else {
            add(startBtn);
            remove(scoreBoard);
            remove(backBtn);
            score = 0;
        }
        repaint();
    }

    public void myKeyAdapter() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
//                        isUp = true;
                        playerY -= 40;
                        break;
                    case KeyEvent.VK_DOWN:
//                        isDown = true;
                        playerY += 40;
                        break;
                    case KeyEvent.VK_RIGHT:
//                        isRight = true;
                        playerX += 40;
                        break;
                    case KeyEvent.VK_LEFT:
//                        isLeft = true;
                        playerX -= 40;
                        break;
                    case KeyEvent.VK_SPACE:
                        break;
                }
            }
        });
    }

    public void setTimerStop() {
        timer.stop();
        timer1.stop();
        timer2.stop();
    }
}
