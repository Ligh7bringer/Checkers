package UI;

import Controller.BoardController;
import Controller.GameHistory;
import Controller.MoveController;
import Model.Board;
import Model.GridPosition;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class InformationPanel extends JPanel {
    private JLabel currentPlayer;
    private JLabel info, gameType;
    private static JTextPane textPane;
    private JScrollPane scroll;
    private JButton undoBtn, redoBtn;

    public static final int WIDTH = 200;
    public static final int HEIGHT = Board.SIZE * Board.TILE_HEIGHT;


    public InformationPanel() {
        Dimension size = getPreferredSize();
        size.width = WIDTH;
        size.height = HEIGHT;
        setPreferredSize(size);

        setBorder(BorderFactory.createTitledBorder("Game Stats:"));
        setBackground(Color.WHITE);

        currentPlayer = new JLabel("Player 1");
        info = new JLabel("Moves:");
        gameType = new JLabel("Game type: ");
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setText("");

        scroll = new JScrollPane (textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(150, 200));

        undoBtn = new JButton("Undo");
        undoBtn.addActionListener(e -> {
            if (undoBtn.isEnabled()) {
                if(MoveController.undoLastMove())
                    removeLastLine();
            }
        });

        redoBtn = new JButton("Redo");
        redoBtn.addActionListener(e -> {
            if(undoBtn.isEnabled()) {
                MoveController.redoLastMove();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        c.anchor = GridBagConstraints.NORTH;
        add(gameType, c);

        c.gridy = 1;
        c.anchor = GridBagConstraints.SOUTH;
        add(currentPlayer, c);

        c.gridy = 2;
        c.weighty = 1;
        c.anchor = GridBagConstraints.SOUTH;
        add(info, c);

        c.gridy = 3;
        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 1;
        add(scroll, c);

        c.gridy = 4;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weighty = 1;
        add(undoBtn, c);

        c.anchor = GridBagConstraints.NORTHEAST;
        add(redoBtn, c);
    }

    public void update() {
        currentPlayer.setText("It's player " + Board.getCurrentPlayer() + "'s turn.");
        gameType.setText("Game type: " + BoardController.getGameType());

        if(!GameHistory.getCopy().isEmpty()) {
            updateMoveHistory();
        }
    }

    private void updateMoveHistory() {
        String text = "";
        GridPosition[] gps;
        gps = GameHistory.getCopy().removeFirst();

        if(Board.getCurrentPlayer() == 2)
            text += "\nBlack: ";
        else
            text += "\nWhite: ";

        text+= gps[0].toString() + " -> ";
        text += gps[1].toString();

        textPane.setText(textPane.getText() + text);
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }

    private void removeLastLine() {
        String content = null;
        try {
            content = textPane.getDocument().getText(0, textPane.getDocument().getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        int lastLineBreak = 0;
        if (content != null) {
            lastLineBreak = content.lastIndexOf('\n');
        }
        try {
            textPane.getDocument().remove(lastLineBreak, textPane.getDocument().getLength() - lastLineBreak);
        } catch (Exception e) {
            System.out.println("nothing to remove");
        }
    }

    public static void clearMoves() {
        textPane.setText("");
    }
}
