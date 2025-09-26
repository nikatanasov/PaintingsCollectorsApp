package com.paintingscollectors.painting.service;

import com.paintingscollectors.painting.model.FavouritePainting;
import com.paintingscollectors.painting.model.Painting;
import com.paintingscollectors.painting.repository.FavouritePaintingRepository;
import com.paintingscollectors.painting.repository.PaintingRepository;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.web.dto.AddPaintingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaintingService {
    private final PaintingRepository paintingRepository;
    private final FavouritePaintingRepository favouritePaintingRepository;

    @Autowired
    public PaintingService(PaintingRepository paintingRepository, FavouritePaintingRepository favouritePaintingRepository) {
        this.paintingRepository = paintingRepository;
        this.favouritePaintingRepository = favouritePaintingRepository;
    }

    public void addNewPainting(AddPaintingRequest addPaintingRequest, User user) {
        Painting painting = Painting.builder()
                .name(addPaintingRequest.getName())
                .author(addPaintingRequest.getAuthor())
                .style(addPaintingRequest.getStyle())
                .owner(user)
                .imageUrl(addPaintingRequest.getImageUrl())
                .build();
        paintingRepository.save(painting);
    }

    public List<Painting> getOtherPaintings(User user) {
        return paintingRepository.findAll().stream().filter(p -> !p.getOwner().getId().equals(user.getId())).collect(Collectors.toList());
    }

    public Painting getPaintingById(UUID paintingId){
        return paintingRepository.findById(paintingId).orElseThrow(() -> new RuntimeException("There is no painting with id ["+paintingId+"]"));
    }

    public FavouritePainting getFavouritePaintingById(UUID paintingId){
        return favouritePaintingRepository.findById(paintingId).orElseThrow(() -> new RuntimeException("There is no favourite painting with id ["+paintingId+"]"));
    }

    public void removePaintingFromDB(UUID paintingId) {
        Painting painting = getPaintingById(paintingId);
        paintingRepository.delete(painting);
    }

    public void addPaintingToFavourites(UUID paintingId, User user) {
        Painting painting = getPaintingById(paintingId);
        FavouritePainting favouritePainting = FavouritePainting.builder()
                .name(painting.getName())
                .author(painting.getAuthor())
                .owner(user)
                .imageUrl(painting.getImageUrl())
                .createdOn(LocalDateTime.now())
                .build();
        favouritePaintingRepository.save(favouritePainting);
    }

    public List<FavouritePainting> getFavouritePaintings(User user) {
        return favouritePaintingRepository.findAll().stream().filter(p -> p.getOwner().getId().equals(user.getId())).collect(Collectors.toList());
    }

    public void removePaintingFromMyFavourites(UUID paintingId) {
        FavouritePainting favouritePainting = getFavouritePaintingById(paintingId);
        favouritePaintingRepository.delete(favouritePainting);
    }

    public List<Painting> getMostRatedPaintings() {
        return paintingRepository.findAllByOrderByVotesDescNameAsc();
    }

    public void voteUp(UUID paintingId) {
        Painting painting = getPaintingById(paintingId);
        painting.setVotes(painting.getVotes() + 1);
        paintingRepository.save(painting);
    }
}
