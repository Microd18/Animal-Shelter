package pro.sky.AnimalShelter.entity;

import pro.sky.AnimalShelter.enums.BotCommand;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "chat_state")
public class ChatState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;
    private BotCommand twoStepBackState;

    private BotCommand stepBackState;
    private BotCommand currentState;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ChatState() {

    }

    public ChatState(Long chatId, BotCommand twoStepBackState, BotCommand stepBackState, BotCommand currentState) {
        this.chatId = chatId;
        this.twoStepBackState = twoStepBackState;
        this.stepBackState = stepBackState;
        this.currentState = currentState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public BotCommand getTwoStepBackState() {
        return twoStepBackState;
    }

    public void setTwoStepBackState(BotCommand twoStepBackState) {
        this.twoStepBackState = twoStepBackState;
    }

    public BotCommand getStepBackState() {
        return stepBackState;
    }

    public void setStepBackState(BotCommand stepBackState) {
        this.stepBackState = stepBackState;
    }

    public BotCommand getCurrentState() {
        return currentState;
    }

    public void setCurrentState(BotCommand currentState) {
        this.currentState = currentState;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clearStates() {
        this.currentState = null;
        this.stepBackState = null;
        this.twoStepBackState = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatState chatState = (ChatState) o;
        return chatId.equals(chatState.chatId) && Objects.equals(twoStepBackState, chatState.twoStepBackState) && Objects.equals(stepBackState, chatState.stepBackState) && currentState.equals(chatState.currentState) && Objects.equals(user, chatState.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, twoStepBackState, stepBackState, currentState, user);
    }

    @Override
    public String toString() {
        return "Состояние чата " + id + ":" + '\'' +
                "2 шага назад - " + twoStepBackState + ", " +
                "шаг назад - " + stepBackState + ", " +
                "текущее - " + currentState;
    }


}
