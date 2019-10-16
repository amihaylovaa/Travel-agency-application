package adelina.luxtravel.domain.transport;

public enum AirplaneClass {
    FIRST, BUSINESS, ECONOMY;

    private int price;

    AirplaneClass(int price) {
        this.price = price;
    }

    AirplaneClass() {
    }
}
