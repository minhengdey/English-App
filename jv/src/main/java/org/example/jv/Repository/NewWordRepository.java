package org.example.jv.Repository;

import org.example.jv.Entity.NewWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewWordRepository extends JpaRepository<NewWordEntity, Long> {

}
