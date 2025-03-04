package nl.novi.event_management_system.services;import jakarta.transaction.Transactional;import jakarta.validation.Valid;import nl.novi.event_management_system.dtos.userDtos.UserCreateDTO;import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;import nl.novi.event_management_system.exceptions.EmailAlreadyExistsException;import nl.novi.event_management_system.exceptions.RecordNotFoundException;import nl.novi.event_management_system.exceptions.UsernameNotFoundException;import nl.novi.event_management_system.mappers.FeedbackMapper;import nl.novi.event_management_system.mappers.TicketMapper;import nl.novi.event_management_system.mappers.UserMapper;import nl.novi.event_management_system.models.*;import nl.novi.event_management_system.repositories.FeedbackRepository;import nl.novi.event_management_system.repositories.TicketRepository;import nl.novi.event_management_system.repositories.UserPhotoRepository;import nl.novi.event_management_system.repositories.UserRepository;import nl.novi.event_management_system.utils.PasswordGenerator;import org.springframework.core.io.Resource;import org.springframework.stereotype.Service;import java.util.*;/** * The UserService class is responsible for handling the business logic of the User entity. */@Servicepublic class UserService {    private final UserRepository userRepository;    private final UserPhotoRepository userPhotoRepository;    private final TicketRepository ticketRepository;    private final FeedbackRepository feedbackRepository;    private final UserPhotoService userPhotoService;    public UserService(UserRepository userRepository, UserPhotoRepository userPhotoRepository, TicketRepository ticketRepository, FeedbackRepository feedbackRepository, UserPhotoService userPhotoService) {        this.userRepository = userRepository;        this.userPhotoRepository = userPhotoRepository;        this.ticketRepository = ticketRepository;        this.feedbackRepository = feedbackRepository;        this.userPhotoService = userPhotoService;    }    /**     * Creates a new user in the system.     *     * @param userCreateDTO the data transfer object containing user details     * @return the response data transfer object containing the created user details     * @throws EmailAlreadyExistsException if a user with the given email already exists     */    public UserResponseDTO createUser(@Valid UserCreateDTO userCreateDTO) {        User user = UserMapper.toUserEntity(userCreateDTO);        String encryptedPwd = PasswordGenerator.generateEncryptedPassword(userCreateDTO.getPassword());        userCreateDTO.setPassword(encryptedPwd);        User newUser;        if (userRepository.existsByEmail(user.getEmail())) {            throw new EmailAlreadyExistsException(user.getEmail());        }        newUser = userRepository.save(user);        return UserMapper.toUserResponseDTO(newUser);    }    /**     * Retrieves all users in the system.     *     * @return a list of response data transfer objects containing user details     */    public List<UserResponseDTO> getAllUsers() {        List<User> users = userRepository.findAll();        return UserMapper.toUserResponseDTOList(users);    }    /**     * Retrieves a user by username.     *     * @param username the username of the user to retrieve     * @return the response data transfer object containing the user details     * @throws UsernameNotFoundException if the user with the given username does not exist     */    @Transactional    public UserResponseDTO getUserByUsername(String username) {        User user = userRepository.findByUsername(username)                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));        UserResponseDTO userResponseDTO = UserMapper.toUserResponseDTO(user);        userResponseDTO.setRoles(new HashSet<>(user.getRoles()));        userResponseDTO.setTickets(user.getTickets() != null ? TicketMapper.toResponseDTOList(user.getTickets()) : Collections.emptyList());        userResponseDTO.setFeedbackList(user.getFeedbackList() != null ? FeedbackMapper.toResponseDTOList(user.getFeedbackList()) : Collections.emptyList());        return userResponseDTO;    }    /**     * Updates a user in the system.     *     * @param username the username of the user to update     * @param newUser  the data transfer object containing the updated user details     * @throws RecordNotFoundException if the user with the given username does not exist     */    public void updateUser(String username, UserCreateDTO newUser) {        if (!userRepository.existsById(username)) throw new RecordNotFoundException();        User user = userRepository.findById(username).get();        String encryptedPwd = PasswordGenerator.generateEncryptedPassword(newUser.getPassword());        user.setPassword(encryptedPwd);        userRepository.save(user);    }    /**     * Deletes a user from the system.     *     * @param username the username of the user to delete     * @throws UsernameNotFoundException if the user with the given username does not exist     */    @Transactional    public void deleteUser(String username) {        if (!userRepository.existsByUsername(username)) {            throw new UsernameNotFoundException("User not found");        }        userRepository.deleteByUsername(username);    }    /**     * Retrieves the roles of a user.     *     * @param username the username of the user to retrieve roles for     * @return a set of roles     * @throws UsernameNotFoundException if the user with the given username does not exist     */    public Set<Role> getUserRoles(String username) {        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);        User user = userRepository.findById(username).get();        UserResponseDTO userResponseDTO = UserMapper.toUserResponseDTO(user);        return userResponseDTO.getRoles();    }    /**     * Adds a role to a user.     *     * @param username the username of the user to add the role to     * @param role     the role to add     * @throws UsernameNotFoundException if the user with the given username does not exist     */    public void addRoleToUser(String username, String role) {        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);        User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));        user.addRole(new Role(username, role));        userRepository.save(user);    }    /**     * Removes a role from a user.     *     * @param username the username of the user to remove the role from     * @param role     the role to remove     * @throws UsernameNotFoundException if the user with the given username does not exist     */    public void deleteUserRole(String username, String role) {        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);        User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));        Role roleToRemove = user.getRoles().stream().filter((a) -> a.getRole().equalsIgnoreCase(role)).findAny().orElseThrow(() -> new RecordNotFoundException("Role not found"));        user.removeRole(roleToRemove);        userRepository.save(user);    }    /**     * Assigns a photo to a user.     *     * @param fileName the name of the photo file     * @param username the username of the user to assign the photo to     * @return the user with the assigned photo     * @throws RecordNotFoundException if the user or photo does not exist     */    public User assignPhotoToUser(String fileName, String username) {        Optional<User> optionalUser = userRepository.findByUsername(username);        Optional<UserPhoto> optionalUserPhoto = userPhotoRepository.findByFileName(fileName);        if (optionalUser.isPresent() && optionalUserPhoto.isPresent()) {            UserPhoto photo = optionalUserPhoto.get();            User user = optionalUser.get();            user.setUserPhoto(photo);            return userRepository.save(user);        } else {            throw new RecordNotFoundException("User or photo not found");        }    }    /**     * Retrieves the photo of a user.     *     * @param username the username of the user to retrieve the photo for     * @return the photo of the user     * @throws RecordNotFoundException if the user does not have a photo     */    @Transactional    public Resource getUserPhoto(String username) {        Optional<User> optionalUser = userRepository.findByUsername(username);        if (optionalUser.isEmpty()) {            throw new RecordNotFoundException("User with username " + username + " not found.");        }        UserPhoto photo = optionalUser.get().getUserPhoto();        if (photo == null) {            throw new RecordNotFoundException("User " + username + " had no photo.");        }        return userPhotoService.downLoadFile(photo.getFileName());    }    /**     * Assigns a ticket to a user.     *     * @param username the username of the user to assign the ticket to     * @param ticketId the ID of the ticket to assign     * @return the user with the assigned ticket     * @throws RecordNotFoundException if the user or ticket does not exist     */    public UserResponseDTO assignTicketToUser(String username, UUID ticketId) {        Optional<User> optionalUser = userRepository.findByUsername(username);        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);        if (optionalUser.isPresent() && optionalTicket.isPresent()) {            User user = optionalUser.get();            Ticket ticket = optionalTicket.get();            List<Ticket> ticketList = new ArrayList<>();            ticketList.add(ticket);            user.setTickets(ticketList);            return UserMapper.toUserResponseDTO(userRepository.save(user));        } else {            throw new RecordNotFoundException("User or ticket not found");        }    }}