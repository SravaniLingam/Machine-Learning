public class Point {
    public double x = 0;
    public double y = 0;
    public int id = 0;
    public Centroid centroid = null;

    public Point(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}