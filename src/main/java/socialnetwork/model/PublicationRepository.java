package socialnetwork.model;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PublicationRepository extends CrudRepository<Publication, Integer> {
    //List<Publication> findByUserOrderByTimestampDesc(User user);
}