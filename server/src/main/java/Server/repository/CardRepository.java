package Server.repository;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c FROM Cardholder c WHERE c.id = (SELECT z.cardholderId FROM Card z WHERE z.id = :id)")
    Cardholder getCardHolder(@Param("id") Long id);
    Card findByNumber(Long number);
    Boolean existsByNumber(Long number);
    List<Card> getAllByExpirationDateBefore(String date);
}
