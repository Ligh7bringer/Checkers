package Controller;

import Model.GridPosition;
import Model.Move;
import Model.Piece;
import Model.Type;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class ReplayHandler {
    private static FileWriter fw;

    //saves the information in GameHistory about the current game in a .txt file
    public static void saveReplay(String name) {
        File file = new File("replays/" + name + ".txt");
        if(file.exists()) {
            try {
                fw = new FileWriter(file,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if(file.createNewFile())
                    fw = new FileWriter(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!GameHistory.getMoves().isEmpty()) {
            Piece source, dest, removedPiece;
            String text;
            for (Move move : GameHistory.getMoves()) {
                source = move.getSource();
                dest = move.getDestination();
                removedPiece = move.getRemoved();

                //System.out.println(source.toString() + dest.toString());

               if (removedPiece != null) {
                   text = source.toString() + ";" + dest.toString() + ";" + removedPiece.toString();
               } else {
                   text = source.toString() + ";" + dest.toString();
               }

                try {
                    fw.write(text);
                    fw.write(System.getProperty("line.separator"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        try {
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //reads a replay file and stores the moves in a linked list
    static LinkedList<Move> parseReplay(String name) {
        LinkedList<Move> replay = new LinkedList<>();

        File file;
        FileReader fileReader;
        BufferedReader bufferedReader;
        try {
            file = new File("replays/" + name);
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            Move move;
            String line;
            String[] positions;
            String type;
            String row;
            String col;
            Piece source, dest, removed;

            while ((line = bufferedReader.readLine()) != null) {
                positions = line.split(";");
                type = positions[0].split(", ")[0];
                row = positions[0].split(", ")[1];
                col = positions[0].split(", ")[2];
                source = new Piece(parseType(type), new GridPosition(Integer.parseInt(row), Integer.parseInt(col)));

                type = positions[1].split(", ")[0];
                row = positions[1].split(", ")[1];
                col = positions[1].split(", ")[2];
                dest = new Piece(parseType(type), new GridPosition(Integer.parseInt(row), Integer.parseInt(col)));

                removed = null;
                if(positions.length >= 3) {
                    type = positions[2].split(", ")[0];
                    row = positions[2].split(", ")[1];
                    col = positions[2].split(", ")[2];
                    removed = new Piece(parseType(type), new GridPosition(Integer.parseInt(row), Integer.parseInt(col)));
                }

                move = new Move(source, dest, removed);

                replay.add(move);
            }

            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("This should never happen!");
            e.printStackTrace();
        }

        return replay;
    }

    //return the names of all files in the replay/ directory
    public static ArrayList<String> getAllReplayNames() {
        ArrayList<String> names = new ArrayList<>();
        File folder = new File("replays/");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File f : listOfFiles) {
                if (f.isFile()) {
                    names.add(f.getName());
                }
            }
        }

        return names;
    }

    //converts a string to a proper Type.
    private static Type parseType(String s) {
        if(s.equals("WHITE"))
            return Type.WHITE;
        if(s.equals("BLACK"))
            return Type.BLACK;
        if(s.equals("WHITE_KING"))
            return Type.WHITE_KING;
        if(s.equals("BLACK_KING"))
            return Type.BLACK_KING;

        return null;
    }

}
