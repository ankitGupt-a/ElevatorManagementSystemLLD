package Model;

public class Floor {
    private int floorNumber;

    public Floor(final int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void changeFloorNumber(final int floorNumber) {
        this.floorNumber = floorNumber;
    }
}
