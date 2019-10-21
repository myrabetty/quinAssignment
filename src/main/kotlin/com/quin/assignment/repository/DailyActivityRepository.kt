package com.quin.assignment.repository

import com.quin.assignment.model.DailyActivity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Date

@Repository
interface DailyActivityRepository : PagingAndSortingRepository<DailyActivity, Long> {

    fun findAllByOrderByDateDesc(page: Pageable): Page<DailyActivity>

    fun findFirstByOrderByDateDesc(): DailyActivity

    fun findByDateBetween(
            @Param("dateBefore") dateBefore: Date,
            @Param("dateAfter") dateAfter: Date
    ): List<DailyActivity>
}
