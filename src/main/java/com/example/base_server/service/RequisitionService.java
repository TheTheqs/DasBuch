package com.example.base_server.service;

import com.example.base_server.client.GoogleBooksClient;
import com.example.base_server.dto.RequisitionDTO;
import com.example.base_server.model.Book;
import com.example.base_server.model.Requisition;
import com.example.base_server.model.User;
import com.example.base_server.repository.RequisitionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequisitionService {

    private final RequisitionRepository requisitionRepository;

    private final EmailSender emailSender;

    private final BookService bookService;

    private final GoogleBooksClient googleBooksClient;

    @Autowired
    public RequisitionService(RequisitionRepository requisitionRepository, EmailSender emailSender, BookService bookService, GoogleBooksClient googleBooksClient) {
        this.requisitionRepository = requisitionRepository;
        this.emailSender = emailSender;
        this.bookService = bookService;
        this.googleBooksClient = googleBooksClient;
    }

    //1- Is there any requisition to be attended?
    public boolean haveRequisition(){
        return requisitionRepository.count() > 0;
    }

    //2- Get the oldest requisition in database.
    private Requisition getOldestRequisition() {
        return requisitionRepository.findOldestRequisition()
                .orElseThrow(() -> new EntityNotFoundException("No requisitions found"));
    }

    //3- Get all requisitions from a user
    public List<Requisition> getAllRequisitionsByUserId(Long id){
        return requisitionRepository.findByUserId(id);
    }

    //4- Save new Requisition
    public Requisition saveRequisition(String title, String author, User user) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        int requisitionLimit = 20;

        if(requisitionRepository.countByUserId(user.getId()) >= requisitionLimit){
            return null;
        }
        Requisition requisition = new Requisition(title, author, user);
        //User notification
        emailSender.sendEmail(requisition.getUser().getEmail(),
                "New Requisition",
                "Dear User: Your new requisition in now in queue. We will be notifying you as soon as your requisition is been attended. Thank you!\n"
                        + new RequisitionDTO(requisition));

        return requisitionRepository.save(requisition);
    }

    //5- Delete requisition by ID
    public void deleteRequisition(Requisition requisition){
        requisitionRepository.deleteById(requisition.getId());
    }

    //6- Populate data bank based on Requisition.
    public String attendRequisition() {
        if(!haveRequisition()){
            return "There is no Requisition to be attended!";
        }
        Requisition requisition = getOldestRequisition();
        List<Book> referredBooks = bookService.getBooksContaining(requisition.getTitle());
        String foundBooks = referredBooks.isEmpty() ?
                "\nWe could not find any book with the referred title in our database :(" :
                "\nWe already have the following books in our database with the referred title: " + String.join("\n", bookService.getBooksTitle(referredBooks));
        if(requisition.getAuthor() == null){
            throw new EntityNotFoundException("Requisition author is null!");
        }
        List<Book> newBooksList = googleBooksClient.fetchBooksByAuthor(requisition.getAuthor());

        String newBooks = newBooksList.isEmpty() ?
                "\nUnfortunately, we could not found new books with the provided author. Please contact our support team for further assistance." :
                "\nThanks to your requisition, the following books were added to our database!\n" + String.join("\n", bookService.getBooksTitle(newBooksList));

        emailSender.sendEmail(
                requisition.getUser().getEmail(),
                "Your request results!",
                "Hello " + requisition.getUser().getName() + ", our system has already processed your requisition ("+ requisition.getTitle() +", " + requisition.getAuthor() +"), here are the results!" +
                foundBooks + newBooks + "\n\nIf the book you are looking is not in this list, please contact our support team for further assistance. \nThank you for your contribution on building a bigger database ^^!");

        requisitionRepository.deleteById(requisition.getId());
        return "Attending to requisition: " + new RequisitionDTO(requisition);
    }

}
