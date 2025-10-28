# 회원 관리 클래스 다이어그램

```mermaid
classDiagram
    direction LR

    package "controller" {
        class UserController {
            <<Controller>>
            -UserService userService
            +signUp(UserSignupRequestDTO) ResponseEntity~DTO~
            +login(UserLoginRequestDTO) ResponseEntity~DTO~
            +logout() ResponseEntity~String~
            +updateUserProfile(UserProfileUpdateRequestDTO) ResponseEntity~DTO~
            +getUserProfile() ResponseEntity~DTO~
        }
    }

    package "service.user" {
        class UserService {
            <<Service>>
            -UserRepository userRepository
            -PasswordEncoder passwordEncoder
            -AuthenticationManager authenticationManager
            +signup(UserSignupRequestDTO) User
            +login(UserLoginRequestDTO) UserResponseDTO
            +updateUserProfile(Long, UserProfileUpdateRequestDTO) User
            +getUserProfile(Long) User
        }
    }

    package "security" {
        class CustomUserDetailsService {
            <<Service>>
            -UserRepository userRepository
            +loadUserByUsername(String) UserDetails
        }

        class CustomUserDetails {
            <<UserDetails>>
            -User user
            +getAuthorities() Collection~GrantedAuthority~
            +getPassword() String
            +getUsername() String
        }
    }

    package "config" {
        class SecurityConfig {
            <<Configuration>>
            +filterChain(HttpSecurity) SecurityFilterChain
            +passwordEncoder() PasswordEncoder
            +authenticationManager(AuthConfiguration) AuthenticationManager
        }
    }

    package "domain" {
        class User {
            <<Entity>>
            -Long id
            -String loginId
            -String loginPwd
            -String usersName
            +toResponseDTO() UserResponseDTO
        }
    }

    package "repository" {
        class UserRepository {
            <<Repository>>
            +findByLoginId(String) Optional~User~
        }
    }

    package "dto.user" {
        class UserLoginRequestDTO {<<DTO>>}
        class UserSignupRequestDTO {<<DTO>>}
        class UserProfileUpdateRequestDTO {<<DTO>>}
        class UserResponseDTO {<<DTO>>}
    }

    package "org.springframework.data.jpa.repository" {
        interface JpaRepository<T, ID>
    }
    package "org.springframework.security.core.userdetails" {
        interface UserDetailsService
        interface UserDetails
    }

    %% Relationships
    UserController ..> UserService : uses
    UserController ..> CustomUserDetails : uses
    UserController ..> UserLoginRequestDTO : uses
    UserController ..> UserSignupRequestDTO : uses
    UserController ..> UserProfileUpdateRequestDTO : uses
    
    UserService ..> UserRepository : uses
    UserService ..> User : creates/updates
    UserService ..> UserResponseDTO : creates
    UserService ..> SecurityConfig : uses beans
    
    CustomUserDetailsService ..> UserRepository : uses
    CustomUserDetailsService ..> CustomUserDetails : creates
    CustomUserDetailsService ..|> UserDetailsService : implements

    CustomUserDetails ..|> UserDetails : implements
    CustomUserDetails "1" *-- "1" User : wraps
    
    UserRepository ..|> JpaRepository : extends
    
    User ..> UserResponseDTO : creates
```