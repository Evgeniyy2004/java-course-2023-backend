package io.swagger.api;

import org.springframework.data.repository.CrudRepository;

public interface JdbcTgChatRepository extends CrudRepository<Long> {
    void add();
}
