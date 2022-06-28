package Server.POJO;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(name = Properties.CUSTOMER_TABLE)
public class Cardholder{
    private static final String str = "Customer ID=%d %s %s %s phone: %d email: %s";
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = Properties.CUSTOMER_ID, nullable = false)
    private long id;

    @NotEmpty(message = "Cardholder name cannot be empty")
    @Size(min = 2, max = 15, message = "Name should be in 2 - 15 symbols")
    @Pattern(regexp="^([^0-9]*)$", message = "Name should contains only chars")
    @Column(name = Properties.CUSTOMER_NAME)
    private String name;

    @NotEmpty(message = "Cardholder surname cannot be empty")
    @Size(min = 2, max = 15, message = "Cardholder surname should be in 2 - 15 symbols")
    @Pattern(regexp="^([^0-9]*)$", message = "Surname should contains only chars")
    @Column (name = Properties.CUSTOMER_SURNAME)
    private String surname;

    @NotEmpty(message = "Cardholder patronymic cannot be empty")
    @Size(min = 2, max = 20, message = "Cardholder patronymic should be in 2 - 20 symbols")
    @Pattern(regexp="^([^0-9]*)$", message = "Patronymic should contains only chars")
    @Column (name = Properties.CUSTOMER_PATRONYMIC)
    private String patronymic;

    @NotNull(message = "Phone number cannot be empty")
    /*@Size(max = 11, min = 11, message = "Phone number should contains 11 nums")*/
    @Column(name = Properties.CUSTOMER_PHONE_NUMBER)
    private Long phoneNumber;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Incorrect email")
    @Column(name = Properties.CUSTOMER_EMAIL)
    private String email;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cardholder",
            targetEntity = Card.class, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    public Cardholder() {
    }

    public Cardholder(String name, String surname, String patronymic, Long phoneNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return String.format(str, id, name, surname, patronymic,
                phoneNumber, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cardholder that = (Cardholder) o;
        return name.equals(that.name) && surname.equals(that.surname) && patronymic.equals(that.patronymic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, patronymic);
    }
}
