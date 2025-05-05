package controller;

import domain.ApplicationSession;
import domain.Map;
import domain.UserSession;
import presentation.CreateMapUI;
import repositories.MapRepository;
import repositories.Repositories;

public class CreateMapController {
    public CreateMapUI.MapCreationResult createMap(String name, int width, int height) {
        try {
            UserSession userSession = ApplicationSession.getInstance().getCurrentSession();
            if (userSession == null || userSession.getCurrentEditor() == null) {
                return new CreateMapUI.MapCreationResult("ERROR: No user is currently logged in.", null);
            }

            MapRepository mapRepository = Repositories.getInstance().getMapRepository();
            if (mapRepository.existsByName(name)) {
                return new CreateMapUI.MapCreationResult("ERROR: A map with this name already exists.", null);
            }

            Map map = new Map(name, width, height);
            map.setCreatedBy(userSession.getCurrentEditor());
            mapRepository.add(map);

            return new CreateMapUI.MapCreationResult("Map '" + name + "' created successfully.", map);

        } catch (IllegalArgumentException e) {
            return new CreateMapUI.MapCreationResult("ERROR: " + e.getMessage(), null);
        } catch (Exception e) {
            return new CreateMapUI.MapCreationResult("ERROR: Unexpected error occurred.", null);
        }
    }
}