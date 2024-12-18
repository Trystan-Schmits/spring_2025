package com.nighthawk.spring_portfolio.mvc.messages;

import com.nighthawk.spring_portfolio.mvc.person.Person;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nighthawk.spring_portfolio.mvc.person.PersonJpaRepository;
import com.nighthawk.spring_portfolio.mvc.person.PersonRole;
import com.nighthawk.spring_portfolio.mvc.person.PersonRoleJpaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api/saigai/comments")
@CrossOrigin(origins = "http://127.0.0.1:4100", allowCredentials = "true")
public class saigaiCommentApiController {

     private static final Logger logger = LoggerFactory.getLogger(saigaiCommentApiController.class);
    @Autowired
    private saigaiCommentJpaRepository commentRepository;

    @Autowired
    private saigaiMessageJpaRepository messageRepository;

      @Autowired
    private PersonJpaRepository personRepository;

    @Autowired
    private PersonRoleJpaRepository personRoleRepository; // For role lookup

    @GetMapping
    public List<saigaiComment> getAllComments() {
        return commentRepository.findAll();
    }

    @PostMapping("/{messageId}")
    public ResponseEntity<saigaiComment> createComment(@PathVariable Long messageId, @RequestBody saigaiComment comment) {
        return messageRepository.findById(messageId).map(message -> {
            comment.setMessage(message);
            return ResponseEntity.ok(commentRepository.save(comment));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (commentRepository.existsById(id)) {
            logger.info("Comment exists, attempting to delete...");
            commentRepository.deleteById(id);
            logger.info("Comment deleted successfully.");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestHeader("username") String username) {
    //     // Find the person making the request
    //     PersonRole personRole = personRoleRepository.findByName(roleName)
    //             .orElseThrow(() -> new RuntimeException("User not found"));

    //     // Check the user's role
    //     if (!"ROLE_ADMIN".equals(personRole.getRole())) {
    //         logger.warn("Unauthorized attempt to delete comment by user: " + username);
    //         return ResponseEntity.status(403).build(); // Forbidden
    //     }

    //     // Allow deletion if the user is an admin
    //     if (commentRepository.existsById(id)) {
    //         logger.info("Comment exists, attempting to delete...");
    //         commentRepository.deleteById(id);
    //         logger.info("Comment deleted successfully.");
    //         return ResponseEntity.ok().build();
    //     }
    //     return ResponseEntity.notFound().build();
    // }
//     @DeleteMapping("/{id}")
// public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestHeader("username") String username) {
//     // Look up the user by username to determine their role
//     Person person = personRepository.findByEmail(username); // You need a method like this in your repository

//     if (person == null) {
//         logger.warn("User not found: " + username);
//         return ResponseEntity.status(404).build(); // Not Found
//     }

//     PersonRole personRole = person.getRoles().stream()
//         .filter(role -> "ROLE_ADMIN".equals(role.getName()))
//         .findFirst()
//         .orElse(null); // If no admin role is found, personRole will be null

//     if (personRole == null) {
//         logger.warn("Unauthorized attempt to delete comment by user: " + username);
//         return ResponseEntity.status(403).build(); // Forbidden
//     }

//     // Allow deletion if the user is an admin
//     if (commentRepository.existsById(id)) {
//         logger.info("Comment exists, attempting to delete...");
//         commentRepository.deleteById(id);
//         logger.info("Comment deleted successfully.");
//         return ResponseEntity.ok().build();
//     }

//     return ResponseEntity.notFound().build();
// }

}
