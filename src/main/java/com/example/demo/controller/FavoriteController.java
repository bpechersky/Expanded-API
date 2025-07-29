package com.example.demo.controller;

import com.example.demo.model.Favorite;
import com.example.demo.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Favorite getById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Favorite update(@PathVariable Long id, @RequestBody Favorite obj) {
        obj.setId(id);
        return repository.save(obj);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
