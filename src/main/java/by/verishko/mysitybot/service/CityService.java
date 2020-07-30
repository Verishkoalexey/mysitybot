package by.verishko.mysitybot.service;

import by.verishko.mysitybot.botapi.handlers.fillingprofile.City;
import by.verishko.mysitybot.repository.CityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    @Autowired
    private CityRepo cityRepo;

    public void saveDBCity(City city){
        cityRepo.save(city);
    }
}
