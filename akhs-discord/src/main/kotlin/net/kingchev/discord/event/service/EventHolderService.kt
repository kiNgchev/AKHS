package net.kingchev.discord.event.service

import net.kingchev.discord.event.model.Event

interface EventHolderService {
    fun register(events: Array<Event>)

    fun getEvents(): Array<Event>
}