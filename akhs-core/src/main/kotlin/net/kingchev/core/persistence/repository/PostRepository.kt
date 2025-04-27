package net.kingchev.core.persistence.repository

import net.kingchev.core.persistence.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.history.RevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long>, RevisionRepository<Post, Long, Long>