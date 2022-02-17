package passenger;

import java.util.List;

public interface PassengerDao {
    List<Passenger> getAllPassengers();
    void updateAllPassengers(List<Passenger> allPassengers);
}
