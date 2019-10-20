package com.quin.assignment.controller

import com.quin.assignment.service.DailyActivityService

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap

/**
 * test class for MainController
 */
internal class MainControllerTest {

    private val redirectAttributes: RedirectAttributes = Mockito.mock(RedirectAttributesModelMap::class.java)
    private val multipartFile: MultipartFile = Mockito.mock(MultipartFile::class.java)
    private val dailyActivityService: DailyActivityService = Mockito.mock(DailyActivityService::class.java)
    private val mainController = MainController(dailyActivityService)
    private val keyCaptor = ArgumentCaptor.forClass(String::class.java)
    private val valueCaptor = ArgumentCaptor.forClass(Object::class.java)
    @Test
    fun uploadMultiPartFile_fileIsProcessed_successReturned() {
        //arrange
        doReturn("name").`when`(multipartFile).originalFilename
        doReturn("content".toByteArray()).`when`(multipartFile).bytes
        doReturn(true).`when`(dailyActivityService).process(anyString())
        doReturn(redirectAttributes).`when`(redirectAttributes).addFlashAttribute(anyString())

        //act
        mainController.uploadMultipartFile(multipartFile, redirectAttributes)

        //assert
        verify(dailyActivityService).process(anyString())
        verify(redirectAttributes).addFlashAttribute(keyCaptor.capture(), valueCaptor.capture())
        assertEquals("message", keyCaptor.value, "key message as expected")
        assertEquals("File uploaded successfully! -> filename = " + multipartFile.originalFilename, valueCaptor.value,  "message success as expected")
    }

    @Test
    fun uploadMultiPartFile_fileNotProcessed_failReturned() {
        //arrange
        doReturn("name").`when`(multipartFile).originalFilename
        doReturn("content".toByteArray()).`when`(multipartFile).bytes
        doReturn(false).`when`(dailyActivityService).process(anyString())
        doReturn(redirectAttributes).`when`(redirectAttributes).addFlashAttribute(anyString())

        //act
        mainController.uploadMultipartFile(multipartFile, redirectAttributes)

        //assert
        verify(dailyActivityService).process(anyString())
        verify(redirectAttributes).addFlashAttribute(keyCaptor.capture(), valueCaptor.capture())
        assertEquals("message", keyCaptor.value, "key message as expected")
        assertEquals("File uploaded failed! -> filename = " + multipartFile.originalFilename, valueCaptor.value,  "message success as expected")
    }
}