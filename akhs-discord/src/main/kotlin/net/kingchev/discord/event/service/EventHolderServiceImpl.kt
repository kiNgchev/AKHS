package net.kingchev.discord.event.service

import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kingchev.discord.event.annotation.AkhsListener
import net.kingchev.discord.event.model.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
@Scope("singleton")
class EventHolderServiceImpl : EventHolderService {

    private final val events: MutableList<Event> = mutableListOf()

    @Autowired
    override fun register(events: Array<Event>) {
        events.forEach {
            try {
                it.javaClass.getAnnotation(AkhsListener::class.java)
                if (it !is ListenerAdapter) return@forEach
                this.events.add(it)
            } catch (_: NullPointerException) {
                return@forEach
            }
        }
    }

    override fun getEvents(): Array<Event> = events.toTypedArray()
}