package com.example.mvctest.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
  @Id
  @Column(name = "users_id", nullable = false)
  private Long id;

  @Size(max = 255)
  @NotNull
  @Column(name = "login_id", nullable = false)
  private String loginId;

  @Size(max = 255)
  @NotNull
  @Column(name = "login_pwd", nullable = false)
  private String loginPwd;

  @NotNull
  @Column(name = "point", nullable = false)
  private Long point;

  @Size(max = 255)
  @NotNull
  @Column(name = "users_name", nullable = false)
  private String usersName;

  @Size(max = 255)
  @Column(name = "users_description")
  private String usersDescription;

  @Column(name = "users_birthday")
  private LocalDate usersBirthday;

  @OneToMany(mappedBy = "user")
  private List<Gamemate> gameMates=new ArrayList<Gamemate>();

}