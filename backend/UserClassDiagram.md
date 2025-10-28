
# 회원 관리 클래스 다이어그램

```mermaid
classDiagram
    direction LR

    class UserController {
        <<Controller>>
        -UserService userService
        +signUp(UserSignupRequestDTO): ResponseEntity~UserResponseDTO~
        +login(UserLoginRequestDTO, HttpServletRequest): ResponseEntity~UserResponseDTO~
        +logout(HttpServletRequest, HttpServletResponse): ResponseEntity~String~
        +updateUserProfile(UserProfileUpdateRequestDTO): ResponseEntity~UserResponseDTO~
        +getUserProfile(): ResponseEntity~UserResponseDTO~
    }

    class UserService {
        <<Service>>
        -UserRepository userRepository
        -PasswordEncoder passwordEncoder
        -AuthenticationManager authenticationManager
        -SecurityContextRepository securityContextRepository
        +signup(UserSignupRequestDTO): User
        +login(UserLoginRequestDTO, HttpServletRequest): UserResponseDTO
        +updateUserProfile(Long, UserProfileUpdateRequestDTO): User
        +getUserProfile(Long): User
    }

    class CustomUserDetailsService {
        <<Service>>
        -UserRepository userRepository
        +loadUserByUsername(String loginId): UserDetails
    }

    class SecurityConfig {
        <<Configuration>>
        +filterChain(HttpSecurity): SecurityFilterChain
        +passwordEncoder(): PasswordEncoder
        +authenticationManager(AuthenticationConfiguration): AuthenticationManager
        +securityContextRepository(): SecurityContextRepository
    }

    class User {
        <<Entity>>
        -Long id
        -String loginId
        -String loginPwd
        -String usersName
        -LocalDate usersBirthday
        -String gender
        +toResponseDTO(): UserResponseDTO
    }

    class UserRepository {
        <<Repository>>
        +findByLoginId(String loginId): Optional~User~
    }

    class CustomUserDetails {
        <<UserDetails>>
        -User user
        +getAuthorities(): Collection~GrantedAuthority~
        +getPassword(): String
        +getUsername(): String
        +isAccountNonExpired(): boolean
        +isAccountNonLocked(): boolean
        +isCredentialsNonExpired(): boolean
        +isEnabled(): boolean
    }

    class UserLoginRequestDTO {
        <<DTO>>
        -String loginId
        -String loginPwd
    }

    class UserSignupRequestDTO {
        <<DTO>>
        -String loginId
        -String loginPwd
        -String usersName
        -LocalDate usersBirthday
        -String gender
    }

    class UserProfileUpdateRequestDTO {
        <<DTO>>
        -String usersName
        -String usersDescription
        -LocalDate usersBirthday
        -String gender
        -String profileImageUrl
    }

    class UserResponseDTO {
        <<DTO>>
        -Long id
        -String loginId
        -String usersName
        -LocalDate usersBirthday
        -String gender
    }
    
    class JpaRepository {
        <<Interface>>
    }
    
    class UserDetailsService {
        <<Interface>>
    }
    
    class UserDetails {
        <<Interface>>
    }

    ' Packages
    package "controller" {
      UserController
    }
    package "service.user" {
      UserService
    }
    package "security" {
      CustomUserDetailsService
      CustomUserDetails
    }
    package "domain" {
      User
    }
    package "repository" {
      UserRepository
    }
    package "config" {
      SecurityConfig
    }
    package "dto.user" {
      UserLoginRequestDTO
      UserSignupRequestDTO
      UserProfileUpdateRequestDTO
      UserResponseDTO
    }

    ' Relationships
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
    CustomUserDetailsService ..|> UserDetailsService : implements
    CustomUserDetailsService ..> CustomUserDetails : creates

    CustomUserDetails ..|> UserDetails : implements
    CustomUserDetails "1" *-- "1" User : wraps
    
    UserRepository ..|> JpaRepository : extends
    
    User ..> UserResponseDTO : creates
```
