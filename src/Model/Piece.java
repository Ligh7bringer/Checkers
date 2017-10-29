package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//this class defines a single piece on the board
public class Piece {
    private Type type; //every piece needs to be either black or white
    private BufferedImage image; //image to be drawn

    private GridPosition gridPosition; //is this needed?

    //constructor, takes the type of the piece and it's location
    public Piece(Type t, GridPosition gp) {
        this.type = t; //set the type
        //load the appropriate image
        if(this.getType() == Type.BLACK)
            try {
                image = ImageIO.read(new File("res/blackpiece.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        else if(this.getType() == Type.WHITE)
            try {
                image = ImageIO.read(new File("res/whitepiece.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        this.gridPosition = gp;
    }

    //pieces should draw themselves
    void paintComponent(Graphics g, int x, int y) {
        g.drawImage(image, x + 10, y + 10, null);
    }

    //this method will change the piece type to black or white king
    public void crownPiece() {
        if(this.type == Type.WHITE && this.getGridPosition().getRow() == 7) {
            this.type = Type.WHITE_KING;
            try {
                image = ImageIO.read(new File("res/whiteking.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(this.type == Type.BLACK && this.getGridPosition().getRow() == 0) {
            this.type = Type.BLACK_KING;
            try {
                image = ImageIO.read(new File("res/blackking.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //returns the type of the piece
    public Type getType() {
        return type;
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }

    void setGridPosition(GridPosition gp) {
        this.gridPosition = gp;
    }

    //custom equals method which is called when using ArrayList.contains()
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Piece.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Piece other = (Piece) obj;

        return (this.getGridPosition() == other.getGridPosition() && this.getType() == other.getType());
    }

    //custom toString method mostly used for debugging
    @Override
    public String toString() {
        return this.type + ", " + this.gridPosition.toString();
    }
}
