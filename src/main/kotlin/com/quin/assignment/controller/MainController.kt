package com.quin.assignment.controller

import com.quin.assignment.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


/**
 * Controller for Posting and retrieving information.
 **/

@RestController
class MainController @Autowired constructor(
        private val fileService: FileService
) {

    @PostMapping("/fileUpload")
    fun fileUpload(@RequestParam(value = "file") file: MultipartFile) = fileService.process(file)
}