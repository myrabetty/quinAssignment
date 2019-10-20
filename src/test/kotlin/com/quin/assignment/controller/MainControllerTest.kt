package com.quin.assignment.controller

import com.quin.assignment.service.FileService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes

/**
 * test class for MainController
 */
internal class MainControllerTest {

    private val redirectAttributes: RedirectAttributes = Mockito.mock(RedirectAttributes::class.java)
    private val multipartFile: MultipartFile = Mockito.mock(MultipartFile::class.java)
    private val fileService: FileService = Mockito.mock(FileService::class.java)
    private val mainController = MainController(fileService)
    private val captor = ArgumentCaptor.forClass(String::class.java)

    @Test
    fun uploadMultiPartFile_fileIsProcessed_successReturned() {
        //arrange
        doReturn("name").`when`(multipartFile).originalFilename
        doReturn("content".toByteArray()).`when`(multipartFile).bytes
        doReturn(true).`when`(fileService).process(ArgumentMatchers.anyString())
        doReturn(redirectAttributes).`when`(redirectAttributes).addFlashAttribute(ArgumentMatchers.anyString())

        //act
        mainController.uploadMultipartFile(multipartFile, redirectAttributes)

        //assert
        verify(fileService).process(any())
        verify(redirectAttributes).addFlashAttribute(captor.capture())
        assertEquals(captor.value, "File uploaded successfully! -> filename = " + multipartFile.originalFilename, "message success as expected")
    }

    @Test
    fun uploadMultiPartFile_fileNotProcessed_failReturned() {
        //arrange
        doReturn("name").`when`(multipartFile).originalFilename
        doReturn("content".toByteArray()).`when`(multipartFile).bytes
        doReturn(false).`when`(fileService).process(ArgumentMatchers.anyString())
        doReturn(redirectAttributes).`when`(redirectAttributes).addFlashAttribute(ArgumentMatchers.anyString())

        //act
        mainController.uploadMultipartFile(multipartFile, redirectAttributes)

        //assert
        verify(fileService).process(any())
        verify(redirectAttributes).addFlashAttribute(captor.capture())
        assertEquals(captor.value,  "File uploaded failed! -> filename = " + multipartFile.originalFilename, "message success as expected")
    }
}