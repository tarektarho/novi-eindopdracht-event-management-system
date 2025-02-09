````mermaid

sequenceDiagram
participant User
participant UserController
participant UserService
participant UserRepository
participant Database
participant UserMapper

    User ->> UserController: POST /api/v1/users/create (Create User)
    UserController ->> UserService: createUser(userCreateDTO)
    UserService ->> UserRepository: save(user)
    UserRepository ->> Database: Store User
    Database -->> UserRepository: User
    UserRepository -->> UserService: User
    UserService ->> UserMapper: toResponseDTO(user)
    UserMapper -->> UserService: UserResponseDTO
    UserService -->> UserController: UserResponseDTO
    UserController -->> User: 201 Created

    User ->> UserController: GET /api/v1/users/{username} (Retrieve User)
    UserController ->> UserService: getUser(username)
    UserService ->> UserRepository: findByUsername(username)
    UserRepository ->> Database: Query User by Username
    Database -->> UserRepository: User
    UserRepository -->> UserService: User
    UserService ->> UserMapper: toResponseDTO(user)
    UserMapper -->> UserService: UserResponseDTO
    UserService -->> UserController: UserResponseDTO
    UserController -->> User: 200 OK

    User ->> UserController: PUT /api/v1/users/{username} (Update User)
    UserController ->> UserService: updateUser(username, userCreateDTO)
    UserService ->> UserRepository: findByUsername(username)
    UserRepository -->> UserService: User
    alt User found
        UserService ->> UserRepository: save(updatedUser)
        UserRepository ->> Database: Update User
        Database -->> UserRepository: Updated User
        UserRepository -->> UserService: User
        UserService ->> UserMapper: toResponseDTO(updatedUser)
        UserMapper -->> UserService: UserResponseDTO
        UserService -->> UserController: UserResponseDTO
        UserController -->> User: 200 OK
    else User not found
        UserController -->> User: 404 Not Found
    end

    User ->> UserController: DELETE /api/v1/users/{username} (Delete User)
    UserController ->> UserService: deleteUser(username)
    UserService ->> UserRepository: findByUsername(username)
    UserRepository -->> UserService: User
    alt User found
        UserService ->> UserRepository: deleteByUsername(username)
        UserRepository ->> Database: Delete User
        UserService -->> UserController: 204 No Content
        UserController -->> User: 204 No Content
    else User not found
        UserController -->> User: 404 Not Found
    end
````