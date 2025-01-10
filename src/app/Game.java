package app;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import Logick.ConnectionType;
import Logick.Graph;
import Logick.Player;
import Logick.Station;
import org.w3c.dom.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game extends Component {
    private Graph graph;
    private Station startStation;
    private JFrame frame;
    private GameBoard gameBoard;
    private Player player;
    private List<Player> players;
    private int currentPlayerIndex;
    private int moveCount;

    public Game() {
        graph = new Graph();
        loadGraphFromXML("ScotlandYardMap.xml");
        players = createPlayers(6);
        assignStartingPositions();
        currentPlayerIndex = 0;

        gameBoard = new GameBoard(this, graph, players.get(currentPlayerIndex), players);
        frame = new JFrame("Scotland Yard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameBoard);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private List<Player> createPlayers(int count) {
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            player = new Player(i + 1, "Wertengo", false);
            players.add(player);
        }

        randomMisterX(players);

        for (Player player1 : players) {
            if (player1.isMrX()) {
                player1.createTicket(ConnectionType.Taxi, 4);
                player1.createTicket(ConnectionType.Bus, 3);
                player1.createTicket(ConnectionType.Metro, 3);
                player1.createTicket(ConnectionType.DoubleTicket, 2);
            } else {
                player1.createTicket(ConnectionType.Taxi, 10);
                player1.createTicket(ConnectionType.Bus, 8);
                player1.createTicket(ConnectionType.Metro, 4);
            }
        }

        //проверка инициализации игроков
        for (Player player1 : players) {
            System.out.println("Id: " + player1.getId() + " MrX - " + player1.isMrX());
            System.out.println(player1.getTickets(ConnectionType.Taxi) + " " + player1.getTickets(ConnectionType.Bus) + " " + player1.getTickets(ConnectionType.Metro));
        }

        return players;
    }



     //Loads the graph's stations and connections from an XML file

    private void loadGraphFromXML(String filePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filePath);

            // Load stations from XML
            NodeList nodes = doc.getElementsByTagName("node");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element nodeElement = (Element) nodes.item(i);
                int id = Integer.parseInt(nodeElement.getAttribute("id"));
                int position = Integer.parseInt(nodeElement.getAttribute("pos"));
                String name = Integer.toString(id);
                Station station = new Station(id, name, position);

//                if (id == 1) {
//                    startStation = station;
//                    player = new Logick.Player(1, "Wertengo", false, startStation);
//                }

                graph.addStation(station);
            }


            // Load connections from XML
            NodeList connections = doc.getElementsByTagName("connection");
            for (int i = 0; i < connections.getLength(); i++) {
                Element connectionElement = (Element) connections.item(i);
                String type = connectionElement.getAttribute("type");

                ConnectionType connType = ConnectionType.valueOf(type);


                int from_id = geId(connectionElement, "from_id");
                int to_id = geId(connectionElement, "to_id");

                if (from_id != -1 && to_id != -1) {

                    Station station1 = findStationById(from_id);
                    Station station2 = findStationById(to_id);

                    if (station1 != null && station2 != null) {
                        graph.addConnection(station1, station2, connType);
                    }
                }
            }

            graph.buildAllConnections();
            graph.buildSingleConnections();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int geId(Element connectionElement, String name) {
        int id = -1;
        try {
            id = Integer.parseInt(connectionElement.getAttribute(name));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return id;
    }


      //Finds a station by its coordinates.

    private Station findStationByCoordinates(int x, int y) {
        for (Station station : graph.getAllStations()) {
            if (station != null && (station.getX() == x && station.getY() == y)) {
                return station;
            }
        }
        return null;
    }


     // Finds a station by id

    private Station findStationById(int id) {
        for (Station station : graph.getAllStations()) {
            if (station != null && (station.getId() == id)) {
                return station;
            }
        }
        return null;
    }

    public void assignStartingPositions() {
        List<Station> availableStations = graph.getAllStations();
//        HashSet<Integer> indices = new HashSet<>();

        Random random = new Random();
        for (Player player : players) {
            int randomIndex = random.nextInt(availableStations.size());
            Station startingStation = availableStations.get(randomIndex);
            player.setCurrentStation(startingStation);
            availableStations.remove(randomIndex);
        }
    }

    public void nextPlayerTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        gameBoard.setCurrentPlayer(players.get(currentPlayerIndex));
        gameBoard.repaint();

        if (currentPlayerIndex == 0) {
            incrementMoveCount();
        }
    }

    public void randomMisterX(List<Player> players) {
        int min = 0;
        int max = players.size() - 1;
        max -= min;

        players.get((int) (Math.random() * ++max) + min).setMrX(true);
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void incrementMoveCount() {
        moveCount++;
        System.out.println("Move count: " + moveCount);
    }

//    private static int generateRandomIndex(int size, HashSet<Integer> indices) {
//        int randomIndex;
//        while (true) {
//            randomIndex = new Random().nextInt(size);
//            if (!indices.contains(randomIndex)) {
//                indices.add(randomIndex);
//                break;
//            }
//        }
//        return randomIndex;
//    }
}

