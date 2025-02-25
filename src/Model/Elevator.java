package Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;

public class Elevator {
    private int currentLoad;
    private String elevatorId;
    private int elevatorNumber;
    private int maxCapacity;
    private Direction direction;
    private Floor floorAt;
    private final Queue<Floor> stoppingFloors;
    private Floor minFloor;
    private Floor maxFloor;
    private Condition elevatorCondition;

    public Elevator(final int elevatorNumber, final int maxCapacity, final Floor floorAt,
                    final Floor minFloor, final Floor maxFloor) {
        this.elevatorId = UUID.randomUUID().toString();
        this.elevatorNumber = elevatorNumber;
        this.maxCapacity = maxCapacity;
        this.direction = Direction.STOP;
        this.floorAt = floorAt;
        this.stoppingFloors = new PriorityQueue<>(Comparator.comparingInt(Floor::getFloorNumber));
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.elevatorCondition = Condition.WORKING;
        this.currentLoad = 0;
    }

    public String getElevatorId() {
        return elevatorId;
    }
    public int getElevatorNumber() {
        return elevatorNumber;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public Direction getDirection() {
        return direction;
    }

    public Floor getFloorAt() {
        return floorAt;
    }

    public Queue<Floor> getStoppingFloors() {
        return stoppingFloors;
    }

    public Floor getNextFloor() {
        return stoppingFloors.poll();
    }

    public Floor getMinFloor() {
        return minFloor;
    }

    public Floor getMaxFloor() {
        return maxFloor;
    }

    public String getCondition() {
        return this.elevatorCondition.value;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void setElevatorNumber(final int elevatorNumber) {
        this.elevatorNumber = elevatorNumber;
    }

    public void setMaxCapacity(final int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    public void setFloorAt(final Floor floorAt) {
        this.floorAt = floorAt;
    }

    public void setMinFloor(final Floor minFloor) {
        this.minFloor = minFloor;
    }

    public void setMaxFloor(final Floor maxFloor) {
        this.maxFloor = maxFloor;
    }

    public void addStoppingFloor(final Floor floor) {
        if(!stoppingFloors.contains(floor)) {
            this.stoppingFloors.add(floor);
        }


    }
    public void changeCondition(final Condition condition) {
        this.elevatorCondition = condition;
    }

    public void increaseCurrentLoad(final int person) {
        this.currentLoad += person;
    }

    public void reduceCurrentLoad(final int person) {
        this.currentLoad -= person;
    }


}
