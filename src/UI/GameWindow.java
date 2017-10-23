package UI;

import Model.Board;

import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JPanel {
    //window dimensions
    public static final int WIDTH = Board.TILE_WIDTH * Board.SIZE + 1;
    public static final int HEIGHT = Board.TILE_HEIGHT * Board.SIZE;

    //panels
    private InformationPanel informationPanel;
    private GamePanel gamePanel;

    public GameWindow() {
        Dimension size = getPreferredSize();
        size.width = WIDTH;
        size.height = HEIGHT;
        setPreferredSize(size); //set window width and height
        setFocusable(true); // this should help with mouse events?

        informationPanel = new InformationPanel();
        gamePanel = new GamePanel();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }



        makeGui();

    }

    public void update() {
        gamePanel.update();
        informationPanel.update();
    }

    public void paintComponent(Graphics2D g) {
        gamePanel.paintComponent(g);
    }

    private void makeGui() {
        JFrame frame = new JFrame("Checkers"); //create frame

        //set properties of the frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //layout
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        frame.setLayout(gridBag);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(3,3,3,3);
        gridBag.setConstraints(gamePanel, c);
        frame.add(gamePanel);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        //c.anchor = GridBagConstraints.NORTH;
        gridBag.setConstraints(informationPanel, c);
        frame.add(informationPanel);

        frame.pack();

        frame.setLocationRelativeTo(null); // this should display the window in the middle of the screen ?
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.WHITE); //this doesn't seem to change the background colour?
    }

}
