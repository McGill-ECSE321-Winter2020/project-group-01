package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.CommentRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    
    @Autowired
    CommentRepository commentRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Transactional
    public List<Comment> getAllComments() {
        return toList(commentRepository.findAll());
    }
    
    @Transactional
    public Comment getComment(User user, String text) {
        return commentRepository.findCommentByUserAndText(user, text);
    }
    
    //From tutorial
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
    
    @Transactional
    public Comment createComment(CommentDTO commentDTO) {
        //condition checks
        if (commentDTO.getText() == null) {
            throw new CommentException("Text can't be null!");
        }
        if (commentDTO.getUsername() == null) {
        	throw new CommentException("Username can't be null!");
        }
        
        Comment comment = new Comment();
        comment.setDatePosted(commentDTO.getDatePosted());
        comment.setText(commentDTO.getText());
        comment.setTime(commentDTO.getTime());
        comment.setUser(userRepository.findUserByUserName(commentDTO.getUsername()));
        
        commentRepository.save(comment);
        return comment;
    }
}
