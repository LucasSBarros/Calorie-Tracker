package com.calorietracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.DietModel;

import java.util.UUID;

@Repository
public interface DietRepository extends JpaRepository<DietModel, UUID> {

}
