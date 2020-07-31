package com.example.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.model.Cricketer;
import com.example.demo.repository.CricketerRepository;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class CricketerHandler {
	@Autowired
	CricketerRepository cricketerRepository;

	private static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

	public Mono<ServerResponse> getAllCricketers(ServerRequest serverRequest) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(cricketerRepository.findAll(), Cricketer.class);
	}
	
	public Mono<ServerResponse> getCricketer(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		Mono<Cricketer> cricketerMono = cricketerRepository.findById(id);
		return cricketerMono.flatMap(cricketer ->
				ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				 .body(fromObject(cricketer)))
			     .switchIfEmpty(notFound);
	}	


	public Mono<ServerResponse> addCricketer(ServerRequest serverRequest) {
		Mono<Cricketer> cricketerWrapper = serverRequest.bodyToMono(Cricketer.class);
		return cricketerWrapper.flatMap(
				cricketer ->
						ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
								.body(cricketerRepository.save(cricketer), Cricketer.class)
		);

	}

	public Mono<ServerResponse> updateCricketer(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		Mono<Cricketer> cricketerRequest = serverRequest.bodyToMono(Cricketer.class);
		Mono<Cricketer> cricketerMono = cricketerRepository.findById(id);
		Mono<Cricketer> updatedCricketer = cricketerRequest.flatMap(cricketer -> {
			cricketerMono.flatMap(currentCricketer -> {
				currentCricketer.setCountry(cricketer.getCountry());
				currentCricketer.setName(cricketer.getName());
				currentCricketer.setHighestScore(cricketer.getHighestScore());
				return cricketerRepository.save(currentCricketer);
			});
			return cricketerMono;
		});
		return updatedCricketer.flatMap(cricketer ->
				ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.body(fromObject(cricketer))
		).switchIfEmpty(notFound);
	}
	
	public Mono<ServerResponse> deleteCricketer(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		Mono<Void> deleteCricketer = cricketerRepository.deleteById(id);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(deleteCricketer, Void.class);
	}

	public Mono<ServerResponse> exceptionExample(ServerRequest serverRequest) {
		throw new RuntimeException("RuntimeException occurred");
	}
	
}
