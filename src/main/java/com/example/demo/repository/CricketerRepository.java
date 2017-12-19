package com.example.demo.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Cricketer;

@Repository
public interface CricketerRepository extends ReactiveCrudRepository<Cricketer , String>{

}
