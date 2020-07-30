package by.verishko.mysitybot.repository;

import by.verishko.mysitybot.botapi.handlers.fillingprofile.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepo extends JpaRepository<City,Long> {

}
