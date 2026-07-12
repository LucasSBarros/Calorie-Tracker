package com.calorietracker.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.MealLogModel;
import com.calorietracker.projections.MealLogCaloriesProjection;

/**
 * Fornece acesso aos registros de refeições consumidas.
 */
@Repository
public interface MealLogRepository extends JpaRepository<MealLogModel, UUID> {

    /**
     * Busca refeições de um usuário em um intervalo com limite superior
     * exclusivo.
     *
     * @param userId identificador do usuário
     * @param from data e hora inicial inclusiva
     * @param to data e hora final exclusiva
     * @return refeições ordenadas da mais recente para a mais antiga
     */
    @EntityGraph(attributePaths = { "diet", "items", "items.ingredient" })
    List<MealLogModel> findByUser_IdUserAndConsumedAtGreaterThanEqualAndConsumedAtLessThanOrderByConsumedAtDesc(
            UUID userId,
            LocalDateTime from,
            LocalDateTime to);

    /**
     * Projeta data e calorias das refeições usadas na geração do relatório.
     *
     * @param userId identificador do usuário
     * @param from data e hora inicial inclusiva
     * @param to data e hora final exclusiva
     * @return projeções ordenadas cronologicamente
     */
    @Query("""
            select new com.calorietracker.projections.MealLogCaloriesProjection(
                m.consumedAt,
                m.totalCalories
            )
            from MealLogModel m
            where m.user.idUser = :userId
              and m.consumedAt >= :from
              and m.consumedAt < :to
            order by m.consumedAt
            """)
    List<MealLogCaloriesProjection> findCaloriesByUserAndPeriod(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
