package Service;

import Model.Condition;
import Model.Direction;
import Model.Elevator;
import Model.Floor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElevatorService {
    private static final ElevatorService instance = new ElevatorService();

    private final Map<Integer, Elevator> elevatorMap;

    private ElevatorService() {
        this.elevatorMap = new ConcurrentHashMap<>();
    }

    public static ElevatorService getInstance() {
        return instance;
    }

    public void addElevator(final int elevatorNumber, final int maxCapacity, final Floor floorAt,
                            final Floor minFloor, final Floor maxFloor) {

        if (elevatorMap.containsKey(elevatorNumber)) {
            System.out.println("Elevator with same number is already existed");
            return;
        }

        Elevator elevator = new Elevator(elevatorNumber, maxCapacity, floorAt, minFloor, maxFloor);
        elevatorMap.put(elevatorNumber, elevator);
        System.out.println("Elevator with number " + elevatorNumber + " is added");
    }

    public void changeElevatorCondition(final int elevatorNumber, final Condition condition) {
        if (!elevatorMap.containsKey(elevatorNumber)) {
            System.out.println("Elevator with the number " + elevatorNumber + " is not present");
            return;
        }

        elevatorMap.get(elevatorNumber).changeCondition(condition);
    }

    public void moveToNextFloor(final Elevator elevator) {
        if (!elevatorMap.containsKey(elevator.getElevatorNumber())) {
            System.out.println("Invalid elevator Number: " + elevator.getElevatorNumber());
            return;
        }

        if (elevator.getCurrentLoad()>elevator.getMaxCapacity()) {
            System.out.println("Max Capacity");
            return;
        }

        if (elevator.getStoppingFloors().isEmpty()) {
            elevator.setDirection(Direction.STOP);
        } else {
            Floor nextFloor = elevator.getNextFloor();
            elevator.setFloorAt(nextFloor);
        }
    }

    public void stopElevator(final int elevatorNumber) {
        if (!elevatorMap.containsKey(elevatorNumber)) {
            System.out.println("Invalid elevator Number: " + elevatorNumber);
            return;
        }

        for (Floor floor: elevatorMap.get(elevatorNumber).getStoppingFloors()) {
            System.out.println("Stopping at floor: " + floor.getFloorNumber());
        }

        Elevator elevator = elevatorMap.get(elevatorNumber);
        elevator.setDirection(Direction.STOP);
        elevator.getStoppingFloors().clear();

    }

    public void requestStop(final int elevatorNumber, final Floor floor) {
        if (!elevatorMap.containsKey(elevatorNumber)) {
            System.out.println("Invalid elevator Number: " + elevatorNumber);
            return;
        }

        elevatorMap.get(elevatorNumber).addStoppingFloor(floor);
    }

    public List<Elevator> getAllElevator() {
        return new ArrayList<>(elevatorMap.values());
    }
}
