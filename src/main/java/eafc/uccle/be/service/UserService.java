package eafc.uccle.be.service;

import eafc.uccle.be.dao.LivesRepository;
import eafc.uccle.be.dao.RoleRepository;
import eafc.uccle.be.dao.UserAddressRepository;
import eafc.uccle.be.dao.UserRepository;
import eafc.uccle.be.dto.UserDto;
import eafc.uccle.be.entity.Lives;
import eafc.uccle.be.entity.Role;
import eafc.uccle.be.entity.User;
import eafc.uccle.be.entity.UserAddress;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserAddressRepository userAddressRepository;
    private final RoleRepository roleRepository;
    private final LivesRepository livesRepository;

    public UserService(UserRepository userRepository, UserAddressRepository userAddressRepository, RoleRepository roleRepository, LivesRepository livesRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.roleRepository = roleRepository;
        this.livesRepository = livesRepository;
    }


    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public Set<UserAddress> getUserAddresses(Long userId) {
        return userAddressRepository.findUserAddressesByUserId(userId);
    }


    @Transactional
    public User createUser(UserDto userDto) {

        User existingUser = userRepository.findUserByEmail(userDto.getEmail());

        if (existingUser == null) {
            User newUser = new User();
            newUser.setFirstname(userDto.getFirstname());
            newUser.setLastname(userDto.getLastname());
            newUser.setEmail(userDto.getEmail());
            newUser.setDateCreated(new Date());
            newUser.setLastUpdated(new Date());
            newUser.setRole( (Objects.equals(userDto.getRole(), "admin")) ? roleRepository.findRoleById(1L) : roleRepository.findRoleById(2L) );
            return userRepository.save(newUser);
        } else {
            return existingUser;
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Transactional
    public UserAddress addAddressToUser(Long idUser, UserAddress address) {
        User user = userRepository.findById(idUser).orElse(null);
        UserAddress savedAddress = userAddressRepository.save(address);
        Lives lives = new Lives();
        lives.setUser(user);
        lives.setUserAddress(savedAddress);
        livesRepository.save(lives);
        return savedAddress;
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


