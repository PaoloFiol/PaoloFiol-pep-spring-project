package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;

    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        try {
            Account savedAccount = accountService.register(account);
            return ResponseEntity.ok(savedAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        try {
            Account authenticated = accountService.login(account);
            return ResponseEntity.ok(authenticated); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage()); 
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> addMessage(@RequestBody Message message){
       
        try{
            Message createdMessage = messageService.addMessage(message);
            return ResponseEntity.ok(createdMessage);
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages(){
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<?> getMessageById(@PathVariable("message_id") int id){
        Message message = messageService.getMessageById(id);
        if(message != null){
            return ResponseEntity.ok(message);
        }else{
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("message_id") int id){
        int rowsDeleted = messageService.deleteMessageById(id);

        if (rowsDeleted > 0) {
            return ResponseEntity.ok(rowsDeleted); 
        } else {
            return ResponseEntity.ok().build(); 
    }
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable int account_id){
        List<Message> messages = messageService.getMessagesByPostedBy(account_id);
        return ResponseEntity.ok(messages);
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessageText(@PathVariable("message_id") int messageId, @RequestBody Message newMessage){
            String newText = newMessage.getMessageText();
            int rowsUpdated = messageService.updateMessage(messageId, newText);
            if(rowsUpdated == 1){
                return ResponseEntity.ok(1);
            }else{
                return ResponseEntity.badRequest().build();
            }
    }
}
