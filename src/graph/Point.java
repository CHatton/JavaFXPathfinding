package graph;

public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
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
        return x * 91 ^ y / 17;
    }
}
