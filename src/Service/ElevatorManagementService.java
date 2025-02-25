package Service;

import Model.Direction;
import Model.Elevator;
import Model.ElevatorDistance;
import Model.Floor;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ElevatorManagementService {
    private static final ElevatorManagementService instance = new ElevatorManagementService();
    private final ElevatorService elevatorService;
    private final FloorService floorService;
    private List<Elevator> allAvailableElevators;
    private final ScheduledExecutorService scheduler;

    private ElevatorManagementService() {
        this.elevatorService = ElevatorService.getInstance();
        this.floorService = FloorService.getInstance();
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::moveElevators, 1, 1, TimeUnit.SECONDS);
    }

    public static ElevatorManagementService getInstance() {
        return instance;
    }


    public synchronized int requestElevator(final Direction direction, final int startingFloor) {
        allAvailableElevators = new CopyOnWriteArrayList<>(elevatorService.getAllElevator());
        Queue<ElevatorDistance> availableElevators = new PriorityQueue<>();

        int d = 0;
        if (direction==Direction.UP) {
            d = 1;
        } else {
            d = -1;
        }

        for (Elevator elevator: allAvailableElevators) {
            if (elevator.getDirection().equals(Direction.UP)) {
                int distance = d*(startingFloor-elevator.getFloorAt().getFloorNumber());
                if (distance>=0 && startingFloor>=elevator.getMinFloor().getFloorNumber() &&
                        startingFloor<=elevator.getMaxFloor().getFloorNumber()) {
                    availableElevators.add(new ElevatorDistance(elevator, distance));
                }
            } else if (elevator.getDirection().equals(Direction.DOWN)) {
                List<Floor> floors = elevator.getStoppingFloors().stream().toList();
                int distance = d*(floors.get(floors.size()-1).getFloorNumber() - startingFloor);

                if (distance>=0 && startingFloor>=elevator.getMinFloor().getFloorNumber() &&
                        startingFloor<=elevator.getMaxFloor().getFloorNumber()) {
                    availableElevators.add(new ElevatorDistance(elevator, distance));
                }
            } else {
                if (startingFloor>=elevator.getMinFloor().getFloorNumber() && startingFloor<=elevator.getMaxFloor().getFloorNumber()) {
                    availableElevators.add(new ElevatorDistance(elevator, Math.abs(startingFloor-elevator.getFloorAt().getFloorNumber())));
                }
            }
        }

        if (availableElevators.isEmpty()) {
            System.out.println("No Elevator is available");
            return -1;
        }

        Elevator elevator = availableElevators.peek().getElevator();
        elevator.addStoppingFloor(floorService.getFloor(startingFloor));

        if (elevator.getDirection()==Direction.STOP) {
            if (startingFloor >= elevator.getFloorAt().getFloorNumber()) {
                elevator.setDirection(Direction.UP);
            } else {
                elevator.setDirection(Direction.DOWN);
            }
        }
        System.out.println("Requested elevator is " + elevator.getElevatorNumber());
        return elevator.getElevatorNumber();
    }

    public void moveElevators() {
        for (Elevator elevator: elevatorService.getAllElevator()) {
            if (elevator.getDirection() != Direction.STOP) {
                System.out.println("Elevator moved to: " + elevator.getFloorAt().getFloorNumber());
                elevatorService.moveToNextFloor(elevator);
            }
        }
    }
}
