package mygames.sos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class SOSGUIGame extends JFrame {

    // Symbol radio buttons
    JRadioButton redORadioButton;
    JRadioButton redSRadioButton;
    JRadioButton blueORadioButton;
    JRadioButton blueSRadioButton;

    // Human / Computer radio buttons
    JRadioButton blueHumanRadioButton;
    JRadioButton blueComputerRadioButton;
    JRadioButton redHumanRadioButton;
    JRadioButton redComputerRadioButton;

    JPanel bluePlayerEmptyPanel;
    JPanel bluePlayerRadioPanel;
    JPanel redPlayerRadioPanel;
    JPanel mainPanel;
    JPanel topPanel;
    JLabel sosLabel;
    JLabel boardSizeLabel;
    JTextField boardSizeTextField;
    JRadioButton simpleGameRadioButton;
    JRadioButton generalGameRadioButton;
    JPanel centerPanel;
    JPanel bluePlayerPanel;
    JLabel bluePlayerLabel;
    JPanel redPlayerPanel;
    JPanel redPlayerEmptyPanel;
    JLabel redPlayerLabel;
    JPanel boardPanel;
    JPanel bottomPanel;
    JLabel currentTurnLabel;
    JButton newGameButton;

    // Sprint-5 buttons
    JButton recordButton;
    JButton replayButton;

    // -------- OPTION-3 --------
    private JLabel statsLabel;
    private GameStatistics statistics = new GameStatistics();

    boolean gameModeChanged = false;
    boolean boardSizeChanged = false;
    boolean gameOver = false;

    private SOSGame sosGame;
    CustomButton[][] buttons;
    private Player bluePlayer;
    private Player redPlayer;

    private Timer computerTimer;

    // Sprint-5
    private GameRecorder recorder;
    private GameReplayer replayer;
    private boolean isRecording = false;

    public SOSGUIGame() {
        initGameModel();
        initFrame();
        buildUI();
        attachPlayerTypeListeners();
        pack();
        setLocationRelativeTo(null);
        runComputerIfNeeded();

        recordButton.addActionListener(e -> {
            try {
                recorder = new GameRecorder("sos_record.txt");
                recorder.start();
                isRecording = true;
                JOptionPane.showMessageDialog(this, "Recording Started");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        replayButton.addActionListener(e -> startReplay());
    }

    private void initGameModel() {
        sosGame = new SOSSimpleGame(8);
        bluePlayer = new HumanPlayer("Blue");
        redPlayer = new HumanPlayer("Red");
    }

    private void initFrame() {
        setTitle("SOS Game - Sprint 5");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 650));
        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
    }

    private void buildUI() {
        createTopPanel();
        createPlayerPanels();
        createCenterPanel();
        createBottomPanel();
    }

    private void createTopPanel() {
        topPanel = new JPanel(new FlowLayout());
        sosLabel = new JLabel("SOS");

        simpleGameRadioButton = new JRadioButton("Simple Game", true);
        generalGameRadioButton = new JRadioButton("General Game");

        simpleGameRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                sosGame = new SOSSimpleGame(sosGame.getBoardSize());
                gameModeChanged = true;
            }
        });

        generalGameRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                sosGame = new SOSGeneralGame(sosGame.getBoardSize());
                gameModeChanged = true;
            }
        });

        ButtonGroup gameTypeGroup = new ButtonGroup();
        gameTypeGroup.add(simpleGameRadioButton);
        gameTypeGroup.add(generalGameRadioButton);

        boardSizeLabel = new JLabel("Board Size:");
        boardSizeTextField = new JTextField(String.valueOf(sosGame.getBoardSize()), 2);
        boardSizeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { boardSizeChanged = true; }
            @Override public void removeUpdate(DocumentEvent e) {}
            @Override public void changedUpdate(DocumentEvent e) {}
        });

        recordButton = new JButton("Record");
        replayButton = new JButton("Replay");

        topPanel.add(sosLabel);
        topPanel.add(simpleGameRadioButton);
        topPanel.add(generalGameRadioButton);
        topPanel.add(boardSizeLabel);
        topPanel.add(boardSizeTextField);
        topPanel.add(recordButton);
        topPanel.add(replayButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
    }

    private void createPlayerPanels() {
        bluePlayerPanel = new JPanel(new BorderLayout());
        bluePlayerLabel = new JLabel("Blue Player");
        bluePlayerPanel.add(bluePlayerLabel, BorderLayout.NORTH);

        bluePlayerRadioPanel = new JPanel(new GridLayout(4, 1));
        blueHumanRadioButton = new JRadioButton("Human", true);
        blueComputerRadioButton = new JRadioButton("Computer");
        blueSRadioButton = new JRadioButton("S", true);
        blueORadioButton = new JRadioButton("O");

        ButtonGroup blueTypeGroup = new ButtonGroup();
        blueTypeGroup.add(blueHumanRadioButton);
        blueTypeGroup.add(blueComputerRadioButton);

        ButtonGroup blueSymbolGroup = new ButtonGroup();
        blueSymbolGroup.add(blueSRadioButton);
        blueSymbolGroup.add(blueORadioButton);

        bluePlayerRadioPanel.add(blueHumanRadioButton);
        bluePlayerRadioPanel.add(blueComputerRadioButton);
        bluePlayerRadioPanel.add(blueSRadioButton);
        bluePlayerRadioPanel.add(blueORadioButton);

        bluePlayerPanel.add(bluePlayerRadioPanel, BorderLayout.CENTER);

        redPlayerPanel = new JPanel(new BorderLayout());
        redPlayerLabel = new JLabel("Red Player");
        redPlayerPanel.add(redPlayerLabel, BorderLayout.NORTH);

        redPlayerRadioPanel = new JPanel(new GridLayout(4, 1));
        redHumanRadioButton = new JRadioButton("Human", true);
        redComputerRadioButton = new JRadioButton("Computer");
        redSRadioButton = new JRadioButton("S", true);
        redORadioButton = new JRadioButton("O");

        ButtonGroup redTypeGroup = new ButtonGroup();
        redTypeGroup.add(redHumanRadioButton);
        redTypeGroup.add(redComputerRadioButton);

        ButtonGroup redSymbolGroup = new ButtonGroup();
        redSymbolGroup.add(redSRadioButton);
        redSymbolGroup.add(redORadioButton);

        redPlayerRadioPanel.add(redHumanRadioButton);
        redPlayerRadioPanel.add(redComputerRadioButton);
        redPlayerRadioPanel.add(redSRadioButton);
        redPlayerRadioPanel.add(redORadioButton);

        redPlayerPanel.add(redPlayerRadioPanel, BorderLayout.CENTER);
    }

    private void createCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        updateBoard();
        centerPanel.add(bluePlayerPanel, BorderLayout.WEST);
        centerPanel.add(redPlayerPanel, BorderLayout.EAST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    private void createBottomPanel() {
        bottomPanel = new JPanel(new BorderLayout());

        statsLabel = new JLabel("Games: 0 | Blue Wins: 0 | Red Wins: 0 | Draws: 0");
        statsLabel.setBorder(new EmptyBorder(0, 20, 0, 0));

        currentTurnLabel = new JLabel("Current Turn: blue");
        currentTurnLabel.setBorder(new EmptyBorder(0, 250, 0, 0));

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> newGameButtonClicked());

        bottomPanel.add(statsLabel, BorderLayout.WEST);
        bottomPanel.add(currentTurnLabel, BorderLayout.CENTER);
        bottomPanel.add(newGameButton, BorderLayout.EAST);

        bottomPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshStatisticsLabel() {
        statsLabel.setText(
            "Games: " + statistics.getGamesPlayed() +
            " | Blue Wins: " + statistics.getBlueWins() +
            " | Red Wins: " + statistics.getRedWins() +
            " | Draws: " + statistics.getDraws()
        );
    }

    private void newGameButtonClicked() {
        gameOver = false;
        sosGame.reset(Integer.parseInt(boardSizeTextField.getText()));
        currentTurnLabel.setText("Current Turn: blue");
        updateBoard();
        runComputerIfNeeded();
    }

    private void updateBoard() {
        if (boardPanel != null) centerPanel.remove(boardPanel);

        int size = sosGame.getBoardSize();
        boardPanel = new JPanel(new GridLayout(size, size));
        buttons = new CustomButton[size][size];

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                CustomButton b = new CustomButton("");
                int rr = r, cc = c;
                b.addActionListener(e -> handleCellClick(b, rr, cc));
                buttons[r][c] = b;
                boardPanel.add(b);
            }
        }

        centerPanel.add(boardPanel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void handleCellClick(JButton button, int row, int col) {
        if (gameOver || !button.getText().equals("")) return;

        String symbol = sosGame.isBluePlayersTurn()
                ? (blueSRadioButton.isSelected() ? "S" : "O")
                : (redSRadioButton.isSelected() ? "S" : "O");

        button.setText(symbol);
        sosGame.makeMove(row, col, symbol);

        if (sosGame.isGameOver()) {
            showGameOverMessage();
        } else {
            sosGame.setBluePlayersTurn(!sosGame.isBluePlayersTurn());
            currentTurnLabel.setText("Current Turn: " +
                    (sosGame.isBluePlayersTurn() ? "blue" : "red"));
        }
    }

    private void showGameOverMessage() {
        gameOver = true;

        statistics.recordGameResult(
                sosGame.getBluePlayerSOSCount(),
                sosGame.getRedPlayerSOSCount()
        );
        refreshStatisticsLabel();

        String msg =
                sosGame.getBluePlayerSOSCount() == sosGame.getRedPlayerSOSCount()
                        ? "Its a draw"
                        : (sosGame.getBluePlayerSOSCount() > sosGame.getRedPlayerSOSCount()
                        ? "Blue player won!!"
                        : "Red player won!!");

        JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void startReplay() {}

    private void attachPlayerTypeListeners() {}

    private void runComputerIfNeeded() {}

    private class CustomButton extends JButton {
        public CustomButton(String text) {
            super(text);
        }
    }
}
