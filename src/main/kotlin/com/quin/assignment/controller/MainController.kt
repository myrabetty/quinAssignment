package com.quin.assignment.controller

import com.quin.assignment.model.ActivityStatistics
import com.quin.assignment.model.DailyActivity
import com.quin.assignment.service.DailyActivityService
import com.quin.assignment.service.StatisticsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.nio.charset.Charset

/**
 * Controller for Posting and retrieving information.
 **/

@Controller
class MainController @Autowired constructor(
        private val dailyActivityService: DailyActivityService,
        private val statisticsService: StatisticsService
) {
    /**
     * File upload page view.
     */
    @GetMapping("/home", "/")
    fun landingPageView(): String {
        return "home.html"
    }

    @PostMapping("/fileUpload")
    fun uploadMultipartFile(@RequestParam("file") multipartFile: MultipartFile, redirectAttributes: RedirectAttributes): String {
        val content = String(multipartFile.bytes, Charset.defaultCharset())

        if (dailyActivityService.process(content)) {
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully! -> filename = " + multipartFile.originalFilename)
        } else {
            redirectAttributes.addFlashAttribute("message", "File uploaded failed! -> filename = " + multipartFile.originalFilename)
        }
        return "redirect:/home"
    }

    @RequestMapping(value = ["/browse"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun browseDailyActivity(
            @RequestParam("page", defaultValue = "0", required = false) page: Int,
            @RequestParam("limit", defaultValue = "1000", required = false) limit: Int
    ): List<DailyActivity> {
        return dailyActivityService.getData(page, limit)
    }

    @RequestMapping(value = ["/statistics"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun browseStatistics(): ActivityStatistics {
        return statisticsService.getActivityStatistics()
    }

}