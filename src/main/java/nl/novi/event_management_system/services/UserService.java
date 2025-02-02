package nl.novi.event_management_system.services;import jakarta.transaction.Transactional;import jakarta.validation.Valid;import nl.novi.event_management_system.dtos.FeedbackDTO;import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;import nl.novi.event_management_system.dtos.userDtos.UserCreateDTO;import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;import nl.novi.event_management_system.exceptions.EmailAlreadyExistsException;import nl.novi.event_management_system.exceptions.RecordNotFoundException;import nl.novi.event_management_system.exceptions.UsernameNotFoundException;import nl.novi.event_management_system.mappers.FeedbackMapper;import nl.novi.event_management_system.mappers.TicketMapper;import nl.novi.event_management_system.mappers.UserMapper;import nl.novi.event_management_system.models.*;import nl.novi.event_management_system.repositories.FeedbackRepository;import nl.novi.event_management_system.repositories.TicketRepository;import nl.novi.event_management_system.repositories.UserPhotoRepository;import nl.novi.event_management_system.repositories.UserRepository;import nl.novi.event_management_system.utils.PasswordGenerator;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.core.io.Resource;import org.springframework.stereotype.Service;import java.util.*;@Servicepublic class UserService {    @Autowired    private UserRepository userRepository;    @Autowired    private UserPhotoRepository userPhotoRepository;    @Autowired    private UserPhotoService userPhotoService;    @Autowired    private TicketRepository ticketRepository;    @Autowired    private FeedbackRepository feedbackRepository;    public String createUser(@Valid UserCreateDTO userCreateDTO) {        User user = UserMapper.toUserEntity(userCreateDTO);  // Convert UserCreateDTO to User entity        String encryptedPwd = PasswordGenerator.generateEncryptedPassword(userCreateDTO.getPassword());        userCreateDTO.setPassword(encryptedPwd);        User newUser;        try {            newUser = userRepository.save(user);        } catch (EmailAlreadyExistsException ex) {            throw new EmailAlreadyExistsException(userCreateDTO.getEmail());        }        return newUser.getUsername();    }    public List<UserResponseDTO> getAllUsers() {        List<User> users = userRepository.findAll();        return UserMapper.toUserResponseDTOList(users);    }    @Transactional    public UserResponseDTO getUserByUsername(String username) {        UserResponseDTO userResponseDTO = new UserResponseDTO();        // Fetch the user from the database (or other data source)        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));        if (user != null) {            userResponseDTO.setUsername(user.getUsername());            userResponseDTO.setPassword(user.getPassword());            userResponseDTO.setEnabled(user.getEnabled());            Set<Role> roles = user.getRoles();            // Ensure that roles is not null, and initialize it if necessary            if (roles == null || roles.isEmpty()) {                roles = new HashSet<>(); // Default to an empty set if no roles are assigned            }            userResponseDTO.setRoles(roles);            userResponseDTO.setUserPhoto(user.getUserPhoto());            if (user.getTickets() != null) {                userResponseDTO.setTickets(TicketMapper.toResponseDTOList(user.getTickets()));            }            if (user.getFeedbackList() != null) {                userResponseDTO.setFeedbackList(FeedbackMapper.toResponseDTOList(user.getFeedbackList()));            }        }        return userResponseDTO;    }    public void updateUser(String username, UserCreateDTO newUser) {        if (!userRepository.existsById(username)) throw new RecordNotFoundException();        User user = userRepository.findById(username).get();        String encryptedPwd = PasswordGenerator.generateEncryptedPassword(newUser.getPassword());        user.setPassword(encryptedPwd);        userRepository.save(user);    }    public void deleteUser(String username) {        if (!userRepository.existsByUsername(username)) {            throw new UsernameNotFoundException("User not found");        }        userRepository.deleteByUsername(username);    }    public Set<Role> getRoles(String username) {        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);        User user = userRepository.findById(username).get();        UserResponseDTO userResponseDTO = UserMapper.toUserResponseDTO(user);        return userResponseDTO.getRoles();    }    public void addRole(String username, String role) {        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);        User user = userRepository.findById(username).get();        user.addRole(new Role(username, role));        userRepository.save(user);    }    public void removeRole(String username, String role) {        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);        User user = userRepository.findById(username).get();        Role roleToRemove = user.getRoles().stream().filter((a) -> a.getRole().equalsIgnoreCase(role)).findAny().get();        user.removeRole(roleToRemove);        userRepository.save(user);    }    public User assignPhotoToUser(String fileName, String username) {        Optional<User> optionalUser = userRepository.findByUsername(username);        Optional<UserPhoto> optionalUserPhoto = userPhotoRepository.findByFileName(fileName);        if (optionalUser.isPresent() && optionalUserPhoto.isPresent()) {            UserPhoto photo = optionalUserPhoto.get();            User user = optionalUser.get();            user.setUserPhoto(photo);            return userRepository.save(user);        } else {            throw new RecordNotFoundException("User or photo not found");        }    }    @Transactional    public Resource getPhotoFromUser(String username) {        Optional<User> optionalUser = userRepository.findByUsername(username);        if (optionalUser.isEmpty()) {            throw new RecordNotFoundException("User with username " + username + " not found.");        }        UserPhoto photo = optionalUser.get().getUserPhoto();        if (photo == null) {            throw new RecordNotFoundException("User " + username + " had no photo.");        }        return userPhotoService.downLoadFile(photo.getFileName());    }    public UserResponseDTO assignTicketToUser(String username, UUID ticketId) {        Optional<User> optionalUser = userRepository.findByUsername(username);        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);        if(optionalUser.isPresent() && optionalTicket.isPresent()) {            User user = optionalUser.get();            Ticket ticket = optionalTicket.get();            List<Ticket> ticketList = new ArrayList<>();            ticketList.add(ticket);            user.setTickets(ticketList);            return UserMapper.toUserResponseDTO(userRepository.save(user));        } else {            throw new RecordNotFoundException("User or ticket not found");        }    }    public UserResponseDTO assignFeedbackToUser(String username, UUID feedbackId) {        User user = userRepository.findByUsername(username).orElseThrow(UsernameNotFoundException::new);        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(RecordNotFoundException::new);        List<Feedback> feedbackList = new ArrayList<>();        feedbackList.add(feedback);        user.setFeedbackList(feedbackList);        return UserMapper.toUserResponseDTO(userRepository.save(user));    }}