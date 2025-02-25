package Model;

public class ElevatorDistance implements Comparable<ElevatorDistance>{
    private final Elevator elevator;
    private final int distance;

    public ElevatorDistance(final Elevator elevator, final int distance) {
        this.elevator = elevator;
        this.distance = distance;
    }

    @Override
    public int compareTo(final ElevatorDistance elevatorDistance) {
        return this.distance - elevatorDistance.distance;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public int getDistance() {
        return distance;
    }
}
