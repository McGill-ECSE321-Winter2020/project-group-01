package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.dto.ForumDTO;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.ApplicationRepository;
import ca.mcgill.ecse321.petshelter.repository.CommentRepository;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            throw new CommentException("Title can't be null!");
        }
        if (forumDTO.getComments() == null || forumDTO.getComments().size() < 1) {
        	throw new CommentException("A forum must have at least one comment");
        }
        
        Forum forum = new Forum();
        forum.setTitle(forumDTO.getTitle());
        forum.setComments(forumDTO.getComments());
        List<String> subscribersList = new ArrayList<>(forumDTO.getSubscribers());
        Set<User> usersSet = new HashSet<User>();
        for (String name : subscribersList) {
        	usersSet.add(userRepository.findUserByUserName(name));
        }
        forum.setSubscribers(usersSet);
        
        forumRepository.save(forum);
        return forum;
    }
}
