package graph;

public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("{ x: %s, y: %s}", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof Point)) {
            return false;
        }

        Point other = (Point) o;
        return this.x == other.x() && this.y == other.y();
    }

    @Override
    public int hashCode() {
        return (int)x * 91 ^ (int)y / 17;
    }
}
