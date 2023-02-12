package aleks.spring.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Title should not be empty")
    @Size(min =2, max = 30, message = "Title should be between 2 and 30 characters")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Name should not be empty")
    @Size(min =2, max = 30, message = "Name should be between 2 and 30 characters")
    @Column(name = "author")
    private String author;

    @Min(value = 1, message = "Year should not be greater than 0")
    @Column(name = "year")
    private int year;

    @ManyToOne
    @JoinColumn(name = "holder", referencedColumnName = "id")
    private Person holder;

    @Column(name = "time_last_book_operation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastBookOperationsTime;

    @Transient
    private boolean isOverdue;

    public Book() { }

    //нужен ли в конструкторе holder ?
    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getHolder() {
        return holder;
    }

    public void setHolder(Person holder) {
        this.holder = holder;
    }

    public Date getLastBookOperationsTime() {
        return lastBookOperationsTime;
    }

    public void setLastBookOperationsTime(Date lastBookOperationsTime) {
        this.lastBookOperationsTime = lastBookOperationsTime;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
}
