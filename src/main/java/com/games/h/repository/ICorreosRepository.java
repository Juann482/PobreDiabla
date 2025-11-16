package com.games.h.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.games.h.model.Correos;

@Repository
public interface ICorreosRepository extends JpaRepository<Correos, Integer> {

	List<Correos> findByEmail(String email);

	Optional<Correos> findByNombre(String nombre);

}
