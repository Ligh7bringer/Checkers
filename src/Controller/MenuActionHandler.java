package Controller;

import Model.GameType;
import Model.Type;
import UI.GameWindow;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//this class handles the events for the Menu bar
public class MenuActionHandler implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("New game vs. AI")) {
            int i = showConfirmDialog();
            if(i == 0)
                BoardController.setupGame(GameType.VS_AI);
        } else if(e.getActionCommand().equals("New game vs. another player")) {
            int i = showConfirmDialog();
            if(i == 0)
                BoardController.setupGame(GameType.TWO_PLAYERS);
        }  else if(e.getActionCommand().equals("New game AI vs. AI")) {
            int i = showConfirmDialog();
            if (i == 0)
                BoardController.setupGame(GameType.AI_VS_AI);
        }else if(e.getActionCommand().equals("Save replay")) {
            if(BoardController.getGameType() != null) {
                String s = JOptionPane.showInputDialog("Enter a name for the replay: ");
                if ((s != null) && (s.length() > 0)) {
                    ReplayHandler.saveReplay(s);
                }
            } else {
                JOptionPane.showMessageDialog(GameWindow.getFrame(), "Nothing to save! Play a game first!");
            }
        } else if(e.getActionCommand().equals("Load replay")) {
            String s = showLoadDialog();
            if ((s != null) && (s.length() > 0)) {
                BoardController.setupGame(GameType.REPLAY);
                BoardController.replayGame(s);
            }
        } else if(e.getActionCommand().equals("Rules")) {
            try {
                java.awt.Desktop.getDesktop().browse(new URI("http://europedraughts.org/edc/general-rules-of-draughts/general-rules/"));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        } else if(e.getActionCommand().equals("Exit")) {
            System.exit(0);
        } else if(e.getActionCommand().equals("Black/White")) {
            BoardController.setColours(1);
        } else if(e.getActionCommand().equals("Black/Red")) {
            BoardController.setColours(2);
        } else if(e.getActionCommand().equals("Brown/White")) {
            BoardController.setColours(3);
        } else if(e.getActionCommand().equals("Show last move")) {
            BoardController.setShowLastMove();
        }
    }

    //displays the dialog for loading a replay
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

    //displays a confirmation dialog
    private int showConfirmDialog() {
        return JOptionPane.showConfirmDialog(null, "Do you want to start a new game\n(any current game will be lost)?", "Message", JOptionPane.YES_NO_OPTION);
    }
}
