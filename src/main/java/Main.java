import domain.ApplicationSession;
import domain.Editor;
import domain.UserSession;
import presentation.CreateMapUI;

public class Main {
    public static void main(String[] args) {
        // Criação do editor e início da sessão
        Editor editor = new Editor("ana", "editor01", "pass123");
        UserSession userSession = new UserSession(editor, null);
        ApplicationSession.getInstance().setUserSession(userSession);

        CreateMapUI ui = new CreateMapUI();
        ui.getInput();
    }
}