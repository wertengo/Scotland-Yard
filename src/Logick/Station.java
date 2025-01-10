package Logick;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Station {
    private int id;
    private String name;
    private int x, y; // Координаты для отображения на экране
    private int position;

    public Station(int id, String name, int position) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Logick.Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
