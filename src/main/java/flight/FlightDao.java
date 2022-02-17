package flight;

import java.util.List;

public interface FlightDao {
    List<Flight> getAllFlights();
    void updateAllFlights(List<Flight> allFlights);
}
