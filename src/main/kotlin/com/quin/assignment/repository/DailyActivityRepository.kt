package com.quin.assignment.repository

import com.quin.assignment.model.DailyActivity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DailyActivityRepository : JpaRepository<DailyActivity, Long>
