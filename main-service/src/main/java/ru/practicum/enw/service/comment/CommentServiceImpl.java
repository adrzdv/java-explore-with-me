package ru.practicum.enw.service.comment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enw.exceptions.BadRequestCustomException;
import ru.practicum.enw.exceptions.ConflictCustomException;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.comment.CommentDto;
import ru.practicum.enw.model.comment.NewCommentDto;
import ru.practicum.enw.model.entity.Comment;
import ru.practicum.enw.model.entity.Event;
import ru.practicum.enw.model.entity.User;
import ru.practicum.enw.model.enums.EventStates;
import ru.practicum.enw.model.event.EventShortWithCommentDto;
import ru.practicum.enw.model.mapper.custom.CommentMapper;
import ru.practicum.enw.model.mapper.custom.EventMapper;
import ru.practicum.enw.repo.CommentRepo;
import ru.practicum.enw.repo.EventRepo;
import ru.practicum.enw.repo.UserRepo;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;
    private final EventRepo eventRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public CommentDto addComment(NewCommentDto comment, long idEvent, long idAuthor) throws NotFoundCustomException {

        User user = userRepo.findById(idAuthor)
                .orElseThrow(() -> new NotFoundCustomException("Error: User with id=" + idAuthor + " not found"));
        Event event = eventRepo.findById(idEvent)
                .orElseThrow(() -> new NotFoundCustomException("Error: Event with id=" + idEvent + " not found"));

        if (!event.getState().equals(EventStates.PUBLISHED.name())) {
            throw new ConflictCustomException("Event not published");
        }

        if (event.getInitiator().getId() == idAuthor) {
            throw new ConflictCustomException("Initiator can't post comments");
        }

        Comment newComment = Comment.builder()
                .text(comment.getText())
                .created(LocalDateTime.now())
                .author(user)
                .event(event)
                .build();

        return CommentMapper.fromEntityToDto(commentRepo.save(newComment));
    }

    @Override
    @Transactional
    public CommentDto updateComment(NewCommentDto newComment, long idEvent,
                                    long idAuthor, long idComment) throws NotFoundCustomException {

        Comment comment = commentRepo.findById(idComment)
                .orElseThrow(() -> new NotFoundCustomException("Comment with id=" + idComment + " not found"));

        if (comment.getAuthor().getId() != idAuthor || comment.getEvent().getId() != idEvent) {
            throw new ConflictCustomException("Error: Updating comment not for event with id=" +
                    idEvent + " or user is not an author of comment");
        }

        if (comment.getCreated().isAfter(LocalDateTime.now().plusHours(1L))) {
            throw new ConflictCustomException("Error: Comment can't be changed more than 1 hour after being posted");
        }

        comment.setText(newComment.getText());

        return CommentMapper.fromEntityToDto(commentRepo.save(comment));

    }

    @Override
    public void deleteCommentByAuthor(long idAuthor, long idEvent, long idComment) throws NotFoundCustomException {

        Comment comment = commentRepo.findById(idComment)
                .orElseThrow(() -> new NotFoundCustomException("Comment not found"));

        if (comment.getEvent().getId() != idEvent) {
            throw new BadRequestCustomException("Comment is not from current event");
        }

        if (comment.getId() != idAuthor) {
            throw new BadRequestCustomException("Comment can be deleted only by owner");
        }

        commentRepo.deleteById(idComment);
    }

    @Override
    public void deleteCommentByAdmin(long idComment) throws NotFoundCustomException {

        if (!commentRepo.existsById(idComment)) {
            throw new NotFoundCustomException("Comment not found");
        }

        commentRepo.deleteById(idComment);
    }

    @Override
    public EventShortWithCommentDto getEventComments(long idEvent, long idUser,
                                                     int size, int from) throws NotFoundCustomException {

        Event event = eventRepo.findEventByIdAndInitiatorId(idEvent, idUser);

        if (event == null) {
            throw new NotFoundCustomException("Error: Event with id=" + idEvent + " not found");
        }

        List<CommentDto> commentsDto = commentRepo.findByEventId(idEvent, size, from).stream()
                .map(CommentMapper::fromEntityToDto)
                .toList();

        EventShortWithCommentDto eventWithComments = EventMapper.fromEntityToDtoWithComments(event);
        eventWithComments.setComments(commentsDto);

        return eventWithComments;
    }

    @Override
    public EventShortWithCommentDto getEventCommentsForPublic(long idEvent, int size, int from) throws NotFoundCustomException {

        Event event = eventRepo.findById(idEvent)
                .orElseThrow(() -> new NotFoundCustomException("Event with id=" + idEvent + " not found"));

        List<CommentDto> commentsDto = commentRepo.findByEventId(idEvent, size, from).stream()
                .map(CommentMapper::fromEntityToDto)
                .toList();

        EventShortWithCommentDto eventWithComments = EventMapper.fromEntityToDtoWithComments(event);
        eventWithComments.setComments(commentsDto);

        return eventWithComments;

    }
}
