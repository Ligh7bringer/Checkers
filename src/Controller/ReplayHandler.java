package Controller;

import Model.GridPosition;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class ReplayHandler {
    private static Charset utf8 = StandardCharsets.UTF_8;
    private static FileWriter fw;

    public static void saveReplay() {
        File file = new File("replays/replay" + 1 + ".txt");
        if(file.exists()) {
            try {
                fw = new FileWriter(file,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!GameHistory.getMoves().isEmpty()) {
            for (GridPosition[] gps : GameHistory.getMoves()) {
                GridPosition source = gps[0];
                GridPosition dest = gps[1];
                GridPosition removedPiece = gps[2];

                String text = "asd";
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

    public static LinkedList<GridPosition[]> parseReplay() {
        LinkedList<GridPosition[]> replay = new LinkedList<>();

        File file;
        FileReader fileReader;
        BufferedReader bufferedReader;
        try {
            file = new File("replays/replay1.txt");
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
                row = positions[0].split(" ")[0];
                col = positions[0].split(" ")[1];
                gps[0] = new GridPosition(Integer.parseInt(row), Integer.parseInt(col));

                row = positions[1].split(" ")[0];
                col = positions[1].split(" ")[1];
                gps[1] = new GridPosition(Integer.parseInt(row), Integer.parseInt(col));

                gps[2] = null;
                if(line.length() == 3) {
                    row = positions[2].split(" ")[0];
                    col = positions[2].split(" ")[1];
                    gps[2] = new GridPosition(Integer.parseInt(row), Integer.parseInt(col));
                }

                System.out.println(gps[0].toString() + "; " + gps[1].toString());

                replay.add(gps);
            }

            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        test(replay);

        return replay;
    }

    public static void test(LinkedList<GridPosition[]> asd) {
        for(GridPosition[] gp : asd)
            System.out.println(gp[0].toString() + " " + gp[1].toString());
    }


}
