package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.AdvertisementService;
import ca.mcgill.ecse321.petshelter.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@Autowired UserRepository userRepository;

	public CommentController() {
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllAdvertisements() {
		return new ResponseEntity<>(
				commentService.getAllComments().stream().map(this::convertToDto).collect(Collectors.toList()),
				HttpStatus.OK);
	}
	
	@GetMapping("/{user}")
	public ResponseEntity<?> getUserComments(@PathVariable String user) {
		return new ResponseEntity<>(commentService.getAllUserComments(userRepository.findUserByUserName(user))
				.stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
	}

	public CommentDTO convertToDto(Comment comment) {
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setDatePosted(comment.getDatePosted());
		commentDTO.setText(comment.getText());
		commentDTO.setTime(comment.getTime());
		commentDTO.setUsername(comment.getUser().getUserName());

		return commentDTO;
	}

	@PostMapping()
	public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO) {
		Comment comment = commentService.createComment(commentDTO);
		try {
			commentDTO.setDatePosted(comment.getDatePosted());
			commentDTO.setText(comment.getText());
			commentDTO.setTime(comment.getTime());
			commentDTO.setUsername(comment.getUser().getUserName());
			
			return new ResponseEntity<>(commentDTO, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
