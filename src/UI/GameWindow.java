package UI;

import Controller.MenuActionHandler;
import Model.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JPanel {
    //window dimensions
    private static final int WIDTH = Board.TILE_WIDTH * Board.SIZE + 1;
    private static final int HEIGHT = Board.TILE_HEIGHT * Board.SIZE;

    private static JFrame frame;

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
        JMenuBar menuBar = createMenuBar();
        menuBar.setPreferredSize(new Dimension(20, 40));
        frame = new JFrame("Checkers"); //create frame

        //set properties of the frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        try {
            frame.setIconImage(ImageIO.read(new File("res/blackking.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //layout
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        frame.setLayout(gridBag);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        gridBag.setConstraints(menuBar, c);
        frame.add(menuBar);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(3,3,3,3);
        gridBag.setConstraints(gamePanel, c);
        frame.add(gamePanel);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        //c.anchor = GridBagConstraints.NORTH;
        gridBag.setConstraints(informationPanel, c);
        frame.add(informationPanel);

        frame.pack();

        frame.setLocationRelativeTo(null); // this should display the window in the middle of the screen ?
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.WHITE); //this doesn't seem to change the background colour?
    }


    private static JMenuBar createMenuBar() {
        MenuActionHandler actionHandler = new MenuActionHandler();
        Font f = new Font("Arial", Font.PLAIN, 14);
        UIManager.put("Menu.font", f);
        UIManager.put("MenuItem.font", f);

        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the Game Menu.
        menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        menu.getAccessibleContext().setAccessibleDescription("Start a new game.");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("New game vs. AI");
        menuItem.addActionListener(actionHandler);
        menu.add(menuItem);

        menuItem = new JMenuItem("New game vs. another player");
        menuItem.addActionListener(actionHandler);
        menu.add(menuItem);

        menuItem = new JMenuItem("New game AI vs. AI");
        menuItem.addActionListener(actionHandler);
        menu.add(menuItem);

        menu.addSeparator();
        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(actionHandler);
        menu.add(menuItem);

        //Build Replays menu in the menu bar.
        menu = new JMenu("Replays");
        menu.setMnemonic(KeyEvent.VK_R);
        menu.getAccessibleContext().setAccessibleDescription("Replays Menu");

        menuItem = new JMenuItem("Save replay");
        menuItem.addActionListener(actionHandler);
        menu.add(menuItem);

        menuItem = new JMenuItem("Load replay");
        menuItem.addActionListener(actionHandler);
        menu.add(menuItem);

        menuBar.add(menu);

        //help menu
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menu.getAccessibleContext().setAccessibleDescription(
                "Help Menu");

        menuItem = new JMenuItem("Rules");
        menuItem.addActionListener(actionHandler);
        menu.add(menuItem);

        menuBar.add(menu);
        return menuBar;
    }

    public static JFrame getFrame() {
        return frame;
    }

}
