package svidnytskyy.glassesspring.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"order"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AuditModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @NotEmpty(message = "required")
    @Size(min = 2, max = 30, message = "size")
    @Pattern(regexp = "^([A-Za-zА-Яа-яІіЇїЄє]+(\\'|\\-|\\.?\\s))*[A-Za-zА-Яа-яІіЇїЄє]+$", message = "pattern")
    String firstName;

    @NotEmpty(message = "required")
    @Size(min = 2, max = 30, message = "size")
    @Pattern(regexp = "^([A-Za-zА-Яа-яІіЇїЄє]+(\\'|\\-|\\.?\\s))*[A-Za-zА-Яа-яІіЇїЄє]+$", message = "pattern")
    String lastName;

    @NotEmpty(message = "required")
    @Email(message = "email")
    String email;

    @NotEmpty(message = "required")
    @Pattern(regexp = "((?:\\+|00)[17](?: |\\-)?|(?:\\+|00)[1-9]\\d{0,2}(?: |\\-)?|(?:\\+|00)1\\-\\d{3}(?: |\\-)?)?(0\\d|\\([0-9]{3}\\)|[1-9]{0,3})(?:((?: |\\-)[0-9]{2}){4}|((?:[0-9]{2}){4})|((?: ?|\\-?)[0-9]{3}(?: ?|\\-?)[0-9]{7})|([0-9]{7}))", message = "pattern")
    String phone;
    String password;
    String source;
    String role = CustomRole.USER.name();
    //        @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true)
//    @JoinColumn(name = "orderlist")
            List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);

    }
//    @Column(name = "TimeStamp")
//    Date currentTime = new Date();


    public User(String firstName,
                String lastName,
                String phone,
                List<Order> orders) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.orders = orders;
    }

    public User(String firstName, String lastName, String email, String phone, List<Order> orders) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.orders = orders;
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
