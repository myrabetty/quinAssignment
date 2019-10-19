package com.quin.assignment.controller

import com.quin.assignment.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.File
import java.io.FileOutputStream

/**
 * Controller for Posting and retrieving information.
 **/

@Controller
class MainController @Autowired constructor(
        private val fileService: FileService
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
        val tempFile = convertFile(multipartFile)

        if (fileService.process(tempFile))
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully! -> filename = " + multipartFile.originalFilename)
        else
            redirectAttributes.addFlashAttribute("message", "File uploaded failed! -> filename = " + multipartFile.originalFilename)
        return "redirect:/home"
    }

    private fun convertFile(multipartFile: MultipartFile): File {
        val tempFile = File(multipartFile.originalFilename)
        //tempFile.deleteOnExit()
        tempFile.createNewFile()
        val fos = FileOutputStream(tempFile)
        fos.write(multipartFile.bytes)
        fos.close()
        return tempFile
    }
}