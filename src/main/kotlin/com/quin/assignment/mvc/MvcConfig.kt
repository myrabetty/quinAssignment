package com.quin.assignment.mvc

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/** Model view controller config.
 **/

@Configuration
class MvcConfig : WebMvcConfigurer {

    /**
     * login url.
     */
    val home = "/home"

    /**
     * login model view.
     */
    val homeView = "home"

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController(home).setViewName(homeView)
    }
}