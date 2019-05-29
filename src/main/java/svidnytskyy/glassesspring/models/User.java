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
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString(exclude = {"phoneList"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User/* implements UserDetails */ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String firstName;
    String lastName;
    String email;
//    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Phone> phoneList = new ArrayList<>();
//    List<String> phoneLi = new ArrayList<>();
    String password;
    String source;
    String role = CustomRole.USER.name();
    @Column(name = "TimeStamp")
    Date currentTime = new Date();

    public User(String firstName, String lastName, String email, List<Phone> phoneList,/* List<String> phoneLi,*/ String password, String source) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneList = phoneList;
//        this.phoneLi = phoneLi;
        this.password = password;
        this.source = source;
    }

    public String getNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(role.name()));
//        return null;
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return null;
//    }
//
//    private boolean isAccountNonExpired = true;
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return isAccountNonExpired;
//    }
//
//    private boolean isAccountNonLocked = true;
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return isAccountNonLocked;
//    }
//
//    private boolean isCredentialsNonExpired = true;
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return isCredentialsNonExpired;
//    }
//
//    private boolean isEnabled = true;
//
//    @Override
//    public boolean isEnabled() {
//        return isEnabled;
//    }
}
