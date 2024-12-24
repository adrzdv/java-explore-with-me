package ru.practicum.enw.service.comment;

import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.comment.CommentDto;
import ru.practicum.enw.model.comment.NewCommentDto;
import ru.practicum.enw.model.event.EventShortWithCommentDto;

public interface CommentService {

    /**
     * Add a new comment to event
     *
     * @param comment  comment object
     * @param idEvent  identification number of event
     * @param idAuthor identification number of comment's author
     * @return CommentDto object
     * @throws NotFoundCustomException
     */
    CommentDto addComment(NewCommentDto comment, long idEvent, long idAuthor) throws NotFoundCustomException;

    /**
     * Update an existing comment
     *
     * @param comment   comment object
     * @param idEvent   identification number of event
     * @param idAuthor  identification number of comment's author
     * @param idComment identification number of comment
     * @return CommentDto object of updated comment
     * @throws NotFoundCustomException
     */
    CommentDto updateComment(NewCommentDto comment, long idEvent, long idAuthor, long idComment) throws NotFoundCustomException;

    /**
     * Delete an existing comment by author
     *
     * @param idAuthor  identification number of comment's author
     * @param idEvent   identification number of event
     * @param idComment identification number of comment
     * @throws NotFoundCustomException
     */
    void deleteCommentByAuthor(long idAuthor, long idEvent, long idComment) throws NotFoundCustomException;

    /**
     * Delete ant existing comment by admin
     *
     * @param idComment identification number of comment
     * @throws NotFoundCustomException
     */
    void deleteCommentByAdmin(long idComment) throws NotFoundCustomException;

    /**
     * Get an event with comments
     *
     * @param idEvent identification number of event
     * @param idUser  identification number of event's initiator
     * @param size    the number of result list size
     * @param from    the number of skipped records
     * @return EventShortWithCommentDto object
     * @throws NotFoundCustomException
     */
    EventShortWithCommentDto getEventComments(long idEvent, long idUser, int size, int from) throws NotFoundCustomException;

    /**
     * Get events for public with comments
     *
     * @param idEvent identification number of event
     * @param size    the number of result list size
     * @param from    the number of skipped records
     * @return EventShortWithCommentDto
     * @throws NotFoundCustomException
     */
    EventShortWithCommentDto getEventCommentsForPublic(long idEvent, int size, int from) throws NotFoundCustomException;
}
