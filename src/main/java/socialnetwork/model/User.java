package socialnetwork.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;

@Entity
public class User {

    @ManyToMany
    @JoinTable(name="friends", joinColumns=@JoinColumn(name = "subject_id"), inverseJoinColumns=@JoinColumn(name = "friend_id"))
    private List<User> friends;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Publication> publications;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, length = 64)
    @NotBlank
    @Size(max = 64)
    private String name;

    @Column(unique = true, nullable = false, length = 64)
    @Email
    @NotBlank
    @Size(max = 64)
    private String email;

    @Lob
    private String description;

    @Column(nullable = false)
    @NotBlank
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User: " + name + " <" + email + ">";
    }

    public List<Publication> getPublications() {
        return publications;
    }
    
    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    public List<User> getFriends(){
        return friends;
    }

    public void setFriends(List<User> friends){
        this.friends = friends;
    }
}
