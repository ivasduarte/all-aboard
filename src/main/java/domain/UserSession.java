package domain;

import pt.isep.lei.esoft.auth.domain.model.Email;
import pt.isep.lei.esoft.auth.domain.model.User;
import pt.isep.lei.esoft.auth.mappers.UserRoleMapper;
import pt.isep.lei.esoft.auth.mappers.dto.UserRoleDTO;

import java.util.Collections;
import java.util.List;

public class UserSession {
    private User user = null;
    private Editor currentEditor;
    private Player currentPlayer;

    public UserSession() {
        this.user = null;
    }

    public UserSession(User user, Editor currentEditor, Player currentPlayer) {
        if (user == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        }else{
            this.user = user;
        }
        this.currentEditor = currentEditor;
        this.currentPlayer = currentPlayer;
    }

    public boolean isLoggedIn() {
        return this.user != null;
    }

    public boolean isLoggedInWithRole(String roleId) {
        return this.isLoggedIn() ? this.user.hasRole(roleId) : false;
    }

    public String getUserName() {
        if (this.isLoggedIn()) {
            this.user.getName();
        }

        return null;
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

    public void doLogout(){
        this.user = null;
        this.currentEditor = null;
        this.currentPlayer = null;
    }
}
