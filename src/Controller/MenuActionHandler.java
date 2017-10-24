package Controller;

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
        if(e.getActionCommand().equals("Save replay")) {
            String s = JOptionPane.showInputDialog("Enter a name for the replay: ");
            if ((s != null) && (s.length() > 0)) {
                try {
                    ReplayHandler.saveReplay(s);
                } catch (IOException e1) {
                    System.out.println("Invalid name.");
                }
            }
        } else if(e.getActionCommand().equals("Load replay")) {
            Object[] possibilities = ReplayHandler.getAllReplayNames().toArray();
            String s = (String)JOptionPane.showInputDialog(
                    GameWindow.getFrame(),
                    "Choose a replay to load:",
                    "Load a replay",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    null);
            if ((s != null) && (s.length() > 0)) {
                MoveController.replayGame(s);
            }

        } else if(e.getActionCommand().equals("Rules")) {
            try {
                java.awt.Desktop.getDesktop().browse(new URI("https://www.itsyourturn.com/t_helptopic2030.html"));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }
}
