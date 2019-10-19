package com.quin.assignment.controller

import com.quin.assignment.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.File

/**
 * Controller for Posting and retrieving information.
 **/

@Controller
class MainController @Autowired constructor(
        private val fileService: FileService
) {

    private val tempFilePostFix = "tmp";
    /**
     * File upload page view.
     */
    @GetMapping("/home", "/")
    fun landingPageView(): String {
        return "home.html"
    }

    @PostMapping("/fileUpload")
    fun uploadMultipartFile(@RequestParam("multipartFile") multipartFile: MultipartFile, redirectAttributes: RedirectAttributes): String {
        val tempFile = File.createTempFile("", tempFilePostFix)
        multipartFile.transferTo(tempFile)
        tempFile.deleteOnExit()
        var success = fileService.process(tempFile)
        if (success)
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully! -> filename = " + multipartFile.originalFilename)
        else
            redirectAttributes.addFlashAttribute("message", "File uploaded failed! -> filename = " + multipartFile.originalFilename)
        return "redirect:/home"
    }
}