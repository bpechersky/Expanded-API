package com.example.demo.controller;

import com.example.demo.model.Favorite;
import com.example.demo.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepository repository;

    @GetMapping
    public List<Favorite> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Favorite create(@RequestBody Favorite obj) {
        return repository.save(obj);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Favorite> getFavoriteById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PutMapping("/{id}")
    public Favorite update(@PathVariable Long id, @RequestBody Favorite obj) {
        obj.setId(id);
        return repository.save(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFavorite(@PathVariable Long id) {
        return repository.findById(id)
                .map(fav -> {
                    repository.deleteById(id);
                    return ResponseEntity.noContent().build(); // ✅ 204 if found and deleted
                })
                .orElse(ResponseEntity.notFound().build()); // ✅ 404 if not found
    }

}