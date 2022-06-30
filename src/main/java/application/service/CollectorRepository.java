package application.service;

import application.model.Club;
import application.model.Collector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CollectorRepository extends JpaRepository<Collector, Long> {
    Iterable<Collector> findByRegio(String regio);
    Iterable<Collector> findAllByRegioContaining(String regio);

    Iterable<Collector> findByName(String name);

    Iterable<Collector> findByFirstname(String firstname);

    @Query("SELECT v FROM Collector v WHERE v.age> :age")
    Iterable<Collector> findVerzamelaarByAge(@Param(value="age") int age);




    Iterable<Collector> findCollectorsByNameAndFirstname(String name, String firstname);


}
