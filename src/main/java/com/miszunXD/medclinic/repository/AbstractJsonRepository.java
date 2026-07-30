package com.miszunXD.medclinic.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public abstract class AbstractJsonRepository<T extends Identifiable<ID>, ID> implements CrudRepository<T, ID>{
    protected final List<T> entities;
    protected final ObjectMapper objectMapper;
    private final File file;
    private final TypeReference<List<T>> typeReference;

    protected AbstractJsonRepository(String fileName, TypeReference<List<T>> typeReference) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.file = new File(fileName);
        this.typeReference = typeReference;
        this.entities = load();
    }

    private List<T> load() {
        try {
            if (!file.exists()) {
                return new ArrayList<>();
            }

            return objectMapper.readValue(
                    file,
                    typeReference
            );
        } catch (IOException e) {
            throw new RuntimeException("Błąd ładowania pliku " + file, e);
        }
    }

    @Override
    public List<T> findAll() {
        return List.copyOf(entities);
    }

    @Override
    public Optional<T> findById(ID id) {
        return entities.stream()
                .filter(e -> id.equals(e.getId()))
                .findFirst();
    }

    @Override
    public void save(T entity) {
        entities.removeIf(e -> e.getId().equals(entity.getId()));
        entities.add(entity);
        saveToFile();
    }

    @Override
    public void delete(ID id) {
        entities.removeIf(e -> id.equals(e.getId()));
        saveToFile();

    }

    protected void saveToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, entities);
        } catch (IOException e) {
            throw new RuntimeException("Nie udało się zapisać pliku " + file, e);
        }
    }

    public void saveAll() {
        saveToFile();
    }
}
