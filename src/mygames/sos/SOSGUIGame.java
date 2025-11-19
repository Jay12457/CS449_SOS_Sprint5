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

    // Panels and layout components
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

    // State flags
    boolean gameModeChanged = false;
    boolean boardSizeChanged = false;
    boolean gameOver = false;

    // Game model and players
    private SOSGame sosGame;
    CustomButton[][] buttons;
    private Player bluePlayer;
    private Player redPlayer;

    // Timer for computer turns
    private Timer computerTimer;

    public SOSGUIGame() {
        initGameModel();
        initFrame();
        buildUI();
        attachPlayerTypeListeners();
        pack();
        setLocationRelativeTo(null);
        runComputerIfNeeded();
    }

    // ---------------------- Initialization / UI building ----------------------

    private void initGameModel() {
        sosGame = new SOSSimpleGame(8);
        bluePlayer = new HumanPlayer("Blue");
        redPlayer = new HumanPlayer("Red");
    }

    private void initFrame() {
        setTitle("SOS Game - Sprint 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 650));
        mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);
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

        simpleGameRadioButton = new JRadioButton("Simple Game");
        simpleGameRadioButton.setSelected(true);
        simpleGameRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                sosGame = new SOSSimpleGame(sosGame.getBoardSize());
                gameModeChanged = true;
            }
        });

        generalGameRadioButton = new JRadioButton("General Game");
        generalGameRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                sosGame = new SOSGeneralGame(Integer.parseInt(boardSizeTextField.getText()));
                gameModeChanged = true;
            }
        });

        ButtonGroup gameTypeGroup = new ButtonGroup();
        gameTypeGroup.add(simpleGameRadioButton);
        gameTypeGroup.add(generalGameRadioButton);

        boardSizeLabel = new JLabel("Board Size:");
        boardSizeLabel.setBorder(new EmptyBorder(0, 100, 0, 0));
        boardSizeTextField = new JTextField(String.valueOf(sosGame.getBoardSize()));
        boardSizeTextField.setColumns(2);
        boardSizeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                boardSizeChanged = true;
                handleBoardSizeTextChange(boardSizeTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) { }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        });

        topPanel.add(sosLabel);
        topPanel.add(simpleGameRadioButton);
        topPanel.add(generalGameRadioButton);
        topPanel.add(boardSizeLabel);
        topPanel.add(boardSizeTextField);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(topPanel, BorderLayout.NORTH);
    }

    private void createPlayerPanels() {
        // ----- Blue player -----
        bluePlayerPanel = new JPanel(new BorderLayout());
        bluePlayerLabel = new JLabel("Blue Player");
        bluePlayerLabel.setBorder(new EmptyBorder(40, 20, 10, 20));
        bluePlayerPanel.add(bluePlayerLabel, BorderLayout.NORTH);

        bluePlayerRadioPanel = new JPanel(new GridLayout(4, 1));

        blueHumanRadioButton = new JRadioButton("Human", true);
        blueComputerRadioButton = new JRadioButton("Computer");
        ButtonGroup blueTypeGroup = new ButtonGroup();
        blueTypeGroup.add(blueHumanRadioButton);
        blueTypeGroup.add(blueComputerRadioButton);

        blueSRadioButton = new JRadioButton("S");
        blueSRadioButton.setSelected(true);
        blueORadioButton = new JRadioButton("O");
        ButtonGroup blueSymbolGroup = new ButtonGroup();
        blueSymbolGroup.add(blueSRadioButton);
        blueSymbolGroup.add(blueORadioButton);

        bluePlayerRadioPanel.add(blueHumanRadioButton);
        bluePlayerRadioPanel.add(blueComputerRadioButton);
        bluePlayerRadioPanel.add(blueSRadioButton);
        bluePlayerRadioPanel.add(blueORadioButton);

        bluePlayerPanel.add(bluePlayerRadioPanel, BorderLayout.CENTER);
        bluePlayerEmptyPanel = new JPanel();
        bluePlayerEmptyPanel.setPreferredSize(new Dimension(50, 300));
        bluePlayerPanel.add(bluePlayerEmptyPanel, BorderLayout.SOUTH);

        // ----- Red player -----
        redPlayerPanel = new JPanel(new BorderLayout());
        redPlayerLabel = new JLabel("Red Player");
        redPlayerLabel.setBorder(new EmptyBorder(40, 20, 10, 20));
        redPlayerPanel.add(redPlayerLabel, BorderLayout.NORTH);

        redPlayerRadioPanel = new JPanel(new GridLayout(4, 1));

        redHumanRadioButton = new JRadioButton("Human", true);
        redComputerRadioButton = new JRadioButton("Computer");
        ButtonGroup redTypeGroup = new ButtonGroup();
        redTypeGroup.add(redHumanRadioButton);
        redTypeGroup.add(redComputerRadioButton);

        redSRadioButton = new JRadioButton("S");
        redSRadioButton.setSelected(true);
        redORadioButton = new JRadioButton("O");
        ButtonGroup redSymbolGroup = new ButtonGroup();
        redSymbolGroup.add(redSRadioButton);
        redSymbolGroup.add(redORadioButton);

        redPlayerRadioPanel.add(redHumanRadioButton);
        redPlayerRadioPanel.add(redComputerRadioButton);
        redPlayerRadioPanel.add(redSRadioButton);
        redPlayerRadioPanel.add(redORadioButton);

        redPlayerPanel.add(redPlayerRadioPanel, BorderLayout.CENTER);
        redPlayerEmptyPanel = new JPanel();
        redPlayerEmptyPanel.setPreferredSize(new Dimension(50, 300));
        redPlayerPanel.add(redPlayerEmptyPanel, BorderLayout.SOUTH);
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
        currentTurnLabel = new JLabel("Current Turn: blue");
        currentTurnLabel.setBorder(new EmptyBorder(0, 250, 0, 0));

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> newGameButtonClicked());

        bottomPanel.add(currentTurnLabel, BorderLayout.CENTER);
        bottomPanel.add(newGameButton, BorderLayout.EAST);
        bottomPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void attachPlayerTypeListeners() {
        blueHumanRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                bluePlayer = new HumanPlayer("Blue");
            }
        });
        blueComputerRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                bluePlayer = new ComputerPlayer("Blue-CPU");
                runComputerIfNeeded();
            }
        });

        redHumanRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                redPlayer = new HumanPlayer("Red");
            }
        });
        redComputerRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                redPlayer = new ComputerPlayer("Red-CPU");
                runComputerIfNeeded();
            }
        });
    }

    // ---------------------- New Game / Board Size ----------------------

    private void newGameButtonClicked() {
        gameModeChanged = false;
        boardSizeChanged = false;
        gameOver = false;

        if (computerTimer != null) {
            computerTimer.stop();
            computerTimer = null;
        }

        boolean isValidBoardSize = handleBoardSizeTextChange(boardSizeTextField.getText());
        if (isValidBoardSize) {
            sosGame.reset(Integer.parseInt(boardSizeTextField.getText()));
            String mode = (sosGame instanceof SOSSimpleGame) ? "Simple" : "General";
            JOptionPane.showMessageDialog(
                    SOSGUIGame.this,
                    "Starting new " + mode + " Game with board size " + sosGame.getBoardSize(),
                    "Starting New Game",
                    JOptionPane.INFORMATION_MESSAGE
            );
            currentTurnLabel.setText("Current Turn: blue");
            updateBoard();
            runComputerIfNeeded();
        }
    }

    boolean handleBoardSizeTextChange(String txtBoardSize) {
        boolean isSuccess = false;
        if (sosGame.isBoardSizeTextNumeric(txtBoardSize)) {
            int newSize = Integer.parseInt(txtBoardSize);
            if (sosGame.isBoardSizeGreaterThanTwo(newSize)) {
                sosGame.setBoardSize(newSize);
                updateBoard();
                isSuccess = true;
            } else {
                JOptionPane.showMessageDialog(
                        SOSGUIGame.this,
                        "Board size must be greater than 2.",
                        "Invalid Board Size",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    SOSGUIGame.this,
                    "Invalid input for board size.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return isSuccess;
    }

    private void updateBoard() {
        if (centerPanel == null) return; // called early in constructor
        if (boardPanel != null) {
            centerPanel.remove(boardPanel);
        }
        int boardSize = sosGame.getBoardSize();
        boardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        buttons = new CustomButton[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                CustomButton button = new CustomButton("");
                int finalI = i;
                int finalJ = j;
                button.addActionListener(e -> handleBoardButtonClick(button, finalI, finalJ));
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }
        centerPanel.add(boardPanel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    // ---------------------- Click Handling (modular) ----------------------

    private void handleBoardButtonClick(JButton button, int row, int col) {
        if (isCurrentPlayerComputer()) {
            JOptionPane.showMessageDialog(
                    SOSGUIGame.this,
                    "It's the computer's turn.",
                    "Computer Turn",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        cellClicked(button, row, col);
    }

    private void cellClicked(JButton button, int row, int col) {
        if (preMoveValidationFailed(button)) {
            return;
        }

        String symbolToPlace = getCurrentPlayerSymbol();
        Color color = getCurrentPlayerColor();

        applyMoveToButtonAndModel(button, row, col, symbolToPlace);
        handleMoveResult(row, col, symbolToPlace, color);

        runComputerIfNeeded();
    }

    private boolean preMoveValidationFailed(JButton button) {
        if (boardSizeChanged) {
            JOptionPane.showMessageDialog(
                    SOSGUIGame.this,
                    "Board size has been changed, please press New Game button to start the new game",
                    "Start new game",
                    JOptionPane.ERROR_MESSAGE
            );
            return true;
        }
        if (gameModeChanged) {
            JOptionPane.showMessageDialog(
                    SOSGUIGame.this,
                    "Game mode has been changed, please press New Game button to start the new game",
                    "Start new game",
                    JOptionPane.ERROR_MESSAGE
            );
            return true;
        }
        if (gameOver) {
            JOptionPane.showMessageDialog(
                    SOSGUIGame.this,
                    "Game over, please press New Game button to start the new game",
                    "Start new game",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        }
        if (button.getText().contains("S") || button.getText().contains("O")) {
            JOptionPane.showMessageDialog(
                    SOSGUIGame.this,
                    "Please click on an empty square",
                    "Square already filled",
                    JOptionPane.ERROR_MESSAGE
            );
            return true;
        }
        return false;
    }

    private String getCurrentPlayerSymbol() {
        if (sosGame.isBluePlayersTurn()) {
            return blueSRadioButton.isSelected() ? "S" : "O";
        } else {
            return redSRadioButton.isSelected() ? "S" : "O";
        }
    }

    private Color getCurrentPlayerColor() {
        return sosGame.isBluePlayersTurn() ? Color.BLUE : Color.RED;
    }

    private void applyMoveToButtonAndModel(JButton button, int row, int col, String symbol) {
        button.setText(symbol);
        sosGame.makeMove(row, col, symbol);
    }

    private void handleMoveResult(int row, int col, String symbol, Color color) {
        boolean sos = checkSequenceAndDrawLine(row, col, symbol, color);

        if (sosGame.isGameOver()) {
            showGameOverMessage();
        } else if (!sos) {
            toggleTurnAndUpdateLabel();
        }
    }

    private void toggleTurnAndUpdateLabel() {
        sosGame.setBluePlayersTurn(!sosGame.isBluePlayersTurn());
        currentTurnLabel.setText("Current Turn: " +
                (sosGame.isBluePlayersTurn() ? "blue" : "red"));
    }

    // ---------------------- Computer Turn Logic ----------------------

    private boolean isCurrentPlayerComputer() {
        return sosGame.isBluePlayersTurn() ? bluePlayer.isComputer() : redPlayer.isComputer();
    }

    private Player getCurrentPlayer() {
        return sosGame.isBluePlayersTurn() ? bluePlayer : redPlayer;
    }

    private void runComputerIfNeeded() {
        if (gameOver || !isCurrentPlayerComputer()) {
            return;
        }

        if (computerTimer != null && computerTimer.isRunning()) {
            return;
        }

        computerTimer = new Timer(300, null);
        computerTimer.addActionListener(e -> {
            if (gameOver || !isCurrentPlayerComputer()) {
                computerTimer.stop();
                return;
            }
            Player cpu = getCurrentPlayer();
            Move m = cpu.chooseMove(sosGame);
            if (m == null) {
                computerTimer.stop();
                return;
            }
            applyComputerMove(m);
            if (gameOver || !isCurrentPlayerComputer()) {
                computerTimer.stop();
            }
        });
        computerTimer.setRepeats(true);
        computerTimer.start();
    }

    private void applyComputerMove(Move m) {
        int r = m.row;
        int c = m.col;
        String symbol = m.symbol;

        if (buttons[r][c].getText().length() > 0) {
            return; // already occupied
        }

        Color color = getCurrentPlayerColor();
        applyMoveToButtonAndModel(buttons[r][c], r, c, symbol);
        handleMoveResult(r, c, symbol, color);
    }

    private void showGameOverMessage() {
        gameOver = true;
        if (computerTimer != null) {
            computerTimer.stop();
            computerTimer = null;
        }
        String msg =
                sosGame.getBluePlayerSOSCount() == sosGame.getRedPlayerSOSCount()
                        ? "Its a draw"
                        : (sosGame.getBluePlayerSOSCount() > sosGame.getRedPlayerSOSCount()
                        ? "Blue player won!!"
                        : "Red player won!!");
        JOptionPane.showMessageDialog(
                SOSGUIGame.this,
                msg,
                "Game Over!!!",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ---------------------- Modular checkSequenceAndDrawLine ----------------------

    private boolean checkSequenceAndDrawLine(int row, int col, String symbol, Color color) {
        if (symbol.equals("S")) {
            return handleSMoveSequences(row, col, color);
        } else {
            return handleOMoveSequences(row, col, color);
        }
    }

    private boolean handleSMoveSequences(int row, int col, Color color) {
        if (sosGame.checkTopVerticalSOS(row, col)) {
            drawVerticalLine(color, row - 2, col);
            return true;
        }
        if (sosGame.checkBottomVerticalSOS(row, col)) {
            drawVerticalLine(color, row, col);
            return true;
        }
        if (sosGame.checkForwardHorizontalSOS(row, col)) {
            drawHorizontalLine(color, row, col);
            return true;
        }
        if (sosGame.checkBackwardHorizontalSOS(row, col)) {
            drawHorizontalLine(color, row, col - 2);
            return true;
        }
        if (sosGame.checkDiagonalSOSFromBottomLeft(row, col)) {
            drawDiagonalLineFromTopRight(color, row - 2, col + 2);
            return true;
        }
        if (sosGame.checkDiagonalSOSFromBottomRight(row, col)) {
            drawDiagonalLineFromTopLeft(color, row - 2, col - 2);
            return true;
        }
        if (sosGame.checkDiagonalSOSFromTopLeft(row, col)) {
            drawDiagonalLineFromTopLeft(color, row, col);
            return true;
        }
        if (sosGame.checkDiagonalSOSFromTopRight(row, col)) {
            drawDiagonalLineFromTopRight(color, row, col);
            return true;
        }
        return false;
    }

    private boolean handleOMoveSequences(int row, int col, Color color) {
        if (sosGame.checkTopVerticalSOS(row + 1, col)) {
            drawVerticalLine(color, row - 1, col);
            return true;
        }
        if (sosGame.checkBottomVerticalSOS(row - 1, col)) {
            drawVerticalLine(color, row - 1, col);
            return true;
        }
        if (sosGame.checkForwardHorizontalSOS(row, col - 1)) {
            drawHorizontalLine(color, row, col - 1);
            return true;
        }
        if (sosGame.checkBackwardHorizontalSOS(row, col + 1)) {
            drawHorizontalLine(color, row, col - 1);
            return true;
        }
        if (sosGame.checkDiagonalSOSFromBottomLeft(row + 1, col - 1)) {
            drawDiagonalLineFromTopRight(color, row - 1, col + 1);
            return true;
        }
        if (sosGame.checkDiagonalSOSFromBottomRight(row + 1, col + 1)) {
            drawDiagonalLineFromTopLeft(color, row - 1, col - 1);
            return true;
        }
        if (sosGame.checkDiagonalSOSFromTopLeft(row - 1, col - 1)) {
            drawDiagonalLineFromTopLeft(color, row - 1, col - 1);
            return true;
        }
        if (sosGame.checkDiagonalSOSFromTopRight(row - 1, col + 1)) {
            drawDiagonalLineFromTopRight(color, row - 1, col + 1);
            return true;
        }
        return false;
    }

    // ---------------------- Drawing helper methods ----------------------

    public void drawVerticalLine(Color color, int startRow, int col) {
        for (int row = startRow; row < startRow + 3; row++) {
            buttons[row][col].addCenterVerticalLine(color);
        }
    }

    public void drawHorizontalLine(Color color, int row, int startCol) {
        for (int col = startCol; col < startCol + 3; col++) {
            buttons[row][col].addCenterHorizontalLine(color);
        }
    }

    public void drawDiagonalLineFromTopLeft(Color color, int startRow, int startCol) {
        for (int i = 0; i < 3; i++) {
            buttons[startRow++][startCol++].addTopLeftToBottomRightLine(color);
        }
    }

    public void drawDiagonalLineFromTopRight(Color color, int startRow, int startCol) {
        for (int i = 0; i < 3; i++) {
            buttons[startRow++][startCol--].addTopRightToBottomLeftLine(color);
        }
    }

    // ---------------------- CustomButton inner class ----------------------

    private class CustomButton extends JButton {
        private boolean topLeftToBottomRightLine;
        private boolean topRightToBottomLeftLine;
        private boolean centerHorizontalLine;
        private boolean centerVerticalLine;
        private Color topLeftToBottomRightLineColor;
        private Color topRightToBottomLeftLineColor;
        private Color centerHorizontalLineColor;
        private Color centerVerticalLineColor;

        public CustomButton(String text) {
            super(text);
            topLeftToBottomRightLine = false;
            topRightToBottomLeftLine = false;
            centerHorizontalLine = false;
            centerVerticalLine = false;
        }

        public void addTopLeftToBottomRightLine(Color color) {
            topLeftToBottomRightLine = true;
            topLeftToBottomRightLineColor = color;
            repaint();
        }

        public void addTopRightToBottomLeftLine(Color color) {
            topRightToBottomLeftLine = true;
            topRightToBottomLeftLineColor = color;
            repaint();
        }

        public void addCenterHorizontalLine(Color color) {
            centerHorizontalLine = true;
            centerHorizontalLineColor = color;
            repaint();
        }

        public void addCenterVerticalLine(Color color) {
            centerVerticalLine = true;
            centerVerticalLineColor = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (topLeftToBottomRightLine) {
                g.setColor(topLeftToBottomRightLineColor);
                g.drawLine(0, 0, getWidth(), getHeight());
            }
            if (topRightToBottomLeftLine) {
                g.setColor(topRightToBottomLeftLineColor);
                g.drawLine(getWidth(), 0, 0, getHeight());
            }
            if (centerHorizontalLine) {
                g.setColor(centerHorizontalLineColor);
                int y = getHeight() / 2;
                g.drawLine(0, y, getWidth(), y);
            }
            if (centerVerticalLine) {
                g.setColor(centerVerticalLineColor);
                int x = getWidth() / 2;
                g.drawLine(x, 0, x, getHeight());
            }
        }
    }
}
