package com.example.repository;
import java.util.Optional;
import com.example.entity.Account;
import java.util.List;
import com.example.entity.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer>{
    List<Message> findByPostedBy(int postedBy);
}
