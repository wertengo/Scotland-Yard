import java.awt.*;
import java.util.Objects;

public class Connection {
    private Station station1;
    private Station station2;
    private ConnectionType type; // Поле для хранения типа соединения (taxi, bus, metro)

    public Connection(Station station1, Station station2, ConnectionType type) {
        this.station1 = station1;
        this.station2 = station2;
        this.type = type;
    }

    public Station getStation1() {
        return station1;
    }

    public Station getStation2() {
        return station2;
    }

    public ConnectionType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Objects.equals(station1, that.station1) && Objects.equals(station2, that.station2) && type == that.type;
    }

}
