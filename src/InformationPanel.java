import javax.swing.*;
import java.awt.*;

public class InformationPanel extends JPanel {
    private JLabel currentPlayer;
    private JLabel info;
    private JTextArea textArea;
    private JScrollPane scroll;

    public InformationPanel() {
        Dimension size = getPreferredSize();
        size.width = 200;
        size.height = Board.SIZE * Board.TILE_HEIGHT;
        setPreferredSize(size);

        setBorder(BorderFactory.createTitledBorder("Game Stats:"));
        setBackground(Color.WHITE);

        currentPlayer = new JLabel("Player 1");
        info = new JLabel("Moves:");
        textArea = new JTextArea();
        textArea.setEditable(false);
        //textArea.setSize(20, 20);
        //textArea.setMaximumSize(new Dimension(20, 20));
        textArea.setText("");

        scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //scroll.setMaximumSize(new Dimension(50, 10));
        scroll.setPreferredSize(new Dimension(150, 100));

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        c.anchor = GridBagConstraints.NORTH;
        add(currentPlayer, c);

        c.gridy = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.SOUTH;
        add(info, c);

        c.gridy = 2;
        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 1;
        add(scroll, c);

    }

    public void update() {
        currentPlayer.setText("It's player " + Board.getCurrentPlayer() + "'s turn.");
        if(!GameHistory.getMoves().isEmpty()) {
            updateMoveHistory();
        }
        validate();
    }

    private void updateMoveHistory() {
        String text = "";
        GridPosition[] gps;
        gps = GameHistory.getMoves().removeFirst();

        text +=gps[0].toString() + " -> ";
        text += gps[1].toString() + "\n";

        textArea.append(text);
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }
}
