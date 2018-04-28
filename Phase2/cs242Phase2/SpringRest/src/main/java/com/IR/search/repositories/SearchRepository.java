package com.IR.search.repositories;

import com.IR.search.models.Search;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends MongoRepository<Search, String> {
  public List<Search> findByWord(String word);
}
