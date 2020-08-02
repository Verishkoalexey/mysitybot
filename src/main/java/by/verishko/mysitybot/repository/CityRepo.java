package by.verishko.mysitybot.repository;

import by.verishko.mysitybot.botapi.handlers.fillingprofile.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CityRepo extends JpaRepository<City,Long> {
    List<City> findByNameContainsIgnoreCase(String city);
}
