package aleks.spring.controllers;

import aleks.spring.models.Book;
import aleks.spring.models.Person;
import aleks.spring.services.BookService;
import aleks.spring.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BooksController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_name", required = false) boolean sortByName) {
        //получаем все книги и передаем на отображение
        model.addAttribute("book", bookService.findAll(page, booksPerPage, sortByName));
        return "book/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person)  {
        // получим одну книгу по id и передадим на отображение + список возможных владельцев
        model.addAttribute("book", bookService.findById(id));
        Person holder = bookService.findById(id).getHolder();
        if (holder != null)
            model.addAttribute("holder", holder);
        else
            model.addAttribute("people", peopleService.findAll());

        return "book/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "book/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book) {
        bookService.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findById(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, @PathVariable("id") int id) {
        bookService.update(id, book);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/book";
    }

    @PatchMapping("/{id}/add")
    public String addHolder(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        bookService.addHolder(id, person);
        return "redirect:/book/" + id;
    }

    @PatchMapping("/{id}/clear")
    public String clearHolder(@PathVariable("id") int id) {
        bookService.clearHolder(id);
        return "redirect:/book/" + id;
    }

    @GetMapping("/search")
    public String searchBook() {
        return "book/search";
    }

    @PostMapping("/search")
    public String searchBook(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", bookService.findByTitleStartingWith(query));
        return "book/search";
    }
}
