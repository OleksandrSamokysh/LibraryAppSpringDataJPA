package aleks.spring.services;

import aleks.spring.models.Book;
import aleks.spring.models.Person;
import aleks.spring.repositories.BooksRepository;
import aleks.spring.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;
    private final BookService bookService;


    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository, BookService bookService) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
        this.bookService = bookService;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll(Sort.by("name"));
    }

    public Person findById(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> findByName(String name) {
        Optional<Person> foundPersons = peopleRepository.findByName(name);
        return foundPersons;
    }

    public List<Book> findBooksByPersonId(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        if (foundPerson.isPresent()) {
            Hibernate.initialize(foundPerson.get().getBooks());
            List<Book> books = booksRepository.findByHolder(foundPerson.get(), Sort.by("title"));
            bookService.isOverdue(books);
            return books;
        } else {
            return Collections.emptyList();
        }

    }
}
