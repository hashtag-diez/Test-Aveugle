package client.src.ihm.model;

public class Theme {
    private String name;
    private String color;

    public Theme(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
