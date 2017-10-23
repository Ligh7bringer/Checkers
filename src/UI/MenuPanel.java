package UI;

import Model.Board;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private JButton playBtn;
    private CardLayout cardLayout;
    private GameWindow gameWindow;
    private JPanel mainPanel;

    public MenuPanel(){
        Dimension size = getPreferredSize();
        size.width = GameWindow.WIDTH + InformationPanel.WIDTH;
        size.height = GameWindow.HEIGHT;
        setPreferredSize(size);

        gameWindow = new GameWindow();
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        playBtn = new JButton("Play");

        mainPanel.setLayout(cardLayout);
        mainPanel.add(gameWindow, "gameWindow");
        mainPanel.add(this, "menuPanel");
        cardLayout.show(mainPanel, "menuPanel");

        JFrame frame = new JFrame("Checkers"); //create frame

        //set properties of the frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        makeUi();
        frame.add(mainPanel);

        frame.pack();
        frame.setLocationRelativeTo(null); // this should display the window in the middle of the screen ?
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.WHITE); //this doesn't seem to change the background colour?
    }

    private void makeUi() {
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridBag);

        playBtn.addActionListener(e -> {
            if(playBtn.isEnabled()) {
                cardLayout.show(mainPanel, "gameWindow");
            }
        });
        c.gridy = 0;
        c.gridx = 0;
        add(playBtn, c);
    }

    public void update() {
        gameWindow.update();
    }

    public void paintComponent(Graphics2D g2d) {
        gameWindow.paintComponent(g2d);
    }
}
