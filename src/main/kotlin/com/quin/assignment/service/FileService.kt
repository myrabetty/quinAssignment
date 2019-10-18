package com.quin.assignment.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

/**
 * processes the file.
 **/
@Service
class FileService(){
    fun process(file: MultipartFile): String {
        return "ok"
    }

}
