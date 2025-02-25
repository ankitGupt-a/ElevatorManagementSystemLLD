import Model.Direction;
import Model.Floor;
import Service.ElevatorManagementService;
import Service.ElevatorService;
import Service.FloorService;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Driver {
    private static final ElevatorManagementService elevatorManagementService = ElevatorManagementService.getInstance();
    private static final ElevatorService elevatorService = ElevatorService.getInstance();
    private static final FloorService floorService = FloorService.getInstance();

    public static void main(String[] args) {
        initializeSystem();

        // Run different test scenarios
        try {
            System.out.println("\n===== RUNNING TEST SCENARIOS =====\n");

            // Test basic elevator request
            testBasicElevatorRequest();
            TimeUnit.SECONDS.sleep(5);

            // Test multiple elevator requests
            testMultipleElevatorRequests();
            TimeUnit.SECONDS.sleep(5);

            // Test capacity handling
            testCapacityHandling();
            TimeUnit.SECONDS.sleep(5);

            // Test direction changes
            testDirectionChanges();
            TimeUnit.SECONDS.sleep(5);

            // Test interactive mode
//            runInteractiveMode();

        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        }
    }

    private static void initializeSystem() {
        System.out.println("===== INITIALIZING ELEVATOR SYSTEM =====");

        // Initialize floors (0 to 10)
        for (int i = 0; i <= 10; i++) {
            floorService.addFloor(i);
        }

        // Get floor references
        Floor groundFloor = floorService.getFloor(0);
        Floor topFloor = floorService.getFloor(10);

        // Add 4 elevators with different characteristics
        elevatorService.addElevator(1, 8, groundFloor, groundFloor, topFloor);  // Standard elevator
        elevatorService.addElevator(2, 15, groundFloor, groundFloor, topFloor); // Higher capacity
        elevatorService.addElevator(3, 5, groundFloor, groundFloor, topFloor);  // Lower capacity
        elevatorService.addElevator(4, 10, groundFloor, groundFloor, topFloor); // Medium capacity

        System.out.println("System initialized with 11 floors (0-10) and 4 elevators\n");
    }

    private static void testBasicElevatorRequest() {
        System.out.println("===== TEST 1: BASIC ELEVATOR REQUEST =====");

        // Request an elevator going up from floor 3
        System.out.println("Requesting an elevator going UP from floor 3");
        int elevator = elevatorManagementService.requestElevator(Direction.UP, 3);

        if (elevator != -1) {
            System.out.println("Elevator #" + elevator + " assigned");

            // Request to go to floor 7
            System.out.println("Passenger enters and requests floor 7");
            elevatorService.requestStop(elevator, floorService.getFloor(7));

            // Wait for movement simulation
            System.out.println("Waiting for elevator to move...");
        } else {
            System.out.println("No elevator was assigned");
        }
    }

    private static void testMultipleElevatorRequests() {
        System.out.println("\n===== TEST 2: MULTIPLE ELEVATOR REQUESTS =====");

        // Multiple requests from different floors
        System.out.println("Simulating multiple requests from different floors");

        int elevator1 = elevatorManagementService.requestElevator(Direction.UP, 2);
        System.out.println("Request UP from floor 2 - Assigned elevator #" + elevator1);
        elevatorService.requestStop(elevator1, floorService.getFloor(8));

        int elevator2 = elevatorManagementService.requestElevator(Direction.DOWN, 9);
        System.out.println("Request DOWN from floor 9 - Assigned elevator #" + elevator2);
        elevatorService.requestStop(elevator2, floorService.getFloor(1));

        int elevator3 = elevatorManagementService.requestElevator(Direction.UP, 4);
        System.out.println("Request UP from floor 4 - Assigned elevator #" + elevator3);
        elevatorService.requestStop(elevator3, floorService.getFloor(10));

        System.out.println("Waiting for elevators to move...");
    }

    private static void testCapacityHandling() {
        System.out.println("\n===== TEST 3: CAPACITY HANDLING =====");

        // Get an elevator with low capacity (elevator #3 with capacity 5)
        int elevator = 3;

        System.out.println("Testing capacity handling for elevator #" + elevator + " (capacity: 5)");
        System.out.println("Simulating 4 people entering elevator");

        // Simulate people entering
        for (int i = 0; i < 4; i++) {
            elevatorService.getAllElevator().stream()
                    .filter(e -> e.getElevatorNumber() == elevator)
                    .findFirst()
                    .ifPresent(e -> e.increaseCurrentLoad(1));
        }

        // Request a floor
        elevatorService.requestStop(elevator, floorService.getFloor(5));
        System.out.println("Requested floor 5");

        // Add more people to exceed capacity
        System.out.println("Adding 2 more people (exceeding capacity)");
        elevatorService.getAllElevator().stream()
                .filter(e -> e.getElevatorNumber() == elevator)
                .findFirst()
                .ifPresent(e -> e.increaseCurrentLoad(2));

        // Try to move the elevator
        System.out.println("Attempting to move the elevator (should show capacity warning)");
        elevatorService.getAllElevator().stream()
                .filter(e -> e.getElevatorNumber() == elevator)
                .findFirst()
                .ifPresent(elevatorService::moveToNextFloor);

        // Reduce load and try again
        System.out.println("Reducing load by 1 person");
        elevatorService.getAllElevator().stream()
                .filter(e -> e.getElevatorNumber() == elevator)
                .findFirst()
                .ifPresent(e -> e.reduceCurrentLoad(1));

        System.out.println("Attempting to move again (should work now)");
        elevatorService.getAllElevator().stream()
                .filter(e -> e.getElevatorNumber() == elevator)
                .findFirst()
                .ifPresent(elevatorService::moveToNextFloor);
    }

    private static void testDirectionChanges() {
        System.out.println("\n===== TEST 4: DIRECTION CHANGES =====");

        // Get elevator #2
        int elevator = 2;

        System.out.println("Testing direction changes for elevator #" + elevator);

        // Set initial state - going UP from floor 2
        System.out.println("Setting initial state: elevator at floor 2, going UP");
        elevatorService.getAllElevator().stream()
                .filter(e -> e.getElevatorNumber() == elevator)
                .findFirst()
                .ifPresent(e -> {
                    e.setFloorAt(floorService.getFloor(2));
                    e.setDirection(Direction.UP);
                    e.getStoppingFloors().clear();
                });

        // Add multiple stops in both directions
        System.out.println("Adding stops at floors 5, 8, and then 3 (requiring direction change)");
        elevatorService.requestStop(elevator, floorService.getFloor(5));
        elevatorService.requestStop(elevator, floorService.getFloor(8));
        elevatorService.requestStop(elevator, floorService.getFloor(3));

        System.out.println("Elevator should go up to 5 and 8, then change direction to go down to 3");
        System.out.println("Waiting for movements...");
    }

    private static void runInteractiveMode() {
        System.out.println("\n===== INTERACTIVE TEST MODE =====");
        System.out.println("You can now interactively test the elevator system.");
        System.out.println("Available commands:");
        System.out.println("  request <direction: UP/DOWN> <floor>");
        System.out.println("  stop <elevator> <floor>");
        System.out.println("  move");
        System.out.println("  status");
        System.out.println("  exit");

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("\nEnter command: ");
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] parts = input.split("\\s+");
            try {
                switch (parts[0].toLowerCase()) {
                    case "request":
                        Direction direction = Direction.valueOf(parts[1].toUpperCase());
                        int floor = Integer.parseInt(parts[2]);
                        int assigned = elevatorManagementService.requestElevator(direction, floor);
                        System.out.println("Assigned elevator: #" + assigned);
                        break;

                    case "stop":
                        int elevator = Integer.parseInt(parts[1]);
                        int stopFloor = Integer.parseInt(parts[2]);
                        elevatorService.requestStop(elevator, floorService.getFloor(stopFloor));
                        System.out.println("Stop requested at floor " + stopFloor + " for elevator #" + elevator);
                        break;

                    case "move":
                        elevatorManagementService.moveElevators();
                        System.out.println("Moved all elevators");
                        break;

                    case "status":
                        System.out.println("=== Elevator Status ===");
                        elevatorService.getAllElevator().forEach(e -> {
                            System.out.println("Elevator #" + e.getElevatorNumber() +
                                    " at floor " + e.getFloorAt().getFloorNumber() +
                                    ", direction: " + e.getDirection() +
                                    ", load: " + e.getCurrentLoad() + "/" + e.getMaxCapacity());
                            System.out.print("   Stopping floors: ");
                            e.getStoppingFloors().forEach(f -> System.out.print(f.getFloorNumber() + " "));
                            System.out.println();
                        });
                        break;

                    default:
                        System.out.println("Unknown command");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Usage examples:");
                System.out.println("  request UP 3");
                System.out.println("  stop 2 7");
            }
        }

        System.out.println("Exiting interactive mode");
        scanner.close();
    }
}




