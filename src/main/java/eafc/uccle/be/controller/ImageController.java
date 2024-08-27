package eafc.uccle.be.controller;

import eafc.uccle.be.entity.Image;
import eafc.uccle.be.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/product/{productId}")
    public Set<Image> getImagesByProductId(@PathVariable Long productId) {
        return imageService.getImagesByProductId(productId);
    }
}