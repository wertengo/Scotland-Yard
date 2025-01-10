package Logick;

import java.util.HashMap;
import java.util.Map;

//class Logick.Player {
//    private int id;
//    private String name;
//    private Logick.Station currentStation;
//    private boolean isMrX; // True, если игрок - мистер Х
//    private Map<Logick.ConnectionType, Integer> tickets; // Количество билетов
//
//    public Logick.Player(int id, String name, boolean isMrX, Logick.Station startingStation) {
//        this.id = id;
//        this.name = name;
//        this.isMrX = isMrX;
//        this.currentStation = startingStation;
//        this.tickets = new HashMap<>();
//        for (Logick.ConnectionType type : Logick.ConnectionType.values()) {
//            tickets.put(type, 10); // Например, стартовое количество билетов
//        }
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Logick.Station getCurrentStation() {
//        return currentStation;
//    }
//
//    public void setCurrentStation(Logick.Station station) {
//        this.currentStation = station;
//    }
//
//    public boolean isMrX() {
//        return isMrX;
//    }
//
//    public boolean useTicket(Logick.ConnectionType type) {
//        int count = tickets.getOrDefault(type, 0);
//        if (count > 0) {
//            tickets.put(type, count - 1);
//            return true;
//        }
//        return false;
//    }
//
//    public int getTickets(Logick.ConnectionType type) {
//        return tickets.getOrDefault(type, 0);
//    }
//}


public class Player {
    private int id;
    private Graph graph;
    private String name;
    private Station currentStation;
    private boolean isMrX; // True, если игрок - мистер Х
    private Map<ConnectionType, Integer> tickets; // Количество билетов

    public Player(int id, String name, boolean isMrX) {
        this.id = id;
        this.name = name;
        this.isMrX = isMrX;
        this.tickets = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(Station station) {
        this.currentStation = station;
    }

    public boolean isMrX() {
        return isMrX;
    }

    public void setMrX(boolean mrX) {
        isMrX = mrX;
    }

    public boolean useTicket(ConnectionType type) {
        int count = tickets.getOrDefault(type, 0);
        if (count > 0) {
            tickets.put(type, count - 1);
            return true;
        }
        return false;
    }

    public void createTicket(ConnectionType type, int count) {
            tickets.put(type, count);
    }

    public int getTickets(ConnectionType type) {
        return tickets.getOrDefault(type, 0);
    }

    // Метод для перемещения игрока
    public boolean moveToStation(Station station, ConnectionType ticketType) {
        if (useTicket(ticketType)) {
            setCurrentStation(station);
            return true;
        }
        return false;
    }
}
