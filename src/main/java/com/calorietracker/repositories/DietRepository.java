package com.calorietracker.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.DietModel;

@Repository
public interface DietRepository extends JpaRepository<DietModel, UUID> {

        @EntityGraph(attributePaths = {
                        "meals",
                        "meals.mealIngredients",
                        "meals.mealIngredients.ingredient",
                        "meals.mealIngredients.ingredient.macro"
        })
        Optional<DietModel> findWithDetailsByIdDiet(UUID idDiet);

        @Override
        @EntityGraph(attributePaths = {
                        "meals",
                        "meals.mealIngredients",
                        "meals.mealIngredients.ingredient",
                        "meals.mealIngredients.ingredient.macro"
        })
        List<DietModel> findAll();

        @Query("""
                        select d.name
                        from DietModel d
                        where d.user.idUser = :userId
                        order by d.initialDate desc
                                """)
        List<String> findDietNamesByUserId(@Param("userId") UUID userId);

        @EntityGraph(attributePaths = {
                        "meals",
                        "meals.mealIngredients",
                        "meals.mealIngredients.ingredient",
                        "meals.mealIngredients.ingredient.macro"
        })
        List<DietModel> findByUser_IdUserOrderByInitialDateDesc(UUID userId);

        /**
         * Busca as dietas vigentes para o usuário na data informada.
         *
         * @param userId identificador do usuário
         * @param date data usada na verificação de vigência
         * @return dietas vigentes ordenadas pela data inicial mais recente
         */
        @Query("""
                        select d
                        from DietModel d
                        where d.user.idUser = :userId
                          and (d.initialDate is null or d.initialDate <= :date)
                          and (d.finalDate is null or d.finalDate >= :date)
                        order by d.initialDate desc, d.idDiet
                        """)
        List<DietModel> findCurrentByUserId(
                        @Param("userId") UUID userId,
                        @Param("date") LocalDate date);

        /**
         * Conta dietas do usuário cujo período se sobrepõe ao intervalo informado.
         *
         * @param userId identificador do usuário
         * @param excludedDietId dieta ignorada na contagem ou nula
         * @param initialDate início do período
         * @param finalDate fim do período
         * @return quantidade de períodos sobrepostos
         */
        @Query("""
                        select count(d)
                        from DietModel d
                        where d.user.idUser = :userId
                          and (:excludedDietId is null or d.idDiet <> :excludedDietId)
                          and (d.finalDate is null or :initialDate is null or d.finalDate >= :initialDate)
                          and (:finalDate is null or d.initialDate is null or d.initialDate <= :finalDate)
                        """)
        long countOverlappingPeriods(
                        @Param("userId") UUID userId,
                        @Param("excludedDietId") UUID excludedDietId,
                        @Param("initialDate") LocalDate initialDate,
                        @Param("finalDate") LocalDate finalDate);

}
