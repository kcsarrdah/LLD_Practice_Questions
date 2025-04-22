class library{
    string name;
    Address location
    list<BookItem> books;
}

class Address{
    int pinciode;
    string street;
    string state;
    string country;
}

// since there can be multiple copies of a book, 
// we can make a base class of books and extendto to a bookIten class 
// have as many objects of it as we need
// these will be stored in the list<booklist> books


class book{
    String uniqueIdNumber;
    string title;
    List<Author> authors;
    BookType bookType;
}

class BookItem extends Book{

    //unique for each bookItem
    String barcode;
    date publicationDate;

    //class
    Rack rackLocation;

    //enums
    BookStatus bookStatus;
    BookFormat bookFormat;

    date issueDate;
}

public enum bookType{
    SCI_FI, Romantic, Horror, Thriller, Mystery, Fantasy, Non_Fiction
}

public enum BookFormat{
    HARDCOVER, PAPERBACK, EBOOK, NEWSPAPER, JOURNAL, MAGAZINE
}

public enum BookStatus{
    ISSUED, AVAILABLE, CHECKED_OUT, LOST, DAMAGED
}


class Rack{
    int number;
    String locationId;
}

// actors:

//inheritance can be used here

class Person{
    String FirstName;
    String LastName
}

class Author extends Person{
    // all the books published by this author
    List<Book> booksPublished;

}

class SystemUser extends Person{
    String email;
    String phoneNumber;
    Account Account;
}

class Member exrtends SystemUser{

    // since one member at a time can only check out 5 books
    int totalBooksCheckOut;

    // thje search and issue book is a shared feature, 
    // so writing it here would result in code duplication
    Search searchObj;
    BookIssuedService issueService;
}

class Librarian extends SystemUser{
    // can add books, remove books, check out books
    Search searchObj;
    BookIssuedService issueService;

    public void addBookItem(BookItem bookItem){
    public BookItem deleteBookItem(String barcode)
    public EditBookItem editBookItem(BookItem bookItem)
}

class Account{
    String userName;
    String password;
    int accountID;
}

class Search{
    // search by title, author, bookType, bookStatus
    public List<BookItem> getBookByTitle(String title);

    public List<BookItem> getBookByAuthor(Author aurthor);

    public List<BookItem> getBookByBookType(BookType bookType);

    public List<BookItem> getBookByPublicationDate(Date publicationDate);
}

class BookIssuedService{

    // for fines
    FineService fineService;

    public BookReservationDetail getReservationDetail(BookItem, book)
    public void updateReservationDetail(BookReservationDetail bookReservationDetail);

    // reserve or check out book
    public BookReservationDetail reserveBook(BookItem bookItem, SystemUser user);

    // return book
    public void returnBook(BookItem bookItem, SystemUser user);

    // renew book
    public BookIssueDetail renewBook(BookItem bookItem, SystemUser user);
}


class BookLendingClass{
    BookItem book;
    Date startDate;
    SytemUser user;
}

class BookReservationDetail extends BookLending{
    reservationStatus reservationStatus;   
}

class BookIssueDetail extends BookLending{
    Date dueDate;
}

class FineService{
    public Fine calculateFine(BookItem bookItem, SystemUser user, int days)

}

class Fine{
    Date date
    BookItem bookItem;
    SystemUser user;
    Double fineValue
}

