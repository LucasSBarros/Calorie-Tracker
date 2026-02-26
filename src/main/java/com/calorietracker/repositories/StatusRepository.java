package com.calorietracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.StatusModel;

import java.util.UUID;

@Repository
public interface StatusRepository extends JpaRepository<StatusModel, UUID> {

}
