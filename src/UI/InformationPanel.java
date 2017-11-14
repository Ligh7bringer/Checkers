package UI;

import Controller.BoardController;
import Controller.GameHistory;
import Controller.MoveController;
import Controller.TurnManager;
import Model.Board;
import Model.GameType;
import Model.Move;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//this GUI class displays information about the current game
public class InformationPanel extends JPanel implements ActionListener {
    //GUI components
    private JLabel currentPlayer;
    private JLabel info, gameType, blackCount, whiteCount;
    private static JLabel error;
    private static JTextPane textPane;
    private JScrollPane scroll;
    private JButton undoBtn, redoBtn;

    //panel size
    private static final int WIDTH = 200;
    private static final int HEIGHT = Board.SIZE * Board.TILE_HEIGHT;

    //the constructor initialises the panel
    public InformationPanel() {
        Font oldLabelFont = UIManager.getFont("Label.font");
        UIManager.put("Label.font", oldLabelFont.deriveFont(Font.PLAIN, 15));

        Dimension size = getPreferredSize();
        size.width = WIDTH;
        size.height = HEIGHT;
        setPreferredSize(size);

        setBorder(BorderFactory.createTitledBorder("Game Stats:"));
        setBackground(Color.WHITE);

        makeGUI();
    }

    //initialises all JComponents
    private void makeGUI() {
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
        undoBtn.setToolTipText("Undo the last move.");
        undoBtn.addActionListener(this);

        redoBtn = new JButton("Redo");
        redoBtn.setToolTipText("Redo the last undone move.");
        redoBtn.addActionListener(this);

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

    //update method which updates the text of the JLabels and the move history text pane
    public void update() {
        currentPlayer.setText("<html>It's player <font color='gray'>" + TurnManager.getCurrentPlayer() + "'s </font> turn!</html>");

        if(BoardController.getGameType() == null)
            gameType.setText("Hello!");
        else
            gameType.setText("Game type: " + BoardController.getGameType());

        updateCount();

        if(!GameHistory.getCopy().isEmpty()) {
            updateMoveHistory();
        }
    }

    //updates the piece count labels
    private void updateCount() {
        int[] count = BoardController.getPieceCount();
        blackCount.setText("Black checkers: " + count[0]);
        whiteCount.setText("<html><font color='gray'>White checkers: " + count[1] + "</font></html>");
    }

    //updates the move history text pane with the last move
    private void updateMoveHistory() {
        Move move = GameHistory.getCopy().removeFirst(); //get move

        textPane.setText(textPane.getText() + "\n" + move.toString()); //set the text

        textPane.setCaretPosition(textPane.getDocument().getLength()); //scroll to the very bottom
    }

    //removes the last line of move history text pane. used when the undo button is clicked
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

    //sets the text of the error message label
    public static void setErrorText(String text) {
        error.setText("<html><font color='#536356'>" + text + "</font></html>");
    }

    //clears all text from the move history text pane
    public static void clearMoves() {
        textPane.setText("");
    }

    //action listener for button presses
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == undoBtn && BoardController.getGameType() != GameType.AI_VS_AI) {
            if(MoveController.undoLastMove())
                removeLastLine();
        }
        if(e.getSource() == redoBtn && BoardController.getGameType() != GameType.AI_VS_AI) {
            MoveController.redoLastMove();
        }
    }
}
