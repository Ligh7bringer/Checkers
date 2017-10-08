import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Piece {
    private Type type;
    private BufferedImage image;

    public Piece(Type t) throws IOException {
        this.type = t;
        if(this.getType() == Type.BLACK)
            image = ImageIO.read(new File("res/black_pawn.png"));
        else if(this.getType() == Type.WHITE)
            image = ImageIO.read(new File("res/white_pawn.png"));
    }

    public void paint(Graphics g, int x, int y) {
        g.drawImage(image, x + 15, y + 15, null);
    }

    public Type getType() {
        return type;
    }
}
