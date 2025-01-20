package main;

public class Coordinates {
    public int col;
    public int row;

    public Coordinates(int col, int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public String toString() {
        return "Col: " + col + ", Row: " + row;
    }
}

