package aleks.spring.repositories;

import aleks.spring.models.Book;
import aleks.spring.models.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByHolder(Person p, Sort var1);
    List<Book> findByTitleStartingWith(String title);
}
