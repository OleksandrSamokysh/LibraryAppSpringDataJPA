package aleks.spring.services;

import aleks.spring.models.Book;
import aleks.spring.models.Person;
import aleks.spring.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BooksRepository booksRepository;

    @Autowired
    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(Integer page, Integer booksPerPage, boolean sortByName) {
        if (page != null & booksPerPage != null & sortByName == true) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("title"))).getContent();
        } else if (page != null & booksPerPage != null & sortByName == false) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        } else if (page == null & booksPerPage == null & sortByName == true) {
        return booksRepository.findAll(Sort.by("title"));
        } else {
            return booksRepository.findAll();
        }
    }

    public List<Book> findAllSortByTitle() {
        return booksRepository.findAll(Sort.by("title"));
    }



    public Book findById(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
        book.setLastBookOperationsTime(new Date());
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void addHolder(int id, Person holder) {
        Optional<Book> updatedBook = booksRepository.findById(id);
        updatedBook.ifPresent(book -> book.setHolder(holder));
        updatedBook.ifPresent(book -> book.setLastBookOperationsTime(new Date()));
        updatedBook.ifPresent(bookR -> booksRepository.save(updatedBook.get()));
//        ? или
//        updatedBook.get().setHolder(holder);
//        booksRepository.save(updatedBook.get());

    }

    @Transactional
    public void clearHolder(int id) {
        Optional<Book> updatedBook = booksRepository.findById(id);
        updatedBook.ifPresent(book -> book.setHolder(null));
        updatedBook.ifPresent(book -> book.setLastBookOperationsTime(new Date()));
        updatedBook.ifPresent(bookR -> booksRepository.save(updatedBook.get()));
    }

    public void isOverdue(List<Book> books) {
        long currentTime = new Date().getTime();
            for (Book book:books) {
                if (book.getHolder() != null) {
                    long lastBookOperation = book.getLastBookOperationsTime().getTime();
                    if (currentTime - lastBookOperation > 864000000) {
                        book.setOverdue(true);
                    }
                }
            }
    }

    public List<Book> findByTitleStartingWith(String query) {
        List<Book> books = booksRepository.findByTitleStartingWith(query);
        isOverdue(books);
        return books;
    }
}

