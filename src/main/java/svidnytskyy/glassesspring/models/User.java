package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(exclude = "order")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "order")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AuditModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String password;
    String source;
    String role = CustomRole.USER.name();
    //    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
//    @JoinColumn(name = "orderlist")
            List<Order> order = new ArrayList<>();
//    @Column(name = "TimeStamp")
//    Date currentTime = new Date();


    public User(String firstName, String lastName, String phone, List<Order> order) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.order = order;
    }

    public User(String firstName, String lastName, String email, String phone, List<Order> order) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.order = order;
    }

    public User(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public User(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public User(String firstName, String lastName, String email, String phone, String password, String source) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.source = source;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        System.out.println("MY ROLE!!!!" + getRole() + role);
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    private boolean isAccountNonExpired = true;

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    private boolean isAccountNonLocked = true;

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    private boolean isCredentialsNonExpired = true;

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    private boolean isEnabled = true;

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
