package web.model.Role;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
@NamedQueries({
        @NamedQuery(name = "getByName", query = "SELECT role FROM Role role WHERE role.authority = :name"),
        @NamedQuery(name = "getAll", query = "SELECT role FROM Role role"),
})
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String authority;

    public Role() {
        // NO-OP
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != Role.class) return false;
        Role role = (Role) o;
        return Objects.equals(getAuthority(), role.getAuthority());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthority());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Role{");
        sb.append("id=").append(id);
        sb.append(", authority='").append(authority).append('\'');
        sb.append('}');
        return sb.toString();
    }

    // TODO: private static class PreventUpdate{}
    // TODO: @EntityListeners(PreventUpdate.class)

}
