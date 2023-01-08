package SnakeGame;

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
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public final class SnakeGamePanel extends JPanel implements ActionListener {

    private Graphics2D g2d;
    private Timer timer;
    private final int delay = 120;
    private boolean running = false;
    private boolean start = false;

    private final int PANEL_X = 5;
    private final int PANEL_Y = 5;
    private final int PANEL_WIDTH = 780;
    private final int PANEL_HEIGHT = 500;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (PANEL_WIDTH * PANEL_HEIGHT) / UNIT_SIZE;
    private int bodyParts = 6;
    private char direction = 'R';
    private int[] X = new int[GAME_UNITS];
    private int[] Y = new int[GAME_UNITS];
    private int appleX;
    private int appleY;

    private int boxX = 0;
    private int boxY = 0;
    private final int BOX_WIDTH = UNIT_SIZE;
    private final int BOX_HEIGHT = UNIT_SIZE;

    private int applesEaten = 0;
    private Random random;
    private int level = 1;

    private JLabel scoreBoard;
    private JButton startBtn;
    private JButton backBtn;

    private Rectangle rectApple;
    private Rectangle rectSnakeHead;

    public SnakeGamePanel() {
        start();
    }

    public void start() {
        random = new Random();
        newApple();
        timer = new Timer(delay, this);
        timer.start();

        allButtons();
        scoreBoard = new JLabel();
        scoreBoard.setBounds(PANEL_X, PANEL_Y + PANEL_HEIGHT, 150, 30);

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') {
                            direction = 'D';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        if (running) {
                            if (!timer.isRunning()) {
                                timer.start();
                            } else {
                                timer.stop();
                            }

                        }
                        break;
                }
            }
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
                appleBody(g2d);
                snakeBody(g2d);
                createBox(g2d);
//                for (int i = 0; i < (PANEL_HEIGHT / UNIT_SIZE); i++) {
//                    g2d.setColor(Color.BLACK);
//                    g2d.drawLine(
//                            PANEL_X,
//                            i * UNIT_SIZE + PANEL_Y,
//                            PANEL_WIDTH + PANEL_X,
//                            i * UNIT_SIZE + PANEL_Y);
//                }
//                for (int i = 0; i < (PANEL_WIDTH / UNIT_SIZE); i++) {
//                    g2d.setColor(Color.BLACK);
//                    g2d.drawLine(
//                            PANEL_X + i * UNIT_SIZE,
//                            PANEL_Y,
//                            PANEL_X + i * UNIT_SIZE,
//                            PANEL_HEIGHT + PANEL_Y);
//                }
            } else {
                gameOver(g2d);
            }
        }
    }

    public void createBox(Graphics2D g2d) {
        switch (level) {
            // For Level One
            case 1:
                for (int i = 0; i < PANEL_HEIGHT / UNIT_SIZE; i++) {
                    for (int j = 0; j < PANEL_WIDTH / UNIT_SIZE; j++) {
                        if ((i == 12 || i == 13)
                                || (j == 19 || j == 20)) {
                            creatingBrick(g2d, j, i);
                        }
                    }
                }
                if (applesEaten == 20) {
                    level++;
                    bodyParts = 6;
                    X = new int[GAME_UNITS];
                    Y = new int[GAME_UNITS];
                    X[bodyParts] = 60;
                    Y[bodyParts] = 60;
                    X[0] = 60;
                    Y[0] = 60;
                    direction = 'R';
                }
                break;
            // For level Two
            case 2:
                for (int i = 0; i < PANEL_HEIGHT / UNIT_SIZE; i++) {
                    for (int j = 0; j < PANEL_WIDTH / UNIT_SIZE; j++) {
                        if (((j >= 15 && j <= 35) && (i >= 5 && i <= 10))
                                || ((j >= 3 && j <= 10) && (i >= 5 && i <= 15))) {
                            creatingBrick(g2d, j, i);
                        }
                    }
                }
                if (applesEaten == 40) {
                    level++;
                    bodyParts = 6;
                    X = new int[GAME_UNITS];
                    Y = new int[GAME_UNITS];
                    X[bodyParts] = 60;
                    Y[bodyParts] = 60;
                    X[0] = 60;
                    Y[0] = 60;
                    direction = 'R';
                }
                break;
            // For Level Three
            case 3:
                for (int i = 0; i < PANEL_HEIGHT / UNIT_SIZE; i++) {
                    for (int j = 0; j < PANEL_WIDTH / UNIT_SIZE; j++) {
                        if ((i == 0 || i == PANEL_HEIGHT / UNIT_SIZE - 1) || (j == 0 || j == PANEL_WIDTH / UNIT_SIZE - 1)
                                || (j >= 7 && j <= 21) && (i >= 10 && i <= 14)
                                || (j >= 12 && j <= 16) && (i >= 5 && i <= 19)) {
                            creatingBrick(g2d, j, i);
                        }
                    }
                }
                if (applesEaten == 60) {
                    running = false;
                }
                break;
            default:
                running = false;
                break;
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (PANEL_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (PANEL_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    private void appleBody(Graphics2D g2d) {
        g2d.setColor(new Color(
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255)));
        g2d.fillRoundRect(PANEL_X + appleX, PANEL_Y + appleY, UNIT_SIZE, UNIT_SIZE, 100, 100);
    }

    public void snakeBody(Graphics2D g2d) {
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g2d.setColor(new Color(153, 255, 102));
                g2d.fillRoundRect(PANEL_X + X[i], PANEL_Y + Y[i], UNIT_SIZE, UNIT_SIZE, 10, 10);
            } else {
                g2d.setColor(new Color(230, 138, 0));
                g2d.fillRoundRect(PANEL_X + X[i], PANEL_Y + Y[i], UNIT_SIZE, UNIT_SIZE, 10, 10);
            }
        }
    }

    public void gameOver(Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g2d.getFont());
        g2d.drawString("Game Over",
                (PANEL_WIDTH - metrics1.stringWidth("Game Over")) / 2,
                PANEL_HEIGHT / 2);
    }

    public void snakeMove() {
        for (int i = bodyParts; i > 0; i--) {
            X[i] = X[i - 1];
            Y[i] = Y[i - 1];
        }
        switch (direction) {
            case 'U':
                Y[0] -= UNIT_SIZE;
                break;
            case 'D':
                Y[0] += UNIT_SIZE;
                break;
            case 'R':
                X[0] += UNIT_SIZE;
                break;
            case 'L':
                X[0] -= UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((X[0] == appleX) && (Y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        rectApple = new Rectangle(
                PANEL_X + appleX, PANEL_Y + appleY,
                UNIT_SIZE, UNIT_SIZE);
        rectSnakeHead = new Rectangle(
                PANEL_X + X[0], PANEL_Y + Y[0],
                UNIT_SIZE, UNIT_SIZE);
        switch (level) {
            case 1: // For level One
                for (int i = 0; i < PANEL_HEIGHT / UNIT_SIZE; i++) {
                    for (int j = 0; j < PANEL_WIDTH / UNIT_SIZE; j++) {
                        if ((i == 12 || i == 13)
                                || (j == 19 || j == 20)) {
                            checkingCollisionsBetweenSnakeHeadAndBrick(g2d, j, i);
                        }
                    }
                }
                break;
            case 2:// For Level Two
                for (int i = 0; i < PANEL_HEIGHT / UNIT_SIZE; i++) {
                    for (int j = 0; j < PANEL_WIDTH / UNIT_SIZE; j++) {
                        if (((j >= 15 && j <= 35) && (i >= 5 && i <= 10))
                                || ((j >= 3 && j <= 10) && (i >= 5 && i <= 15))) {
                            checkingCollisionsBetweenSnakeHeadAndBrick(g2d, j, i);
                        }
                    }
                }
                break;
            case 3:// For Level Three
                for (int i = 0; i < PANEL_HEIGHT / UNIT_SIZE; i++) {
                    for (int j = 0; j < PANEL_WIDTH / UNIT_SIZE; j++) {
                        if ((i == 0 || i == PANEL_HEIGHT / UNIT_SIZE - 1) || (j == 0 || j == PANEL_WIDTH / UNIT_SIZE - 1)
                                || (j >= 7 && j <= 21) && (i >= 10 && i <= 14)
                                || (j >= 12 && j <= 16) && (i >= 5 && i <= 19)) {
                            checkingCollisionsBetweenSnakeHeadAndBrick(g2d, j, i);
                        }
                    }
                }
                break;
            default:
                break;
        }
        for (int i = bodyParts; i > 0; i--) {
            if ((X[0] == X[i]) && (Y[0] == Y[i])) {
                running = false;
            }
            if (X[0] < 0) {
                X[0] = PANEL_WIDTH - UNIT_SIZE;
            }
            if ((X[0] >= PANEL_WIDTH)) {
                X[0] = 0;
            }
            if (Y[0] < 0) {
                Y[0] = PANEL_HEIGHT - UNIT_SIZE;
            }
            if (Y[0] >= PANEL_HEIGHT) {
                Y[0] = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (start) {
            if (running) {
                snakeMove();
                checkApple();
                checkCollisions();
                scoreBoard.setText("Score : " + applesEaten);

                remove(backBtn);
                add(scoreBoard);
            } else {
                add(backBtn);
            }
            remove(startBtn);
        } else {
            add(startBtn);
            remove(backBtn);
            remove(scoreBoard);
        }
        repaint();
    }

    public void creatingBrick(Graphics2D g2d, int x, int y) {
        boxX = x * UNIT_SIZE;
        boxY = y * UNIT_SIZE;
        g2d.setColor(Color.red);
        g2d.fillRect(PANEL_X + boxX, PANEL_Y + boxY,
                BOX_WIDTH, BOX_HEIGHT);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(PANEL_X + boxX, PANEL_Y + boxY,
                BOX_WIDTH, BOX_HEIGHT);
    }

    public void checkingCollisionsBetweenSnakeHeadAndBrick(Graphics2D g2d, int x, int y) {
        boxX = x * UNIT_SIZE;
        boxY = y * UNIT_SIZE;
        Rectangle rectBox = new Rectangle(
                PANEL_X + boxX, PANEL_Y + boxY,
                BOX_WIDTH, BOX_HEIGHT);
        if (rectApple.intersects(rectBox)) {
            newApple();
        }
        if (rectSnakeHead.intersects(rectBox)) {
            running = false;
        }
    }

    public void allButtons() {
        startBtn = new JButton();
        startBtn.setBounds(PANEL_X + (PANEL_WIDTH - 150) / 2, PANEL_Y + 80, 150, 30);
        startBtn.setText("Start");

        startBtn.addActionListener((ActionEvent e) -> {
            start = true;
            running = true;
            applesEaten = 0;
            bodyParts = 6;
            X = new int[GAME_UNITS];
            Y = new int[GAME_UNITS];
            X[bodyParts] = 7 * UNIT_SIZE;
            Y[bodyParts] = 7 * UNIT_SIZE;
            X[0] = 7 * UNIT_SIZE;
            Y[0] = 7 * UNIT_SIZE;
            direction = 'R';
            level = 1;
        });

        backBtn = new JButton();
        backBtn.setBounds(PANEL_X + (PANEL_WIDTH - 150) / 2, PANEL_Y + PANEL_HEIGHT + 5, 150, 30);
        backBtn.setText("Main Menu");

        backBtn.addActionListener((ActionEvent e) -> {
            start = false;
        });
    }

    public void setTimerStop() {
        timer.stop();
    }
}
