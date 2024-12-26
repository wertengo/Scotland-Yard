import java.util.*;

class Graph {
    private TreeMap<Integer, Station> stations;
    private HashMap<Integer, HashSet<Connection>> connections;

    private HashMap<Integer, HashSet<Connection>> singleConnections;
    private ArrayList<Connection> allConnections;
    private ArrayList<Connection> singleConnectionsList;

    public Graph() {
        stations = new TreeMap<Integer, Station>();
        connections = new HashMap<Integer, HashSet<Connection>>();

        singleConnections = new HashMap<Integer, HashSet<Connection>>();
    }

    public void addStation(Station station) {
        stations.put(station.getId(), station);
    }

    public void addConnection(Station station1, Station station2, ConnectionType connectionType) {
        putConnection(station1, station2, connectionType);
        putConnection(station2, station1, connectionType);
        putSingleConnection(station1, station2, connectionType);
    }

    private void putSingleConnection(Station station1, Station station2, ConnectionType connectionType) {
        HashSet<Connection> connSet = singleConnections.get(station1.getId());
        if (connSet == null) {
            connSet = new HashSet<Connection>();
            singleConnections.put(station1.getId(), connSet);
        }
        connSet.add(new Connection(station1, station2, connectionType));
    }

    private void putConnection(Station station1, Station station2, ConnectionType connectionType) {
        HashSet<Connection> connSet = connections.get(station1.getId());
        if (connSet == null) {
            connSet = new HashSet<Connection>();
            connections.put(station1.getId(), connSet);
        }
        connSet.add(new Connection(station1, station2, connectionType));
    }


    public Station getStation(int id) {
        return stations.get(id);
    }

    /**
     * Выдает список станций, связанных с заданной
     */
    public ArrayList<Station> getConnectedStations(Station station) {
        HashSet<Connection> connections1 = singleConnections.get(station.getId());
        ArrayList<Station> stationsList = new ArrayList<Station>();
        for (Connection connection : connections1) {
            stationsList.add(connection.getStation2());
        }

        return stationsList;
    }

    public ArrayList<Station> getConnectedStations(int station_id) {
        HashSet<Connection> connections1 = singleConnections.get(station_id);
        ArrayList<Station> stationsList = new ArrayList<Station>();
        for (Connection connection : connections1) {
            stationsList.add(connection.getStation2());
        }

        return stationsList;
    }

    public ArrayList<Integer> getSingleConnectedStationsIds(int station_id) {
        HashSet<Connection> connections1 = singleConnections.get(station_id);
        ArrayList<Integer> stationsIdList = new ArrayList<Integer>();
        for (Connection connection : connections1) {
            stationsIdList.add(connection.getStation2().getId());
        }

        return stationsIdList;
    }

    public ArrayList<Integer> getConnectedStationsIds(int station_id) {
        HashSet<Connection> connections1 = connections.get(station_id);
        ArrayList<Integer> stationsIdList = new ArrayList<Integer>();
        for (Connection connection : connections1) {
            stationsIdList.add(connection.getStation2().getId());
        }

        return stationsIdList;
    }

    public ConnectionType getConnectionType(Station station1, Station station2) {
        HashSet<Connection> connSet = connections.get(station1.getId());
        if (connSet != null) {
            for (Connection connection : connSet) {
                if (connection.getStation2().equals(station2)) {
                    return connection.getType();
                }
            }
        }
        return null;
    }

    public List<Station> getAllStations() {
        return new ArrayList<>(stations.values());
    }

    public ArrayList<Connection> getAllConnections() {
        return allConnections;
    }

    public ArrayList<Connection> getSingleConnectionsList() {
        return singleConnectionsList;
    }

    public void buildAllConnections() {
        ArrayList<Connection> list = new ArrayList<Connection>();

        for (HashSet<Connection> set : connections.values()) {
            list.addAll(new ArrayList<>(set));
        }
        allConnections = list;
    }

    public void buildSingleConnections() {
        ArrayList<Connection> list = new ArrayList<Connection>();

        for (HashSet<Connection> set : singleConnections.values()) {
            list.addAll(new ArrayList<>(set));
        }
        singleConnectionsList = list;
    }
}
