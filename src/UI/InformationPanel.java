package UI;

import Controller.BoardController;
import Controller.GameHistory;
import Controller.MoveController;
import Controller.TurnManager;
import Model.Board;
import Model.GridPosition;
import Model.Move;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class InformationPanel extends JPanel {
    private JLabel currentPlayer;
    private JLabel info, gameType, blackCount, whiteCount;
    private static JLabel error;
    private static JTextPane textPane;
    private JScrollPane scroll;
    private JButton undoBtn, redoBtn;

    public static final int WIDTH = 200;
    public static final int HEIGHT = Board.SIZE * Board.TILE_HEIGHT;


    public InformationPanel() {
        Font oldLabelFont = UIManager.getFont("Label.font");
        UIManager.put("Label.font", oldLabelFont.deriveFont(Font.PLAIN, 15));

        Dimension size = getPreferredSize();
        size.width = WIDTH;
        size.height = HEIGHT;
        setPreferredSize(size);

        setBorder(BorderFactory.createTitledBorder("Game Stats:"));
        setBackground(Color.WHITE);

        currentPlayer = new JLabel("Player 1");
        //currentPlayer.setFont(currentPlayer.getFont().deriveFont(13.0f));
        info = new JLabel("Moves:");
        error = new JLabel("");
        //error.setFont(currentPlayer.getFont().deriveFont(14.0f));
        error.setMinimumSize(new Dimension(0, 0));
        //error.setMaximumSize(new Dimension(20, 1));
        error.setPreferredSize(new Dimension(120, 40));
        gameType = new JLabel("Game type: ");

        blackCount = new JLabel("Black checkers: ");
        whiteCount = new JLabel("<html><font color='gray'>White checkers: </font></html>");

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setText("");
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        scroll = new JScrollPane (textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(170, 200));

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
        c.weighty =1;
        c.insets = new Insets(5, 5, 5, 5);
        add(currentPlayer, c);

        c.gridy = 2;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(blackCount, c);

        c.anchor = GridBagConstraints.NORTH;
        add(whiteCount, c);

        c.gridy = 3;
        c.weighty = 1;
        c.anchor = GridBagConstraints.SOUTH;
        add(info, c);

        c.gridy = 4;
        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 1;
        add(scroll, c);

        c.gridy = 5;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weighty = 0.5;
        add(undoBtn, c);

        c.anchor = GridBagConstraints.NORTHEAST;
        add(redoBtn, c);

        c.gridy = 6;
        c.weighty = 1;
        //c.weightx = 1;
        c.anchor = GridBagConstraints.NORTH;
        add(error, c);
    }

    public void update() {
        currentPlayer.setText("<html>It's player <font color='gray'>" + TurnManager.getCurrentPlayer() + "'s </font> turn!</html>");
        gameType.setText("Game type: " + BoardController.getGameType());

        updateCount();

        if(!GameHistory.getCopy().isEmpty()) {
            updateMoveHistory();
        }
    }

    private void updateCount() {
        int[] count = BoardController.getPieceCount();
        blackCount.setText("Black checkers: " + count[0]);
        whiteCount.setText("<html><font color='gray'>White checkers: " + count[1] + "</font></html>");
    }

    private void updateMoveHistory() {
        Move move = GameHistory.getCopy().removeFirst();

        textPane.setText(textPane.getText() + "\n" + move.toString());

//        JScrollBar vertical = scroll.getVerticalScrollBar();
//        vertical.setValue( vertical.getMaximum() );
        textPane.setCaretPosition(textPane.getDocument().getLength());
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

    public static void setErrorText(String text) {
        error.setText("<html><font color='#536356'>" + text + "</font></html>");
    }

    public static void clearMoves() {
        textPane.setText("");
    }
}
