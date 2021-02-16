package web.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import web.model.Role.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "getAllUser", query = "SELECT user FROM User user"),
        @NamedQuery(name = "getByEmail", query = "SELECT user FROM User user WHERE user.email = :name"),
})
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;

    public User() {
        // NO-OP
    }

    public User(String firstName, String secondName, String email, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.password = password;
    }

    public void addRole(Role role) {
        /**/
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }

        /**/
        this.roles.add(role);
    }

    public void addAllRole(Set<Role> roles) {
        /**/
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }

        /**/
        this.roles.addAll(roles);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", secondName='").append(secondName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // UserDetails impl
    // ================================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public Collection<String> getAuthorityNames() {
        return roles.stream().map(Role::getAuthority).collect(Collectors.toList());
    }

    public String getAuthorityNamesAsString() {
        return roles.stream().map(Role::getAuthority).collect(Collectors.joining(", "));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO: STUB
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO: STUB
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO: STUB
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO: STUB
        return true;
    }
    // ================================
}
