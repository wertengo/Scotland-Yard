import java.awt.*;

public enum ConnectionType {
    Taxi,
    Bus,
    Metro,
    DoubleTicket;
    final Color[] color = {Color.YELLOW, Color.GREEN, Color.RED};
//    final java.awt.Stroke strokes = {new BasicStroke(2),
//            new BasicStroke(2),
//            new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{10.f, 10.f}, 0)};

    public Color getColor() {
        return color[ordinal()];
    }

//    public java.awt.Stroke getStroke() {
//        return strokes[ordinal()];
//    }
//
//    private final Color TAXI_COLOR = Color.YELLOW;
//    private final Color BUS_COLOR = Color.GREEN;
//    private final Color METRO_COLOR = Color.RED;
}
