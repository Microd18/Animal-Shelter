package pro.sky.AnimalShelter.entity;

import pro.sky.AnimalShelter.state.ChatStateHolder;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Long chatId;

    private String fullname;
    private String phone;
    private String email;

    /*
    @OneToOne
    @JoinColumn(name = "chat_state_id")
    private ChatState chatState;

     */

    public User() {

    }

    public User(String username, Long chatId) {
        this.username = username;
        this.chatId = chatId;
    }

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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullName) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

/*
    public ChatState getChatState() {
        return chatState;
    }

    public void setChatStateHolder(ChatState chatState) {
        this.chatState = chatState;
    }

 */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && chatId.equals(user.chatId) && Objects.equals(fullname, user.fullname) && Objects.equals(phone, user.phone) && Objects.equals(email, user.email);
    }
    //todo что делать, если в процессе пользования ботом пользователь изменит username?

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    @Override
    public String toString() {
        return "User{id:" + id + " - " + '\'' +
                "username='" + username + '\'' +
                ", chatId='" + chatId + '\'' +
         //       ", chatState='" + chatState + '\'' +
                '}';
    }


}
