package socialnetwork.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FriendshipRequestRepository extends CrudRepository<FriendshipRequest, Integer> {
    List<FriendshipRequest> findBySenderAndReceiverAndState(User sender, User receiver, FriendshipRequest.State state);
    List<FriendshipRequest> findByReceiverAndState(User receiver, FriendshipRequest.State state);
}