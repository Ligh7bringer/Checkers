package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Piece {
    private Type type; //every piece needs to be either black or white
    private BufferedImage image; //image to be drawn

    private GridPosition gridPosition; //is this needed?

    //constructor, takes the type of the piece
    public Piece(Type t, GridPosition gp) {
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

            this.gridPosition = gp;
    }

    //pieces should draw themselves
    public void paintComponent(Graphics g, int x, int y) {
        g.drawImage(image, x +image.getWidth() / 2, y + image.getHeight() / 2, null); //TODO:change this to calculate center of tile
    }

    //this method will change the piece type to black or white king
    public void crownPiece() {
        if(this.type == Type.WHITE)
            this.type = Type.WHITE_KING;
        else if(this.type == Type.BLACK)
            this.type = Type.BLACK_KING;
    }

    //returns the type of the piece
    public Type getType() {
        return type;
    }

    public void setGridPosition(GridPosition gp) {
        this.gridPosition = gp;
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }

    //same problem as GridPosition, need to have a custom equals method
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Piece.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Piece other = (Piece) obj;
        if ((this.getGridPosition() != other.getGridPosition() || this.getType() != other.getType())) {
            return false;
        }

        return true;
    }
}
