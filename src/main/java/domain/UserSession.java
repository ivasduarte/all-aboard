package domain;

import pt.isep.lei.esoft.auth.domain.model.Email;
import pt.isep.lei.esoft.auth.domain.model.User;
import pt.isep.lei.esoft.auth.mappers.UserRoleMapper;
import pt.isep.lei.esoft.auth.mappers.dto.UserRoleDTO;

import java.util.Collections;
import java.util.List;

public class UserSession {

    private static UserSession instance;

    private User user;

    private Editor currentEditor;
    private Player currentPlayer;

    public UserSession() {
        this.user = null;
        this.currentEditor = null;
        this.currentPlayer = null;
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void doLogin(User user, Editor editor, Player player) {
        if (user == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        }
        this.user = user;
        this.currentEditor = editor;
        this.currentPlayer = player;
    }

    public void doLogout(){
        this.user = null;
        this.currentEditor = null;
        this.currentPlayer = null;
        instance = null;
    }

    public boolean isLoggedIn() {
        return this.user != null;
    }

    public boolean isLoggedInWithRole(String roleId) {
        return this.isLoggedIn() ? this.user.hasRole(roleId) : false;
    }

    public Email getUserId() {
        return this.isLoggedIn() ? this.user.getId() : null;
    }

    public List<UserRoleDTO> getUserRoles() {
        if (this.isLoggedIn()) {
            UserRoleMapper mapper = new UserRoleMapper();
            return mapper.toDTO(this.user.getRoles());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public User getUser() {
        return user;
    }

    public Editor getCurrentEditor() {
        return currentEditor;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentEditor(Editor editor){
        this.currentEditor = editor;
    }

    public void setCurrentPlayer(Player player){
        this.currentPlayer = player;
    }
}
