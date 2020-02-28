package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.CommentRepository;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Date;
import java.sql.Time;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestCommentService {

    private static final long USER_ID = 13;
    private static final String USER_NAME = "TestPerson";
    private static final String USER_EMAIL = "TestPerson@email.com";
    private static final String USER_PASSWORD = "myP1+abc";

    private static final long COMMENT_ID = 42;
    private static final String COMMENT_TEXT = "My comment. Hewwo~!";
    private static final Date COMMENT_DATE = Date.valueOf("2006-12-30");
    private static final Time COMMENT_TIME = Time.valueOf("00:38:54.840");

    private static final long FORUM_ID = 343;
    private static final long FORUM_LOCKED_ID = 345;
    private static final String FORUM_TITLE = "New Forum. What's this~?";


    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ForumRepository forumRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setMockOutput() {
        MockitoAnnotations.initMocks(this);

        lenient().when(userRepository.findById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(USER_ID)) {
                User user = new User();
                user.setId(USER_ID);
                user.setUserName(USER_NAME);
                user.setEmail(USER_EMAIL);
                user.setPassword(USER_PASSWORD);
                Optional<User> userWrapper = Optional.of(user);
                return userWrapper;
            } else {
                return null;
            }
        });

        lenient().when(forumRepository.findById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(FORUM_ID)) {
                User user = new User();
                user.setId(USER_ID);
                user.setUserName(USER_NAME);
                user.setEmail(USER_EMAIL);
                user.setPassword(USER_PASSWORD);
                Set<User> subscribers = new HashSet<>();
                subscribers.add(user);
                Comment comment = new Comment();
                comment.setTime(COMMENT_TIME);
                comment.setDatePosted(COMMENT_DATE);
                comment.setId(COMMENT_ID);
                comment.setText(COMMENT_TEXT);
                comment.setUser(user);
                Set<Comment> comments = new HashSet<>();
                comments.add(comment);
                Forum forum = new Forum();
                forum.setId(FORUM_ID);
                forum.setTitle(FORUM_TITLE);
                forum.setAuthor(user);
                forum.setComments(comments);
                forum.setLocked(false);
                forum.setSubscribers(subscribers);
                Optional<Forum> forumWrapper = Optional.of(forum);
                return forumWrapper;
            } else if (invocation.getArgument(0).equals(FORUM_LOCKED_ID)) {
                User user = new User();
                user.setId(USER_ID);
                user.setUserName(USER_NAME);
                user.setEmail(USER_EMAIL);
                user.setPassword(USER_PASSWORD);
                Set<User> subscribers = new HashSet<>();
                subscribers.add(user);
                Comment comment = new Comment();
                comment.setTime(COMMENT_TIME);
                comment.setDatePosted(COMMENT_DATE);
                comment.setId(COMMENT_ID);
                comment.setText(COMMENT_TEXT);
                comment.setUser(user);
                Set<Comment> comments = new HashSet<>();
                comments.add(comment);
                Forum forum = new Forum();
                forum.setId(FORUM_ID);
                forum.setTitle(FORUM_TITLE);
                forum.setAuthor(user);
                forum.setComments(comments);
                forum.setLocked(true);
                forum.setSubscribers(subscribers);
                Optional<Forum> forumWrapper = Optional.of(forum);
                return forumWrapper;
            } else {
                return null;
            }
        });

        lenient().when(commentRepository.findById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(COMMENT_ID)) {
                User user = new User();
                user.setId(USER_ID);
                user.setUserName(USER_NAME);
                user.setEmail(USER_EMAIL);
                user.setPassword(USER_PASSWORD);
                Comment comment = new Comment();
                comment.setTime(COMMENT_TIME);
                comment.setDatePosted(COMMENT_DATE);
                comment.setId(COMMENT_ID);
                comment.setText(COMMENT_TEXT);
                comment.setUser(user);
                Optional<Comment> commentWrapper = Optional.of(comment);
                return commentWrapper;
            }
            return null;
        });

        lenient().when(commentRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            User user = new User();
            user.setId(USER_ID);
            user.setUserName(USER_NAME);
            user.setEmail(USER_EMAIL);
            user.setPassword(USER_PASSWORD);
            Comment comment = new Comment();
            comment.setTime(COMMENT_TIME);
            comment.setDatePosted(COMMENT_DATE);
            comment.setId(COMMENT_ID);
            comment.setText(COMMENT_TEXT);
            comment.setUser(user);
            List<Comment> comments = new ArrayList<Comment>();
            comments.add(comment);
            return comments;
        });

        // Set a reflexive return answer.
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(commentRepository.save(any(Comment.class))).thenAnswer(returnParameterAsAnswer);

    }

    /**
     * This test verifies the operation of addComment().
     */
    @Test
    public void testAddComment() { ;
        Comment comment = commentService.addComment(COMMENT_TEXT, FORUM_ID, USER_ID);
        assertEquals(USER_ID, comment.getUser().getId());
        assertEquals(COMMENT_ID, comment.getId());
        assertEquals(COMMENT_TEXT, comment.getText());
    }

    /**
     * This test verifies that comment creation is prevented by the isLocked() flag.
     */
    @Test
    public void testLockedThreadComment() {
        CommentException thrown = assertThrows(
                CommentException.class,
                () -> commentService.addComment(COMMENT_TEXT, FORUM_LOCKED_ID, USER_ID)
        );
        assertTrue(thrown.getMessage().contains("Forum thread is locked."));
    }

    /**
     * This test verifies that you need a valid forum to create a comment.
     */
    @Test
    public void testMissingThreadComment() {
        CommentException thrown = assertThrows(
                CommentException.class,
                () -> commentService.addComment(COMMENT_TEXT, 0, USER_ID)
        );
        assertTrue(thrown.getMessage().contains("No such forum thread."));
    }

    /**
     * This test verifies that you need a valid user to create a comment.
     */
    @Test
    public void testMissingUserComment() {
        CommentException thrown = assertThrows(
                CommentException.class,
                () -> commentService.addComment(COMMENT_TEXT, FORUM_ID, 0)
        );
        assertTrue(thrown.getMessage().contains("No such user."));
    }

}

