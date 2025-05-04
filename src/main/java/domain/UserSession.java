package domain;

import pt.isep.lei.esoft.auth.domain.model.User;

public class UserSession {
    private User user = null;
    private Editor currentEditor;
    private Player currentPlayer;

    public UserSession(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        } else {
            this.user = user;
        }
    }

    public UserSession(User user, Editor currentEditor, Player currentPlayer) {
        this.user = null;
        this.currentEditor = currentEditor;
        this.currentPlayer = currentPlayer;
    }

    public boolean isLoggedIn() {
        return this.user != null;
    }

    public Editor getCurrentEditor() {
        return currentEditor;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void doLogout(){
        this.currentEditor = null;
        this.currentPlayer = null;
    }
}
