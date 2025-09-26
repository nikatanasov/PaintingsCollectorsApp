package com.paintingscollectors.web;

import com.paintingscollectors.painting.model.Painting;
import com.paintingscollectors.painting.service.PaintingService;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.user.service.UserService;
import com.paintingscollectors.web.dto.AddPaintingRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/paintings")
public class PaintingsController {
    private final UserService userService;
    private final PaintingService paintingService;

    @Autowired
    public PaintingsController(UserService userService, PaintingService paintingService) {
        this.userService = userService;
        this.paintingService = paintingService;
    }

    @GetMapping("/new")
    public ModelAndView getAddPaintingPage(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        UUID userId = (UUID) session.getAttribute("user_id");
        //User user = userService.getById(userId);

        modelAndView.setViewName("new-painting");
        modelAndView.addObject("addPaintingRequest", new AddPaintingRequest());
        return modelAndView;
    }

    @PostMapping
    public String processAddPainting(@Valid AddPaintingRequest addPaintingRequest, BindingResult bindingResult, HttpSession session){
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);

        if(bindingResult.hasErrors()){
            return "new-painting";
        }

        paintingService.addNewPainting(addPaintingRequest, user);
        return "redirect:/home";
    }

    @DeleteMapping("/{id}")
    public String removeFromDatabase(@PathVariable UUID id, HttpSession session){
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);
        paintingService.removePaintingFromDB(id);
        return "redirect:/home";
    }

    @PostMapping("/favourites/{id}")
    public String addToMyFavourites(@PathVariable UUID id, HttpSession session){
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);
        paintingService.addPaintingToFavourites(id, user);
        return "redirect:/home";
    }


    @DeleteMapping("/favourites/{id}")
    public String removeFromMyFavourites(@PathVariable UUID id, HttpSession session){
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);
        paintingService.removePaintingFromMyFavourites(id);
        return "redirect:/home";
    }

    @PostMapping("/rate/{id}")
    public String voteForPainting(@PathVariable UUID id, HttpSession session){
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);
        paintingService.voteUp(id);
        return "redirect:/home";
    }

}
