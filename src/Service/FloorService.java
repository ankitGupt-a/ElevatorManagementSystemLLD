package Service;

import Model.Floor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FloorService {
    private static final FloorService instance = new FloorService();

    private final Map<Integer, Floor> floorMap;

    private FloorService() {
        this.floorMap = new ConcurrentHashMap<>();
    }

    public static FloorService getInstance() {
        return instance;
    }

    public void addFloor(final int floorNumber) {
        if (floorMap.containsKey(floorNumber)) {
            System.out.println("Floor with floor number is already existed: " + floorNumber);
            return;
        }

        floorMap.put(floorNumber, new Floor(floorNumber));
    }

    public Floor getFloor(final int floor) {
        return floorMap.get(floor);
    }

    public boolean checkFloorNumber(final int floorNumber) {
        return floorMap.containsKey(floorNumber);
    }
}
