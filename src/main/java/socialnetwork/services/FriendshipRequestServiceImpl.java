package socialnetwork.services;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import socialnetwork.model.FriendshipRequest;
import socialnetwork.model.FriendshipRequestRepository;
import socialnetwork.model.User;
import socialnetwork.model.FriendshipRequest.State;

@Service
public class FriendshipRequestServiceImpl implements FriendshipRequestService {

    @Autowired
    private FriendshipRequestRepository friendshipRequestRepository;

    @Override
    public FriendshipRequest createFriendshipRequest(User sender, User receiver)
        throws FriendshipRequestException {
        if (sender.getFriends().contains(receiver)) {
            throw new FriendshipRequestException("Users are friends already");
        } else if (!friendshipRequestRepository
                    .findBySenderAndReceiverAndState(sender, receiver, FriendshipRequest.State.OPEN).isEmpty()) {
            throw new FriendshipRequestException("A pending request exists");
        } else if (!friendshipRequestRepository
                    .findBySenderAndReceiverAndState(receiver, sender, FriendshipRequest.State.OPEN).isEmpty()) {
            throw new FriendshipRequestException("A pending request exists");
        }
        FriendshipRequest request = new FriendshipRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setCreationTimestamp(new Date());
        request.setState(State.OPEN);
        friendshipRequestRepository.save(request);
        return request;
    }

    @Override
    public void acceptFriendshipRequest(FriendshipRequest request, User receiver)
        throws FriendshipRequestException {
            if (request.getState()!=State.OPEN && request.getReceiver()!=receiver){
                throw new FriendshipRequestException("Error en la solicitud");
            }else{

                request.setState(State.ACCEPTED);
                request.setReceiver(receiver);
                
                User sender = request.getSender();
                sender.getFriends().add(receiver);
                
                receiver.getFriends().add(sender);
                
                friendshipRequestRepository.save(request);
            } 
    }

    @Override
    public void declineFriendshipRequest(FriendshipRequest request, User receiver)
        throws FriendshipRequestException {
            if (request.getState()!=State.OPEN && request.getReceiver()!=receiver){
                throw new FriendshipRequestException("Error en la solicitud");
            }else{
                request.setState(State.DECLINED);
                request.setReceiver(receiver);
                friendshipRequestRepository.save(request);
            } 
    }
}