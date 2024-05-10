package net.kingchev.discord.event.annotation

import org.springframework.stereotype.Component
import java.lang.annotation.Inherited

@Component
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class AkhsListener
