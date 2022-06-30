package application.service;

import application.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    Page<Collection> findCollectionsByLand(String land, Pageable pageable);
    Iterable<Collection> findCollectionsSortedByLand(String land);
    Page<Collection> findCollectionsByJaartal(int jaartal, Pageable pageable);
    Iterable<Collection> findMuntCollectiesByJaartal(int jaartal);
    Page<Collection> findCollectionsByJaartalBefore(int jaartal, Pageable pageable);

    //deze functie zorgt ervoor dat als je sorteert op een kolom dat je geen fout krijgt
    //en dat de paginatie hier ook blijft werken
    Page<Collection> findAll(Pageable pageable);

    Iterable<Collection> findAllByOrderByLand();
}
