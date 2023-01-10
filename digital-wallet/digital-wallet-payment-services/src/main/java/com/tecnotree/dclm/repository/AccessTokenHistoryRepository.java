package com.tecnotree.dclm.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tecnotree.dclm.model.AccessTokenHistory;

public interface AccessTokenHistoryRepository extends MongoRepository<AccessTokenHistory, String>{

}
