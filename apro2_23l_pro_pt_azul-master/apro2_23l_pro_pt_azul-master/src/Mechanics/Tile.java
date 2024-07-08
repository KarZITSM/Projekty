package Mechanics;

import java.io.Serializable;

/**
 * The Colour enum determines the type of tile
 */
public enum Tile implements Serializable {
    BLACK("img/black.png"),
    RED("img/red.png"),
    WHITE("img/white.png"),
    YELLOW("img/yellow.png"),
    BLUE("img/blue.png"),
    FIRSTTILE("img/notile.png");

    private String imageName;

    Tile(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
}



