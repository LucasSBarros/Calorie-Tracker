package com.calorietracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.MacroModel;

import java.util.UUID;

@Repository
public interface MacroRepository extends JpaRepository<MacroModel, UUID> {

}
