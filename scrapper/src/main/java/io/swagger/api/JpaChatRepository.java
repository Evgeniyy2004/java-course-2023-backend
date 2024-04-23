package io.swagger.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class JpaChatRepository implements  TgChatApi{
    @PersistenceContext
    private EntityManager manager;

    @Override
    public ResponseEntity tgChatIdDelete(Long id) {
        return null;
    }

    @Override
    public ResponseEntity tgChatIdPost(Long id) {
        return null;
    }
}
