package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    MessageRepository messageRepository;
    AccountRepository accountRepository;
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message addMessage(Message message) {
        if (message.getMessageText().isBlank() || message.getMessageText().length() >= 255) {
            throw new IllegalArgumentException("Message text cannot be blank or greater than 255 characters");
        }
        if (!this.accountRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("User does not exist");
        }
        return messageRepository.save(message);
    }

    public Iterable<Message> getAllMessages(){
        return this.messageRepository.findAll();
    }

    public Message getMessageById(int id){
        return messageRepository.findById(id).orElse(null);
        
    }

    public int deleteMessageById(int id){
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1;
        } 
        else {
            return 0;
        }
    }

    public List<Message> getMessagesByPostedBy(int id){
        return messageRepository.findByPostedBy(id);
    }

    public int updateMessage(int message_id, String message_text){
        Message existingMsg = getMessageById(message_id);
        if(message_text.isBlank() || message_text.length() > 255 || getMessageById(message_id) == null){
            return 0;
        }
        existingMsg.setMessageText(message_text);
        messageRepository.save(existingMsg);
        return 1;
    }
    

}

