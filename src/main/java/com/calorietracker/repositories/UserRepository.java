package com.calorietracker.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
                select u
                from UserModel u
                where u.email is not null
                  and exists (
                      select d.idDiet
                      from DietModel d
                      where d.user = u
                  )
                  and not exists (
                      select m.idMeal
                      from MealModel m
                      join m.diet d
                      where d.user = u
                        and m.mealDateTime >= :limitDateTime
                  )
            """)
    List<UserModel> findUsersWithoutMealsSince(@Param("limitDateTime") LocalDateTime limitDateTime);
}