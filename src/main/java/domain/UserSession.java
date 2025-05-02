package domain;

public class UserSession {
    private Editor currentEditor;
    private Player currentPlayer;

    public UserSession(Editor currentEditor, Player currentPlayer) {
        this.currentEditor = currentEditor;
        this.currentPlayer = currentPlayer;
    }

    public Editor getCurrentEditor() {
        return currentEditor;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
