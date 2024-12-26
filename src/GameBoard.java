import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GameBoard extends JPanel {
    private Graph graph;
    private Game game;
    private List<Player> players;
    private Player currentPlayer;
    private Station stationNext;
    private JPopupMenu jpu;
    private Player player;

    private final int COLUMS = 23;
    private final int ROWS = 17;

    private final int deltaX = 35;
    private final int deltaY = 35;

    public GameBoard(Game game,Graph graph, Player player, List<Player> players) {
        this.graph = graph;
        this.game = game;
        this.player = player;
        this.currentPlayer = player;
        this.players = players;

        setPreferredSize(new Dimension(830, 600));
        setBackground(Color.GRAY);

        jpu = new JPopupMenu();

        JMenuItem jmiFrom = new JMenuItem();

        JMenuItem jmiTaxi = new JMenuItem("Taxi ticket");
        JMenuItem jmiBus = new JMenuItem("Bus ticket");
        JMenuItem jmiMetro = new JMenuItem("Metro ticket");

        jmiFrom.setEnabled(false);

        jpu.add(jmiFrom);
        jpu.add(jmiTaxi);
        jpu.add(jmiBus);
        jpu.add(jmiMetro);

        jmiTaxi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(GameBoard.this, "минус билет)))", "Station Info", JOptionPane.INFORMATION_MESSAGE);
                if (graph.getConnectedStationsIds(currentPlayer.getCurrentStation().getId()).contains(stationNext.getId()) &&
                        graph.getConnectionType(currentPlayer.getCurrentStation(), stationNext) == ConnectionType.Taxi &&
                        currentPlayer.getTickets(ConnectionType.Taxi) > 0) {

                    System.out.println("Id: " + currentPlayer.getId() + " MrX - " + currentPlayer.isMrX());
                    System.out.println(currentPlayer.getTickets(ConnectionType.Taxi));

//                    JOptionPane.showMessageDialog(GameBoard.this, "был ход на точку: " + player.getCurrentStation().getId(), "Station Info", JOptionPane.INFORMATION_MESSAGE);
                    movePlayer(stationNext,ConnectionType.Taxi);
                }
            }
        });

        jmiBus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graph.getConnectedStationsIds(currentPlayer.getCurrentStation().getId()).contains(stationNext.getId()) &&
                        graph.getConnectionType(currentPlayer.getCurrentStation(), stationNext) == ConnectionType.Bus &&
                        currentPlayer.getTickets(ConnectionType.Bus) > 0) {
//                    currentPlayer.setCurrentStation(stationNext);
//                    currentPlayer.useTicket(ConnectionType.Bus);
                    movePlayer(stationNext,ConnectionType.Bus);
//                    JOptionPane.showMessageDialog(GameBoard.this, "был ход на точку: " + player.getCurrentStation().getId(), "Station Info", JOptionPane.INFORMATION_MESSAGE);
//                    repaint();
//                    game.nextPlayerTurn();
                }
            }
        });

        jmiMetro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graph.getConnectedStationsIds(currentPlayer.getCurrentStation().getId()).contains(stationNext.getId()) &&
                        graph.getConnectionType(currentPlayer.getCurrentStation(), stationNext) == ConnectionType.Metro &&
                        currentPlayer.getTickets(ConnectionType.Metro) > 0) {
//                    currentPlayer.setCurrentStation(stationNext);
//                    currentPlayer.useTicket(ConnectionType.Metro);
//                    JOptionPane.showMessageDialog(GameBoard.this, "был ход на точку: " + player.getCurrentStation().getId(), "Station Info", JOptionPane.INFORMATION_MESSAGE);
//                    repaint();
//                    game.nextPlayerTurn();
                    movePlayer(stationNext,ConnectionType.Metro);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (SwingUtilities.isLeftMouseButton(e) && handleMouseClick(e) != null) {
                    stationNext = handleMouseClick(e);
//                    showStationInfo(handleMouseClick(e));
                    jmiFrom.setText(player.getCurrentStation().getId() + " -> " + stationNext.getId());
                    jpu.show(e.getComponent(), e.getX() + 20, e.getY() - 10);
                }

                if (SwingUtilities.isRightMouseButton(e) && handleMouseClick(e) != null) {
//                    handleMouseClick(e);
                    jpu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Рисуем связи между станциями
        drawConnections(g2d);

        // Рисуем станции так же, как на исходной картинке
        drawStations(g2d);

        // Рисуем игроков
        drawPlayer(g2d);
    }

    private void drawConnections(Graphics2D g2d) {
        for (Connection connection : graph.getSingleConnectionsList()) {
            //System.out.println(connection);
            Station station1 = connection.getStation1();
            Station station2 = connection.getStation2();

            if (station1 != null && station2 != null) {
                // Устанавливаем цвет и стиль линии в зависимости от типа связи
                g2d.setColor(connection.getType().getColor());
                switch (connection.getType()) {
                    case Taxi -> {
//                        g2d.setColor(TAXI_COLOR);
                        g2d.setStroke(new BasicStroke(2));
                    }
                    case Bus -> {
//                        g2d.setColor(BUS_COLOR);
                        g2d.setStroke(new BasicStroke(2));
                    }
                    case Metro -> {
//                        g2d.setColor(METRO_COLOR);
                        float[] dashPattern = {10, 10}; // Шаблон для пунктирной линии
                        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
                    }
                    default -> g2d.setColor(Color.BLACK);
                }

                int x1 = station1.getX();
                int y1 = station1.getY();
                int x2 = station2.getX();
                int y2 = station2.getY();

                // Рисуем линию между станциями
                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }

    private void drawTable() {
        for (Station station : graph.getAllStations()) {
            station.setX(20 + deltaX * (station.getPosition() % COLUMS));
            station.setY(20 + deltaY * (station.getPosition() / COLUMS));
        }
    }

    private void drawStations(Graphics2D g2d) {
        drawTable();
        int radius = 25;
        for (Station station : graph.getAllStations()) {

            // Настроим стиль узлов, чтобы он соответствовал исходной карте
            g2d.setColor(Color.WHITE);
            g2d.fillOval(station.getX() - radius / 2, station.getY() - radius / 2, radius, radius);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(station.getX() - radius / 2, station.getY() - radius / 2, radius, radius);

            // Отобразим идентификатор станции в центре узла
            g2d.drawString(station.getName(), station.getX() - 10, station.getY() + 5);
        }
    }

    private void drawPlayer(Graphics2D g2d) {
        int radius = 15;

        for (Player player : players) {
            g2d.setColor(player == currentPlayer ? new Color(155,9,199) : Color.BLUE);
            g2d.fillOval(player.getCurrentStation().getX() - radius / 2 + 10, player.getCurrentStation().getY() - radius / 2, radius / 2, radius / 2);
            g2d.drawString(player.getName(), player.getCurrentStation().getX(), player.getCurrentStation().getY());
        }
    }

    private Station handleMouseClick(MouseEvent e) {
        int radius = 25;
        for (Station station : graph.getAllStations()) {
            int x = station.getX();
            int y = station.getY();
            if (e.getX() >= x - radius / 2 && e.getX() <= x + radius / 2 &&
                    e.getY() >= y - radius / 2 && e.getY() <= y + radius / 2) {
//                showStationInfo(station);
//                break;
                return station;
            }
        }
        return null;
    }

    private void showStationInfo(Station station) {
        JOptionPane.showMessageDialog(this, "Station: " + station.getName(), "Station Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void movePlayer(Station nextStation, ConnectionType ticketType) {
        for (Player player : players) {
            if (player != currentPlayer && player.getCurrentStation() == nextStation) {
                if (player.isMrX() || currentPlayer.isMrX()) {
                    JOptionPane.showMessageDialog(this, "Mr. X is caught! Game over.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Detectives cannot occupy the same station.", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        currentPlayer.setCurrentStation(nextStation);
        currentPlayer.useTicket(ticketType);
        repaint();
        game.nextPlayerTurn();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}

