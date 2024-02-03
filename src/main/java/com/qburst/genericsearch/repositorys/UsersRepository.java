package com.qburst.genericsearch.repositorys;

import com.qburst.genericsearch.entitys.UsersEntity;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity,Long>, JpaSpecificationExecutor<UsersEntity> {
}
