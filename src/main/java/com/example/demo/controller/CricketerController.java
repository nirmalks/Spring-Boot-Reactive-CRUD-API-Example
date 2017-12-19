package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Cricketer;
import com.example.demo.repository.CricketerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CricketerController {
	@Autowired
	CricketerRepository cricketerRepository;
	
	@GetMapping("/api/cricketers/")
	public Flux<Cricketer> getAllCricketers() {
		return cricketerRepository.findAll();
	}
	
	@GetMapping("/api/cricketer/{id}")
	public Mono<ResponseEntity<Cricketer>> getCricketer(@PathVariable("id") String id) {
		return cricketerRepository.findById(id)
			     .map(ResponseEntity::ok)
			     .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}	

	@PostMapping("/api/cricketer/")
	public Mono<Cricketer> addCricketer(@RequestBody Cricketer cricketer) {
		return cricketerRepository.save(cricketer);
	}
	
	@PutMapping("/api/cricketer/{id}")
	public Mono<ResponseEntity<Cricketer>> updateCricketer(@PathVariable("id") String id, @RequestBody Cricketer cricketer) {
		return cricketerRepository.findById(id).flatMap(currentCricketer -> {
			currentCricketer.setCountry(cricketer.getCountry());
			currentCricketer.setName(cricketer.getName());
			currentCricketer.setHighestScore(cricketer.getHighestScore());
			return cricketerRepository.save(currentCricketer);
		})
		.map(updatedCricketer -> new ResponseEntity<Cricketer>(updatedCricketer, HttpStatus.OK))
		.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@DeleteMapping("/api/cricketer/{id}")
	public Mono<ResponseEntity<Void>> deleteCricketer(@PathVariable("id") String id) {
		return cricketerRepository.deleteById(id).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)
				));
	}
	
}
