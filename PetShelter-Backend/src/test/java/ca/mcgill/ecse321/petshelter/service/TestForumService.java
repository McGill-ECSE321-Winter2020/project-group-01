package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.dto.ForumDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.CommentRepository;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.ForumException;
import org.junit.jupiter.api.Test;
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
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestForumService {

    private static final long USER_ID = 13;
    private static final String USER_NAME = "TestPerson";
    private static final String USER_EMAIL = "TestPerson@email.com";
    private static final String USER_PASSWORD = "myP1+abc";

    private static final long USER_ID_2 = 14;
    private static final String USER_NAME_2 = "TestPerson2";
    private static final String USER_EMAIL_2 = "TestPerson2@email.com";
    private static final String USER_PASSWORD_2 = "myP1+abccdzc";

    private static final long COMMENT_ID = 42;
    private static final String COMMENT_TEXT = "My comment. Hewwo~!";
    private static final Date COMMENT_DATE = Date.valueOf("2006-12-30");
    private static final Time COMMENT_TIME = Time.valueOf("00:38:54");

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
    private ForumService forumService;

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
                return Optional.of(user);
            } else if (invocation.getArgument(0).equals(USER_ID_2)) {
                User user = new User();
                user.setId(USER_ID_2);
                user.setUserName(USER_NAME_2);
                user.setEmail(USER_EMAIL_2);
                user.setPassword(USER_PASSWORD_2);
                return Optional.of(user);
            } else {
                return Optional.empty();
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

                return Optional.of(forum);
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
                forum.setId(FORUM_LOCKED_ID);
                forum.setTitle(FORUM_TITLE);
                forum.setAuthor(user);
                forum.setComments(comments);
                forum.setLocked(true);
                forum.setSubscribers(subscribers);

                return Optional.of(forum);
            } else {
                return Optional.empty();
            }
        });

        lenient().when(forumRepository.findForumsByAuthor(any(User.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (((User)invocation.getArgument(0)).getId() == (USER_ID)) {
                // Create user.
                User user = new User();
                user.setId(USER_ID);
                user.setUserName(USER_NAME);
                user.setEmail(USER_EMAIL);
                user.setPassword(USER_PASSWORD);

                // Create subscribers.
                Set<User> subscribers = new HashSet<>();
                subscribers.add(user);

                // Create comments.
                Comment comment = new Comment();
                comment.setTime(COMMENT_TIME);
                comment.setDatePosted(COMMENT_DATE);
                comment.setId(COMMENT_ID);
                comment.setText(COMMENT_TEXT);
                comment.setUser(user);
                Set<Comment> comments = new HashSet<>();
                comments.add(comment);

                // Create forums.
                Forum forum = new Forum();
                forum.setId(FORUM_ID);
                forum.setTitle(FORUM_TITLE);
                forum.setAuthor(user);
                forum.setComments(comments);
                forum.setLocked(false);
                forum.setSubscribers(subscribers);

                Forum forum2 = new Forum();
                forum2.setId(FORUM_LOCKED_ID);
                forum2.setTitle(FORUM_TITLE);
                forum2.setAuthor(user);
                forum2.setComments(comments);
                forum2.setLocked(true);
                forum2.setSubscribers(subscribers);

                List<Forum> forums = new ArrayList<>();
                forums.add(forum);
                forums.add(forum2);

                return forums;
            } else {
                return new ArrayList<ForumDTO>();
            }
        });

        lenient().when(forumRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
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

            Forum forum1 = new Forum();
            forum1.setId(FORUM_ID);
            forum1.setAuthor(user);
            forum1.setComments(comments);
            forum1.setLocked(false);
            forum1.setSubscribers(subscribers);

            Forum forum2 = new Forum();
            forum2.setId(FORUM_LOCKED_ID);
            forum2.setAuthor(user);
            forum2.setComments(comments);
            forum2.setLocked(true);
            forum2.setSubscribers(subscribers);

            List<Forum> forums = new ArrayList<>();
            forums.add(forum1);
            forums.add(forum2);

            return forums;
        });

        // Set a reflexive return answer.
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(commentRepository.save(any(Comment.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(forumRepository.save(any(Forum.class))).thenAnswer(returnParameterAsAnswer);

    }

    /**
     * This test verify that a forum can be added.
     */
    @Test
    public void testAddForum() {
        User user = new User();
        user.setId(USER_ID);
        user.setUserName(USER_NAME);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);

        try {
            ForumDTO forumDTO = forumService.addForum(FORUM_TITLE, user);
            assertEquals(USER_NAME, forumDTO.getAuthor().getUsername());
            assertEquals(FORUM_TITLE, forumDTO.getTitle());
            assertTrue(forumDTO.getSubscribers().stream()
                    .map(UserDTO::getUsername)
                    .collect(Collectors.toSet())
                    .contains(USER_NAME)
            );
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that no exception are encountered when deleting a forum.
     */
    @Test
    public void testDeleteForum() {
        try {
            ForumDTO forumDTO = forumService.deleteForum(FORUM_ID);
            assertEquals(FORUM_ID, forumDTO.getId());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that no exception are encountered when deleting a forum that doesn't exist.
     */
    @Test
    public void testDeleteMissingForum() {
        try {
            ForumDTO forumDTO = forumService.deleteForum(0);
            assertNull(forumDTO);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that forum locking works.
     */
    @Test
    public void testLockForum() {
        try {
            ForumDTO forumDTO = forumService.lockForum(FORUM_ID);
            assertTrue(forumDTO.isLocked());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that locking a forum that doesn't exist fails.
     */
    @Test
    public void testLockMissingThread() {
        ForumException thrown = assertThrows(ForumException.class,
                () -> forumService.lockForum(0));
        assertTrue(thrown.getMessage().contains("No such forum thread."));
    }

    /**
     * Test that forum unlocking works.
     */
    @Test
    public void testUnlockForum() {
        try {
            ForumDTO forumDTO = forumService.unlockForum(FORUM_LOCKED_ID);
            assertFalse(forumDTO.isLocked());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that unlocking a forum that doesn't exist fails.
     */
    @Test
    public void testUnlockMissingThread() {
        ForumException thrown = assertThrows(ForumException.class,
                () -> forumService.unlockForum(0));
        assertTrue(thrown.getMessage().contains("No such forum thread."));
    }

    /**
     * Test that subscription works.
     */
    @Test
    public void testForumSubscribe() {
        try {
            ForumDTO forumDTO = forumService.subscribeTo(FORUM_ID, USER_ID_2);
            assertTrue(forumDTO.getSubscribers().stream()
                    .map(UserDTO::getUsername)
                    .collect(Collectors.toSet())
                    .contains(USER_NAME)
            );
            assertTrue(forumDTO.getSubscribers().stream()
                    .map(UserDTO::getUsername)
                    .collect(Collectors.toSet())
                    .contains(USER_NAME_2)
            );
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that subscription fails when using a forum that doesn't exists.
     */
    @Test
    public void testMissingForumSubscribe() {
        ForumException thrown = assertThrows(ForumException.class,
                () -> forumService.subscribeTo(0, USER_ID_2));
        assertTrue(thrown.getMessage().contains("No such forum thread."));
    }

    /**
     * Test that subscription fails when using an user that doesn't exists.
     */
    @Test
    public void testForumSubscribeMissingUser() {
        ForumException thrown = assertThrows(ForumException.class,
                () -> forumService.subscribeTo(FORUM_ID, 0));
        assertTrue(thrown.getMessage().contains("No such user."));
    }

    /**
     * Test that unsubscription works.
     */
    @Test
    public void testForumUnsubscribe() {
        try {
            ForumDTO forumDTO = forumService.unsubscribeFrom(FORUM_ID, USER_ID);
            assertFalse(forumDTO.getSubscribers().stream()
                    .map(UserDTO::getUsername)
                    .collect(Collectors.toSet())
                    .contains(USER_NAME)
            );
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that unsubscription fails when using a forum that doesn't exists.
     */
    @Test
    public void testMissingForumUnsubscribe() {
        ForumException thrown = assertThrows(ForumException.class,
                () -> forumService.unsubscribeFrom(0, USER_ID));
        assertTrue(thrown.getMessage().contains("No such forum thread."));
    }

    /**
     * Test forum update works.
     */
    @Test
    public void testUpdateForum() {
        String newTitle = "New title!";
        try {
            ForumDTO forumDTO = forumService.updateForum(FORUM_ID, newTitle);
            assertEquals(newTitle, forumDTO.getTitle());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test forum update fails when supplied a forum that doesn't exists.
     */
    @Test
    public void testUpdateMissingForum() {
        String newTitle = "New title!";
        ForumException thrown = assertThrows(ForumException.class,
                () -> forumService.updateForum(0, newTitle));
        assertTrue(thrown.getMessage().contains("No such forum thread."));
    }

    /**
     * Test the find all forum method.
     */
    @Test
    public void testFindAllForums() {
        try {
            List<ForumDTO> forumDTOS = forumService.getAllForum();
            assertTrue(forumDTOS.stream()
                    .map(ForumDTO::getId)
                    .collect(Collectors.toSet())
                    .contains(FORUM_ID)
            );
            assertTrue(forumDTOS.stream()
                    .map(ForumDTO::getId)
                    .collect(Collectors.toSet())
                    .contains(FORUM_LOCKED_ID)
            );
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test if the find by ID method works.
     */
    @Test
    public void testGetForumByID() {
        try {
            ForumDTO forumDTO = forumService.getForumWithID(FORUM_ID);
            assertEquals(FORUM_ID, forumDTO.getId());
            assertEquals(USER_NAME, forumDTO.getAuthor().getUsername());
            assertEquals(FORUM_TITLE, forumDTO.getTitle());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test find by ID fails when supplied a forum ID that doesn't exists.
     */
    @Test
    public void testGetMissingForumByID() {
        ForumException thrown = assertThrows(ForumException.class,
                () -> forumService.getForumWithID((long) 0));
        assertTrue(thrown.getMessage().contains("Forum not found"));
    }

    /**
     * Test that finding forum by user works.
     */
    @Test
    public void testGetForumByUser() {
        try {
            List<ForumDTO> forumDTOS = forumService.getForumsByUser(USER_ID);
            assertTrue(
                    forumDTOS.stream()
                            .map(ForumDTO::getId)
                            .collect(Collectors.toSet())
                            .contains(FORUM_ID)
            );
            assertTrue(
                    forumDTOS.stream()
                            .map(ForumDTO::getId)
                            .collect(Collectors.toSet())
                            .contains(FORUM_LOCKED_ID)
            );
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test that finding forum by an user that doesn't exists fails.
     */
    @Test
    public void testGetForumByMissingUser() {
        ForumException thrown = assertThrows(ForumException.class,
                () -> forumService.getForumsByUser(0));
        assertTrue(thrown.getMessage().contains("No such user."));
    }

    /**
     * Test the DTO conversion.
     */
    @Test
    public void testForumToDTO() {
        try {
            // Create a forum.
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

            // Convert.
            ForumDTO forumDTO = forumService.forumToDTO(forum);

            // Test values.
            assertEquals(FORUM_ID, forumDTO.getId());
            assertEquals(USER_NAME, forumDTO.getAuthor().getUsername());
            assertTrue(
                    forumDTO.getComments().stream()
                            .map(CommentDTO::getText)
                            .collect(Collectors.toSet())
                            .contains(COMMENT_TEXT)
            );
            assertTrue(
                    forumDTO.getSubscribers().stream()
                            .map(UserDTO::getUsername)
                            .collect(Collectors.toSet())
                            .contains(USER_NAME)
            );
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
