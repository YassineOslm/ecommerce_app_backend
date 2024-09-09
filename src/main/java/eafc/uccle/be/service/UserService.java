package eafc.uccle.be.service;

import eafc.uccle.be.dao.UserAddressRepository;
import eafc.uccle.be.dao.UserRepository;
import eafc.uccle.be.entity.User;
import eafc.uccle.be.entity.UserAddress;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserAddressRepository userAddressRepository;

    public UserService(UserRepository userRepository, UserAddressRepository userAddressRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
    }


    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public Set<UserAddress> getUserAddresses(Long userId) {
        return userAddressRepository.findUserAddressesByUserId(userId);
    }


    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /*public User addUserAddress(Long userId, UserAddress userAddress) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getAddresses().add(userAddress);
            userAddressRepository.save(userAddress);
            return userRepository.save(user);
        }
        return null;
    }*/

    public boolean removeUserAddress(Long userId, Long addressId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<UserAddress> addressOptional = userAddressRepository.findById(addressId);

        if (userOptional.isPresent() && addressOptional.isPresent()) {
            User user = userOptional.get();
            UserAddress address = addressOptional.get();
            user.getAddresses().remove(address);
            userAddressRepository.delete(address);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public User updateUser(Long userId, User userDetails) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstname(userDetails.getFirstname());
            user.setLastname(userDetails.getLastname());
            user.setEmail(userDetails.getEmail());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setGender(userDetails.getGender());
            return userRepository.save(user);
        }
        return null;
    }

    public boolean deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getAddresses().clear();
            userRepository.delete(user);
            return true;
        }
        return false;
    }
}


