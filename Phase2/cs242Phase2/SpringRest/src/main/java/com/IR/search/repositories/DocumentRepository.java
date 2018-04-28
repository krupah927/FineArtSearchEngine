package com.IR.search.repositories;
import com.IR.search.models.Paintings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

 @Repository
   // public interface DocumentRepository extends MongoRepository<Paintings, S>
    public interface DocumentRepository extends MongoRepository<Paintings, String> {
        public List<Paintings> findByIdname(String idname);
    }

