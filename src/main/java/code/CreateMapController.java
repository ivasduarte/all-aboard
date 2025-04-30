package code;

public class CreateMapController {
    public String createMap(String name, int width, int height){
        MapRepository mapRepository = Repositories.getInstance().getMapRepository();
        Editor editor = ApplicationSession.getInstance().getCurrentSession().getCurrentEditor();

        if (mapRepository.existsByName(name)){
            return "ERROR: Name already exists";
        }

        Map map = new Map(name, width, height);

        if (!map.validateName()){
            return "ERROR: Invalid name";
        }

        if (!map.validateSize()){
            return "ERROR: Invalid size";
        }

        mapRepository.save(map);
        return "Map" + name + "saved successfully.";
    }

}
