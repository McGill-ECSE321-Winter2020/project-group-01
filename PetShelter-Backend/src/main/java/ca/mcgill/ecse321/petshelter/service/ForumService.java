package ca.mcgill.ecse321.petshelter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.petshelter.dto.ForumDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

@Service
public class ForumService {
    
    @Autowired
    ForumRepository forumRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Transactional
    public List<Forum> getAllForums() {
        return toList(forumRepository.findAll());
    }
    
    @Transactional
    public Forum getForum(String title) {
        return forumRepository.findForumByTitle(title);
    }
    
    @Transactional
    public List<Forum> getAllUserForums(User user) {
        return toList(forumRepository.findAllByUser(user));
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
    public Forum createForum(ForumDTO forumDTO) {
        //condition checks
        if (forumDTO.getTitle() == null) {
            throw new ForumException("Title can't be null!");
        }
        if (forumDTO.getComments() == null || forumDTO.getComments().size() < 1) {
        	throw new ForumException("A forum must have at least one comment");
        }
        
        Forum forum = new Forum();
        forum.setTitle(forumDTO.getTitle());
        forum.setComments(forumDTO.getComments());
        forum.setSubscribers(forumDTO.getSubscribers());
        
        forumRepository.save(forum);
        return forum;
    }
}
