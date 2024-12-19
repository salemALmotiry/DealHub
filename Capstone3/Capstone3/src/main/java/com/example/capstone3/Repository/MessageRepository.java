package com.example.capstone3.Repository;

import com.example.capstone3.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Integer> {

    Message findMessageById(Integer id);
}
