import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Piece {
    private Type type; //every piece needs to be either black or white
    private BufferedImage image; //image to be drawn

    //constructor, takes the type of the piece
    public Piece(Type t) {
        this.type = t; //set the type
        //load the appropriate image
        if(this.getType() == Type.BLACK)
            try {
                image = ImageIO.read(new File("res/black_pawn.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        else if(this.getType() == Type.WHITE)
            try {
                image = ImageIO.read(new File("res/white_pawn.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    //pieces should draw themselves
    public void paint(Graphics g, int x, int y) {
        g.drawImage(image, x + 15, y + 15, null); //some magic numbers to make sure pieces are centered, TODO:change this use maths
    }

    //returns the type of the piece
    public Type getType() {
        return type;
    }
}
