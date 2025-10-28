# 회원 관리 클래스 다이어그램

```mermaid
classDiagram
    direction LR

    class UserController {
        <<Controller>>
        +signUp(UserSignupRequestDTO)
        +login(UserLoginRequestDTO)
        +logout()
        +updateUserProfile(UserProfileUpdateRequestDTO)
        +getUserProfile()
    }

    class UserService {
        <<Service>>
        +signup(UserSignupRequestDTO)
        +login(UserLoginRequestDTO)
        +updateUserProfile(Long, UserProfileUpdateRequestDTO)
        +getUserProfile(Long)
    }

    class CustomUserDetailsService {
        <<Service>>
        +loadUserByUsername(String) : UserDetails
    }

    class CustomUserDetails {
        <<UserDetails>>
        -User user
        +getAuthorities()
        +getPassword()
        +getUsername()
    }

    class SecurityConfig {
        <<Configuration>>
        +filterChain(HttpSecurity)
        +passwordEncoder()
        +authenticationManager(AuthConfiguration)
    }

    class User {
        <<Entity>>
        -Long id
        -String loginId
        -String loginPwd
        -String usersName
        +toResponseDTO() : UserResponseDTO
    }

    class UserRepository {
        <<Repository>>
        +findByLoginId(String) : Optional~User~
    }

    class UserLoginRequestDTO {<<DTO>>}
    class UserSignupRequestDTO {<<DTO>>}
    class UserProfileUpdateRequestDTO {<<DTO>>}
    class UserResponseDTO {<<DTO>>}

    %% Relationships
    UserController ..> UserService : uses
    UserController ..> CustomUserDetails : uses
    UserController ..> UserLoginRequestDTO : uses
    UserController ..> UserSignupRequestDTO : uses
    UserController ..> UserProfileUpdateRequestDTO : uses
    
    UserService ..> UserRepository : uses
    UserService ..> User : uses
    UserService ..> UserResponseDTO : uses
    UserService ..> SecurityConfig : uses
    
    CustomUserDetailsService ..> UserRepository : uses
    CustomUserDetailsService ..> CustomUserDetails : creates
    CustomUserDetailsService ..|> "UserDetailsService" : implements

    CustomUserDetails ..|> "UserDetails" : implements
    CustomUserDetails "1" *-- "1" User : wraps
    
    UserRepository ..|> "JpaRepository" : extends
    
    User ..> UserResponseDTO : creates
```
