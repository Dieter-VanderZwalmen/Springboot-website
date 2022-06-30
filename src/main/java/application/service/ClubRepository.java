package application.service;

import application.model.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Page<Club> findByName(String naam, Pageable pageable);

    Page<Club> findByNameContaining(String naam, Pageable pageable);




    @Query("Select c FROM Club c ORDER BY lower(c.name)")
    Page<Club> findAllSortedNamePage(Pageable pageable);
    // it is here because otherwise the sorting is case-sensitive (Capital first)
    @Query("Select c FROM Club c ORDER BY lower(c.name)")
    Iterable<Club> findAllSortedName();

    @Query("Select c FROM Club c ORDER BY c.maxLeden")
    Page<Club> findAllSortedByMaxLedenPage(Pageable pageable);

    Page<Club> getAllByMaxLedenEquals(Integer parseInt, Pageable pageable);

    Iterable<Club> getAllByMaxLedenEquals(Integer parseInt);

    Page<Club> getAllByMaxLedenAfter(Integer parseInt, Pageable pageable);
}
