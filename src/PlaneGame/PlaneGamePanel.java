package PlaneGame;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public final class PlaneGamePanel extends JPanel implements ActionListener {

    private Clip clip;
    private AudioInputStream sound;

    private Timer timer;
    private Timer timerForGeneratingPlayerBullet;
    private Timer timerForGeneratingEnemySpaceShettle;
    private Timer timerForGeneratingEnemyBullet; // For Generating Enemy Bullets
    private Timer timerForEnemyBulletMovement; // For Moving Enemy Bullets
    private final Random random = new Random();
    private final int delayTimer = 1;
    private Graphics2D g2d;

    private boolean start = false;
    private boolean running = false;

    private final JButton startBtn = new JButton();
    private final JButton backBtn = new JButton();
    private final JButton restartBtn = new JButton();
    private final JLabel scoreBoard = new JLabel();
    private final JLabel playerLifeBarText = new JLabel();
    private int score = 0;

    private BufferedImage planeImage;
    private BufferedImage enemyImage1;
    private BufferedImage enemyImage2;
    private BufferedImage enemyImage3;
    private BufferedImage enemyImage4;
    private BufferedImage bulletImage;

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    private final int PANEL_X = 7;
    private final int PANEL_Y = 7;
    private final int PANEL_WIDTH = 320;
    private final int PANEL_HEIGHT = 560;

    private final int UNIT_SIZE = 40;
    private final int totalXPos = PANEL_WIDTH / UNIT_SIZE;
    private final int totalYPos = PANEL_HEIGHT / UNIT_SIZE;

    private final int PLAYER_WIDTH = 40;
    private final int PLAYER_HEIGHT = 40;
    private int playerX = 0;
    private int playerY = PANEL_HEIGHT - PLAYER_HEIGHT;

    private final ArrayList<Integer> bulletX = new ArrayList<>();
    private final ArrayList<Integer> bulletY = new ArrayList<>();
    private int bulletYSpeed = 5;
    private final int BULLET_WIDTH = 20;
    private final int BULLET_HEIGHT = 20;
    private int playerLife = 10;

    private final ArrayList<Integer> enemyBulletX = new ArrayList<>();
    private final ArrayList<Integer> enemyBulletY = new ArrayList<>();
    private int enemyBulletYSpeed = 5;
    private final int ENEMY_BULLET_WIDTH = 20;
    private final int ENEMY_BULLET_HEIGHT = 20;

    private final ArrayList<Integer> ENEMY_X = new ArrayList<>();
    private final ArrayList<Integer> ENEMY_Y = new ArrayList<>();
    private final ArrayList<Integer> ENEMY_HEALTH = new ArrayList<>();
    private int enemySpeed = 1;
    private final int ENEMY_WIDTH = 40;
    private final int ENEMY_HEIGHT = 40;
    private final int ENEMY_MAX_HEALTH = 8;

    private final List<BufferedImage> enemyImages = new ArrayList<>();

    private Rectangle enemyRect;
    private Rectangle bulletRect;
    private Rectangle playerRect;

    public PlaneGamePanel(int width, int height) {
        this.SCREEN_WIDTH = width;
        this.SCREEN_HEIGHT = height;
        start();
        setFocusable(true);
        myKeyAdapter();
        myMouseAdapter();
    }

    public void start() {
        try {
            planeImage = ImageIO.read(getClass().getResource("player.png"));
            enemyImage1 = ImageIO.read(getClass().getResource("enemy1.png"));
            enemyImage2 = ImageIO.read(getClass().getResource("enemy2.png"));
            enemyImage3 = ImageIO.read(getClass().getResource("enemy3.png"));
            enemyImage4 = ImageIO.read(getClass().getResource("enemy4.png"));
            bulletImage = ImageIO.read(getClass().getResource("bullet.png"));
            System.out.println(totalXPos + " " + totalYPos);
        } catch (IOException ex) {
            Logger.getLogger(PlaneGamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        // timer for all collisions and other movements
        timer = new Timer(delayTimer, this);
        timer.start();
        // timerForGeneratingPlayerBullet is for generating bullets
        timerForGeneratingPlayerBullet = new Timer(80, (ActionEvent e) -> {
            if (running) {
                bulletX.add(playerX + (PLAYER_WIDTH - BULLET_WIDTH) / 2);
                bulletY.add(playerY + (PLAYER_HEIGHT - BULLET_HEIGHT) / 2);

            }
        });
        timerForGeneratingPlayerBullet.start();
        // timerForGeneratingEnemySpaceShettle is for generating enemy space shettle
        timerForGeneratingEnemySpaceShettle = new Timer(1000, (ActionEvent e) -> {
            int random1 = 1 + random.nextInt(totalXPos - 2);
            int random2 = 1 + random.nextInt(4);
            if (running) {
                switch (random2) {
                    case 1:
                        enemyImages.add(enemyImage1);
                        break;
                    case 2:
                        enemyImages.add(enemyImage2);
                        break;
                    case 3:
                        enemyImages.add(enemyImage3);
                        break;
                    default:
                        enemyImages.add(enemyImage4);
                        break;
                }
                ENEMY_X.add(random1 * UNIT_SIZE);
                ENEMY_Y.add(0);
                ENEMY_HEALTH.add(ENEMY_MAX_HEALTH);
            }
        });
        timerForGeneratingEnemySpaceShettle.start();
        // timerForGeneratingEnemyBullet for generating enemy bullet
        timerForGeneratingEnemyBullet = new Timer(4000, (ActionEvent e) -> {
            if (running) {
                for (int x = 0; x < ENEMY_X.size(); x++) {
                    enemyBulletX.add(ENEMY_X.get(x) + (ENEMY_WIDTH - ENEMY_BULLET_WIDTH) / 2);
                    enemyBulletY.add(ENEMY_Y.get(x) + (ENEMY_HEIGHT - ENEMY_BULLET_HEIGHT) / 2);
                }
            }
        });
        timerForGeneratingEnemyBullet.start();
        // timerForEnemyBulletMovement for enemy bullet movement
        timerForEnemyBulletMovement = new Timer(20, (ActionEvent e) -> {
            if (running) {
                enemyBulletMove();
            }
        });

        timerForEnemyBulletMovement.start();

        startBtn.setBounds((SCREEN_WIDTH - 100) / 2, 100, 100, 30);
        startBtn.setText("Start");
        add(startBtn);

        backBtn.setBounds(SCREEN_WIDTH - 150, 400, 100, 30);
        backBtn.setText("Back");
        add(backBtn);

        scoreBoard.setBounds(SCREEN_WIDTH - 150, 440, 100, 30);
        scoreBoard.setText("Score : ");
        add(scoreBoard);

        playerLifeBarText.setBounds(SCREEN_WIDTH - 150, 30, 100, 30);
        playerLifeBarText.setText("Remaing Life : ");
        add(playerLifeBarText);

        restartBtn.setBounds(SCREEN_WIDTH - 150, 370, 100, 30);
        restartBtn.setText("Restart");
        add(restartBtn);

        startBtn.addActionListener((ActionEvent e) -> {
            start = true;
            running = true;
        });

        backBtn.addActionListener((ActionEvent e) -> {
            bulletX.removeAll(bulletX);
            bulletY.removeAll(bulletY);
            removeAllEnemyElements();
            start = false;
            running = false;
        });

        restartBtn.addActionListener((ActionEvent e) -> {
            bulletX.removeAll(bulletX);
            bulletY.removeAll(bulletY);
            removeAllEnemyElements();
            score = 0;
            playerLife = 10;
            running = true;
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g2d = (Graphics2D) g;
        if (start) {
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );
            drawPanelBackground(g2d);
            if (running) {
//                drawCordinates(g2d);
                drawBullet(g2d);
                drawPlayer(g2d);
                drawEnemyPlayer(g2d);
                drawEnemyBullet(g2d);
                drawPlayerLiveBar(g2d);
            } else {
                gameOver(g2d);
            }
        }
    }

    public void gameOver(Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g2d.getFont());
        g2d.drawString("Game Over",
                (PANEL_WIDTH - metrics1.stringWidth("Game Over")) / 2,
                PANEL_HEIGHT / 2);
    }

    public void drawPlayerLiveBar(Graphics2D g2d) {
        int lifeLineWidth = 10;
        int lifeLineHeight = 20;
        g2d.setColor(Color.BLUE);
        for (int i = 1; i <= playerLife; i++) {
            g2d.fillRect(PANEL_X + 350, PANEL_Y + 50, lifeLineWidth * i, lifeLineHeight);
        }
        g2d.setColor(Color.BLACK);
        for (int i = 1; i <= playerLife; i++) {
            g2d.drawRect(PANEL_X + 350, PANEL_Y + 50, lifeLineWidth * i, lifeLineHeight);
        }
    }

    public void drawPanelBackground(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(
                PANEL_X,
                PANEL_Y,
                PANEL_WIDTH,
                PANEL_HEIGHT
        );
    }

    public void drawCordinates(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        for (int i = 0; i < totalYPos; i++) {
            for (int j = 0; j < totalXPos; j++) {
                g2d.drawRect(
                        PANEL_X + j * UNIT_SIZE,
                        PANEL_Y + i * UNIT_SIZE,
                        UNIT_SIZE,
                        UNIT_SIZE
                );
            }
        }
    }

    public void drawPlayer(Graphics2D g2d) {
        g2d.drawImage(planeImage,
                PANEL_X + playerX,
                PANEL_Y + playerY,
                PLAYER_WIDTH,
                PLAYER_HEIGHT, this);
    }

    public void drawBullet(Graphics2D g2d) {
        for (int x = 0; x < bulletX.size(); x++) {
            g2d.drawImage(bulletImage,
                    PANEL_X + bulletX.get(x),
                    PANEL_Y + bulletY.get(x),
                    BULLET_WIDTH,
                    BULLET_HEIGHT,
                    this
            );
        }
    }

    public void drawEnemyPlayer(Graphics2D g2d) {
        for (int x = 0; x < ENEMY_X.size(); x++) {
            g2d.drawImage(enemyImages.get(x),
                    PANEL_X + ENEMY_X.get(x),
                    PANEL_Y + ENEMY_Y.get(x),
                    ENEMY_WIDTH,
                    ENEMY_HEIGHT,
                    this
            );
        }
    }

    public void drawEnemyBullet(Graphics2D g2d) {
        for (int x = 0; x < enemyBulletX.size(); x++) {
            g2d.drawImage(bulletImage,
                    PANEL_X + enemyBulletX.get(x),
                    PANEL_Y + enemyBulletY.get(x),
                    ENEMY_BULLET_WIDTH,
                    ENEMY_BULLET_HEIGHT,
                    this
            );
        }
    }

    public void enemyMove() {
        for (int i = 0; i < ENEMY_X.size(); i++) {
            ENEMY_Y.set(i, ENEMY_Y.get(i) + enemySpeed);
        }
    }

    public void bulletMove() {
        for (int i = 0; i < bulletX.size(); i++) {
            bulletY.set(i, bulletY.get(i) - bulletYSpeed);
        }
    }

    public void enemyBulletMove() {
        for (int i = 0; i < enemyBulletX.size(); i++) {
            enemyBulletY.set(i, enemyBulletY.get(i) + enemyBulletYSpeed);
        }
    }

    public void checkCollisions() {
        // Player with sides
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
        // Bullet with top side
        for (int x = 0; x < bulletX.size(); x++) {
            if (bulletY.get(x) <= 0) {
                bulletX.remove(x);
                bulletY.remove(x);
            }
        }
        // Enemy bullet with bottom side
        for (int x = 0; x < enemyBulletX.size(); x++) {
            if (enemyBulletY.get(x) >= PANEL_HEIGHT - ENEMY_BULLET_HEIGHT) {
                enemyBulletX.remove(x);
                enemyBulletY.remove(x);
            }
        }

        playerRect = new Rectangle(
                PANEL_X + playerX,
                PANEL_X + playerY,
                PLAYER_WIDTH,
                PLAYER_HEIGHT
        );
        // Enemy with player
        for (int j = 0; j < ENEMY_X.size(); j++) {
            enemyRect = new Rectangle(
                    PANEL_X + ENEMY_X.get(j),
                    PANEL_Y + ENEMY_Y.get(j),
                    ENEMY_WIDTH,
                    ENEMY_HEIGHT
            );
            if (enemyRect.intersects(playerRect)) {
                ENEMY_X.remove(j);
                ENEMY_Y.remove(j);
                ENEMY_HEALTH.remove(j);
                enemyImages.remove(j);
                playerLife--;
                break;
            }
        }

        // Enemy bullet with player
        for (int x = 0; x < enemyBulletX.size(); x++) {
            Rectangle enemyBulletRect = new Rectangle(
                    PANEL_X + enemyBulletX.get(x),
                    PANEL_Y + enemyBulletY.get(x),
                    ENEMY_BULLET_WIDTH,
                    ENEMY_BULLET_HEIGHT
            );
            if (enemyBulletRect.intersects(playerRect)) {
                enemyBulletX.remove(x);
                enemyBulletY.remove(x);
                playerLife--;
            }
        }
        // Enemy with bottom
        for (int x = 0; x < ENEMY_X.size(); x++) {
            if (ENEMY_Y.get(x) >= PANEL_HEIGHT) {
                ENEMY_X.remove(x);
                ENEMY_Y.remove(x);
                ENEMY_HEALTH.remove(x);
                enemyImages.remove(x);
                running = false;
            }
        }
        // Bullet with Enemy
        for (int x = 0; x < bulletX.size(); x++) {
            bulletRect = new Rectangle(
                    PANEL_X + bulletX.get(x),
                    PANEL_Y + bulletY.get(x),
                    BULLET_WIDTH,
                    BULLET_HEIGHT
            );
            for (int j = 0; j < ENEMY_X.size(); j++) {
                enemyRect = new Rectangle(
                        PANEL_X + ENEMY_X.get(j),
                        PANEL_Y + ENEMY_Y.get(j),
                        ENEMY_WIDTH,
                        ENEMY_HEIGHT
                );
                if (bulletRect.intersects(enemyRect)) {
                    ENEMY_HEALTH.set(j, ENEMY_HEALTH.get(j) - 1);
                    bulletX.remove(x);
                    bulletY.remove(x);
                    if (ENEMY_HEALTH.get(j) == 0) {
                        ENEMY_X.remove(j);
                        ENEMY_Y.remove(j);
                        ENEMY_HEALTH.remove(j);
                        enemyImages.remove(j);
                        score++;
                    }
                    break;
                }
            }
        }
    }

    public void checkPlayerLife() {
        if (playerLife == 0) {
            running = false;
            playerLife = 10;
        }
    }

    public void checkScore() {
        switch (score) {
            case 0:
                enemyBulletYSpeed = 5;
                enemySpeed = 1;
                timerForGeneratingPlayerBullet.setDelay(80);
                timerForGeneratingEnemySpaceShettle.setDelay(1000);
                break;
            case 10:
                enemyBulletYSpeed = 7;
                break;
            case 20:
                bulletYSpeed = 7;
                timerForGeneratingPlayerBullet.setDelay(65);
                timerForGeneratingEnemySpaceShettle.setDelay(800);
                break;
            case 40:
                timerForGeneratingPlayerBullet.setDelay(40);
                enemySpeed = 2;
                break;
            case 90:
                timerForGeneratingEnemySpaceShettle.setDelay(500);
                break;
            default:
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (start) {
            if (running) {
                checkPlayerLife();
                checkScore();
                checkCollisions();
                bulletMove();
                enemyMove();
                remove(backBtn);
            } else {
                add(backBtn);
            }
            add(scoreBoard);
            add(playerLifeBarText);
            add(restartBtn);
            scoreBoard.setText("Score : " + score);
            remove(startBtn);
        } else {
            add(startBtn);
            remove(restartBtn);
            remove(backBtn);
            remove(scoreBoard);
            remove(playerLifeBarText);
            score = 0;
            playerLife = 10;
        }
        repaint();
    }

    public void myKeyAdapter() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        playerY -= 40;
                        break;
                    case KeyEvent.VK_DOWN:
                        playerY += 40;
                        break;
                    case KeyEvent.VK_RIGHT:
                        playerX += 40;
                        break;
                    case KeyEvent.VK_LEFT:
                        playerX -= 40;
                        break;
                    case KeyEvent.VK_SPACE:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void myMouseAdapter() {
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (running) {
                    playerX = e.getX() - (PLAYER_WIDTH / 2) - PANEL_X;
                    playerY = e.getY() - (PLAYER_HEIGHT / 2) - PANEL_Y;
                }
            }
        });
    }

    public void removeAllEnemyElements() {
        ENEMY_X.removeAll(ENEMY_X);
        ENEMY_Y.removeAll(ENEMY_Y);
        ENEMY_HEALTH.removeAll(ENEMY_HEALTH);
        enemyImages.removeAll(enemyImages);
        enemyBulletX.removeAll(enemyBulletX);
        enemyBulletY.removeAll(enemyBulletY);
    }

    public void setTimerStop() {
        timer.stop();
        timerForGeneratingPlayerBullet.stop();
        timerForGeneratingEnemySpaceShettle.stop();
        timerForGeneratingEnemyBullet.stop();
        timerForEnemyBulletMovement.stop();
    }
}
