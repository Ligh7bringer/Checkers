package Controller;

import Model.GridPosition;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class ReplayHandler {
    private static FileWriter fw;

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
            for (GridPosition[] gps : GameHistory.getMoves()) {
                GridPosition source = gps[0];
                GridPosition dest = gps[1];
                GridPosition removedPiece = gps[2];

                String text;
                if (removedPiece != null)
                    text = source.toString() + ";" + dest.toString() + ";" + removedPiece.toString();
                else
                    text = source.toString() + ";" + dest.toString();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static LinkedList<GridPosition[]> parseReplay(String name) {
        LinkedList<GridPosition[]> replay = new LinkedList<>();

        File file;
        FileReader fileReader;
        BufferedReader bufferedReader;
        try {
            file = new File("replays/" + name);
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            String[] positions;
            String row;
            String col;
            GridPosition[] gps;

            while ((line = bufferedReader.readLine()) != null) {
                gps =  new GridPosition[3];
                positions = line.split(";");
                row = positions[0].split(", ")[0];
                col = positions[0].split(", ")[1];
                gps[0] = new GridPosition(Integer.parseInt(row), Integer.parseInt(col));

                row = positions[1].split(", ")[0];
                col = positions[1].split(", ")[1];
                gps[1] = new GridPosition(Integer.parseInt(row), Integer.parseInt(col));

                gps[2] = null;
                if(positions.length >= 3) {
                    row = positions[2].split(", ")[0];
                    col = positions[2].split(", ")[1];
                    gps[2] = new GridPosition(Integer.parseInt(row), Integer.parseInt(col));
                }

                replay.add(gps);
            }

            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return replay;
    }

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

}
