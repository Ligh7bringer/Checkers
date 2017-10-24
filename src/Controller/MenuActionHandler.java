package Controller;

import Model.GameType;
import UI.GameWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MenuActionHandler implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("New game vs. another player")) {
            int i = JOptionPane.showConfirmDialog(null, "Do you want to start a new game\n(any current game will be lost)?", "Message", JOptionPane.YES_NO_OPTION);
            if(i == 0)
                BoardController.setupGame(GameType.TWO_PLAYERS);
        } else if(e.getActionCommand().equals("Save replay")) {
            String s = JOptionPane.showInputDialog("Enter a name for the replay: ");
            if ((s != null) && (s.length() > 0)) {
                ReplayHandler.saveReplay(s);
            }
        } else if(e.getActionCommand().equals("Load replay")) {
            String s = showLoadDialog();
            if ((s != null) && (s.length() > 0)) {
                BoardController.setupGame(GameType.TWO_PLAYERS);
                MoveController.replayGame(s);
            }
        } else if(e.getActionCommand().equals("Rules")) {
            try {
                java.awt.Desktop.getDesktop().browse(new URI("http://europedraughts.org/edc/general-rules-of-draughts/general-rules/"));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        } else if(e.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
    }

    private String showLoadDialog() {
        Object[] possibilities = ReplayHandler.getAllReplayNames().toArray();
        return (String)JOptionPane.showInputDialog(
                GameWindow.getFrame(),
                "Choose a replay to load:",
                "Load a replay",
                JOptionPane.QUESTION_MESSAGE,
                null,
                possibilities,
                null);
    }
}
