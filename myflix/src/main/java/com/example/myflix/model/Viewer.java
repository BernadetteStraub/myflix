package com.example.myflix.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Viewer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> authorities;

    public Viewer(String username, String password, Set<String> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public Viewer(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Viewer)) return false;
        Viewer viewer = (Viewer) o;
        return Objects.equals(getId(), viewer.getId()) && Objects.equals(getUsername(), viewer.getUsername()) && Objects.equals(getPassword(), viewer.getPassword()) && Objects.equals(getAuthorities(), viewer.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword(), getAuthorities());
    }
}
