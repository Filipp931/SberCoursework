package Server.POJO;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import java.util.Objects;

import static Server.POJO.Properties.*;


@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(name = Properties.CARD_TABLE)
public class Card {

    @Id
    @Column(name = CARD_ID, nullable = false)
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column (name = CARD_HOLDER_ID, insertable = false, updatable = false)
    private Long cardholderId;

    @ManyToOne
    @JoinColumn(name = "cardholder_id")
    private Cardholder cardholder;

    @Column (name = CARD_ISSUE_DATE)
    private String issueDate;

    @Column (name = Properties.CARD_EXPIRATION_DATE, nullable = false)
    private String expirationDate;

    @Column (name = Properties.CARD_NUMBER, nullable = false)
    private Long number;

    public Card() {

    }
    public Card(Long cardholderId, String issueDate, String expirationDate, Long number) {
        this.cardholderId = cardholderId;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.number = number;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cardholder getCardholder() {
        return cardholder;
    }

    public void setCardholder(Cardholder cardholder) {
        this.cardholder = cardholder;
    }

    public Long getId() {
        return id;
    }

    public Long getCardholderId() {
        return cardholderId;
    }

    public void setCardholderId(Long cardholderId) {
        this.cardholderId = cardholderId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Card: {ID = ").append(id).append("; CARDHOLDER_ID = ")
                .append(cardholderId).append("; ISSUE_DATE = ")
                .append(issueDate).append("; EXPIRATION_DATE = ")
                .append(expirationDate).append("; NUMBER = ")
                .append(number).append( "}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, cardholderId);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Card))
            return false;
        if(this == obj)
            return true;
        Card card = (Card) obj;
        return Objects.equals(number, card.number) && Objects.equals(cardholderId, card.cardholderId);
    }
}
