package catchtheballgame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

class GamePanel extends JPanel implements ActionListener, MouseMotionListener
{
    private int ballX = 100;
    private int ballY = 0;
    private int ballDiameter = 30;
    private int ballSpeed = 9;
    
    private int paddleX = 250;
    private final int paddleY = 330;
    private final int paddleWidth = 100;
    private final int paddleHeight = 20;
    private final int paddleSpeed = 35;
    
    private JButton startButton;
    private JButton restartButton;

    private Color ballColor = Color.BLUE;
    
    private final Random rand = new Random();
    private int score = 0;
    private int missCount = 0;
    
    private boolean gameOver = false;
    private boolean gameStarted = false;
    
    private Timer timer;
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (!gameStarted) return;
        g.setColor(Color.BLUE);
        g.fillOval(ballX, ballY, ballDiameter, ballDiameter);
        g.setColor(Color.WHITE);
        g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);
        
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", 1, 18));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Misses: " + missCount + "/3", 10, 45);
        
        if (gameOver)
        {
            g.setFont(new Font("Arial", 1, 36));
            g.setColor(Color.RED);
            g.drawString("Game Over!", getWidth() / 2 - 100, getHeight() / 2);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(!gameOver)
        {
            ballY += ballSpeed;

            if (ballY + ballDiameter >= paddleY && ballY + ballDiameter <= paddleY + paddleHeight)
            {
                if (ballX + ballDiameter >= paddleX && ballX <= paddleX + paddleWidth)
                {
                    score++;
                    resetBall();
                }
            }
            if (ballY > getHeight())
            {
                missCount++;
                if (missCount >= 3)
                {
                    gameOver = true;
                    timer.stop();
                } else
                {
                    resetBall();
                }
            }
            if (missCount >= 3)
            {
                gameOver = true;
                timer.stop();
                restartButton.setVisible(true);
            }
            repaint();
        }
    }
    private void resetBall()
    {
        ballY = 0;
        ballX = rand.nextInt(getWidth() - ballDiameter);
    }
    private void restartGame()
    {
        score = 0;
        missCount = 0;
        gameOver = false;
        restartButton.setVisible(false);
        resetBall();
        timer.start();
        repaint();
    }   
    private void startGame()
    {
        startButton.setVisible(false);
        requestFocusInWindow();
        gameStarted = true;
        gameOver = false;
        score = 0;
        missCount = 0;
        ballSpeed = 9;
        resetBall();
        timer = new Timer(15, this);
        timer.start();
        repaint();
    }
    @Override
    public void mouseMoved(MouseEvent e)
    {
        int mouseX = e.getX();
        paddleX = mouseX - paddleWidth / 2;
        if (paddleX < 0) paddleX = 0;
        if (paddleX + paddleWidth > getWidth()) paddleX = getWidth() - paddleWidth;

        repaint();
    }
    
    @Override
    public void mouseDragged(MouseEvent e){}
    
    public GamePanel()
    {
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseMotionListener(this);
        setLayout(null);
        
        startButton = new JButton("START GAME...");
        startButton.setBounds(150, 150, 300, 40);
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setForeground(new Color(0,255,200));
        startButton.setFont(new Font("Courier New", Font.BOLD, 30));
        startButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        startButton.addActionListener(e -> startGame());
        add(startButton);
        
        restartButton = new JButton("RESTART");
        restartButton.setBounds(230, 200, 140, 40);
        restartButton.setFont(new Font("Verdana", Font.BOLD, 20));
        restartButton.setForeground(Color.YELLOW);
        restartButton.setOpaque(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setBorderPainted(false);
        restartButton.setFocusPainted(false);
        restartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        restartButton.setVisible(false); // initially hidden
        restartButton.addActionListener(e -> restartGame());
        add(restartButton);

    }
}
public class MainClass
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Catch the Ball");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setVisible(true);
    } 
}
