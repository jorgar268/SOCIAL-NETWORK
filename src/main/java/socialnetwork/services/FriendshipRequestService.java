package socialnetwork.services;

import socialnetwork.model.FriendshipRequest;
import socialnetwork.model.User;

public interface FriendshipRequestService {
    FriendshipRequest createFriendshipRequest(User sender, User receiver)
        throws FriendshipRequestException;

    void acceptFriendshipRequest(FriendshipRequest request, User receiver)
        throws FriendshipRequestException;

    void declineFriendshipRequest(FriendshipRequest request, User receiver)
        throws FriendshipRequestException;
}