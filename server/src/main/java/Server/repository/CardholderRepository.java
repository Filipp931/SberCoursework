package Server.repository;

import Server.POJO.Cardholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CardholderRepository extends JpaRepository<Cardholder, Long> {
    @Query("SELECT c FROM Cardholder c WHERE c.name LIKE :name AND c.surname LIKE :surname AND c.patronymic LIKE :patronymic")
    Cardholder getByName(@Param("name") String name, @Param("surname") String surname, @Param("patronymic") String patronymic) ;
    Cardholder getByPhoneNumber(long phoneNumber);
    Boolean existsByNameAndSurnameAndPatronymicAndPhoneNumber(String name, String surname, String patronymic, long phoneNumber);
}
