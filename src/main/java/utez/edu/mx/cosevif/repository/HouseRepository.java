package utez.edu.mx.cosevif.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import utez.edu.mx.cosevif.model.House;

public interface HouseRepository extends MongoRepository<House, String> {


}
